package com.pravo.pravo.domain.payment.model;

import com.pravo.pravo.global.external.toss.dto.response.CardDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Card {

    @Id
    @Column(name = "card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issuerCode;
    private String acquirerCode;
    private String number;
    private int installmentPlanMonths;
    private boolean isInterestFree;
    private String interestPayer;
    private String approveNo;
    private boolean useCardPoint;
    private String cardType;
    private String ownerType;
    private String acquireStatus;
    private String receiptUrl;
    private int amount;

    public Card(CardDto dto) {
        this.issuerCode = dto.getIssuerCode();
        this.acquirerCode = dto.getAcquirerCode();
        this.number = dto.getNumber();
        this.installmentPlanMonths = dto.getInstallmentPlanMonths();
        this.isInterestFree = dto.isInterestFree();
        this.interestPayer = dto.getInterestPayer();
        this.approveNo = dto.getApproveNo();
        this.useCardPoint = dto.getUseCardPoint();
        this.cardType = dto.getCardType();
        this.ownerType = dto.getOwnerType();
        this.acquireStatus = dto.getAcquireStatus();
        this.amount = dto.getAmount();
    }

    public Card() {}

    public Long getId() {
        return this.id;
    }
}
