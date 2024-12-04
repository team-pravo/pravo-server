package com.pravo.pravo.global.error

import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.error.exception.BadRequestException
import com.pravo.pravo.global.error.exception.BaseException
import com.pravo.pravo.global.error.exception.NotFoundException
import com.pravo.pravo.global.error.exception.UnauthorizedException
import com.pravo.pravo.global.util.logger
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    val logger = logger()

    @ExceptionHandler(BaseException::class)
    protected fun handleException(e: BaseException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = e.errorCode
        val response =
            ApiResponseDto.error(
                e.message ?: errorCode.message,
                errorCode.status,
                errorCode.code,
            )

        logger.error(e.message, e)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(exception: Exception): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)

        logger.error(exception.message, exception)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)

        logger.error(e.message, e)
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

        logger.error(e.message, e)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = ErrorCode.NOT_FOUND
        val response = ApiResponseDto.error(errorCode.message, errorCode.status, errorCode.code)

        logger.error(e.message, e)
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

        logger.error(e.message, e)
        return ResponseEntity.status(errorCode.status).body(response)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<ApiResponseDto<Nothing>> {
        val errorCode = e.errorCode
        val response =
            ApiResponseDto.error(
                e.message ?: errorCode.message,
                errorCode.status,
                errorCode.code,
            )

        logger.error(e.message, e)
        return ResponseEntity.status(errorCode.status).body(response)
    }
}
