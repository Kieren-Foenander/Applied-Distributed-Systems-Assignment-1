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
        String menuResponse = "****** Travel Kiosk ******\n         1: IN\n         2: OUT\n         3: EXIT\nEnter: ";
        boolean optionOneActive = false;
        boolean optionTwoActive = false;
        
        
        
        Timer timer = new Timer(); // creates new timer
        WriteToFile wtf = new WriteToFile(); // creates instance of WriteToFile class

        timer.schedule(wtf, interval, interval); // schedules a write to file action after 2 minutes of the program running and every 2 minutes after

        DatagramSocket socketA = null;
        try {
            socketA = new DatagramSocket(serverPort);

            byte[] buffer = new byte[1000];
            System.out.println("server started");

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socketA.receive(request);
                String str = new String(request.getData(), 0, request.getLength());
                System.out.println("Client Request: " + new String(request.getData(), 0, request.getLength()));
               
                if (str.equalsIgnoreCase("1")){
                    response = "Enter Client Id";
                    optionOneActive = true;
                    
                }else if (str.equalsIgnoreCase("2")){
                    response = "Enter Client Id";
                    optionTwoActive = true;
                    
                }else if(str.equalsIgnoreCase("3")){
                    response = "exit";
                }
                
                for (int i = 0; i < customerList.size(); i++){
                        if (str.equalsIgnoreCase(customerList.get(i).getClientId())){
                            response = "Enter Client Pin";
                            user = customerList.get(i).getClientId();
                            userNum = i; 
                            
                            if (i == customerList.size()){
                                response = "Invalid Customer";
                            }
                        } 
                            
                        
                }
                if (str.equalsIgnoreCase(String.valueOf(customerList.get(userNum).getPinNumber()))) {
                    pin = Integer.parseInt(str);

                    if (optionOneActive == true) {
                        signIn(user, pin);
                        response = "Success Welcome" + menuResponse;
                        optionOneActive = false;
                    } else if (optionTwoActive == true) {
                        signOut(user, pin);
                        response = "Success Goodbye" + menuResponse;
                        optionTwoActive = false;

                    }

                }
 
                    byte[] b = response.getBytes();
                    DatagramPacket reply = new DatagramPacket(b, response.length(), request.getAddress(), request.getPort());
                    socketA.send(reply);
                    
                    Arrays.fill(buffer, (byte)0);

            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socketA != null) {
                socketA.close();
            }
        }

        System.out.println(customerList);

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
