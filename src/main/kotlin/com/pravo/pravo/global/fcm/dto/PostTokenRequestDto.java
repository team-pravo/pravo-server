package com.pravo.pravo.global.fcm.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PostTokenRequestDto {
    @Schema(description = "FCM Token", example = "")
    String token;

    public String getToken() {
        return this.token;
    }
}