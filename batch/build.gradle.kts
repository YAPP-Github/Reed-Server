import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.DOMAIN))
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.INFRA))

    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Spring.BOOT_STARTER_SECURITY)
    implementation(Dependencies.Spring.BOOT_STARTER_VALIDATION)
    implementation(Dependencies.Spring.BOOT_STARTER_BATCH)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)

    implementation(Dependencies.Database.MYSQL_CONNECTOR)
    implementation(Dependencies.Database.JAKARTA_PERSISTENCE_API)

    implementation(Dependencies.Firebase.FIREBASE_ADMIN)

    testImplementation(Dependencies.TestContainers.MYSQL)
    testImplementation(Dependencies.TestContainers.JUNIT_JUPITER)
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
