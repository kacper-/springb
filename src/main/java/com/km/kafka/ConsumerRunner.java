package com.km.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.km.KafkaConfiguration;
import com.km.model.DBMsg;
import com.km.model.Message;
import com.km.repository.DBMsgRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsumerRunner extends KafkaRunner {

    private final KafkaConsumer<String, String> consumer;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ConsumerRunner(KafkaConfiguration configuration, DBMsgRepository repository) {
        super(configuration, repository);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));
        logger.info("Subscribed to {}", topic);
    }

    @Override
    protected Properties configure() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getServer());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfiguration.getConsumer());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    @Override
    public void start() {
        running.set(true);
        new Thread(this::consumeMessages).start();
        logger.info("Kafka consumer started");
    }

    @Override
    public void stop() {
        running.set(false);
        consumer.wakeup();
        logger.info("Kafka consumer stopped");
        logger.info("{} messages consumed", counter);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    private void consumeMessages() {
        try {
            while (running.get()) {
                saveToDB(consumer.poll(Duration.ofMillis(1000)));
            }
        } catch (WakeupException e) {
            if (running.get())
                logger.error("Unexpected Kafka consumer state");
        }
    }

    private void saveToDB(ConsumerRecords<String, String> records) {
        if(records == null)
            return;

        for(ConsumerRecord<String, String> record : records) {
            try {
                Message message = mapper.readValue(record.value(), Message.class);
                dbMsgRepository.save(new DBMsg(null, record.key(), message.getVal(), message.isStatus()));
            } catch (JsonProcessingException e) {
                logger.error("Cannot deserialize from JSON");
            } finally {
                counter.incrementAndGet();
            }
        }
    }
}
