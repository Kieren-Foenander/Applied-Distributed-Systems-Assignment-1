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
    public static String menuResponse = "****** Travel Kiosk ******\n         1: IN\n         2: OUT\n         3: EXIT\nEnter: ";
    
    public static void main(String[] args) {

        //menuResponse();
        DatagramSocket socketA = null;

        try {

            socketA = new DatagramSocket(); // create udp socket

            Scanner scanner = new Scanner(System.in); // creates new scanner

            String msg = "";

            while (!msg.equalsIgnoreCase("exit")) {
                System.out.println(menuResponse);
                int option = scanner.nextInt();

                if (option == 1) {// client wants to sign in
                    System.out.println("Enter Client Id:");
                    String id = scanner.next();
                    System.out.println("Enter Pin Id:");
                    int pin = scanner.nextInt();

                    msg = id + " " + pin + " " + "In";
                }
                if (option == 2) { // client wants to sign out
                    System.out.println("Enter Client Id:");
                    String id = scanner.next();
                    System.out.println("Enter Pin Id:");
                    int pin = scanner.nextInt();

                    msg = id + " " + pin + " " + "Out";
                }
                if (option == 3) { // client watns to exit
                    msg = "exit";
                }

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


                System.out.println("server response: " + new String(reply.getData(), 0, reply.getLength()));

            }
            socketA.close();

        } catch (SocketException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (socketA != null) {
                socketA.close();
            }
        }


    }


}
