package com.pravo.pravo.global.external.toss.dto.response

data class TossResponseDto(
    val mId: String,
    val lastTransactionKey: String?,
    val paymentKey: String,
    val orderId: String,
    val orderName: String,
    val taxExemptionAmount: Int?,
    val status: String,
    val requestedAt: String,
    val approvedAt: String?,
    val useEscrow: Boolean,
    val cultureExpense: Boolean,
    val card: CardDto?,
    val virtualAccount: VirtualAccountDto?,
    val transfer: TransferDto?,
    val mobilePhone: MobilePhoneDto?,
    val giftCertificate: GiftCertificate?,
    val cashReceipt: CashReceiptDto?,
    val cashReceipts: List<CashReceiptHistoryDto>?,
    val discount: DiscountDto?,
    val cancels: List<CancelDto>?,
    val secret: String?,
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
    val metadata: Any?,
)

data class DiscountDto(
    val amount: Int,
)

data class GiftCertificate(
    val approveNo: String,
    val settlementStatus: String,
)

data class MobilePhoneDto(
    val customerMobilePhone: String,
    val settlementStatus: String,
    val receiptUrl: String,
)

data class TransferDto(
    val bankCode: String,
    val settlementStatus: String,
)

data class ReceiptDto(
    val url: String,
)

data class CheckOutDto(
    val url: String,
)
