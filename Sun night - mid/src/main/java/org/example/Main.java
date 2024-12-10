package org.example;

import java.util.Scanner;

public class Main {
    private static boolean isRunning = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // System Configuration
        System.out.println("=== Ticket System Configuration ===");
        System.out.print("Enter total number of tickets: ");
        int totalTickets = scanner.nextInt();
        System.out.print("Enter ticket release rate (milliseconds): ");
        int ticketReleaseRate = scanner.nextInt();
        System.out.print("Enter customer retrieval rate (milliseconds): ");
        int customerRetrievalRate = scanner.nextInt();
        System.out.print("Enter maximum ticket capacity: ");
        int maxCapacity = scanner.nextInt();

        // Create Configuration object
        Configuration config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxCapacity);

        // Log configuration details
        FileHandler.logSystemStart(config);

        // Save configuration to files
        FileHandler.saveConfigurationToJson(config, "config.json");
        FileHandler.saveConfigurationToTextFile(config, "config.txt");

        // Initialize TicketPool
        TicketPool pool = new TicketPool(maxCapacity, totalTickets);

        // Wait for the start command
        System.out.println("\nSystem is ready. Enter 'start' to begin or 'stop' to exit.");

        Thread commandThread = new Thread(() -> monitorCommands(pool, ticketReleaseRate, customerRetrievalRate));
        commandThread.start();

        // Wait until the system is running
        synchronized (Main.class) {
            while (!isRunning) {
                try {
                    Main.class.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Initialize Vendors
        for (int i = 1; i <= 10; i++) {
            new Vendor(i, pool, ticketReleaseRate).start();
        }

        // Initialize Customers
        for (int i = 1; i <= 10; i++) {
            new Thread(new Customer(pool, customerRetrievalRate, i)).start();
        }

        // Wait until the system is stopped
        synchronized (Main.class) {
            while (isRunning) {
                try {
                    Main.class.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Log system stop
        FileHandler.logSystemStop();
        System.out.println("System stopped. All operations are complete.");
    }

    private static void monitorCommands(TicketPool pool, int ticketReleaseRate, int customerRetrievalRate) {
        Scanner scanner = new Scanner(System.in);
        boolean commandPrompt = true;
        while (true) {
            if (commandPrompt) {
                System.out.println("\nEnter a command (start/stop):");
            }
            String command = scanner.nextLine().trim().toLowerCase();

            if (command.equals("start")) {
                startSystem();
                commandPrompt = false; // Prevent showing the prompt again
            } else if (command.equals("stop")) {
                stopSystem();
                break;
            } else {
                System.out.println("Invalid command. Please enter 'start' or 'stop'.");
                commandPrompt = true; // Show prompt again for invalid commands
            }
        }
    }

    private static synchronized void startSystem() {
        if (isRunning) {
            System.out.println("System is already running.");
        } else {
            isRunning = true;
            System.out.println("System is starting...");
            synchronized (Main.class) {
                Main.class.notifyAll();
            }
        }
    }

    private static synchronized void stopSystem() {
        if (!isRunning) {
            System.out.println("System is not running.");
        } else {
            isRunning = false;
            System.out.println("Stopping the system...");
            synchronized (Main.class) {
                Main.class.notifyAll();
            }
        }
    }
}
