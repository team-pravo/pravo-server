package com.pravo.pravo.domain.fine.service

import com.pravo.pravo.domain.fine.model.FineLog
import com.pravo.pravo.domain.fine.repository.FineLogRepository
import org.springframework.stereotype.Service

@Service
class FineLogService(
        private val fineLogRepository: FineLogRepository,
) {
    // TODO 정산 로직에서 사용
    fun saveFineLog(
            amount: Long,
            memberId: Long,
            promiseId: Long,
    ): FineLog = fineLogRepository.save(FineLog.of(amount, memberId, promiseId))
}
