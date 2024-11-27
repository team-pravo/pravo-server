package com.pravo.pravo.domain.fine.model;

import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fine_log")
public class FineLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    private Long memberId;
    private Long promiseId;

    public FineLog(Long amount, Long memberId, Long promiseId) {
        this.amount = amount;
        this.memberId = memberId;
        this.promiseId = promiseId;
    }

    public FineLog() {

    }

    public static FineLog of(Long amount, Long memberId, Long promiseId) {
        return new FineLog(amount, memberId, promiseId);
    }

    public Long getPromiseId() {
        return this.promiseId;
    }

    public Long getAmount() {
        return this.amount;
    }
}
