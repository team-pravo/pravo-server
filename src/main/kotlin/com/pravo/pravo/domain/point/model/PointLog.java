package com.pravo.pravo.domain.point.model;

import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long promiseId;

    public PointLog(PointLogStatus pointLogStatus, Long amount, Long memberId, Long promiseId) {
        this.pointLogStatus = pointLogStatus;
        this.amount = amount;
        this.memberId = memberId;
        this.promiseId = promiseId;
    }

    public PointLog() {

    }

    public static PointLog of(PointLogStatus pointLogStatus, Long amount, Long memberId,
        Long promiseId) {
        return new PointLog(pointLogStatus, amount, memberId, promiseId);
    }

    public Long getPromiseId() {
        return this.promiseId;
    }

    public PointLogStatus getPointLogStatus() {
        return this.pointLogStatus;
    }

    public Long getAmount() {
        return this.amount;
    }
}
