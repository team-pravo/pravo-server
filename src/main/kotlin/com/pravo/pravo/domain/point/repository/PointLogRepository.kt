package com.pravo.pravo.domain.point.repository

import com.pravo.pravo.domain.point.model.PointLog
import org.springframework.data.jpa.repository.JpaRepository

interface PointLogRepository : JpaRepository<PointLog, Long>
