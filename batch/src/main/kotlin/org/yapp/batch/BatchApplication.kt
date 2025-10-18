package org.yapp.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "org.yapp.batch",
        "org.yapp.infra",
        "org.yapp.domain",
        "org.yapp.globalutils"
    ]
)
class BatchApplication

fun main(args: Array<String>) {
    runApplication<BatchApplication>(*args)
}
