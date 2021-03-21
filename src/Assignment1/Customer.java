/*
 * /*
Programmer: Kieren Foenander S012141776
Course: Applied Distributed Systems
Assignment 1
 */

package Assignment1;

import java.io.Serializable;


public class Customer implements Serializable{
    
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

    // method is used to calculate the cost that a customer owes based on the amount of trips they have taken
    public void calculateCost(){
        
        if (numberOfTravels > 5){ // states that if the customer has passed their 5 free rides their total cost will now increase by $3.00 per ride
            totalCost = totalCost + 3.00;
        }else {
            totalCost = 0.00;
        }
        
       
    }

    @Override
    public String toString() {
        return clientId + pinNumber + status +  numberOfTravels + totalCost;
    }
    

    
    
    
    
    
    
     
}
