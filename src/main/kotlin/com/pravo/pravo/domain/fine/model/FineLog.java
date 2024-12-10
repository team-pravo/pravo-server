package com.pravo.pravo.domain.fine.model;

import com.pravo.pravo.domain.promise.model.Promise;
import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fine_log")
public class FineLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_id")
    private Promise promise;

    public FineLog(Long amount, Long memberId, Promise promise) {
        this.amount = amount;
        this.memberId = memberId;
        this.promise = promise;
    }

    public FineLog() {

    }

    public static FineLog of(Long amount, Long memberId, Promise promise) {
        return new FineLog(amount, memberId, promise);
    }

    public Long getAmount() {
        return this.amount;
    }

    public Promise getPromise() {
        return this.promise;
    }
}
