package com.pravo.pravo.domain.payment.controller

import com.pravo.pravo.domain.payment.dto.response.RequestOrderResponseDto
import com.pravo.pravo.domain.promise.dto.request.PromiseCreateDto
import com.pravo.pravo.global.common.ApiResponseDto
import com.pravo.pravo.global.external.toss.dto.request.ConfirmRequestDto
import com.pravo.pravo.global.jwt.AuthenticateUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Payment", description = "결제 API")
interface PaymentApi {
    @Operation(summary = "결제 요청", description = "약속, 결제 정보를 생성 후 orderId, promiseId를 반환합니다.")
    @SecurityRequirement(name = "jwt")
    fun requestOrder(
        @Parameter(hidden = true) authenticatedUser: AuthenticateUser,
        promiseCreateDto: PromiseCreateDto,
    ): ApiResponseDto<RequestOrderResponseDto>

    @Operation(summary = "참가자 결제 요청", description = "promiseId를 이용해 결제 정보를 생성합니다.")
    @SecurityRequirement(name = "jwt")
    fun requestOrderParticipant(
        @Parameter(hidden = true) authenticatedUser: AuthenticateUser,
        promiseId: Long,
    ): ApiResponseDto<RequestOrderResponseDto>

    @Operation(summary = "결제 승인", description = "paymentKey에 해당하는 결제를 검증하고 승인합니다.")
    @SecurityRequirement(name = "jwt")
    fun confirmOrder(confirmRequestDto: ConfirmRequestDto): ApiResponseDto<Unit>
}
