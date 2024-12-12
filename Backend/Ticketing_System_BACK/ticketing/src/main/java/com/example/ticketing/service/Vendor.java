package com.example.ticketing.service;

import com.example.ticketing.handler.TicketWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Vendor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Vendor.class);
    private final TicketPool ticketPool;
    private final int totalTickets;
    private final long releaseInterval; // Changed to long
    private final AtomicInteger ticketCounter;
    private final List<String> logs;
    private final TicketWebSocketHandler webSocketHandler;
    private final Map<Integer, Integer> ticketVendorMap;

    public Vendor(TicketPool ticketPool, int totalTickets, long releaseInterval, AtomicInteger ticketCounter, List<String> logs, TicketWebSocketHandler webSocketHandler, Map<Integer, Integer> ticketVendorMap) {
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
        this.releaseInterval = releaseInterval;
        this.ticketCounter = ticketCounter;
        this.logs = logs;
        this.webSocketHandler = webSocketHandler;
        this.ticketVendorMap = ticketVendorMap;
    }

    @Override
    public void run() {
        try {
            while (ticketCounter.get() < totalTickets) {
                int ticketId = ticketCounter.incrementAndGet();
                if (ticketId <= totalTickets) {
                    int vendorId = (int) (Math.random() * 10) + 1; // Assign a random vendor ID
                    ticketVendorMap.put(ticketId, vendorId); // Map ticket to vendor
                    ticketPool.addTicket(ticketId);
                    String message = createVendorLogMessage(vendorId, ticketId);
                    synchronized (logs) {
                        logs.add(message);
                    }
                    webSocketHandler.broadcastMessage(message);
                    Thread.sleep(releaseInterval); // Use releaseInterval
                } else {
                    break; // Stop adding tickets if the counter exceeds totalTickets
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Vendor interrupted: {}", e.getMessage());
        }
    }

    private String createVendorLogMessage(int vendorId, int ticketId) {
        // Use Gson to create a JSON string
        return String.format("{\"type\":\"vendor\",\"vendorId\":%d,\"ticketId\":%d,\"poolSize\":%d}",
                vendorId, ticketId, ticketPool.getSize());
    }
}