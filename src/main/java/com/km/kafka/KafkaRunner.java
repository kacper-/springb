package com.km.kafka;

import com.km.KafkaConfiguration;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class KafkaRunner {
    protected KafkaConfiguration kafkaConfiguration;
    protected boolean running;
    protected Properties properties;
    protected final String topic;
    protected final AtomicInteger counter = new AtomicInteger(0);

    public KafkaRunner(KafkaConfiguration kafkaConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
        topic = kafkaConfiguration.getTopic();
        running = true;
        properties = configure();
    }

    protected abstract Properties configure();

    public abstract void start();

    public abstract void stop();

    public int getCounter() {
        return counter.get();
    }
}
