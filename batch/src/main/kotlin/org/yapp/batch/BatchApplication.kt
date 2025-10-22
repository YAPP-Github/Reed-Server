package org.yapp.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(
    exclude = [JpaRepositoriesAutoConfiguration::class] // infra 모듈에서 @EnableJpaRepositories로 명시적으로 설정하여 수동 관리
)
@ComponentScan(basePackages = ["org.yapp"])
class BatchApplication

fun main(args: Array<String>) {
    runApplication<BatchApplication>(*args)
}
