package com.pravo.pravo.domain.home.dto

import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import io.swagger.v3.oas.annotations.media.Schema

data class HomeResponseDto(
    val todayPromiseDto: List<PromiseResponseDto>,
    @Schema(description = "포인트", example = "3000")
    val point: Long,
    val upcomingPromiseDto: List<UpcomingPromiseDto>,
)
