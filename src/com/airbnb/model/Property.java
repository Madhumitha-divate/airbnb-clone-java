package com.airbnb.model;

public class Property {

    private int id;
    private String location;
    private double price;
    private boolean isAvailable;

    public Property(int id, String location, double price, boolean isAvailable) {
        this.id = id;
        this.location = location;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public int getId(){
    	return id;
    	}
    public String getLocation(){
    	return location;
    	}
    public double getPrice(){
    	return price;
    	}
    public boolean isAvailable(){
    	return isAvailable;
    	}

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "Property[id=" + id +
               ", location=" + location +
               String.format(", price=Rs.%.2f", price) +
               ", status=" + (isAvailable ? "Available" : "Booked") + "]";
    
    }
}