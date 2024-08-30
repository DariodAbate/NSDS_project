package it.polimi.nsds.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;

public class AtMostOncePrinter {
    private static final String topic = "inputTopic";
    private static final String serverAddr = "localhost:9092";
    private static final int threshold = 500;
    private static final boolean autoCommit = false; //the offset are  not automatically committed

    private static final String offsetResetStrategy = "latest";


    public static void main(String[] args) {
        String groupId = args[0];

        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddr);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        //autocommit
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(autoCommit));

        //reset strategy
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetResetStrategy);

        //deserializer
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//from string
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());//from integer


        KafkaConsumer<String, Integer> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            final ConsumerRecords<String, Integer> records = consumer.poll(Duration.of(5, ChronoUnit.MINUTES));
            for (final ConsumerRecord<String, Integer> record : records) {

                consumer.commitSync();

                if (record.value() > threshold) {
                    System.out.println("Partition: " + record.partition() +
                            "\tOffset: " + record.offset() +
                            "\tKey: " + record.key() +
                            "\tValue: " + record.value()
                    );
                }
            }

        }
    }
}
