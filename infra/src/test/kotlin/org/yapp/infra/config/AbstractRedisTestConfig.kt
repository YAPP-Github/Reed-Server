package org.yapp.infra.config

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Redis를 사용하는 테스트 환경에서 상속해서 사용할 설정 클래스입니다.
 * JUnit 5 + Testcontainers 통합을 통해 Redis 컨테이너의 자동 실행 및 종료를 지원하며,
 * 실행된 Redis 컨테이너의 host/port 정보를 Spring 프로퍼티로 동적으로 주입합니다.
 *
 * 사용법:
 * Redis 설정이 필요한 테스트 환경에서 활용하세요.
 * 테스트 클래스에서 이 클래스를 상속하면 Redis 컨테이너가 자동으로 실행됩니다.
 * 테스트 클래스는 모듈을 의존을 해도, 다른 모듈의 Test 폴더를 사용할 수 없으니 추후 Gradle Test Fixture 도입할 것
 */
@Testcontainers
@TestConfiguration
abstract class AbstractRedisTestConfig {

    companion object {
        @Container
        @JvmStatic
        private val redisContainer = RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG)
        )

        @JvmStatic
        @DynamicPropertySource
        fun redisProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", redisContainer::getHost)
            registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort)
        }
    }
}
