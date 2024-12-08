package com.pravo.pravo.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostConstruct
    public void init(){
        try{
            logger.info("Firebase 초기화 시작");
            File file = new File("serviceAccountKey.json");
            logger.info("File exists: {}", file.exists());
            logger.info("Absolute path: {}", file.getAbsolutePath());

            ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
            logger.info("Resource exists: {}", resource.exists());

            InputStream serviceAccount = resource.getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
