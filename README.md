# Multithreaded Web Server with JMeter Load Testing

This project implements a simple multithreaded TCP-based web server in Java, along with a client to simulate multiple concurrent requests. The server handles incoming client connections using separate threads, while the client spawns multiple threads to send requests. Additionally, JMeter is configured to perform load testing on the server to measure its performance under high traffic (e.g., 60,000 requests in 1 minute).

## Project Overview

- **Server**: A multithreaded TCP server that listens on port `8010` and responds with a simple "Hello" message to each client.
- **Client**: A multithreaded TCP client that simulates up to 60,000 concurrent connections to the server using a thread pool.
- **Load Testing**: JMeter configuration to test the server’s performance under 1,000 requests per second (RPS).

### Files
- `Server.java`: Implements the multithreaded TCP server.
- `Client.java`: Implements the client to send concurrent requests to the server.

## Prerequisites
- Java Development Kit (JDK) 8 or higher installed.
- JMeter installed for load testing (optional, see JMeter section below).

## How to Run

1. **Start the Server**  
   - Compile and run `Server.java`:  
     ```bash
     javac Server.java
     java Server
     ```
   - The server will start listening on `localhost:8010`.

2. **Run the Client**  
   - Compile and run `Client.java`:  
     ```bash
     javac Client.java
     java Client
     ```
   - The client will spawn 20,000 threads (using a thread pool of 100) to send requests to the server. Each thread retries up to 5 times with a 2-second delay between attempts.

3. **Expected Output**  
   - Server: Prints "Server is listening on port 8010" and handles incoming requests.  
   - Client: Prints responses from the server like "Response from Server Hello from server /127.0.0.1".

## Server Details
- **Port**: Listens on `8010`.
- **Multithreading**: Each client connection is handled in a separate thread for concurrency.
- **Timeout**: Server socket timeout is set to 70 seconds.
- **Response**: Sends "Hello from server" with the client’s IP address to each connected client.

## Client Details
- **Thread Pool**: Uses `ExecutorService` with a fixed thread pool of 100 to manage up to 20,000 requests.
- **Retries**: Each thread attempts to connect up to 5 times with a 2-second delay if the connection fails.
- **Request**: Sends "Hello from Client" with its local socket address to the server.

## JMeter Configuration Steps

To test the server’s performance under heavy load, use JMeter with the following configuration:

1. **Install JMeter**  
   - Download and install JMeter on your system. You can find installation instructions online.

2. **Create a Test Plan**  
   - Open JMeter and create a new Test Plan.  
   - Give it a name (e.g., "LoadTestPlan").

3. **Add a Thread Group**  
   - Right-click on the Test Plan, then go to `Add > Threads (Users) > Thread Group`.  
   - Configure the Thread Group:  
     - **Number of Threads (Users):** Set to 60,000 (this is the number of users/requests to spawn).  
     - **Ramp-Up Period:** Set to 60 seconds (this means 60,000 requests will be sent in 1 minute, roughly 1,000 requests per second).  
     - **Loop Count:** Set to 1 (or as needed).

4. **Add a TCP Sampler**  
   - Right-click on the Thread Group, then go to `Add > Sampler > TCP Sampler`.  
   - Configure the TCP Sampler:  
     - **Server Name or IP:** Set to `localhost` (or your server’s IP).  
     - **Port Number:** Set to `8010` (or your server’s port).  
     - Leave other settings as default unless specific changes are needed.

5. **Add Listeners to View Results**  
   - Right-click on the Thread Group and add the following listeners:  
     - **View Results Tree:** Go to `Add > Listener > View Results Tree` (to see detailed request/response data).  
     - **View Results in Table:** Go to `Add > Listener > View Results in Table` (to view data in tabular form).  
     - **Graph Results:** Go to `Add > Listener > Graph Results` (to visualize throughput and performance graphically).

6. **Run the Test**  
   - Save your Test Plan.  
   - Start your server (e.g., `java Server`).  
   - Click the green "Start" button in JMeter to run the test.  
   - Monitor the results in the listeners to see how your server handles 60,000 requests in 1 minute (1,000 RPS).

## Notes
- The server is lightweight and responds quickly to "Hello" requests, making it suitable for high concurrency testing.
- For heavier workloads (e.g., file reading), expect potential timeouts or delays due to thread blocking.
- Adjust the thread pool size in `Client.java` or JMeter’s thread count based on your system’s capacity to avoid crashes.
