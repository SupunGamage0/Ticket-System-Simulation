# Ticket System Simulation

This project simulates a dynamic ticket vending and purchasing system, providing a visual and interactive experience through a React-based frontend, a robust Spring Boot backend, and a standalone Java command-line interface.

## Frontend (React): 

## 🔗 Live Demo
[Ticket System Simulation](https://ticket-system-simulation.vercel.app/)

---

The frontend, built with React, offers a user-friendly interface to configure, control, and monitor the ticket system simulation. Watch in real-time as vendors add tickets and customers eagerly buy them!

### Features

*   **Intuitive Configuration:** Easily set up the simulation parameters using the **Configuration Form**. Define the total number of tickets, vendor release rate, customer retrieval rate, and the maximum ticket pool capacity.
*   **Real-time Control:** Start and stop the simulation with the click of a button using the **Control Panel**.
*   **Dynamic Log Display:**  Observe the simulation unfold with the **Log Display**, providing a live feed of vendor and customer actions, keeping you informed every step of the way.
*   **Visual Ticket Status:** Get an instant overview of the ticket pool with the **Ticket Status** component. It displays:
    *   A visually appealing progress bar indicating the pool's fullness.
    *   The number of available tickets versus the maximum capacity.
    *   The number of tickets remaining to be released.
    *   A clear "Sold Out!" message when all tickets are gone.

### Getting Started with the Frontend

1. **Prerequisites:**
    *   Make sure you have Node.js and npm (or Yarn) installed on your system.
2. **Navigate to the Frontend Directory:**
    ```bash
    cd frontend
    ```
3. **Install Dependencies:**
    ```bash
    npm install
    ```
4. **Start the Development Server:**
    ```bash
    npm start
    ```
    This will launch the React app in your browser, usually at `http://localhost:3000`.

## CLI (Java Command-Line Interface): The Simulation Engine

Experience the core logic of the ticket system through a standalone Java command-line interface. Configure, run, and observe the simulation in your terminal.

### Features

*   **Text-Based Configuration:** Define the simulation parameters directly in your terminal when launching the CLI.
*   **Detailed Logging:**  The CLI outputs comprehensive logs to the console, detailing every vendor addition and customer purchase, including ticket IDs and pool status.
*   **Self-Contained Simulation:** The CLI runs independently of the frontend and backend, making it ideal for quick testing and understanding the core mechanics.

### Running the CLI

1. **Prerequisites:**
    *   Ensure you have Java Development Kit (JDK) 8 or later installed.
2. **Navigate to the CLI Directory:**
    ```bash
    cd cli
    ```
3. **Compile the Code:**
    ```bash
    javac src/main/java/org/example/*.java
    ```
4. **Run the Simulation:**
    ```bash
    java -cp src/main/java org.example.Main
    ```
    The CLI will prompt you to enter the configuration parameters.

## Backend (Spring Boot): The Powerhouse of the System

The Spring Boot backend serves as the central hub, powering the simulation logic, managing the ticket pool, and providing real-time updates to the frontend via WebSockets.

### Features

*   **REST API:** Exposes endpoints to configure the system, retrieve logs, and get the current system status.
*   **WebSocket Server:** Enables real-time communication with the frontend, pushing updates on ticket events as they happen.
*   **Robust Ticket Management:**  Handles concurrent ticket additions by vendors and retrievals by customers, ensuring data consistency.
*   **Rate Limiting:**  Enforces the configured ticket release and customer retrieval rates.

### Setting up the Backend

1. **Prerequisites:**
    *   Java Development Kit (JDK) 17 or later
    *   Maven
2. **Navigate to the Backend Directory:**
    ```bash
    cd backend
    ```
3. **Build the Project:**
    ```bash
    mvn clean install
    ```
4. **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```
    The backend will start on `http://localhost:8080`.

### API Endpoints

*   **`POST /api/configure`:**
    *   Configure the system parameters.
    *   Request body example:
        ```json
        {
            "totalTickets": 100,
            "ticketReleaseRate": 5,
            "customerRetrievalRate": 7,
            "maxTicketCapacity": 10
        }
        ```

*   **`GET /api/logs`:** Get the current system logs.
*   **`GET /api/status`:** Get the current system status.

### WebSocket Endpoint

*   **`ws://localhost:8080/ticket-status`:**  Connect to this endpoint to receive real-time updates on ticket operations.


