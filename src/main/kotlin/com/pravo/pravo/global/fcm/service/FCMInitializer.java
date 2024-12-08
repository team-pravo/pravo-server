package com.pravo.pravo.global.fcm.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class FCMInitializer {
    @Value("${firebase.config-path}")
    private String FIREBASE_CONFIG_PATH;

    @PostConstruct
    public void initialize() {
        try {

            ClassPathResource resource = new ClassPathResource(FIREBASE_CONFIG_PATH);

            Gson gson = new GsonBuilder()
                .setLenient()  // JSON 파싱을 좀 더 유연하게 함
                .create();

            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                String jsonString = gson.toJson(jsonObject);

                // 파싱된 JSON을 다시 InputStream으로 변환
                InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(
                    StandardCharsets.UTF_8));

                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Firebase 초기화 실패", e);
        }
    }
}
