package com.pravo.pravo.domain.payment.model;

import com.pravo.pravo.domain.payment.enums.PaymentStatus;
import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class PaymentLog extends BaseTimeEntity {

    private String paymentId;
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

    private PaymentStatus paymentStatus;

    private PaymentLog setOrderId(String id) {
        this.orderId = id;
        return this;
    }

    public static PaymentLog getPendingPaymentLog(String id) {
        PaymentLog paymentLog = new PaymentLog();
        return paymentLog.setOrderId(id);
    }

    public String getOrderId() {
        return this.orderId;
    }
}
