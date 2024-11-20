package com.pravo.pravo.domain.payment.repository

import com.pravo.pravo.domain.payment.model.Card
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<Card, Long>
