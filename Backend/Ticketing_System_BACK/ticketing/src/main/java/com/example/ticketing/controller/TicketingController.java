package com.example.ticketing.controller;

import com.example.ticketing.model.SystemConfig;
import com.example.ticketing.service.Customer;
import com.example.ticketing.service.TicketPool;
import com.example.ticketing.service.Vendor;
import com.example.ticketing.handler.TicketWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class TicketingController {
    private final TicketPool ticketPool;
    private final TicketWebSocketHandler webSocketHandler;
    private final ScheduledExecutorService vendorExecutorService = Executors.newScheduledThreadPool(5);
    private final ScheduledExecutorService customerExecutorService = Executors.newScheduledThreadPool(5); // Changed to ScheduledExecutorService
    private final AtomicInteger ticketCounter = new AtomicInteger(0);
    private final AtomicInteger ticketsConsumed = new AtomicInteger(0);
    private final List<String> logs = new ArrayList<>();
    private final Map<Integer, Integer> ticketVendorMap = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    @Autowired
    public TicketingController(TicketPool ticketPool, TicketWebSocketHandler webSocketHandler) {
        this.ticketPool = ticketPool;
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/configure")
    public ResponseEntity<String> configureSystem(@RequestBody SystemConfig config) {
        ticketCounter.set(0);
        ticketsConsumed.set(0);
        logs.clear();
        ticketVendorMap.clear();
        ticketPool.resetPool();
        ticketPool.setCapacity(config.getMaxTicketCapacity());

        // Convert rates from tickets per second to milliseconds per ticket
        long vendorReleaseInterval = (long) (1000 / config.getTicketReleaseRate());
        long customerRetrievalInterval = (long) (1000 / config.getCustomerRetrievalRate());

        // Schedule vendor tasks
        for (int i = 0; i < 10; i++) { // Example: 10 vendors
            vendorExecutorService.scheduleAtFixedRate(new Vendor(ticketPool, config.getTotalTickets(), vendorReleaseInterval, ticketCounter, logs, webSocketHandler, ticketVendorMap),
                    0, vendorReleaseInterval, TimeUnit.MILLISECONDS);
        }

        // Schedule customer tasks to start after a delay
        customerExecutorService.schedule(() -> {
            for (int i = 0; i < 10; i++) { // Example: 10 customers
                customerExecutorService.scheduleAtFixedRate(new Customer(ticketPool, customerRetrievalInterval, ticketsConsumed, config.getTotalTickets(), logs, webSocketHandler),
                        0, customerRetrievalInterval, TimeUnit.MILLISECONDS);
            }
        }, 5, TimeUnit.SECONDS); // 5 seconds delay

        return ResponseEntity.ok("System configured and running!");
    }

    private String createVendorLogMessage(int vendorId, int ticketId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "vendor");
        jsonObject.addProperty("vendorId", vendorId);
        jsonObject.addProperty("ticketId", ticketId);
        jsonObject.addProperty("poolSize", ticketPool.getSize());
        return gson.toJson(jsonObject);
    }

    private String createCustomerLogMessage(int customerId, int ticketId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "customer");
        jsonObject.addProperty("customerId", customerId);
        jsonObject.addProperty("ticketId", ticketId);
        jsonObject.addProperty("event", "Music Festival");
        jsonObject.addProperty("location", "Main Auditorium");
        jsonObject.addProperty("price", 50 + (int)(Math.random() * 3) * 50);
        jsonObject.addProperty("poolSize", ticketPool.getSize());
        return gson.toJson(jsonObject);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs() {
        synchronized (logs) {
            return ResponseEntity.ok(new ArrayList<>(logs));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Ticketing system is running.");
    }
}