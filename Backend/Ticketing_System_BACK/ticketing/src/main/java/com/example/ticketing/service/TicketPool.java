package com.example.ticketing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TicketPool {
    private static final Logger logger = LoggerFactory.getLogger(TicketPool.class);
    private final BlockingQueue<Integer> tickets = new LinkedBlockingQueue<>();
    private int capacity;

    public void addTicket(int ticketId) throws InterruptedException {
        if (tickets.size() >= capacity) {
            logger.info("Pool is full, Vendor is waiting to add Ticket - Ticket ID: {}", ticketId);
        }
        tickets.put(ticketId); // Use put to block if the queue is full
        logger.info("Vendor added a Ticket - Ticket ID: {} - Current pool size: {}", ticketId, tickets.size());
    }

    public Integer retrieveTicket() throws InterruptedException {
        Integer ticket = tickets.take(); // Use take to block if the queue is empty
        logger.info("Customer bought a Ticket - Ticket ID: {} - Current pool size: {}", ticket, tickets.size());
        return ticket;
    }

    public synchronized int getSize() {
        return tickets.size();
    }

    public synchronized void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void resetPool() {
        tickets.clear();
    }
}