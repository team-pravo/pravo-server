package com.pravo.pravo.global.common.error

import io.swagger.v3.oas.annotations.media.Schema

data class ErrorResponse(
    @Schema(description = "HTTP 응답 메시지", example = "API Request is failed.")
    val message: String,
    @Schema(description = "HTTP 상태 코드", example = "500")
    val status: Int,
    @Schema(description = "에러 코드", example = "E01")
    val code: String,
) {
    constructor(
        errorCode: ErrorCode,
    ) : this(
        message = errorCode.message,
        status = errorCode.status,
        code = errorCode.code,
    )
}
