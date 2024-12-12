package org.example;

public class Vendor extends Thread {
    private final int vendorId;
    private final TicketPool pool;
    private final int ticketReleaseRate;

    public Vendor(int vendorId, TicketPool pool, int ticketReleaseRate) {
        this.vendorId = vendorId;
        this.pool = pool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        while (!pool.isAllTicketsReleased()) { // Vendor stops when all tickets are released
            pool.addTicket(vendorId);
            try {
                Thread.sleep(ticketReleaseRate); // Simulate ticket release rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
