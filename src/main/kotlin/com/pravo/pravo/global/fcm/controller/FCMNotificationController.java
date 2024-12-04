package com.pravo.pravo.global.fcm.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.pravo.pravo.global.auth.annotation.AuthUser;
import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.error.exception.BaseException;
import com.pravo.pravo.global.fcm.dto.FCMNotificationRequestDto;
import com.pravo.pravo.global.fcm.dto.PostTokenRequestDto;
import com.pravo.pravo.global.fcm.service.FCMNotificationService;
import com.pravo.pravo.global.jwt.AuthenticateUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "FCM", description = "FCM 관련 API")
public class FCMNotificationController {

    private final FCMNotificationService fcmNotificationService;

    public FCMNotificationController(FCMNotificationService fcmNotificationService) {
        this.fcmNotificationService = fcmNotificationService;
    }

    @PostMapping("/admin/send/push")
    @Operation(summary = "FCM 푸시 메시지 전송", description = "FCM 푸시 메시지를 전송합니다")
    public ApiResponseDto<?> pushMessage(
        @Parameter(description = "FCM 알림 요청 정보")
        @RequestBody FCMNotificationRequestDto requestDto)
        throws FirebaseMessagingException {
        System.out.println("requestDto = " + requestDto.getToken());
        fcmNotificationService.sendMessage(
            requestDto.getToken(),
            requestDto.getTitle(),
            requestDto.getBody());
        return ApiResponseDto.success();
    }

    @PostMapping("/token")
    @SecurityRequirement(name = "jwt")
    @Operation(summary = "FCM 토큰 등록", description = "FCM 토큰을 등록합니다")
    public ApiResponseDto<String> getToken(
        @Parameter(hidden = true) @AuthUser AuthenticateUser authenticateUser,
        @Valid @RequestBody PostTokenRequestDto postTokenReq) {
        try {
            return ApiResponseDto.success(
                fcmNotificationService.getToken(authenticateUser.getMemberId(), postTokenReq.getToken()));
        } catch (BaseException e) {
            return ApiResponseDto.error(e.getMessage(), HttpStatus.BAD_REQUEST.value(), "E01");
        }
    }

}
