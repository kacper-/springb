package com.km.service;

import com.km.KafkaConfiguration;
import com.km.kafka.ConsumerRunner;
import com.km.kafka.ProducerRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaService {

    private final ProducerRunner producerRunner;
    private final ConsumerRunner consumerRunner;

    @Autowired
    public KafkaService(KafkaConfiguration kafkaConfiguration) {
        producerRunner = new ProducerRunner(kafkaConfiguration);
        consumerRunner = new ConsumerRunner(kafkaConfiguration);
    }

    public ProducerRunner getProducerRunner() {
        return producerRunner;
    }

    public ConsumerRunner getConsumerRunner() {
        return consumerRunner;
    }
}
