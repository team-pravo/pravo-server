package com.pravo.pravo.domain.payment.model;

import com.pravo.pravo.domain.payment.enums.PaymentStatus;
import com.pravo.pravo.domain.promise.model.Promise;
import com.pravo.pravo.global.common.model.BaseTimeEntity;
import com.pravo.pravo.global.external.toss.dto.response.TossResponseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
public class PaymentLog extends BaseTimeEntity {

    private String mId;
    private String lastTransactionKey;
    private String paymentKey;
    @Id
    private String orderId;
    private String orderName;
    private int taxExemptionAmount;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private boolean useEscrow;
    private boolean cultureExpense;
    private String receiptUrl;
    private String checkoutUrl;
    private String country;
    private boolean isPartialCancelable;
    private String currency;
    private int totalAmount;
    private int balanceAmount;
    private int suppliedAmount;
    private int vat;
    private int taxFreeAmount;
    private String method;
    private String version;

    private Long cardId;      // Card 객체의 ID
    private Long easyPayId;     // EasyPay 객체의 ID

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_id")
    private Promise promise;

    public static PaymentLog getPendingPaymentLog(String id, Long memberId, Promise promise) {
        PaymentLog paymentLog = new PaymentLog();
        return paymentLog.setPendingPaymentLog(id, memberId, promise);
    }

    public void updateFromConfirmResponse(TossResponseDto dto, Long cardId, Long easyPayId,
        PaymentStatus paymentStatus) {
        this.mId = dto.getMId();
        this.lastTransactionKey = dto.getLastTransactionKey();
        this.paymentKey = dto.getPaymentKey();
        this.orderName = dto.getOrderName();
        this.taxExemptionAmount =
            dto.getTaxExemptionAmount() != null ? dto.getTaxExemptionAmount() : 0;
        this.status = dto.getStatus();

        this.requestedAt = OffsetDateTime.parse(dto.getRequestedAt()).toLocalDateTime();
        this.approvedAt =
            dto.getApprovedAt() != null ? OffsetDateTime.parse(dto.getApprovedAt())
                .toLocalDateTime() : null;

        this.useEscrow = dto.getUseEscrow();
        this.cultureExpense = dto.getCultureExpense();
        this.receiptUrl = dto.getReceipt() != null ? dto.getReceipt().getUrl() : null;
        this.checkoutUrl = dto.getCheckout() != null ? dto.getCheckout().getUrl() : null;
        this.country = dto.getCountry();
        this.isPartialCancelable = dto.isPartialCancelable();
        this.currency = dto.getCurrency();
        this.totalAmount = dto.getTotalAmount();
        this.balanceAmount = dto.getBalanceAmount();
        this.suppliedAmount = dto.getSuppliedAmount();
        this.vat = dto.getVat();
        this.taxFreeAmount = dto.getTaxFreeAmount();
        this.method = dto.getMethod();
        this.version = dto.getVersion();
        if (cardId != null) {
            this.cardId = cardId;
        }
        if (easyPayId != null) {
            this.easyPayId = easyPayId;
        }
        this.paymentStatus = paymentStatus;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getPaymentKey() {
        return this.paymentKey;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    private PaymentLog setPendingPaymentLog(String id, Long memberId, Promise promise) {
        this.orderId = id;
        this.memberId = memberId;
        this.promise = promise;
        return this;
    }

    public int getBalanceAmount() {
        return this.balanceAmount;
    }

    public LocalDateTime getApprovedAt() {
        return this.approvedAt;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public Promise getPromise() {
        return this.promise;
    }

}
