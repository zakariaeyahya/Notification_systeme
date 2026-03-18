package com.example.notification.producer;

import com.example.notification.model.RawNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RawNotificationProducer {

    // KafkaTemplate<String, String> — on envoie du JSON pur, sans headers de type
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.topics.raw}")
    private String rawTopic;

    public RawNotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(RawNotification notification) {
        try {
            String json = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(rawTopic, notification.getClientId(), json);
            System.out.println("[Producer] Envoyé sur " + rawTopic + " : " + json);
        } catch (Exception e) {
            System.err.println("[Producer] Erreur sérialisation : " + e.getMessage());
        }
    }
}
