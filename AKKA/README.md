# Evaluation lab - Akka
## Description of the problem

## Type of messages
This project consists in implementing a simple sensor data processing system usign actors. There are three different actor types:
- `Sensor data processor actor`, which mantain the average of the sensor reading it receives
- `Dispatcher actor`, which distribute incoming sensor readings according to different policies (load balancing / round robin)
- `Temperature actor`, which generate temperature readings
In the system there is a single `Dispatcher actor`, and a variable number of both `Sensor data processor actor` and `Temperature actor`.
## Message types
- `TemperatureMsg`, which carries temperature readings
- `DispatchLogic`, which tells the dispatcher what dispatching logic to use (load balancing / round robin)
- `GenerateMsg`, which tells the temperature sensors to generate a new reading
## Failures
When a `Sensor data processor actor` receives a negative temperature reading, it fails. The failure is handled in a way that the temperature before the failure is retained, while the faulty temperature is excluded from the computation.
## Description of message flows
The following list shows a typical flow of messages from a sender to a receiver.
The sender is on the left, while the receiver is on the right.
The class name in the middle states the message that is sent.

1. **Dispatcher actor registration to remperature actor**
   - **DispatcherActor** → `SensorAddressMsg` → **TemperatureSensorActor**
       
2. **Dispatch Logic Setup**
   - **Main Class** → `DispatchLogicMsg` → **DispatcherActor**

3. **Temperature Generation Request**
   - **DispatcherActor** → `GenerateMsg` → **TemperatureSensorActor**

4. **Temperature Reading Generation**
   - **TemperatureSensorActor** → `TemperatureMsg` → **DispatcherActor**

5. **Temperature Dispatch**
   - **DispatcherActor** → `TemperatureMsg` → **SensorProcessorActor**

6. **Handling Faulty Sensors**
   - **TemperatureSensorFaultyActor** → `TemperatureMsg` → **DispatcherActor**

   - **DispatcherActor** → `TemperatureMsg` → **SensorProcessorActor**

   - **SensorProcessorActor** → `Exception` → **DispatcherActor**

