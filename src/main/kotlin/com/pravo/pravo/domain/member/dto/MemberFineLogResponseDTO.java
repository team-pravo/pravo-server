package com.pravo.pravo.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record MemberFineLogResponseDTO(
    @Schema(description = "약속 이름", example = "약속 이름")
    String promiseName,

    @Schema(description = "별금 금액", example = "10000")
    Long fineAmount,

    @Schema(description = "별금 일시", example = "2021-08-01T00:00:00")
    LocalDateTime fineDate

) {

}
