package com.example.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.raw}")
    private String rawTopic;

    @Value("${kafka.topics.sms}")
    private String smsTopic;

    @Value("${kafka.topics.email}")
    private String emailTopic;

    @Value("${kafka.topics.push}")
    private String pushTopic;

    @Bean
    public NewTopic rawNotificationsTopic() {
        return TopicBuilder.name(rawTopic).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic notificationsSMSTopic() {
        return TopicBuilder.name(smsTopic).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic notificationsEmailTopic() {
        return TopicBuilder.name(emailTopic).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic notificationsPushTopic() {
        return TopicBuilder.name(pushTopic).partitions(1).replicas(1).build();
    }
}
