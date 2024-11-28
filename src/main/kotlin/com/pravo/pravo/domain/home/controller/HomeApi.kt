package com.pravo.pravo.domain.home.controller

import com.pravo.pravo.domain.home.dto.HomeResponseDto
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Home", description = "홈 API")
@SecurityRequirement(name = "jwt")
interface HomeApi {
    @Operation(summary = "홈화면 조회")
    fun getHome(
        @Parameter(hidden = true) authenticateUser: AuthenticateUser,
    ): ApiResponseDto<HomeResponseDto>
}
