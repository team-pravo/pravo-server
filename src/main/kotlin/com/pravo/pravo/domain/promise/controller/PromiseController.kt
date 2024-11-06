package com.pravo.pravo.domain.promise.controller

import com.pravo.pravo.domain.promise.dto.request.PromiseSearchDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.service.PromiseService
import com.pravo.pravo.global.auth.annotation.AuthUser
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/promise")
class PromiseController(
    private val promiseService: PromiseService,
) : PromiseApi {
    @GetMapping
    @SecurityRequirement(name = "jwt")
    override fun getPromisesByMember(
        request: PromiseSearchDto?,
        @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<List<PromiseResponseDto>> {
        // TODO: 토큰 기반 유저 인증 추가
        System.out.println("authenticatedUser: $authenticatedUser")
        System.out.println("request: $authenticatedUser.memberId")
        return ApiResponseDto.success(promiseService.getPromisesByMember(authenticatedUser.memberId, request?.startedAt, request?.endedAt))
    }
}
