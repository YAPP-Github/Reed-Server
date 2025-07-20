object Dependencies {

    object Spring {
        const val BOOT_STARTER_WEB = "org.springframework.boot:spring-boot-starter-web"
        const val BOOT_STARTER_DATA_JPA = "org.springframework.boot:spring-boot-starter-data-jpa"
        const val BOOT_STARTER_SECURITY = "org.springframework.boot:spring-boot-starter-security"
        const val BOOT_STARTER_VALIDATION = "org.springframework.boot:spring-boot-starter-validation"
        const val BOOT_STARTER_ACTUATOR = "org.springframework.boot:spring-boot-starter-actuator"
        const val BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test"
        const val BOOT_STARTER_DATA_REDIS = "org.springframework.boot:spring-boot-starter-data-redis"
        const val BOOT_STARTER_OAUTH2_RESOURCE_SERVER =
            "org.springframework.boot:spring-boot-starter-oauth2-resource-server"
        const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
    }

    object Database {
        const val MYSQL_CONNECTOR = "com.mysql:mysql-connector-j"
    }

    object Swagger {
        const val SPRINGDOC_OPENAPI_STARTER_WEBMVC_UI = "org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0"
    }

    object Projects {
        const val INFRA = ":infra"
        const val DOMAIN = ":domain"
        const val GLOBAL_UTILS = ":global-utils"
        const val GATEWAY = ":gateway"
    }

    object Logging {
        const val KOTLIN_LOGGING = "io.github.microutils:kotlin-logging-jvm:3.0.5"
    }

    object Uuid {
        const val GENERATOR = "com.github.f4b6a3:uuid-creator:5.3.2"
    }

    object RestClient {
        private const val HTTP_CLIENT5_VERSION = "5.2.1"

        const val HTTP_CLIENT5 = "org.apache.httpcomponents.client5:httpclient5:$HTTP_CLIENT5_VERSION"
        const val HTTP_CORE5 = "org.apache.httpcomponents.core5:httpcore5:$HTTP_CLIENT5_VERSION"
    }

    object TestContainers {
        const val MYSQL = "org.testcontainers:mysql:1.21.3"
        const val JUNIT_JUPITER = "org.testcontainers:junit-jupiter:1.21.3"
        const val REDIS = "com.redis:testcontainers-redis:2.2.2"
    }

    object Flyway {
        const val MYSQL = "org.flywaydb:flyway-mysql"
    }

    object QueryDsl {
        const val JPA = "com.querydsl:querydsl-jpa:5.0.0:jakarta"
        const val APT = "com.querydsl:querydsl-apt:5.0.0:jakarta"
    }
}
