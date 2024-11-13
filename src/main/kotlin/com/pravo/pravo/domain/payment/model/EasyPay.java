package com.pravo.pravo.domain.payment.model;

import com.pravo.pravo.global.external.toss.dto.response.EasyPayDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EasyPay {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;
    private int amount;
    private int discountAmount;

    public EasyPay(EasyPayDto easyPayDto) {
        this.provider = easyPayDto.getProvider();
        this.amount = easyPayDto.getAmount();
        this.discountAmount = easyPayDto.getDiscountAmount();
    }

    public EasyPay() {}

    public Long getId() {
        return this.id;
    }
}
