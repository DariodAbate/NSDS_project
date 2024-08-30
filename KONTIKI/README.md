# Evaluation lab - Contiki-NG

## Group number: 17

## Group members

- Dario d'Abate 
- Lorenzo Corrado
- Filippo Ranieri Pantaleone

## Solution description
The server accepts connections from clients: it stores the IP_address of each client, up to MAX_RECEIVERS, so that the messages of unhandled clients are ignored.

When the client cannot reach the server, it starts batching the generated temperatures: the storage is a circular array of size MAX_READINGS. 

When the client can reach the server, we have two situations:
- the client was already connected, therefore it sends a single temperature;
- the client just reconnected after a temporary disconnection, therefore it sends an average temperature. The latter is computed on the batch of readings. The local storage is then flushed.

