package com.pravo.pravo.domain.point.repository;

import com.pravo.pravo.domain.point.model.PointLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    List<PointLog> findByMemberId(Long memberId);
}
