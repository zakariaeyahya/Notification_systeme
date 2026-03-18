package com.example.notification.model;

import java.util.Map;

public class RawNotification {

    private String clientId;
    private String operationType;
    private Map<String, Object> metadata;

    public RawNotification() {}

    public RawNotification(String clientId, String operationType, Map<String, Object> metadata) {
        this.clientId = clientId;
        this.operationType = operationType;
        this.metadata = metadata;
    }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    @Override
    public String toString() {
        return "RawNotification{clientId='" + clientId + "', operationType='" + operationType + "', metadata=" + metadata + "}";
    }
}
