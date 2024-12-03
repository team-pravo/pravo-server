package com.pravo.pravo.domain.point.repository;

import com.pravo.pravo.domain.point.model.PointLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    @Query("""
            SELECT pl FROM PointLog pl
            JOIN FETCH pl.promise p
            WHERE pl.memberId = :memberId
        """)
    List<PointLog> findByMemberId(Long memberId);
}
