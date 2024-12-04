package com.pravo.pravo.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class FirebaseConfig {

    private final String firebaseConfigPath;

    public FirebaseConfig(@Value("${firebase.config-path}") String firebaseConfigPath) {
        this.firebaseConfigPath = firebaseConfigPath;
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }

    public void init(){
        try {
            Resource resource = new ClassPathResource(firebaseConfigPath);
            System.out.println("Firebase config path: " + firebaseConfigPath);
            System.out.println("Resource exists: "+ resource.exists());

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully");
            }
        } catch (Exception e){
            System.out.println("Firebase initialization failed" + e);
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }
}
