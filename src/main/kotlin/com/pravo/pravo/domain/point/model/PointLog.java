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
}
