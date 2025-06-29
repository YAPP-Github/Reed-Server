import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.INFRA))
    implementation(project(Dependencies.Projects.DOMAIN))
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.GATEWAY))

    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
    implementation(Dependencies.Spring.BOOT_STARTER_SECURITY)
    implementation(Dependencies.Spring.BOOT_STARTER_VALIDATION)
    implementation(Dependencies.Spring.BOOT_STARTER_ACTUATOR)

    implementation(Dependencies.Database.MYSQL_CONNECTOR)

    implementation(Dependencies.Auth.JWT)
    implementation(Dependencies.Auth.JWT_IMPL)
    implementation(Dependencies.Auth.JWT_JACKSON)

    implementation(Dependencies.Swagger.SPRINGDOC_OPENAPI_STARTER_WEBMVC_UI)

    implementation(Dependencies.Logging.KOTLIN_LOGGING)
    implementation(Dependencies.Feign.STARTER_OPENFEIGN)

    testImplementation(Dependencies.TestContainers.MYSQL)
    testImplementation(Dependencies.TestContainers.JUNIT_JUPITER)
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> {
        enabled = true
        mainClass.set("org.yapp.apis.ApisApplicationKt")
    }
}
