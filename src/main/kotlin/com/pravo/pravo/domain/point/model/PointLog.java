package com.pravo.pravo.domain.point.model;

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
@Table(name = "point_log")
public class PointLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PointLogStatus pointLogStatus;
    private Long amount;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_id")
    private Promise promise;


    public PointLog(PointLogStatus pointLogStatus, Long amount, Long memberId, Promise promise) {
        this.pointLogStatus = pointLogStatus;
        this.amount = amount;
        this.memberId = memberId;
        this.promise = promise;
    }

    public PointLog() {

    }

    public static PointLog of(PointLogStatus pointLogStatus, Long amount, Long memberId,
        Promise promise) {
        return new PointLog(pointLogStatus, amount, memberId, promise);
    }
    

    public PointLogStatus getPointLogStatus() {
        return this.pointLogStatus;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Promise getPromise() {
        return this.promise;
    }
}
