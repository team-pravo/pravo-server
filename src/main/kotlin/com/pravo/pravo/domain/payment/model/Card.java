package com.pravo.pravo.domain.payment.model;

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
}
