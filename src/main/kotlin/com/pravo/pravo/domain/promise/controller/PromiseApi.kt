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
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable

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
    fun getPromiseDetailByMember(
        @PathVariable promiseId: Long,
        @Parameter(hidden = true) @AuthUser authenticatedUser: AuthenticateUser,
    ): PromiseDetailResponseDto

    @Operation(summary = "약속 삭제", description = "모임장이 약속을 삭제합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "약속 삭제 성공",
        content = [
            Content(
                schema = Schema(implementation = ApiResponseDto::class),
            ),
        ],
    )
    fun deletePromise(
        @PathVariable promiseId: Long,
        @Parameter(hidden = true) @AuthUser authenticatedUser: AuthenticateUser,
    ): ApiResponseDto<Unit>
}
