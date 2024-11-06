package com.pravo.pravo.global.external.toss.dto.response

data class ConfirmResponseDto (
    val mId: String,
    val lastTransactionKey: String?,
    val paymentKey: String,
    val orderId: String,
    val orderName: String,
    val taxExemptionAmount: Int?,
    val status: String,
    val requestedAt: String, // ISO 형식의 String 또는 DateTimeFormatter로 변환 가능
    val approvedAt: String?,
    val useEscrow: Boolean,
    val cultureExpense: Boolean,
    val card: CardDto?,
    val virtualAccount: VirtualAccountDto?, // 타입을 알 수 없을 때는 Any로 정의
    val transfer: Any?,
    val mobilePhone: Any?,
    val giftCertificate: Any?,
    val cashReceipt: CashReceiptDto?,
    val cashReceipts: List<CashReceiptHistoryDto>?,
    val discount: Any?,
    val cancels: Any?,
    val secret: Any?,
    val type: String,
    val easyPay: EasyPayDto?,
    val country: String,
    val failure: FailureDto?,
    val isPartialCancelable: Boolean,
    val receipt: ReceiptDto?,
    val checkout: CheckOutDto?,
    val currency: String,
    val totalAmount: Int,
    val balanceAmount: Int,
    val suppliedAmount: Int,
    val vat: Int,
    val taxFreeAmount: Int,
    val method: String?,
    val version: String,
    val metadata: Any
)

data class ReceiptDto(
    val url: String
)

data class CheckOutDto(
    val url: String
)
