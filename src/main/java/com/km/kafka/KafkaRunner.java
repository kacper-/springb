package com.km.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.km.KafkaConfiguration;
import com.km.repository.DBMsgRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class KafkaRunner {
    protected static final Logger logger = LoggerFactory.getLogger(ProducerRunner.class);
    protected final Properties properties;
    protected final String topic;
    protected final AtomicInteger counter = new AtomicInteger(0);
    protected volatile boolean running;
    protected KafkaConfiguration kafkaConfiguration;
    protected DBMsgRepository dbMsgRepository;
    protected ObjectMapper mapper = new ObjectMapper();

    public KafkaRunner(KafkaConfiguration configuration, DBMsgRepository repository) {
        kafkaConfiguration = configuration;
        dbMsgRepository = repository;
        topic = kafkaConfiguration.getTopic();
        properties = configure();
    }

    protected abstract Properties configure();

    public abstract void start();

    public abstract void stop();

    public int getCounter() {
        return counter.get();
    }

    public boolean isRunning() {
        return running;
    }
}
