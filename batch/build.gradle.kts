import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.DOMAIN))
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.INFRA))

    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Spring.BOOT_STARTER_SECURITY)
    implementation(Dependencies.Spring.BOOT_STARTER_VALIDATION)
    implementation(Dependencies.Spring.BOOT_STARTER_ACTUATOR)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)

    implementation(Dependencies.Database.MYSQL_CONNECTOR)

    testImplementation(Dependencies.TestContainers.MYSQL)
    testImplementation(Dependencies.TestContainers.JUNIT_JUPITER)
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
