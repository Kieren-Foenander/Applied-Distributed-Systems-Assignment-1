/*
 * /*
Programmer: Kieren Foenander S012141776
Course: Applied Distributed Systems
Assignment 1
 */
package Assignment1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;

public class UDPServer {

    public static int serverPort = 6789;
    public static String textFileName = "members.txt";
    public static ArrayList<Customer> customerList = new ArrayList();
    public static long interval = 120000;
    public static String menuResponse = "****** Travel Kiosk ******\n         1: IN\n         2: OUT\n         3: EXIT\nEnter: ";
    
    //creating instance variables
  
    

    public static void readCustomers() throws FileNotFoundException, IOException {

        FileReader reader = new FileReader(textFileName); // creates a new file reader to read members.txt
        Scanner input = new Scanner(reader); // creates new scanner for file

        while (input.hasNextLine()) { // loops through text file to get info
            String line = input.nextLine(); // creates string with each line in document
            Scanner lineInput = new Scanner(line); // creates new scanner to scan each line to extract information further

            while (lineInput.hasNext()) { // loops through each line to separate information
                String clientId = lineInput.next(); // gets client id from line
                int pinNumber = lineInput.nextInt(); // gets pin number from line

                Customer custInput = new Customer(); // creates new customer object
                custInput.setClientId(clientId); // adds client id to object
                custInput.setPinNumber(pinNumber); // adds pin number to object

                customerList.add(custInput); // adds new customer object to arraylist

            }
        }
        reader.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        readCustomers(); // loads customer text file
        
        String response = "";
        String user = "";
        int pin;
        int userNum = 0;
        
        boolean optionOneActive = false;
        boolean optionTwoActive = false;
        boolean found = true;
        boolean exists = true;
        
        //decalring variables required
        
        
        
        
        Timer timer = new Timer(); // creates new timer
        WriteToFile wtf = new WriteToFile(); // creates instance of WriteToFile class

        timer.schedule(wtf, interval, interval); // schedules a write to file action after 2 minutes of the program running and every 2 minutes after

        DatagramSocket socketA = null; // creates new instance of a socket
        try {
            socketA = new DatagramSocket(serverPort); // creates new socket

            byte[] buffer = new byte[1000]; // creates new byte array to buffer incoming messages
            System.out.println("server started");

            while (true) { // loop to keep server running after each command

                DatagramPacket request = new DatagramPacket(buffer, buffer.length); // new packet that reads packet sent from client
                socketA.receive(request); // recieves packet
                String str = new String(request.getData(), 0, request.getLength()); // creates new string from recieved paclet
                System.out.println("Client Request: " + new String(request.getData(), 0, request.getLength())); // server reads string to command line

                if (str.equalsIgnoreCase("1")) { // reads response from client and responds accordingly
                    response = "Enter Client Id";
                    optionOneActive = true;

                } else if (str.equalsIgnoreCase("2")) {
                    response = "Enter Client Id";
                    optionTwoActive = true;

                } else if (str.equalsIgnoreCase("3")) {
                    response = "exit";
                }

                if (optionOneActive == true) { // commands to run if otion one is selected

                    for (int i = 0; i < customerList.size(); i++) { // loop to check through each client id number to see if the id number matches
                        if (str.equalsIgnoreCase(customerList.get(i).getClientId())) {
                            response = "Enter Client Pin"; // gives prompt for next section
                            user = customerList.get(i).getClientId(); // makes user variable the client id that is found
                            userNum = i; // gives specific user num to identify same user later
                            found = true;
                            
                        }                     
                    }

                    if (str.equalsIgnoreCase(String.valueOf(customerList.get(userNum).getPinNumber()))) {
                        pin = Integer.parseInt(str); // sets pin variable to be able to use with sign in method

                        if (customerList.get(userNum).isStatus() == true) { // ensures that if client is already signed in thye cannot be signed in again
                            response = "Error Already Signed In\n" + menuResponse;
                            optionOneActive = false;

                        } else {

                            signIn(user, pin);// signs client in
                            response = "Success Welcome\n" + menuResponse; // gives success repsponse and goes back to main menu
                            optionOneActive = false; // resets options so can be reactivated when nesecary
                            if (customerList.get(userNum).getNumberOfTravels() > 5) {
                                customerList.get(userNum).calculateCost();
                            }

                        }
                    }
                }

                if (optionTwoActive == true) { // commands to run if otion Two is selected

                    for (int i = 0; i < customerList.size(); i++) { // loop to check through each client id number to see if the id number matches
                        if (str.equalsIgnoreCase(customerList.get(i).getClientId())) {
                            response = "Enter Client Pin"; // gives prompt for next section
                            user = customerList.get(i).getClientId(); // makes user variable the client id that is found
                            userNum = i; // gives specific user num to identify same user later
                            found = true;

                        }
                        
                    }

                    if (str.equalsIgnoreCase(String.valueOf(customerList.get(userNum).getPinNumber()))) {
                        pin = Integer.parseInt(str); // sets pin variable to be able to use with sign out method

                        if (customerList.get(userNum).isStatus() == false) { // ensures that if client is already signed out thye cannot be signed in again
                            response = "Error Already Signed Out\n" + menuResponse;
                            optionTwoActive = false;

                        } else {

                            signOut(user, pin);// signs client out
                            response = "Success Goodbye\n" + menuResponse; // gives success repsponse and goes back to main menu
                            optionTwoActive = false; // resets options so can be reactivated when nesecary
                        }
                    }
                }

                byte[] b = response.getBytes(); // creates byte array for response to client
                DatagramPacket reply = new DatagramPacket(b, response.length(), request.getAddress(), request.getPort()); // datagram constructor
                socketA.send(reply); // sends reply to client

                Arrays.fill(buffer, (byte) 0); // fills array

                for (int i = 0; i < customerList.size(); i++) { // loop to display users infomation
                    System.out.println(customerList.get(i).toString());
                }

            }

        } catch (SocketException e) { // exception handling
            System.out.println("Socket: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socketA != null) {
                socketA.close();
            }
        }


    }
    
    public static void signIn(String userId, int pinNumber){

        for (int i = 0; i < customerList.size(); i++){
            if (userId.equalsIgnoreCase(customerList.get(i).getClientId()) && pinNumber ==(customerList.get(i).getPinNumber())){
            
            customerList.get(i).setStatus(true);
            customerList.get(i).setNumberOfTravels(customerList.get(i).getNumberOfTravels()+1);
            }
        }
        
    }
    
    public static void signOut(String userId, int pinNumber) {
        for (int i = 0; i < customerList.size(); i++) {
            if (userId.equalsIgnoreCase(customerList.get(i).getClientId()) && pinNumber == (customerList.get(i).getPinNumber())) {
                customerList.get(i).setStatus(false);
            }
        }
    }

}
