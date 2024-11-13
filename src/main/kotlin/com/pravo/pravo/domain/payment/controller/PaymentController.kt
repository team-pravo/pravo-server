package com.pravo.pravo.domain.payment.controller

import com.pravo.pravo.domain.payment.dto.response.RequestOrderResponseDto
import com.pravo.pravo.domain.payment.service.PaymentFacade
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment")
class PaymentController(
    private val paymentFacade: PaymentFacade,
) : PaymentApi {
    @GetMapping
    override fun requestOrder(authenticatedUser: AuthenticateUser): ApiResponseDto<RequestOrderResponseDto> =
        ApiResponseDto.success(paymentFacade.requestOrder(authenticatedUser.memberId))

    @PostMapping
    override fun confirmOrder(
        @RequestBody confirmRequestDto: ConfirmRequestDto,
    ): ApiResponseDto<Unit> {
        paymentFacade.confirmOrder(confirmRequestDto)
        return ApiResponseDto.success()
    }
}
