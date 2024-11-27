package com.pravo.pravo.domain.fine.repository;

import com.pravo.pravo.domain.fine.model.FineLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineLogRepository extends JpaRepository<FineLog, Long> {

    List<FineLog> findByMemberId(Long memberId);
}
