package no.sysco.middleware.workshop.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.IntegerSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

/**
 *
 */
public class CompactionProducerApp {


  public static void main(String[] args) {
    //Configure Producer Properties
    final Properties config = new Properties();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

    //Instantiate a Producer
    final Producer<Integer, byte[]> producer = new KafkaProducer<>(config);

    //Define Random Key
    Random random = new Random();
    //Define 1K value
    byte[] value = new byte[1000];
    Arrays.fill(value, (byte) 1);

    //Create 100 records
    IntStream.rangeClosed(1, 100).boxed()
        .map(number ->
            new ProducerRecord<>("compaction", random.nextInt(10), value))
        .forEach(record -> {
          try {
            Future<RecordMetadata> futureMetadata = producer.send(record);
            RecordMetadata recordMetadata = futureMetadata.get();
            Map<String, Object> data = new HashMap<>();
            data.put("topic", recordMetadata.topic());
            data.put("partition", recordMetadata.partition());
            data.put("offset", recordMetadata.offset());
            data.put("key", record.key());
            System.out.println(data);
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }
        });
    producer.close();
  }
}
