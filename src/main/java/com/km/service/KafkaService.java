package com.km.service;

import com.km.KafkaConfiguration;
import com.km.kafka.ConsumerRunner;
import com.km.kafka.ProducerRunner;
import com.km.repository.DBMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class KafkaService {

    private final ProducerRunner producerRunner;
    private final ConsumerRunner consumerRunner;

    @Autowired
    public KafkaService(KafkaConfiguration configuration, DBMsgRepository repository) {
        producerRunner = new ProducerRunner(configuration, repository);
        consumerRunner = new ConsumerRunner(configuration, repository);
    }

    public ProducerRunner getProducerRunner() {
        return producerRunner;
    }

    public ConsumerRunner getConsumerRunner() {
        return consumerRunner;
    }
}
