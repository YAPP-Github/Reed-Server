object Dependencies {

    object Spring {
        const val BOOT_STARTER_WEB = "org.springframework.boot:spring-boot-starter-web"
        const val BOOT_STARTER_DATA_JPA = "org.springframework.boot:spring-boot-starter-data-jpa"
        const val BOOT_STARTER_SECURITY = "org.springframework.boot:spring-boot-starter-security"
        const val BOOT_STARTER_VALIDATION = "org.springframework.boot:spring-boot-starter-validation"
        const val BOOT_STARTER_ACTUATOR = "org.springframework.boot:spring-boot-starter-actuator"
        const val BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test"
    }

    object Database {
        const val MYSQL_CONNECTOR = "com.mysql:mysql-connector-j"
    }

    object Projects {
        const val INFRA = ":infra"
        const val DOMAIN = ":domain"
        const val GLOBAL_UTILS = ":global-utils"
    }
}
