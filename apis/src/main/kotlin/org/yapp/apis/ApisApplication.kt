package org.yapp.apis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main application class for the apis module.
 */
@SpringBootApplication(
    scanBasePackages = [
        "org.yapp.apis",
        "org.yapp.infra",
        "org.yapp.domain",
        "org.yapp.gateway",
        "org.yapp.global.util"
    ]
)
class ApisApplication

fun main(args: Array<String>) {
    runApplication<ApisApplication>(*args)
}
