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
import java.util.ArrayList;
import java.util.Scanner;


public class UDPServer {
    
    public static int serverPort = 6789;
    public static String textFileName = "members.txt";
    public static ArrayList <Customer> customerList = new ArrayList();
    
    
    public static void readCustomers() throws FileNotFoundException, IOException{
        
        
        
        FileReader reader = new FileReader(textFileName); // creates a new file reader to read members.txt
        Scanner input = new Scanner(reader); // creates new scanner for file
        
        while (input.hasNextLine()){ // loops through text file to get info
            String line = input.nextLine(); // creates string with each line in document
            Scanner lineInput = new Scanner(line); // creates new scanner to scan each line to extract information further
            
            while (lineInput.hasNext()){ // loops through each line to separate information
                String clientId = lineInput.next(); // gets client id from line
                int pinNumber = lineInput.nextInt(); // gets piin number from line
                
                
                Customer custInput = new Customer(); // creates new customer object
                custInput.setClientId(clientId); // adds client id to object
                custInput.setPinNumber(pinNumber); // adds pin number to object
                
                customerList.add(custInput); // adds new customer object to arraylist
                
            }
        }
        reader.close();
    }
    
    
    
    public static void main (String [] args) throws FileNotFoundException, IOException{
        
        
        readCustomers();
        
        System.out.println(customerList);
 
    }
}
