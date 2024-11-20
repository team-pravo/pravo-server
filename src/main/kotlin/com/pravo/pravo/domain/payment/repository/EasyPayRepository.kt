package com.pravo.pravo.domain.payment.repository

import com.pravo.pravo.domain.payment.model.EasyPay
import org.springframework.data.jpa.repository.JpaRepository

interface EasyPayRepository : JpaRepository<EasyPay, Long>
