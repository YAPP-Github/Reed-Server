import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.DOMAIN))

    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_REDIS)
    implementation(Dependencies.RestClient.HTTP_CLIENT5)
    implementation(Dependencies.RestClient.HTTP_CORE5)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)

    implementation(Dependencies.Spring.KOTLIN_REFLECT)

    implementation(Dependencies.Database.MYSQL_CONNECTOR)

    implementation(Dependencies.Feign.STARTER_OPENFEIGN)

    implementation(Dependencies.Flyway.MYSQL)

    testImplementation(Dependencies.TestContainers.MYSQL)
    testImplementation(Dependencies.TestContainers.JUNIT_JUPITER)
    testImplementation(Dependencies.TestContainers.REDIS)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
