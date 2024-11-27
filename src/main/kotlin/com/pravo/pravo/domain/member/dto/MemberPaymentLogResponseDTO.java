package com.pravo.pravo.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberPaymentLogResponseDTO(

    @Schema(description = "약속 이름", example = "약속 이름")
    String promiseName,

    @Schema(description = "결제 일시", example = "2021-08-01T00:00:00")
    String paymentDate,

    @Schema(description = "결제 금액", example = "10000")
    int paymentAmount
) {

}
