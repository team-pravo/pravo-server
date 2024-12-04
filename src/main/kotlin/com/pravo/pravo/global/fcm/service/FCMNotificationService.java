package com.pravo.pravo.global.fcm.service;

import org.springframework.stereotype.Service;

@Service
public class FCMNotificationService {

    public void sendMessageTo(String token, String title, String body) {
        // FCM 서버로 메시지 전송
    }
}
