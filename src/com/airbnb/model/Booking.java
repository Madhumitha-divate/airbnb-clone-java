package com.airbnb.model;

public class Booking {

    private int id;
    private User user;
    private Property property;
    private String bookingDate;

    public Booking(int id, User user, Property property, String bookingDate) {
        this.id = id;
        this.user = user;
        this.property = property;
        this.bookingDate = bookingDate;
    }

    public int getId()              { return id; }
    public User getUser()           { return user; }
    public Property getProperty()   { return property; }
    public String getBookingDate()  { return bookingDate; }

    @Override
    public String toString() {
        return "Booking[id=" + id +
               ", user=" + user.getName() +
               ", property=" + property.getLocation() +
               ", price=Rs." + property.getPrice() +
               ", date=" + bookingDate + "]";
    }
}