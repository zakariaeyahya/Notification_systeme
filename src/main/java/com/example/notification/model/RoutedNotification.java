package com.example.notification.model;

import java.time.Instant;

public class RoutedNotification {

    private String clientId;
    private String channel;
    private String recipient;
    private String content;
    private String timestamp;

    public RoutedNotification() {}

    public RoutedNotification(String clientId, String channel, String recipient, String content) {
        this.clientId = clientId;
        this.channel = channel;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = Instant.now().toString();
    }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "RoutedNotification{clientId='" + clientId + "', channel='" + channel +
               "', recipient='" + recipient + "', content='" + content + "', timestamp='" + timestamp + "'}";
    }
}
