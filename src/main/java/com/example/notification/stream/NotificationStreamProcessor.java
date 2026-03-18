package com.example.notification.stream;

import com.example.notification.model.RawNotification;
import com.example.notification.model.RoutedNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.Map;

@Configuration
@EnableKafkaStreams
public class NotificationStreamProcessor {

    @Value("${kafka.topics.raw}")
    private String rawTopic;

    @Value("${kafka.topics.sms}")
    private String smsTopic;

    @Value("${kafka.topics.email}")
    private String emailTopic;

    @Value("${kafka.topics.push}")
    private String pushTopic;

    @Autowired
    private UserLookupService userLookupService;

    @Autowired
    private PreferenceLookupService preferenceLookupService;

    @Autowired
    private TemplateEngine templateEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public KStream<String, String> notificationStream(StreamsBuilder builder) {

        // Tout en String — désérialisation manuelle avec ObjectMapper
        // Évite les problèmes de headers __TypeId__ du JsonSerializer Spring Kafka
        KStream<String, String> rawStream = builder.stream(
                rawTopic,
                Consumed.with(Serdes.String(), Serdes.String())
        );

        KStream<String, String> routedStream = rawStream.mapValues(rawJson -> {
            try {
                RawNotification raw = objectMapper.readValue(rawJson, RawNotification.class);

                String clientId = raw.getClientId();
                Map<String, Object> user = userLookupService.findById(clientId);
                String channel = preferenceLookupService.getPreferredChannel(clientId);
                String content = templateEngine.generate(raw.getOperationType(), raw.getMetadata());
                String recipient = resolveRecipient(user, channel);

                System.out.println("[Stream] Client=" + clientId + " | Canal=" + channel + " | Contenu=" + content);

                RoutedNotification routed = new RoutedNotification(clientId, channel, recipient, content);
                return objectMapper.writeValueAsString(routed);

            } catch (Exception e) {
                System.err.println("[Stream] Erreur traitement message : " + e.getMessage() + " | JSON=" + rawJson);
                return null;
            }
        });

        // Filtrer les nulls (erreurs de traitement)
        KStream<String, String> validStream = routedStream.filter((key, val) -> val != null);

        // Router vers le bon topic selon le canal
        validStream
                .filter((key, val) -> val.contains("\"channel\":\"SMS\""))
                .to(smsTopic, Produced.with(Serdes.String(), Serdes.String()));

        validStream
                .filter((key, val) -> val.contains("\"channel\":\"EMAIL\""))
                .to(emailTopic, Produced.with(Serdes.String(), Serdes.String()));

        validStream
                .filter((key, val) -> val.contains("\"channel\":\"PUSH\""))
                .to(pushTopic, Produced.with(Serdes.String(), Serdes.String()));

        return rawStream;
    }

    private String resolveRecipient(Map<String, Object> user, String channel) {
        if (user == null) return "inconnu";
        return switch (channel) {
            case "SMS"   -> (String) user.getOrDefault("phone", "inconnu");
            case "EMAIL" -> (String) user.getOrDefault("email", "inconnu");
            case "PUSH"  -> (String) user.getOrDefault("pushToken", "inconnu");
            default      -> "inconnu";
        };
    }
}
