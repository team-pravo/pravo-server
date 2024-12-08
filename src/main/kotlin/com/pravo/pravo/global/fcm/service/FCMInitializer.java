package com.pravo.pravo.global.fcm.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMInitializer {
    @Value("${firebase.config-path}")
    private String FIREBASE_CONFIG_PATH;

    @PostConstruct
    public void initialize() {
        try {
            log.info("Firebase 초기화 시작");
            log.info("Config path: {}", FIREBASE_CONFIG_PATH);

            ClassPathResource resource = new ClassPathResource(FIREBASE_CONFIG_PATH);
            log.info("리소스 존재 여부: {}", resource.exists());

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(
                    resource.getInputStream()
                ))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase 초기화 성공");
            } else {
                log.info("Firebase가 이미 초기화되어 있음");
            }

            // 초기화 확인
            FirebaseApp app = FirebaseApp.getInstance();
            log.info("Firebase 앱 이름: {}", app.getName());

        } catch (IOException e) {
            log.error("Firebase 초기화 실패", e);
            throw new RuntimeException("Firebase 초기화 실패", e);
        }
    }
}
