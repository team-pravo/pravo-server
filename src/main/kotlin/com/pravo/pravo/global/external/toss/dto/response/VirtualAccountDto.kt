package com.pravo.pravo.global.external.toss.dto.response

data class VirtualAccountDto(
    // 가상계좌 타입 (일반, 고정)
    val accountType: String,
    // 발급된 계좌번호
    val accountNumber: String,
    // 은행 코드
    val bankCode: String,
    // 구매자명
    val customerName: String,
    // 입금 기한 (ISO 8601 형식)
    val dueDate: String,
    // 환불 처리 상태 (NONE, PENDING, FAILED 등)
    val refundStatus: String,
    // 가상계좌의 만료 여부
    val expired: Boolean,
    // 정산 상태 (INCOMPLETED, COMPLETED)
    val settlementStatus: String,
    // nullable 환불 계좌 정보 객체
    val refundReceiveAccount: RefundReceiveAccountDto?,
)

data class RefundReceiveAccountDto(
    // 환불 은행 코드
    val bankCode: String,
    // 환불 계좌번호
    val accountNumber: String,
    // 예금주명
    val holderName: String,
)
