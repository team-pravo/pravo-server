package com.pravo.pravo.domain.promise.repository;

import com.pravo.pravo.domain.promise.model.PromiseRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PromiseRoleRepository extends JpaRepository<PromiseRole, Long> {

    @Query("""
    SELECT pr
    FROM PromiseRole pr
    JOIN FETCH pr.promise
    WHERE pr.promise.id = :promiseId
    AND pr.member.id = :memberId
    """)
    public PromiseRole findByPromiseIdAndMemberId(Long promiseId, Long memberId);

    public List<PromiseRole> findAllByPromiseId(Long promiseId);
}
