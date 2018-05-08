# Kafka Operations

## Observing the Distributed Log

Create a new Topic called replicated-topic with six Partitions and two replicas.

```
bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--create \
--topic replicated-topic \
--partitions 6 \
--replication-factor 2
```

Describe Topic configuration.

```
bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--describe \
--topic replicated-topic
```

Produce some data: 

```
bin/kafka-producer-perf-test.sh \
--topic replicated-topic \
--num-records 6000 \
--record-size 100 \
--throughput 1000 \
--producer-props bootstrap.servers=localhost:9092,localhost:9093
```

On the Brokers, use the DumpLogSegments tool to view details of the .log file.

```
cd 
bin/kafka-run-class.sh kafka.tools.DumpLogSegments \
--print-data-log \
--files kafka-logs-101/replicated-topic-0/00000000000000000000.log \
--deep-iteration
```

View additional batch-level message metadata, like leader epoch for this Partition, in the .log file, this time
without using the --deep-iteration flag.

```
bin/kafka-run-class.sh kafka.tools.DumpLogSegments \
--print-data-log \
--files 00000000000000000000.log
```

### Taking one server offline/online

1. Kill broker 101

2. Describe a replicated topic and validate ISRs.

3. Restart broker 101

