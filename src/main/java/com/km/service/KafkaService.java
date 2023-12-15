package com.km.service;

import com.km.KafkaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaService {

    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    public KafkaService() {

    }

    public void printConfig() {
        System.out.println(kafkaConfiguration.getConsumer());
    }


}
