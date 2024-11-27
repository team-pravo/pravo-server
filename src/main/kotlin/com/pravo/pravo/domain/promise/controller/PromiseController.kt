package com.pravo.pravo.domain.promise.controller

import com.pravo.pravo.domain.promise.dto.request.PromiseSearchDto
import com.pravo.pravo.domain.promise.dto.response.PromiseDetailResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.service.PromiseFacade
import com.pravo.pravo.domain.promise.service.PromiseRoleService
import com.pravo.pravo.domain.promise.service.PromiseService
import com.pravo.pravo.global.auth.annotation.AuthUser
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/promise")
class PromiseController(
    private val promiseService: PromiseService,
    private val promiseRoleService: PromiseRoleService,
    private val promiseFacade: PromiseFacade,
) : PromiseApi {
    @GetMapping
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
    override fun getPromiseDetailByMember(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser,
    ): PromiseDetailResponseDto = promiseService.getPromiseDetailByMember(authenticatedUser.memberId, promiseId)

    @DeleteMapping("/{promiseId}")
    override fun deletePromise(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<Unit> =
        ApiResponseDto.success(
            promiseRoleService.deletePromise(authenticatedUser.memberId, promiseId),
        )

    @PostMapping("/{promiseId}/change")
    override fun changePendingStatus(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<Unit> =
        ApiResponseDto.success(
            promiseFacade.changePendingStatus(authenticatedUser.memberId, promiseId),
        )

    @DeleteMapping("/{promiseId}/cancel")
    override fun cancelPromise(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<Unit> =
        ApiResponseDto.success(
            promiseRoleService.cancelPromise(authenticatedUser.memberId, promiseId),
        )

    @GetMapping("/{promiseId}/settlement")
    override fun getPromiseSettlement(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser
    ): ApiResponseDto<Unit> {
        TODO("Not yet implemented")
    }

    @PostMapping("/{promiseId}/join")
    override fun joinPromise(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<PromiseResponseDto> =
        ApiResponseDto.success(
            promiseFacade.joinPromise(authenticatedUser.memberId, promiseId),
        )

    @PostMapping("/{promiseId}/participant/change")
    override fun changeParticipantPendingStatus(
        @PathVariable promiseId: Long,
        @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<Unit> =
        ApiResponseDto.success(
            promiseRoleService.changePendingStatus(authenticatedUser.memberId, promiseId),
        )
}
