package com.pravo.pravo.domain.promise.model;

import com.pravo.pravo.domain.promise.model.enums.PromiseStatus;
import com.pravo.pravo.domain.promise.model.enums.RoleStatus;
import com.pravo.pravo.global.common.error.ErrorCode;
import com.pravo.pravo.global.common.error.exception.NotFoundException;
import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.CascadeType;
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

    @OneToMany(mappedBy = "promise", cascade = CascadeType.ALL)
    private List<PromiseRole> promiseRoles = new ArrayList<>();

    public PromiseRole getOrganizer() {
        return this.promiseRoles.stream()
            .filter(role -> role.getRole().equals(RoleStatus.ORGANIZER))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

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
