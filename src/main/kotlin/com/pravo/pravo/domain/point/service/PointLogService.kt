package com.pravo.pravo.domain.point.service

import com.pravo.pravo.domain.point.model.PointLog
import com.pravo.pravo.domain.point.model.PointLogStatus
import com.pravo.pravo.domain.point.repository.PointLogRepository
import com.pravo.pravo.domain.promise.model.Promise
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PointLogService(
    private val pointLogRepository: PointLogRepository,
) {
    @Transactional
    fun savePointLog(
        pointLogStatus: PointLogStatus,
        amount: Long,
        memberId: Long,
        promise: Promise,
    ): PointLog = pointLogRepository.save(PointLog.of(pointLogStatus, amount, memberId, promise))

    @Transactional
    fun saveWithdrawPointLog(
        pointLogStatus: PointLogStatus,
        amount: Long,
        memberId: Long,
    ): PointLog = pointLogRepository.save(PointLog.of(pointLogStatus, amount, memberId, null))

    fun calculateSettlementAmount(
        memberId: Long,
        promiseId: Long,
        deposit: Int,
    ): Int =
        pointLogRepository
            .findByMemberIdAndPromiseId(memberId, promiseId)
            ?.amount
            ?.toInt()
            ?: -deposit
}
