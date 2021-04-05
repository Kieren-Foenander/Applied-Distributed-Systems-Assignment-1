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
    public static int userNum = 0;
    public static Customer cust = new Customer();
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
        int pin = 0;
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
                String input = new String(request.getData()).trim(); // creates string for client input and trims exccess 
                System.out.println("Client Response: "+input); // prints clients response to system
                String [] words = input.split(" "); // creates array with clients response
                
/*              System.out.println(words[0]);
                System.out.println(words[1]);
                System.out.println(words[2]);
*/                
                
                if(input.equals("exit")){
                    response = "Good Bye";
                    
                }
                
                else if(words[2].equals("In")){ //client wants to sign in
                    
                    //loops to search through customer list to find a match to client id
                    for (int i = 0; i < customerList.size(); i++){
                        if (words[0].equalsIgnoreCase(customerList.get(i).getClientId())){
                            user = customerList.get(i).getClientId(); // sets user to client id
                            userNum = i; // retains users spot in arraylist

                        }
                    }
                    
                    if (Integer.parseInt(words[1]) == customerList.get(userNum).getPinNumber()){
                        // checks if pin is a match to the client 
                        pin = customerList.get(userNum).getPinNumber(); // sets pin
                    } 
                    //error responses
                    if (user.equals("")){
                        response = "Invalid User";
                        //resets user info
                        user = "";
                        userNum = 0;
                        pin = 0;
                    }else if (pin == 0){
                        response = "Invalid Pin";
                        //resets user info
                        user = "";
                        userNum = 0;
                        pin = 0;
                    }else if (customerList.get(userNum).isStatus() == true){
                        response = "User is already signed in";
                        //resets user info
                        user = "";
                        userNum = 0;
                        pin = 0;
                    }else{
                        //if no errors signs in user
                        
                        signIn(user,pin);
                        customerList.get(userNum).calculateCost();
                        response = "user signed in";
                        
                        //resets user info
                        user = "";
                        userNum = 0;
                        pin = 0;

                        
                    }
                    
                }else if(words[2].equals("Out")){ //client wants to sign out
                    
                    //loops to search through customer list to find a match to client id
                    for (int i = 0; i < customerList.size(); i++){
                        if (words[0].equalsIgnoreCase(customerList.get(i).getClientId())){
                            user = customerList.get(i).getClientId(); // sets user to client id
                            userNum = i; // retains users spot in arraylist

                        }
                    }
                    
                    if (Integer.parseInt(words[1]) == customerList.get(userNum).getPinNumber()){
                        // checks if pin is a match to the client 
                        pin = customerList.get(userNum).getPinNumber(); // sets pin
                        
                    } 
                    //error responses
                    if (user.equals("")){
                        response = "Invalid User";
                        //resets user info
                        user = "";
                        userNum = 0;
                        pin = 0;
                    }else if (pin == 0){
                        response = "Invalid Pin";
                        //resets user info
                        user = "";
                        userNum = 0;
                        pin = 0;
                    }else if (customerList.get(userNum).isStatus() == false){
                        response = "User is already signed Out";
                        //resets user info
                        user = "";
                        userNum = 0;
                        pin = 0;
                    }else{
                        //if no errors signs out user
                        
                        signOut(user,pin);
                        response = "user signed Out";
                        
                        //resets user info
                        user = "";
                        userNum = 0;
                        pin = 0;
                        

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
            if (userId.equals(customerList.get(i).getClientId()) && pinNumber ==(customerList.get(i).getPinNumber())){
            
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
