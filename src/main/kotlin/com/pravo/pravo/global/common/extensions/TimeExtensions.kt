package com.pravo.pravo.global.common.extensions

import java.time.LocalDateTime

val LocalDateTime.isBeforeNow: Boolean
    get() = this.isBefore(LocalDateTime.now())