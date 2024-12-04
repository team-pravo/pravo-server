package com.pravo.pravo.domain.promise.dto.response

import io.swagger.v3.oas.annotations.media.Schema

class PromiseSettlementResponseDto(
    @Schema(description = "약속 ID", example = "1")
    val id: Long,
    @Schema(description = "약속 이름", example = "약속 이름")
    val name: String,
    @Schema(description = "예약금", example = "10000")
    val deposit: Int,
    @Schema(description = "지급포인트", example = "100")
    val earnedPoint: Long,
    @Schema(description = "참석자 수", example = "3")
    val attendanceCount: Int,
    @Schema(description = "결시자 수", example = "1")
    val absentCount: Int,
)
