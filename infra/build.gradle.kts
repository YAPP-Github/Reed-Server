import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.DOMAIN))
    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_REDIS)
    implementation(Dependencies.RestClient.HTTP_CLIENT5)
    implementation(Dependencies.RestClient.HTTP_CORE5)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
    implementation(Dependencies.Spring.KOTLIN_REFLECT)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
