package com.pravo.pravo.domain.promise.dto.request

data class PromiseSettlementRequestDto(
    val memberIds: List<Long>,
    val promiseId: Long,
)
