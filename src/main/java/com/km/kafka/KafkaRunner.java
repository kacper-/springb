package com.km.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.km.KafkaConfiguration;
import com.km.repository.DBMsgRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract class meant to be extended by actual kafka producer or consumer
 *
 * Provides kafka configuration
 *
 * Allows to start and stop background processing thread
 * Allows to count processed messages
 */
public abstract class KafkaRunner {
    protected static final Logger logger = LoggerFactory.getLogger(ProducerRunner.class);
    protected final Properties properties;
    protected final String topic;
    protected final AtomicInteger counter = new AtomicInteger(0);
    protected volatile boolean running;
    protected KafkaConfiguration kafkaConfiguration;
    protected DBMsgRepository dbMsgRepository;
    protected ObjectMapper mapper = new ObjectMapper();

    /**
     * Abstract class for both kafka producer and consumer
     * @param configuration kafka configuration POJO object
     * @param repository messages table repo interface - used only by kafka consumer
     */
    public KafkaRunner(KafkaConfiguration configuration, DBMsgRepository repository) {
        kafkaConfiguration = configuration;
        dbMsgRepository = repository;
        topic = kafkaConfiguration.getTopic();
        properties = configure();
    }

    protected abstract Properties configure();

    /**
     * Starts background producer of consumer thread
     */
    public abstract void start();

    /**
     * Stops background producer or consumer thread
     */
    public abstract void stop();

    /**
     * Gets number of processed messages
     * @return number of processed messages
     */
    public int getCounter() {
        return counter.get();
    }

    /**
     * Indicates if producer or consumer is running
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }
}
