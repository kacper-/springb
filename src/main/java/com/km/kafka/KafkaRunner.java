package com.km.kafka;

import com.km.KafkaConfiguration;

import java.util.Properties;

public abstract  class KafkaRunner implements Runnable {
    protected KafkaConfiguration kafkaConfiguration;
    protected boolean running;
    protected Properties properties;

    public KafkaRunner(KafkaConfiguration kafkaConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
        running = true;
        properties = configure();
    }

    public void shutdown() {
        running = false;
    }

    protected abstract Properties configure();

}
