package com.pravo.pravo.global.common

import com.pravo.pravo.global.common.error.ErrorCode
import io.swagger.v3.oas.annotations.media.Schema

class ApiResponseDto<T>(
    @Schema(description = "HTTP 응답 메시지")
    val message: String,
    @Schema(description = "HTTP 상태 코드")
    val status: Int,
    @Schema(description = "응답 코드")
    val code: String,
    @Schema(description = "응답 데이터")
    val data: T? = null,
) {
    companion object {
        @JvmStatic
        fun success(): ApiResponseDto<Unit> {
            return ApiResponseDto(
                message = "Success",
                status = 200,
                code = "S01",
            )
        }

        @JvmStatic
        fun <T> success(data: T): ApiResponseDto<T> {
            return ApiResponseDto(
                message = "Success",
                status = 200,
                code = "S01",
                data = data,
            )
        }

        @JvmStatic
        fun <T> success(
            message: String,
            data: T,
        ): ApiResponseDto<T> {
            return ApiResponseDto(
                message = message,
                status = 200,
                code = "S01",
                data = data,
            )
        }

        @JvmStatic
        fun error(
            message: String,
            status: Int = 400,
            code: String = "E01",
        ): ApiResponseDto<Nothing> {
            return ApiResponseDto(
                message = message,
                status = status,
                code = code,
            )
        }

        @JvmStatic
        fun error(errorCode: ErrorCode): ApiResponseDto<Nothing> {
            return ApiResponseDto(
                message = errorCode.message,
                status = errorCode.status,
                code = errorCode.code,
            )
        }
    }
}
