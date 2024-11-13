package com.pravo.pravo.domain.promise.controller

import com.pravo.pravo.domain.promise.dto.request.PromiseSearchDto
import com.pravo.pravo.domain.promise.dto.response.PromiseDetailResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.service.PromiseService
import com.pravo.pravo.global.auth.annotation.AuthUser
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/promise")
class PromiseController(
    private val promiseService: PromiseService,
) : PromiseApi {
    @GetMapping
    @SecurityRequirement(name = "jwt")
    override fun getPromisesByMember(
        request: PromiseSearchDto?,
        authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<List<PromiseResponseDto>> {
        return ApiResponseDto.success(promiseService.getPromisesByMember(authenticatedUser.memberId, request?.startedAt, request?.endedAt))
    }

    @GetMapping("/{promiseId}")
    @SecurityRequirement(name = "jwt")
    override fun getPromiseDetailByMember(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser,
    ): PromiseDetailResponseDto {
        return promiseService.getPromiseDetailByMember(authenticatedUser.memberId, promiseId)
    }

    @DeleteMapping("/{promiseId}")
    @SecurityRequirement(name = "jwt")
    override fun deletePromise(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser
    ): ApiResponseDto<Unit> {
        return ApiResponseDto.success(promiseService.deletePromise(authenticatedUser.memberId, promiseId))
    }


}
