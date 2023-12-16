package com.km.kafka;

import com.km.KafkaConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ProducerRunner extends KafkaRunner {
    private static final Logger logger = LoggerFactory.getLogger(ProducerRunner.class);

    private final ScheduledExecutorService executor;
    private final KafkaProducer<String, String> producer;

    public ProducerRunner(KafkaConfiguration configuration) {
        super(configuration);
        executor = Executors.newSingleThreadScheduledExecutor();
        producer = new KafkaProducer<>(properties);
    }

    @Override
    protected Properties configure() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getServer());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    @Override
    public void start() {
        executor.scheduleAtFixedRate(this::sendMessage, 0L, 250L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        try {
            if (executor.awaitTermination(500L, TimeUnit.MILLISECONDS))
                logger.info("Kafka producing thread stopped in timely manner");
            else
                logger.warn("Kafka producing thread stopped forcefully");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        producer.flush();
        producer.close();
        logger.info("{} messages produced", counter);
    }

    private void sendMessage() {
        producer.send(new ProducerRecord<>(topic, "s_key", "s_value"));
        counter.incrementAndGet();
    }

}
