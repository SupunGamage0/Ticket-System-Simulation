package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHandler {
    private static final String LOG_FILE_PATH = "system.log";

    // Save configuration as JSON
    public static void saveConfigurationToJson(Configuration config, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(config, writer);
            System.out.println("Configuration saved to JSON file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving configuration to JSON: " + e.getMessage());
        }
    }

    // Save configuration as Text File
    public static void saveConfigurationToTextFile(Configuration config, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Total Tickets: " + config.getTotalTickets());
            writer.newLine();
            writer.write("Ticket Release Rate: " + config.getTicketReleaseRate() + " milliseconds");
            writer.newLine();
            writer.write("Customer Retrieval Rate: " + config.getCustomerRetrievalRate() + " milliseconds");
            writer.newLine();
            writer.write("Maximum Ticket Capacity: " + config.getMaxCapacity());
            writer.newLine();
            System.out.println("Configuration saved to text file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving configuration to text file: " + e.getMessage());
        }
    }

    // Save log entry
    public static void saveLog(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            String timestamp = getCurrentTimestamp();
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    // Log system start
    public static void logSystemStart(Configuration config) {
        saveLog("System started with the following configuration:");
        saveLog("  Total Tickets: " + config.getTotalTickets());
        saveLog("  Ticket Release Rate: " + config.getTicketReleaseRate() + " ms");
        saveLog("  Customer Retrieval Rate: " + config.getCustomerRetrievalRate() + " ms");
        saveLog("  Maximum Ticket Capacity: " + config.getMaxCapacity());
    }

    // Log system stop
    public static void logSystemStop() {
        saveLog("System has stopped. All tickets have been processed.");
    }

    // Log vendor actions
    public static void logVendorAction(int vendorId, Ticket ticket) {
        saveLog("Vendor " + vendorId + " added Ticket ID " + ticket.getTicketId() +
                " [Event: " + ticket.getEventName() + ", Price: $" + ticket.getPrice() + "].");
    }

    // Log customer actions
    public static void logCustomerAction(int customerId, Ticket ticket, int poolSize) {
        saveLog("Customer " + customerId + " bought Ticket ID " + ticket.getTicketId() +
                " [Event: " + ticket.getEventName() + ", Price: $" + ticket.getPrice() +
                "]. Remaining pool size: " + poolSize + ".");
    }

    // Log sold-out status
    public static void logSoldOutStatus() {
        saveLog("All tickets are sold out. System operations have completed.");
    }

    // Get current timestamp
    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
