# Evaluation lab - Apache Kafka

## Group number: 17

## Group members
- Dario d'Abate 
- Lorenzo Corrado
- Filippo Ranieri Pantaleone

## Exercise 1

- Number of partitions allowed for inputTopic (1, n)
- Number of consumers allowed (1, n) "in a group"
    - Consumer 1: "Group1"
    - Consumer 2: "Group1"
    - ...
    - Consumer n: "Group1"

## Exercise 2

- Number of partitions allowed for inputTopic (1, n)
- Number of consumers allowed (1, 1) "in a group"
    - Consumer 1: "Group2"

Otherwise with our implementations we cannot receive all messages of a topic, since the messages will
be partitioned among consumers within a group.