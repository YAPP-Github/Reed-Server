object Dependencies {

    object Spring {
        const val BOOT_STARTER_WEB = "org.springframework.boot:spring-boot-starter-web"
        const val BOOT_STARTER_DATA_JPA = "org.springframework.boot:spring-boot-starter-data-jpa"
        const val BOOT_STARTER_SECURITY = "org.springframework.boot:spring-boot-starter-security"
        const val BOOT_STARTER_VALIDATION = "org.springframework.boot:spring-boot-starter-validation"
        const val BOOT_STARTER_ACTUATOR = "org.springframework.boot:spring-boot-starter-actuator"
        const val BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test"
        const val BOOT_STARTER_DATA_REDIS = "org.springframework.boot:spring-boot-starter-data-redis"
        const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
    }

    object Database {
        const val MYSQL_CONNECTOR = "com.mysql:mysql-connector-j"
    }

    object Auth {
        const val JWT = "io.jsonwebtoken:jjwt-api:0.11.5"
        const val JWT_IMPL = "io.jsonwebtoken:jjwt-impl:0.11.5"
        const val JWT_JACKSON = "io.jsonwebtoken:jjwt-jackson:0.11.5"
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
}
