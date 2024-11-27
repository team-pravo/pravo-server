package com.pravo.pravo.domain.fine.repository

import com.pravo.pravo.domain.fine.model.FineLog
import org.springframework.data.jpa.repository.JpaRepository

interface FineLogRepository : JpaRepository<FineLog, Long>
