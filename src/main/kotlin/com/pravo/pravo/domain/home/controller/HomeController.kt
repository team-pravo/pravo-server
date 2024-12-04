package com.pravo.pravo.domain.home.controller

import com.pravo.pravo.domain.home.dto.HomeResponseDto
import com.pravo.pravo.domain.home.service.HomeFacade
import com.pravo.pravo.global.auth.annotation.AuthUser
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HomeController(
    private val homeFacade: HomeFacade,
) : HomeApi {
    @GetMapping("/home")
    override fun getHome(
        @AuthUser authenticateUser: AuthenticateUser,
    ): ApiResponseDto<HomeResponseDto> = ApiResponseDto.success(homeFacade.getHome(authenticateUser.memberId))
}
