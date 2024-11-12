package com.pravo.pravo.domain.promise.model;

import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.promise.model.enums.PromiseStatus;
import com.pravo.pravo.domain.promise.model.enums.RoleStatus;
import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Promise extends BaseTimeEntity {

    @Id
    @Column(name = "promise_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime promiseDate;

    @Column
    private String location;

    @Column
    @Enumerated(EnumType.STRING)
    private PromiseStatus status;

    @Column
    private Integer deposit;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "promise_id")
    private List<PromiseRole> promiseRoles = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LocalDateTime getPromiseDate() {
        return this.promiseDate;
    }

    public String getLocation() {
        return this.location;
    }

    public PromiseStatus getStatus() {
        return this.status;
    }

    public List<PromiseRole> getPromiseRoles() {
        return this.promiseRoles;
    }

    public Integer getDeposit() { return this.deposit; }
}
