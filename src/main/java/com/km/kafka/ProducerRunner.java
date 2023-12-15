package com.km.kafka;

import com.km.KafkaConfiguration;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


public class ProducerRunner extends KafkaRunner {

    public ProducerRunner(KafkaConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected Properties configure() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getConsumer());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    @Override
    public void run() {
        while (running) {
            // TODO implement
        }
    }
}
