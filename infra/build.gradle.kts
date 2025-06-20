import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.DOMAIN))
    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_REDIS)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
    implementation(Dependencies.Spring.KOTLIN_REFLECT)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
