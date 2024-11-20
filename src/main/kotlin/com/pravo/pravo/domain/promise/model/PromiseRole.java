package com.pravo.pravo.domain.promise.model;

import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.promise.model.enums.ParticipantStatus;
import com.pravo.pravo.domain.promise.model.enums.RoleStatus;
import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("deleted = false")
public class PromiseRole extends BaseTimeEntity {

    @Id
    @Column(name = "promise_role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_id")
    private Promise promise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleStatus role;

    @Column(nullable = false)
    private boolean deleted = false;

    public PromiseRole(Member member, Promise promise, ParticipantStatus status, RoleStatus role) {
        this.member = member;
        this.promise = promise;
        this.status = status;
        this.role = role;
    }

    public PromiseRole() {

    }

    public Long getId() {
        return this.id;
    }

    public Promise getPromise() {
        return this.promise;
    }

    public Member getMember() {
        return this.member;
    }

    public ParticipantStatus getStatus() {
        return this.status;
    }

    public RoleStatus getRole() {
        return this.role;
    }
    public void setPromise(Promise promise) {
        this.promise = promise;
    }

    public boolean isOrganizer() {
        return this.role == RoleStatus.ORGANIZER;
    }

    public void delete() {
        this.deleted = true;
    }

    public static PromiseRole pendingOf(Promise promise, Member member) {
        return new PromiseRole(member, promise, ParticipantStatus.PENDING, RoleStatus.PARTICIPANT);
    }

    public void changePendingStatus() { this.status = ParticipantStatus.READY; }
}
