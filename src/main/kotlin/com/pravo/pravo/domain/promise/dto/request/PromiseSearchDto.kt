package com.pravo.pravo.domain.promise.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class PromiseSearchDto(
    @Schema(description = "시작일", example = "21/08/01")
    @DateTimeFormat(pattern = "yy/MM/dd")
    val startedAt: LocalDate? = null,
    @Schema(description = "종료일", example = "21/08/01")
    @DateTimeFormat(pattern = "yy/MM/dd")
    val endedAt: LocalDate? = null,
)
