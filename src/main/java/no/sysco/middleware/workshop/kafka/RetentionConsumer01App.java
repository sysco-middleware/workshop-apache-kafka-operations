package no.sysco.middleware.workshop.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class RetentionConsumer01App {

  public static void main(String[] args) {
    //Configure Consumer Properties
    Properties config = new Properties();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "retention-consumer-app-01");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

    //Instantiate a Kafka Consumer
    final Consumer<Integer, byte[]> consumer = new KafkaConsumer<>(config);

    //Subscribe to `retention` topic
    consumer.subscribe(Collections.singletonList("retention"));

    try {
      //Create an infinite loop to poll messages from topics
      while (true) {
        //Poll messages
        final ConsumerRecords<Integer, byte[]> records = consumer.poll(Long.MAX_VALUE);

        //Consume messages
        for (ConsumerRecord<Integer, byte[]> record : records) {
          Map<String, Object> data = new HashMap<>();
          data.put("topic", record.topic());
          data.put("partition", record.partition());
          data.put("offset", record.offset());
          data.put("key", record.key());
          System.out.println(data);
        }

        //Commit offset to Kafka
        consumer.commitSync();
      }
    } catch (WakeupException e) {
      System.out.println("Shutting down");
    } finally {
      consumer.close();
    }
  }
}
