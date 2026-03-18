package com.example.notification.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class EmailConsumer {

    @Value("${notification.output.email}")
    private String outputFile;

    @KafkaListener(topics = "${kafka.topics.email}", groupId = "email-consumer-group")
    public void consume(String message) {
        System.out.println("[EmailConsumer] Reçu : " + message);
        writeToFile(message);
    }

    private void writeToFile(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("[EmailConsumer] Erreur écriture fichier : " + e.getMessage());
        }
    }
}
