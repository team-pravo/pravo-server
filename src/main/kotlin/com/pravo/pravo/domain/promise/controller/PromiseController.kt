package com.pravo.pravo.domain.promise.controller

import com.pravo.pravo.domain.promise.dto.request.PromiseSearchDto
import com.pravo.pravo.domain.promise.dto.response.PromiseDetailResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.service.PromiseService
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
        authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<List<PromiseResponseDto>> =
        ApiResponseDto.success(
            promiseService.getPromisesByMember(
                authenticatedUser.memberId,
                request?.startedAt,
                request?.endedAt,
            ),
        )

    @GetMapping("/{promiseId}")
    @SecurityRequirement(name = "jwt")
    override fun getPromiseDetailByMember(
        promiseId: Long,
        authenticatedUser: AuthenticateUser,
    ): PromiseDetailResponseDto = promiseService.getPromiseDetailByMember(authenticatedUser.memberId, promiseId)

    @PostMapping("/{promiseId}/change")
    override fun changePendingStatus(
        @PathVariable promiseId: Long,
    ): ApiResponseDto<Unit> =
        ApiResponseDto.success(
            promiseService.changePendingStatus(promiseId),
        )
}
