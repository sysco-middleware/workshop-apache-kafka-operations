# Kafka Operations

## Delete a Topic 

```
bin/kafka-topics.sh \
--zookeeper zookeeper1:2181 \
--delete \
--topic replicated-topic
```

## Create Topic and Test Consumer Groups

Create a topic:

```
bin/kafka-topics.sh \
--zookeeper zookeeper1:2181 \
--create \
--topic grow-topic \
--partitions 6 \
--replication-factor 3
```

Generate data:

```
bin/kafka-producer-perf-test.sh \
--topic grow-topic \
--num-records 1000 \
--record-size 100 \
--throughput 100 \
--producer-props bootstrap.servers=localhost:9092,localhost:9093
```

Create a Console Consumer:

```
bin/kafka-console-consumer.sh \
--consumer-property group.id=test-consumer-group \
--from-beginning \
--topic grow-topic \
--bootstrap-server localhost:9092,localhost:9093
```

Describe Consumer Group.

Restart offsets to the beginning and reprocess the topic.

## Simulate a Broker lost

1. Kill broker 103

2. Remove kafka data directory for broker 103

3. Watch Kafka topic describe to check how it gets updated until become ISR