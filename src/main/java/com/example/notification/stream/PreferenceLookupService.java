package com.example.notification.stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PreferenceLookupService {

    private final Map<String, String> channelByClientId = new HashMap<>();

    @PostConstruct
    public void load() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/preferences.json");
        List<Map<String, Object>> prefs = mapper.readValue(is, new TypeReference<>() {});
        for (Map<String, Object> pref : prefs) {
            channelByClientId.put((String) pref.get("clientId"), (String) pref.get("preferredChannel"));
        }
        System.out.println("[PreferenceLookupService] Chargé " + channelByClientId.size() + " préférences.");
    }

    public String getPreferredChannel(String clientId) {
        return channelByClientId.getOrDefault(clientId, "EMAIL");
    }
}
