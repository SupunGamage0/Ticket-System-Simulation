package org.example;

public class Ticket {
    private final int ticketId;
    private final int vendorId;
    private final String eventName;
    private final String location;
    private final double price;

    public Ticket(int ticketId, int vendorId, String eventName, String location, double price) {
        this.ticketId = ticketId;
        this.vendorId = vendorId;
        this.eventName = eventName;
        this.location = location;
        this.price = price;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }
}
