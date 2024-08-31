# Evaluation lab - Apache Kafka
## Overview

This project explores the capabilities of Apache Kafka, focusing on message consumption and topic management. It includes two exercises that demonstrate different Kafka consumer behaviors and configurations.

## Project Structure

- **Exercise 1: AtMostOncePrinter**
- **Exercise 2: PopularTopicConsumer**

### Exercise 1: AtMostOncePrinter

The `AtMostOncePrinter` class is a Kafka consumer that subscribes to the `inputTopic`. This consumer is designed to ensure "at most once" delivery semantics, meaning each message is processed at most once, even in case of failures.

#### Key Features:

- **Topic**: `inputTopic`
- **Message Key**: `String`
- **Message Value**: `Integer`
- **Functionality**: The consumer prints messages to the standard output only if their value exceeds a predefined threshold.
- **Reliability**: Ensures that messages are printed at most once. Messages may be missed only in the case of a failure, but duplicates are never printed.


### Exercise 2: PopularTopicConsumer
The PopularTopicConsumer class is a Kafka consumer designed to compute the number of messages received for each topic and print the topic(s) with the highest message count so far.

#### Key Features:
- Functionality: Upon receiving a new message, this consumer updates its count of messages per topic and prints the topic(s) that have received the highest number of messages up to that point.
- No Failure Guarantees: This consumer does not guarantee accuracy in the case of failures; messages may be lost or counted multiple times if a failure occurs.
