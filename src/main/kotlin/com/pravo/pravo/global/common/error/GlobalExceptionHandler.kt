package com.pravo.pravo.global.common.error

import com.pravo.pravo.global.common.error.exception.UnauthorizedException
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    protected fun handleException(exception: Exception): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ErrorResponse(errorCode)
        return ResponseEntity.ok(response)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.NOT_FOUND
        val response = ErrorResponse(errorCode)
        return ResponseEntity.ok(response)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAuthenticationException(exception: AccessDeniedException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.UNAUTHORIZED
        val response = ErrorResponse(errorCode)
        return ResponseEntity.ok(response)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.UNAUTHORIZED
        val response = ErrorResponse(errorCode)
        return ResponseEntity.ok(response)
    }
}
