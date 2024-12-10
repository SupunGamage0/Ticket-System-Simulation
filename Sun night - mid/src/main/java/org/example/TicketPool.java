package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    private final int maxCapacity;
    private final int totalTickets;
    private int ticketsReleased = 0; // Tracks the total number of tickets released
    private final List<Ticket> pool = new ArrayList<>();
    private boolean allTicketsSoldOut = false;

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
    }

    public synchronized void addTicket(int vendorId) {
        // Wait if pool is full or all tickets are released
        while (pool.size() >= maxCapacity || ticketsReleased >= totalTickets) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (ticketsReleased < totalTickets) {
            ticketsReleased++;
            Ticket ticket = new Ticket(ticketsReleased, vendorId, "Music Festival", "Main Auditorium", generateRandomPrice());
            pool.add(ticket);
            Collections.shuffle(pool); // Shuffle tickets in the pool for randomness
            System.out.println("Vendor " + vendorId + " added Ticket ID " + ticket.getTicketId() + ".");
            System.out.println("Current Pool Size: " + pool.size());
            FileHandler.saveLog("Vendor " + vendorId + " added Ticket ID " + ticket.getTicketId());
            notifyAll(); // Notify customers that a ticket is available
        }
    }

    public synchronized Ticket buyRandomTicket(int customerId) {
        // Wait if no tickets are available
        while (pool.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Ticket ticket = pool.remove(0); // Remove a ticket from the pool
        if (ticket != null) {
            System.out.println("Customer " + customerId + " bought Ticket ID " + ticket.getTicketId() +
                    " [Event: " + ticket.getEventName() + ", Location: " + ticket.getLocation() +
                    ", Price: $" + ticket.getPrice() + "].");
            System.out.println("Current Pool Size: " + pool.size());
            FileHandler.saveLog("Customer " + customerId + " bought Ticket ID " + ticket.getTicketId() +
                    " [Event: " + ticket.getEventName() + ", Location: " + ticket.getLocation() +
                    ", Price: $" + ticket.getPrice() + "]. Pool size: " + pool.size());
        }

        // Log "All tickets are sold out!" only once
        if (ticketsReleased >= totalTickets && pool.isEmpty() && !allTicketsSoldOut) {
            allTicketsSoldOut = true;
            System.out.println("All tickets are sold out!");
            FileHandler.saveLog("All tickets are sold out.");
        }

        notifyAll(); // Notify vendors if space is freed up
        return ticket;
    }

    private double generateRandomPrice() {
        double[] prices = {50.0, 100.0, 150.0};
        return prices[(int) (Math.random() * prices.length)];
    }

    public synchronized boolean isAllTicketsReleased() {
        return ticketsReleased >= totalTickets;
    }

    public synchronized boolean isAllTicketsSold() {
        return ticketsReleased >= totalTickets && pool.isEmpty();
    }
}
