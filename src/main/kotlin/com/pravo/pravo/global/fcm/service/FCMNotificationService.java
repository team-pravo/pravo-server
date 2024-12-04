package com.pravo.pravo.global.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FCMNotificationService {
    private final MemberRepository memberRepository;

    public FCMNotificationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


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

    @Transactional
    public String getToken(Long memberId, String token) throws BaseException {
        // 해당 아이디 가진 유저가 존재하는지 검사
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(ErrorCode.IMAGE_EXTENSION_ERROR, "E01"));

        member.setFcmToken(token);
        return "토큰이 성공적으로 저장되었습니다";
    }
}
