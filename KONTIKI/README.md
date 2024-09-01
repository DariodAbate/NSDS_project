# Evaluation lab - Contiki-NG

## Project Overview
This project implements a simple sensing application. The application consists of a UDP server and multiple UDP clients that simulate temperature sensing and reporting.

### Client-Server Interaction
1. **Client Behavior**:
   - Each client generates a temperature reading every minute using the `get_temperature()` function.
   - If the client is unable to reach the server, it temporarily stores the temperature readings in a circular array with a capacity defined by `MAX_READINGS`.
   - Upon re-establishing a connection:
     - If the client was already connected before the disconnection, it sends the most recent temperature reading to the server.
     - If the client has just reconnected, it computes the average of the stored readings and sends this average to the server. After this, the local storage is flushed.

2. **Server Behavior**:
   - The server accepts connections from clients and stores their IP addresses, up to a maximum of `MAX_RECEIVERS`. Any clients beyond this limit are ignored.
   - Upon receiving a temperature reading from a client, the server:
     - Updates its list of the last `MAX_READINGS` readings.
     - Computes the average of all received readings.
     - If the average temperature exceeds a defined `ALERT_THRESHOLD`, the server sends a "high temperature alert" to all connected clients.

