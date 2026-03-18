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
public class UserLookupService {

    private final Map<String, Map<String, Object>> usersById = new HashMap<>();

    @PostConstruct
    public void load() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/users.json");
        List<Map<String, Object>> users = mapper.readValue(is, new TypeReference<>() {});
        for (Map<String, Object> user : users) {
            usersById.put((String) user.get("clientId"), user);
        }
        System.out.println("[UserLookupService] Chargé " + usersById.size() + " utilisateurs.");
    }

    public Map<String, Object> findById(String clientId) {
        return usersById.get(clientId);
    }
}
