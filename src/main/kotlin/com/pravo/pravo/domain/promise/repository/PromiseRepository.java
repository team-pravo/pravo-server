package com.pravo.pravo.domain.promise.repository;

import com.pravo.pravo.domain.promise.model.Promise;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PromiseRepository extends JpaRepository<Promise, Long>, PromiseRepositoryCustom{
    @Query("""
        SELECT DISTINCT p FROM Promise p
        LEFT JOIN FETCH p.promiseRoles pr
        LEFT JOIN FETCH pr.member
        WHERE p.id = :promiseId
        AND p.deleted = false
    """)
    Optional<Promise> getPromiseById(Long promiseId);
}
