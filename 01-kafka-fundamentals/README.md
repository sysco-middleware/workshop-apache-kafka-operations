# Kafka Fundamentals

## Hands-on Lab: Installation

In this lab, you will install Apache Kafka locally. Then you will bring up services for Zookeeper, Kafka Brokers. 

### Download Apache Kafka

https://www.apache.org/dyn/closer.cgi?path=/kafka/1.1.0/kafka_2.11-1.1.0.tgz

Navigate through the main directories: 

* bin: command line tools
* config: configuration files
* libs: classpath libraries
* site-docs: documentation in HTML

### Configuration

#### Zookeeper

You will start a new instance of Zookeeper, in standalone mode:

```properties
# the directory where the snapshot is stored.
dataDir=zookeeper
# the port at which the clients will connect
clientPort=2181
# disable the per-ip limit on the number of connections since this is a non-production config
maxClientCnxns=0
```

Clients will be able to connect on port **2181** and data will be stored on `zookeeper` directory.

`dataDir` could be absolute or relative path.

#### Kafka Brokers

You will start 3 instances of Kafka Brokers:

```
broker.id=101
broker.rack=r1
...
offsets.topic.replication.factor=3
...
zookeeper.connect=localhost:2181
...
zookeeper.session.timeout.ms=30000
```

Large ZooKeeperCreate session timeout (default is 6000ms) to avoid dropped connections between the Brokers and ZooKeeper.

#### Log files

```
tail -f logs/server.log
```

#### Monitoring

Enable JMX on servers:

```
JMX_PORT=9990 bin/kafka-server-start.sh config/server101.properties
```

and execute JMX tool:

```
bin/kafka-run-class.sh kafka.tools.JmxTool \
--object-name kafka.server:type=app-info,id=101 \
--jmx-url service:jmx:rmi:///jndi/rmi://localhost:9991/jmxrmi
```

To validate Kafka version.

#### Command line tools

Create your first topic:

```
bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--create \
--topic my-first-topic \
--partitions 1 \
--replication-factor 1
```

List topics:

```
bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--list
```

Describe topic:

```
bin/kafka-topics.sh \
--zookeeper localhost:2181 \
--describe \
--topic my-first-topic
```

Produce messages from console:

```
bin/kafka-console-producer.sh \
--broker-list localhost:9092 \
--topic my-first-topic 

>Hello
>world
```

Consume messages from console:

```
bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic my-first-topic \
--from-beginning
```