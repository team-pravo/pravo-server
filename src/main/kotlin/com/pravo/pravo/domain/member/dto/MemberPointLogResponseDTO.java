package com.pravo.pravo.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record MemberPointLogResponseDTO(
    @Schema(description = "약속 이름", example = "약속 이름")
    String promiseName,

    @Schema(description = "pointLogStatus", example = "PLUS(적립+)/MINUS(사용-)/FINE(별금-)")
    String pointLogStatus,

    @Schema(description = "결제 금액", example = "10000")
    Long pointAmount,

    @Schema(description = "포인트 적립/사용 일시", example = "2021-08-01T00:00:00")
    LocalDateTime pointDate
) {

}
