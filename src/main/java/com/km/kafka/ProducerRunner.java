package com.km.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.km.KafkaConfiguration;
import com.km.model.Message;
import com.km.repository.DBMsgRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ProducerRunner extends KafkaRunner {
    private boolean running = false;
    private final KafkaProducer<String, String> producer;
    private final ScheduledExecutorService executor;

    public ProducerRunner(KafkaConfiguration configuration, DBMsgRepository repository) {
        super(configuration, repository);
        producer = new KafkaProducer<>(properties);
        executor = Executors.newSingleThreadScheduledExecutor();
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
        running = true;
        executor.scheduleAtFixedRate(this::sendMessage, 0L, 1000L, TimeUnit.MILLISECONDS);
        logger.info("Kafka producer started");
    }

    @Override
    public void stop() {
        try {
            if (executor.awaitTermination(500L, TimeUnit.MILLISECONDS))
                logger.info("Kafka producer stopped in timely manner");
            else
                logger.warn("Kafka producer stopped forcefully");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        producer.flush();
        running = false;
        logger.info("{} messages produced", counter);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void sendMessage() {
        producer.send(new ProducerRecord<>(topic, UUID.randomUUID().toString(), createMessage()));
        counter.incrementAndGet();
    }

    private String createMessage() {
        Message message = new Message(UUID.randomUUID().toString(), new Random().nextBoolean());
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            logger.error("Cannot serialize to JSON");
            return "";
        }
    }
}
