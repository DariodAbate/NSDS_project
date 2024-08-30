package it.polimi.nsds.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class PopularTopicConsumer {
    private static final String topic = "inputTopic";
    private static final String serverAddr = "localhost:9092";

    private static final boolean autoCommit = true; //offset are automatically committed
    private static final int autoCommitIntervalMs = 15000;
    private static final String offsetResetStrategy = "latest";

    public static void main(String[] args) {
        String groupId = args[0];

        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddr);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//from string
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());//from integer

        //enable autocommit
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(autoCommit));
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(autoCommitIntervalMs));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetResetStrategy);

        KafkaConsumer<String, Integer> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        HashMap<String, Integer> map = new HashMap<>();
        while (true) {
            final ConsumerRecords<String, Integer> records = consumer.poll(Duration.of(5, ChronoUnit.MINUTES));
            for (final ConsumerRecord<String, Integer> record : records) {

                //update the counter
                if(! map.containsKey(record.key()))
                    map.put(record.key(), 1);
                else
                    map.put(record.key(), map.get(record.key()) + 1);

                ArrayList<String> maxKeys = computeMax(map);
                System.out.println("Max value: " + map.get(maxKeys.get(0)));
                for (String elem : maxKeys) {
                    System.out.print(elem + " ");
                }
                System.out.println();
                System.out.println("-----------------------");

            }
        }

    }

    public static ArrayList<String> computeMax(HashMap<String, Integer> map) {
        int max = 0;
        ArrayList<String> list = new ArrayList<>();

        for (String key: map.keySet()) {
            if (map.get(key) > max) {
                max = map.get(key);
            }
        }

        for (String key: map.keySet()) {
            if (map.get(key) == max) {
                list.add(key);
            }
        }

        return list;
    }
}
