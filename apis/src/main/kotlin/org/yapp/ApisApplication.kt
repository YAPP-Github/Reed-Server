package org.yapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApisApplication

fun main(args: Array<String>) {
    runApplication<ApisApplication>(*args)
}
