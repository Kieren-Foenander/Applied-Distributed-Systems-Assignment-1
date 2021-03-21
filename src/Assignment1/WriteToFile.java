/*
 * /*
Programmer: Kieren Foenander S012141776
Course: Applied Distributed Systems
Assignment 1
 */

package Assignment1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.TimerTask;


public class WriteToFile extends TimerTask {
    
    private FileOutputStream fos = null;
    private ObjectOutputStream oos = null;
    private String fileName = "members.dat";
    
    UDPServer server = new UDPServer();
    
    
    
    
    @Override
    public void run(){
        
        try{
            fos = new FileOutputStream(fileName); // creates new file stream 
            oos = new ObjectOutputStream(fos);// creates new object output stream
            
            for (int i = 0; i < server.customerList.size(); i++){ // loop to add each customer to the text file
                oos.writeObject(server.customerList.get(i));
            }
            oos.flush();
            oos.close();

        }catch(IOException e){
            e.printStackTrace();
            
        }
    }

}
