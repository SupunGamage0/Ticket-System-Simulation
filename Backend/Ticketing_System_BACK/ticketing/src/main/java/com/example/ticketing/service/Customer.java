package com.example.ticketing.service;

import com.example.ticketing.handler.TicketWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Customer.class);
    private final TicketPool ticketPool;
    private final long retrievalInterval; // Changed to long
    private final AtomicInteger ticketsConsumed;
    private final int totalTickets;
    private final List<String> logs;
    private final TicketWebSocketHandler webSocketHandler;

    public Customer(TicketPool ticketPool, long retrievalInterval, AtomicInteger ticketsConsumed, int totalTickets, List<String> logs, TicketWebSocketHandler webSocketHandler) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.ticketsConsumed = ticketsConsumed;
        this.totalTickets = totalTickets;
        this.logs = logs;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void run() {
        try {
            while (ticketsConsumed.get() < totalTickets) {
                Integer ticketId = ticketPool.retrieveTicket();
                if (ticketId != null) {
                    ticketsConsumed.incrementAndGet();
                    int customerId = (int) (Math.random() * 100) + 1;
                    String message = createCustomerLogMessage(customerId, ticketId);
                    synchronized (logs) {
                        logs.add(message);
                    }
                    webSocketHandler.broadcastMessage(message);
                }
                Thread.sleep(retrievalInterval); // Use retrievalInterval
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Customer interrupted: {}", e.getMessage());
        }
    }

    private String createCustomerLogMessage(int customerId, int ticketId) {
        // Use Gson to create a JSON string
        return String.format("{\"type\":\"customer\",\"customerId\":%d,\"ticketId\":%d,\"event\":\"Music Festival\",\"location\":\"Main Auditorium\",\"price\":%d,\"poolSize\":%d}",
                customerId, ticketId, (int) (Math.random() * 3) * 50 + 50, ticketPool.getSize());
    }
}