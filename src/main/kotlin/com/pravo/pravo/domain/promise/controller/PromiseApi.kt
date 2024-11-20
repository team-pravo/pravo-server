package com.pravo.pravo.domain.promise.controller

import com.pravo.pravo.domain.promise.dto.request.PromiseSearchDto
import com.pravo.pravo.domain.promise.dto.response.PromiseDetailResponseDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.global.auth.annotation.AuthUser
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "Promise", description = "약속 API")
interface PromiseApi {
    @Operation(summary = "약속 조회", description = "약속을 조회합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "약속 조회 성공",
        content = [
            Content(
                schema = Schema(implementation = PromiseResponseDto::class),
            ),
        ],
    )
    @SecurityRequirement(name = "jwt")
    fun getPromisesByMember(
        @ModelAttribute request: PromiseSearchDto?,
        @Parameter(hidden = true) @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<List<PromiseResponseDto>>

    @Operation(summary = "약속 상세 조회", description = "약속 상세를 조회합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "약속 상세 조회 성공",
        content = [
            Content(
                schema = Schema(implementation = PromiseResponseDto::class),
            ),
        ],
    )
    @SecurityRequirement(name = "jwt")
    fun getPromiseDetailByMember(
        @PathVariable promiseId: Long,
        @Parameter(hidden = true) @AuthUser authenticatedUser: AuthenticateUser,
    ): PromiseDetailResponseDto

    @Operation(summary = "약속 삭제", description = "모임장이 약속을 삭제합니다.")
    @ApiResponse(
        responseCode = "204",
        description = "약속 삭제 성공",
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "jwt")
    fun deletePromise(
        @PathVariable promiseId: Long,
        @Parameter(hidden = true) @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<Unit>

    @Operation(summary = "Pending 약속 상태 변경", description = "결제 완료된 약속을 Ready 상태로 변경합니다.")
    @SecurityRequirement(name = "jwt")
    fun changePendingStatus(promiseId: Long): ApiResponseDto<Unit>

    @Operation(summary = "약속 참가", description = "약속에 참가합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "약속 참가 성공",
        content = [
            Content(
                schema = Schema(implementation = ApiResponseDto::class),
            ),
        ],
    )
    @SecurityRequirement(name = "jwt")
    fun joinPromise(
        @PathVariable promiseId: Long,
        @Parameter(hidden = true) @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<PromiseResponseDto>

    @Operation(summary = "Pending Promise Role 상태 변경", description = "약속 참가 - 결제 이후 모임원의 상태를 Ready 상태로 변경합니다.")
    @SecurityRequirement(name = "jwt")
    fun changeParticipantPendingStatus(
        @PathVariable promiseId: Long,
        @Parameter(hidden = true) @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<Unit>

    @Operation(summary = "약속 취소", description = "모임원이 약속을 취소합니다.")
    @ApiResponse(
        responseCode = "204",
        description = "약속 취소 성공",
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "jwt")
    fun cancelPromise(
        @PathVariable promiseId: Long,
        @Parameter(hidden = true) @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<Unit>
}
