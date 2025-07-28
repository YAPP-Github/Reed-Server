import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.INFRA))
    implementation(project(Dependencies.Projects.DOMAIN))
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.GATEWAY))

    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
    implementation(Dependencies.Spring.BOOT_STARTER_SECURITY)
    implementation(Dependencies.Spring.BOOT_STARTER_VALIDATION)
    implementation(Dependencies.Spring.BOOT_STARTER_ACTUATOR)
    implementation(Dependencies.Spring.BOOT_STARTER_OAUTH2_CLIENT)

    implementation(Dependencies.Database.MYSQL_CONNECTOR)

    implementation(Dependencies.Swagger.SPRINGDOC_OPENAPI_STARTER_WEBMVC_UI)

    implementation(Dependencies.Logging.KOTLIN_LOGGING)

    implementation(Dependencies.BouncyCastle.BC_PROV)

    annotationProcessor(Dependencies.Spring.CONFIGURATION_PROCESSOR)

    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
    testImplementation(Dependencies.TestContainers.MYSQL)
    testImplementation(Dependencies.TestContainers.JUNIT_JUPITER)
    testImplementation(Dependencies.TestContainers.REDIS)

}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> {
        enabled = true
        mainClass.set("org.yapp.apis.ApisApplicationKt")
    }
}
