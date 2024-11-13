package com.pravo.pravo.global.common.error

import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.common.error.exception.BaseException
import com.pravo.pravo.global.common.error.exception.NotFoundException
import com.pravo.pravo.global.common.error.exception.UnauthorizedException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BaseException::class)
    protected fun handleException(e: BaseException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = e.errorCode
        val response =
            ApiResponseDto.error(
                e.message ?: errorCode.message,
                errorCode.status,
                errorCode.code,
            )
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleEntityNotFoundException(e: NotFoundException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = e.errorCode
        val response =
            ApiResponseDto.error(
                e.message ?: errorCode.message,
                errorCode.status,
                errorCode.code,
            )
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAuthenticationException(e: AccessDeniedException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.UNAUTHORIZED
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = e.errorCode
        val response =
            ApiResponseDto.error(
                e.message ?: errorCode.message,
                errorCode.status,
                errorCode.code,
            )
        return ResponseEntity.status(errorCode.status).body(response)
    }
}
