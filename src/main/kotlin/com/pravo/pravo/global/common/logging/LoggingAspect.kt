package com.pravo.pravo.global.common.logging

import com.pravo.pravo.global.util.logger
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.ContentCachingRequestWrapper
import java.nio.charset.StandardCharsets

@Aspect
@Component
class LoggingAspect {
    val logger = logger()

    @Pointcut(
        "execution(* com.pravo.pravo.global.error.GlobalExceptionHandler.handleException*(..)) || " +
            "execution(* com.pravo.pravo.global.error.GlobalExceptionHandler.handleRuntimeException*(..)) || " +
            "execution(* com.pravo.pravo.global.error.GlobalExceptionHandler.handleEntityNotFoundException*(..)) || " +
            "execution(* com.pravo.pravo.global.error.GlobalExceptionHandler.handleUnauthorizedException*(..))",
    )
    fun errorLevelExecute() {}

    @Around("com.pravo.pravo.global.common.logging.LoggingAspect.errorLevelExecute()")
    @Throws(Throwable::class)
    fun requestErrorLevelLogging(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val cachingRequest = request as ContentCachingRequestWrapper
        val startAt = System.currentTimeMillis()
        val returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.args)
        val endAt = System.currentTimeMillis()

        val logMessage = StringBuilder()
        logMessage.append(
            """
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            🔍상세 정보
            - ✨ REQUEST: [${request.method}] ${request.requestURL}
            - ✨ DURATION: ${endAt - startAt}ms
            - ✨ HEADERS: ${getHeaders(request)}
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            """.trimIndent(),
        )

        val args = proceedingJoinPoint.args
        if (args.isNotEmpty()) {
            val signature = proceedingJoinPoint.signature as MethodSignature
            val paramNames = signature.parameterNames
            val paramAnnotations = signature.method.parameterAnnotations

            args.forEachIndexed { i, arg ->
                paramAnnotations[i].forEach { annotation ->
                    if (annotation is PathVariable || annotation is RequestParam) {
                        logMessage.append("- 💌 PARAMETER: ${paramNames[i]}: $arg\n")
                        return@forEach
                    }
                }
            }
        }

        if (request.method.equals("POST", ignoreCase = true) || request.method.equals("PATCH", ignoreCase = true)) {
            val requestBody = String(cachingRequest.contentAsByteArray, StandardCharsets.UTF_8)
            logMessage.append("- 💌 BODY: $requestBody\n")
        }

        if (returnValue != null) {
            logMessage.append("- 🎁 RESPONSE: $returnValue\n")
        }

        logger.error(logMessage.toString())

        return returnValue
    }

    private fun getHeaders(request: HttpServletRequest): Map<String, Any> {
        val headerMap = mutableMapOf<String, Any>()

        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            headerMap[headerName] = request.getHeader(headerName)
        }
        return headerMap
    }
}
