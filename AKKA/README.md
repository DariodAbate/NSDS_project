# Evaluation lab - Akka

## Group number: 17

## Group members

- Lorenzo Corrado 
- Dario D'Abate
- Filippo Ranieri Pantaleone

## Description of message flows
The following list shows a typical flow of messages from a sender to a receiver.
The sender is on the left, while the receiver is on the right.
The class name in the middle states the message that is sent.

DispatcherActor -(*SensorAddressMsg*)-> SensorProcessorActor

DispatcherActor -(*GenerateMsg*)-> TemperatureSensorActor

TemperatureSensorActor -(*TemperatureMsg*)-> DispatcherActor

DispatcherActor -(*TemperatureMsg*)-> SensorProcessorActor

---------

TemperatureSensorFaultyActor -(*TemperatureMsg*)-> DispatcherActor

DispatcherActor -(*TemperatureMsg*)-> SensorProcessorActor

SensorProcessorActor -(*Exception*)-> DispatcherActor+

---------

Main class -(*DispatchLogicMsg*)-> DispatcherActor