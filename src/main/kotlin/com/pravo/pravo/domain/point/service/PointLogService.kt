package com.pravo.pravo.domain.point.service

import com.pravo.pravo.domain.point.model.PointLog
import com.pravo.pravo.domain.point.model.PointLogStatus
import com.pravo.pravo.domain.point.repository.PointLogRepository
import org.springframework.stereotype.Service

@Service
class PointLogService(
        private val pointLogRepository: PointLogRepository,
) {
    // TODO 약속 정산에서 호출
    fun savePointLog(
            pointLogStatus: PointLogStatus,
            amount: Long,
            memberId: Long,
            promiseId: Long,
    ): PointLog = pointLogRepository.save(PointLog.of(pointLogStatus, amount, memberId, promiseId))
}
