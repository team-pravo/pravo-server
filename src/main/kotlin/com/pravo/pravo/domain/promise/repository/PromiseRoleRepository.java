package com.pravo.pravo.domain.promise.repository;

import com.pravo.pravo.domain.promise.model.PromiseRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromiseRoleRepository extends JpaRepository<PromiseRole, Long> {
    public PromiseRole getPromiseRoleById(Long id);

    public PromiseRole findByPromiseIdAndMemberId(Long promiseId, Long memberId);
}
