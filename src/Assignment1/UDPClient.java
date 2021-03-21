/*
 * /*
Programmer: Kieren Foenander S012141776
Course: Applied Distributed Systems
Assignment 1
 */

package Assignment1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;


public class UDPClient {
    
    public static int serverPort = 6789;
    //public static String serverName = "serverOne";
    
    
    public static void main (String [] args){
        
        menuResponse();
        DatagramSocket socketA = null;
        
        try{
            while (true) {

                socketA = new DatagramSocket(); // create udp socket

                Scanner scanner = new Scanner(System.in); // creates new scanner
                
                String msg = scanner.nextLine();

                // prepare message to send to server
                byte[] bytes = msg.getBytes();
                InetAddress aHost = InetAddress.getLocalHost();

                //while(true){
                //create a UDP datagram
                DatagramPacket request = new DatagramPacket(bytes, msg.length(), aHost, serverPort);
                //send the request
                socketA.send(request);

                byte[] buffer = new byte[1000]; // prepare a buffer to recieve the reply from the server

                //waiting for reply
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                socketA.receive(reply);
                
                if (new String (reply.getData(), 0, reply.getLength()).equalsIgnoreCase("exit")){
                    System.out.println("exit System");
                    break;
                }

                System.out.println("server response: " + new String(reply.getData(), 0, reply.getLength()));

            }
            socketA.close();
            
        }
        catch(SocketException e){
            
        }catch(IOException e){
        
        }
        finally{
           if (socketA != null) socketA.close();
        }
        
        
        
        
    }
    
    
    public static void menuResponse(){
        System.out.println("****** Travel Kiosk ******\n         1: IN\n         2: OUT\n         3: EXIT\nEnter: ");

    }

}
