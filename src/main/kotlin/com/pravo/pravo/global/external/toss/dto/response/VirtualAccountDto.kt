package com.pravo.pravo.global.external.toss.dto.response

data class VirtualAccountDto(
    val accountType: String,          // 가상계좌 타입 (일반, 고정)
    val accountNumber: String,        // 발급된 계좌번호
    val bankCode: String,             // 은행 코드
    val customerName: String,         // 구매자명
    val dueDate: String,              // 입금 기한 (ISO 8601 형식)
    val refundStatus: String,         // 환불 처리 상태 (NONE, PENDING, FAILED 등)
    val expired: Boolean,             // 가상계좌의 만료 여부
    val settlementStatus: String,     // 정산 상태 (INCOMPLETED, COMPLETED)
    val refundReceiveAccount: RefundReceiveAccountDto?  // nullable 환불 계좌 정보 객체
)

data class RefundReceiveAccountDto(
    val bankCode: String,             // 환불 은행 코드
    val accountNumber: String,        // 환불 계좌번호
    val holderName: String            // 예금주명
)
