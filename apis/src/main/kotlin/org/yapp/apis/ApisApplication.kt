package org.yapp.apis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

/**
 * Main application class for the apis module.
 */
@SpringBootApplication(
    scanBasePackages = [
        "org.yapp.apis",
        "org.yapp.infra",
        "org.yapp.domain",
        "org.yapp.gateway",
        "org.yapp.globalutils"
    ],
    exclude = [JpaRepositoriesAutoConfiguration::class]
)
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
class ApisApplication

fun main(args: Array<String>) {
    runApplication<ApisApplication>(*args)
}
