package com.pravo.pravo.domain.promise.model;

import com.pravo.pravo.domain.promise.dto.request.PromiseCreateDto;
import com.pravo.pravo.domain.promise.model.enums.PromiseStatus;
import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE promise SET deleted = true WHERE promise_id = ?")
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

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "promise")
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

    public Integer getDeposit() { return this.deposit; }

    public void delete() {
        this.deleted = true;
    }

    public List<PromiseRole> getPromiseRoles() {
        return this.promiseRoles;
    }

    public Promise() {
    }

    public Promise(String name, LocalDateTime promiseDate, String location,
        PromiseStatus promiseStatus, Integer deposit) {
        this.name = name;
        this.promiseDate = promiseDate;
        this.location = location;
        this.status = promiseStatus;
        this.deposit = deposit;
    }

    public static Promise pendingOf(PromiseCreateDto promiseCreateDto) {
        return new Promise(
            promiseCreateDto.getName(),
            promiseCreateDto.getPromiseDate(),
            promiseCreateDto.getLocation(),
            PromiseStatus.PENDING,
            promiseCreateDto.getDeposit()
        );
    }

    public void changePendingStatus() {
        this.status = PromiseStatus.READY;
    }
}
