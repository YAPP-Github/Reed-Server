import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
    implementation(Dependencies.Spring.SPRING_DATA_COMMONS) // Pageable, Page용
    implementation(Dependencies.Spring.SPRING_WEB) // HttpsStatus용으로 추가한 것이기 때문에 추후 Global-utils 모듈에 httpStatus를 정의하고 이를 의존해서 사용하도록 바꾸면 해당 의존성 삭제 가능
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
