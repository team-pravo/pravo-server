package com.pravo.pravo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class PravoApplication

fun main(args: Array<String>) {
    runApplication<PravoApplication>(*args)
}
