package com.pravo.pravo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class PravoApplication

fun main(args: Array<String>) {
    runApplication<PravoApplication>(*args)
}
