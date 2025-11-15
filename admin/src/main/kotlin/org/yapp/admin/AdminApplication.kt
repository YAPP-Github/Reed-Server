package org.yapp.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(
    exclude = [JpaRepositoriesAutoConfiguration::class]
)
@ComponentScan(basePackages = ["org.yapp"])
class AdminApplication

fun main(args: Array<String>) {
    runApplication<AdminApplication>(*args)
}
