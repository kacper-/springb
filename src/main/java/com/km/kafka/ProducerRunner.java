package com.km.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
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

/**
 * Kafka producer class managing background thread that creates random messages at fixed rate
 */
public class ProducerRunner extends KafkaRunner {
    private static final long PRODUCE_INTERVAL = 1000;
    private static final long TIMEOUT = 250;
    private final KafkaProducer<String, String> producer;
    private ScheduledExecutorService executor;

    public ProducerRunner(KafkaConfiguration configuration, DBMsgRepository repository) {
        super(configuration, repository);
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
        counter.set(0);
        running = true;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::sendMessage, 0, PRODUCE_INTERVAL, TimeUnit.MILLISECONDS);
        logger.info("Kafka producer started");
    }

    @Override
    public void stop() {
        running = false;
        executor.shutdown();
        try {
            if (executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS))
                logger.info("Kafka producer stopped in timely manner");
            else
                logger.warn("Kafka producer stopped forcefully");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        producer.flush();
        logger.info("{} messages produced", counter);
    }

    /**
     * Sends random message to a kafka queue at configured topic
     */
    private void sendMessage() {
        producer.send(new ProducerRecord<>(topic, UUID.randomUUID().toString(), createMessage()));
        counter.incrementAndGet();
    }

    /**
     * Creates random message and serializes it to a json string
     * @return json string representing message value
     */
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
