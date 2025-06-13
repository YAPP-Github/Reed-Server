import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
	implementation(project(Dependencies.Projects.INFRA))
	implementation(project(Dependencies.Projects.DOMAIN))
	implementation(project(Dependencies.Projects.GLOBAL_UTILS))

	implementation(Dependencies.Spring.BOOT_STARTER_WEB)
	implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
	implementation(Dependencies.Spring.BOOT_STARTER_SECURITY)
	implementation(Dependencies.Spring.BOOT_STARTER_VALIDATION)
	implementation(Dependencies.Spring.BOOT_STARTER_ACTUATOR)
	implementation(Dependencies.Database.MYSQL_CONNECTOR)

	testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
}

tasks {
	withType<Jar> { enabled = false }
	withType<BootJar> { enabled = true }
}
