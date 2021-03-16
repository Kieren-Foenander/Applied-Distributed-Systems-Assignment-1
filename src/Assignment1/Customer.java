/*
 * /*
Programmer: Kieren Foenander S012141776
Course: Applied Distributed Systems
Assignment 1
 */

package Assignment1;


public class Customer {
    
    // instance variables of a customer
    
    private String clientId;
    private int pinNumber;
    private boolean status;
    private int numberOfTravels;
    private double totalCost;
    
    // default constructor
    Customer(){
        
    }
    
    //parameterised constructor
    Customer(String clientId, int pinNumber, boolean status, int numberOfTravels, double totalCost){
        
        this.clientId = clientId;
        this.pinNumber = pinNumber;
        this.status = status;
        this.numberOfTravels = numberOfTravels;
        this.totalCost = totalCost;
        
        
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(int pinNumber) {
        this.pinNumber = pinNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNumberOfTravels() {
        return numberOfTravels;
    }

    public void setNumberOfTravels(int numberOfTravels) {
        this.numberOfTravels = numberOfTravels;
    }

    public double getTotalCost() {
        return totalCost;
    }

    
    public void calculateCost(){
        /* TO DO method is used to calculate the cost of the trip */
    }

    @Override
    public String toString() {
        return "Customer{" + "clientId=" + clientId + ", pinNumber=" + pinNumber + ", status=" + status + ", numberOfTravels=" + numberOfTravels + ", totalCost=" + totalCost + '}';
    }
    

    
    
    
    
    
    
     
}
