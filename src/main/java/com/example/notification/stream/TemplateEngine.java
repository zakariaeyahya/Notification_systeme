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
public class TemplateEngine {

    private final Map<String, String> templatesByOperation = new HashMap<>();

    @PostConstruct
    public void load() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("templates/notification-templates.json");
        List<Map<String, Object>> templates = mapper.readValue(is, new TypeReference<>() {});
        for (Map<String, Object> t : templates) {
            templatesByOperation.put((String) t.get("operationType"), (String) t.get("template"));
        }
        System.out.println("[TemplateEngine] Chargé " + templatesByOperation.size() + " templates.");
    }

    public String generate(String operationType, Map<String, Object> metadata) {
        String template = templatesByOperation.get(operationType);
        if (template == null) {
            return "Notification pour opération : " + operationType;
        }
        String result = template;
        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return result;
    }
}
