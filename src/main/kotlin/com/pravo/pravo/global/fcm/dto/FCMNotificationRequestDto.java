package com.pravo.pravo.global.fcm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FCMNotificationRequestDto {

    @Schema(example = "device token")
    private String token;
    @Schema(example = "약속 시간이 다 되어가요!")
    private String title;
    @Schema(example = "약속을 어기면 산타가 선물을 안줘요!")
    private String body;

    public String getToken() {
        return this.token;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }
}
