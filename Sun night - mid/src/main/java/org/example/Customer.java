package org.example;

public class Customer implements Runnable {
    private final int retrievalRate;
    private final TicketPool ticketPool;
    private final int customerId;

    public Customer(TicketPool ticketPool, int retrievalRate, int customerId) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        while (!ticketPool.isAllTicketsSold()) {
            Ticket ticket = ticketPool.buyRandomTicket(customerId);
            try {
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
