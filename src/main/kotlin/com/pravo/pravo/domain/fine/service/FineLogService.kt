package com.pravo.pravo.domain.fine.service

import com.pravo.pravo.domain.fine.model.FineLog
import com.pravo.pravo.domain.fine.repository.FineLogRepository
import com.pravo.pravo.domain.promise.model.Promise
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FineLogService(
    private val fineLogRepository: FineLogRepository,
) {
    @Transactional
    fun saveFineLog(
        amount: Long,
        memberId: Long,
        promise: Promise,
    ): FineLog = fineLogRepository.save(FineLog.of(amount, memberId, promise))
}
