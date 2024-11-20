package com.pravo.pravo.global.external.toss.dto.response

data class CashReceiptDto(
    // 현금영수증의 종류 ("소득공제" 또는 "지출증빙")
    val type: String,
    // 현금영수증의 키 값 (최대 길이: 200자)
    val receiptKey: String,
    // 현금영수증 발급 번호 (최대 길이: 9자)
    val issueNumber: String,
    // 발행된 현금영수증을 확인할 수 있는 URL
    val receiptUrl: String,
    // 현금영수증 처리된 금액
    val amount: Int,
    val taxFreeAmount: Int,
)
