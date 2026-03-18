package com.example.notification.config;

import org.apache.kafka.streams.errors.MissingSourceTopicException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;

@Configuration
public class KafkaStreamsConfig {

    /**
     * Au lieu de SHUTDOWN_CLIENT (comportement par défaut),
     * on fait REPLACE_THREAD → Kafka Streams retente automatiquement
     * quand un topic source est manquant au démarrage.
     */
    @Bean
    public StreamsBuilderFactoryBeanConfigurer kafkaStreamsCustomizer() {
        return factoryBean -> factoryBean.setKafkaStreamsCustomizer(kafkaStreams ->
            kafkaStreams.setUncaughtExceptionHandler(exception -> {
                if (exception instanceof MissingSourceTopicException
                        || (exception.getCause() instanceof MissingSourceTopicException)) {
                    System.err.println("[KafkaStreams] Topic source manquant, retry dans 10s...");
                    return org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler
                            .StreamThreadExceptionResponse.REPLACE_THREAD;
                }
                return org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler
                        .StreamThreadExceptionResponse.SHUTDOWN_CLIENT;
            })
        );
    }
}
