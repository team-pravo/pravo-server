package com.pravo.pravo.global.common.error

import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.error.ErrorCode
import com.pravo.pravo.global.error.exception.UnauthorizedException
import feign.FeignException
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    protected fun handleException(exception: Exception): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)
        return ResponseEntity.status(errorCode.status).body(response)
    }

//    @ExceptionHandler(FeignException::class)
//    fun handleFeignException(e: FeignException): ResponseEntity<> {
//        return
//    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.NOT_FOUND
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAuthenticationException(exception: AccessDeniedException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.UNAUTHORIZED
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.UNAUTHORIZED
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)
        return ResponseEntity.status(errorCode.status).body(response)
    }
}
