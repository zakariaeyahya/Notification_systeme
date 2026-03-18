package com.example.notification.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class SmsConsumer {

    @Value("${notification.output.sms}")
    private String outputFile;

    @KafkaListener(topics = "${kafka.topics.sms}", groupId = "sms-consumer-group")
    public void consume(String message) {
        System.out.println("[SmsConsumer] Reçu : " + message);
        writeToFile(message);
    }

    private void writeToFile(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("[SmsConsumer] Erreur écriture fichier : " + e.getMessage());
        }
    }
}
