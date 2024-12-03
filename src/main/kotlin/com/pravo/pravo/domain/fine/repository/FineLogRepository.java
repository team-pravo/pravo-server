package com.pravo.pravo.domain.fine.repository;

import com.pravo.pravo.domain.fine.model.FineLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FineLogRepository extends JpaRepository<FineLog, Long> {

    @Query("""
            SELECT fl FROM FineLog fl
            JOIN FETCH fl.promise p
            WHERE fl.memberId = :memberId
        """)
    List<FineLog> findByMemberId(Long memberId);
}
