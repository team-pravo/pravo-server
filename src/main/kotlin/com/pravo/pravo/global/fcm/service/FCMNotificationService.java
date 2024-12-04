package com.pravo.pravo.global.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMNotificationService {

    public void sendMessage(String token, String title, String body) throws FirebaseMessagingException {
        if (token == null) {
            throw new IllegalArgumentException("Token is null");
        }
        String message = FirebaseMessaging.getInstance().send(Message.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .setToken(token)
            .build());

        System.out.println("Sent message: " + message);
    }
}
