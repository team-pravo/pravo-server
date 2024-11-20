package com.pravo.pravo.domain.payment.repository

import com.pravo.pravo.domain.payment.model.PaymentLog
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentLogRepository : JpaRepository<PaymentLog, String>
