package com.pravo.pravo.domain.payment.controller

import com.pravo.pravo.domain.payment.dto.response.RequestOrderResponseDto
import com.pravo.pravo.domain.payment.service.PaymentFacade
import com.pravo.pravo.domain.promise.dto.request.PromiseCreateDto
import com.pravo.pravo.global.auth.annotation.AuthUser
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment")
class PaymentController(
    private val paymentFacade: PaymentFacade,
) : PaymentApi {
    @PostMapping("/request")
    override fun requestOrder(
        @AuthUser authenticatedUser: AuthenticateUser,
        @RequestBody promiseCreateDto: PromiseCreateDto,
    ): ApiResponseDto<RequestOrderResponseDto> =
        ApiResponseDto.success(
            paymentFacade.requestOrder(
                authenticatedUser.memberId,
                promiseCreateDto,
            ),
        )

    @PostMapping("/request/{promiseId}")
    override fun requestOrderParticipant(
        @AuthUser authenticatedUser: AuthenticateUser,
        @PathVariable promiseId: Long,
    ) {
        ApiResponseDto.success(
            paymentFacade.requestOrderParticipant(
                authenticatedUser.memberId,
                promiseId,
            ),
        )
    }

    @PostMapping("/confirm")
    override fun confirmOrder(
        @RequestBody confirmRequestDto: ConfirmRequestDto,
    ): ApiResponseDto<Unit> = ApiResponseDto.success(paymentFacade.confirmOrder(confirmRequestDto))
}
