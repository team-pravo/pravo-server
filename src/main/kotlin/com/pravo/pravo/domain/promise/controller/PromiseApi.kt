package com.pravo.pravo.domain.promise.controller

import com.pravo.pravo.domain.promise.dto.request.PromiseSearchDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.global.common.ApiResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag

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
        memberId: Long,
        request: PromiseSearchDto?,
    ): ApiResponseDto<List<PromiseResponseDto>>
}
