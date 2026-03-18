package com.example.notification.api;

import com.example.notification.model.RawNotification;
import com.example.notification.producer.RawNotificationProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
public class NotificationController {

    private final RawNotificationProducer producer;

    public NotificationController(RawNotificationProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<String> submit(@RequestBody RawNotification notification) {
        producer.send(notification);
        return ResponseEntity.ok("Notification acceptée pour le client : " + notification.getClientId());
    }
}
