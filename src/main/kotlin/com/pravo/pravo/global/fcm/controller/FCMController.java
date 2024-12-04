package com.pravo.pravo.global.fcm.controller;

import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.fcm.dto.FCMNotificationRequestDto;
import com.pravo.pravo.global.fcm.service.FCMNotificationService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "FCM", description = "FCM 관련 API")
public class FCMController {

    private final FCMNotificationService fcmNotificationService;

    @PostMapping("/admin/send/push")
    public ApiResponseDto<?> pushMessage(@RequestBody FCMNotificationRequestDto requestDto) {

        fcmNotificationService.sendMessageTo(
            requestDto.getToken(),
            requestDto.getTitle(),
            requestDto.getBody());
        return ApiResponseDto.success();
    }

}
