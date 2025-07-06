import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id(Plugins.SPRING_BOOT) version Versions.SPRING_BOOT
    id(Plugins.SPRING_DEPENDENCY_MANAGEMENT) version Versions.SPRING_DEPENDENCY_MANAGEMENT
    kotlin(Plugins.Kotlin.Short.KAPT) version Versions.KOTLIN
    kotlin(Plugins.Kotlin.Short.JVM) version Versions.KOTLIN
    kotlin(Plugins.Kotlin.Short.SPRING) version Versions.KOTLIN
    kotlin(Plugins.Kotlin.Short.JPA) version Versions.KOTLIN
    id(Plugins.DETEKT) version Versions.DETEKT
    id(Plugins.JACOCO)
    id(Plugins.SONAR_QUBE) version Versions.SONAR_QUBE
}

allprojects {
    group = "org"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

// 테스트하지 않는 코드 패턴 (JaCoCo + SonarQube 커버리지 + CPD 공통)
val testExclusionPatterns = listOf(
    "**/*Application.kt",
    "**/config/**",
    "**/*Config.kt",
    "**/exception/**",
    "**/*Exception.kt",
    "**/*ErrorCode.kt",
    "**/dto/**",
    "**/*Request.kt",
    "**/*Response.kt",
    "**/*Entity.kt",
    "**/annotation/**",
    "**/generated/**"
)

// SonarQube 전체 분석 제외 패턴 (분석 자체가 의미 없는 파일들)
val sonarGlobalExclusions = listOf(
    "**/build/**",
)

subprojects {
    apply(plugin = Plugins.SPRING_BOOT)
    apply(plugin = Plugins.SPRING_DEPENDENCY_MANAGEMENT)
    apply(plugin = Plugins.Kotlin.SPRING)
    apply(plugin = Plugins.Kotlin.JPA)
    apply(plugin = Plugins.Kotlin.JVM)
    apply(plugin = Plugins.JACOCO)

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(Versions.JAVA_VERSION.toInt()))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    plugins.withId(Plugins.Kotlin.ALLOPEN) {
        extensions.configure<org.jetbrains.kotlin.allopen.gradle.AllOpenExtension> {
            annotation("jakarta.persistence.Entity")
            annotation("jakarta.persistence.MappedSuperclass")
            annotation("jakarta.persistence.Embeddable")
        }
    }

    // Configure Kotlin compiler options
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xconsistent-data-class-copy-visibility"
            )
            jvmTarget = Versions.JAVA_VERSION
        }
    }
}

// 루트 프로젝트에서 모든 JaCoCo 설정 관리
configure(subprojects) {
    jacoco {
        toolVersion = Versions.JACOCO
    }

    tasks.withType<Test> {
        finalizedBy("jacocoTestReport")

        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = false
        }
    }

    // 각 서브모듈의 JaCoCo 테스트 리포트 설정
    tasks.withType<JacocoReport> {
        dependsOn("test")
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(true)
        }

        classDirectories.setFrom(fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
            exclude(testExclusionPatterns)
        })

        executionData.setFrom(fileTree(layout.buildDirectory) {
            include("jacoco/*.exec")
        })
    }
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}

// 루트 프로젝트 JaCoCo 통합 리포트 설정
tasks.register<JacocoReport>("jacocoRootReport") {
    description = "Generates an aggregate report from all subprojects"
    group = "reporting"

    dependsOn(subprojects.map { it.tasks.named("test") })

    additionalSourceDirs.setFrom(subprojects.map { it.the<SourceSetContainer>()["main"].allSource.srcDirs })
    sourceDirectories.setFrom(subprojects.map { it.the<SourceSetContainer>()["main"].allSource.srcDirs })
    classDirectories.setFrom(subprojects.map { subproject ->
        subproject.fileTree(subproject.layout.buildDirectory.get().asFile.resolve("classes/kotlin/main")) {
            exclude(testExclusionPatterns)
        }
    })
    executionData.from(subprojects.map { subproject ->
        subproject.fileTree(subproject.layout.buildDirectory.dir("jacoco")) {
            include("**/*.exec")
        }
    })

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}

// SonarQube 설정을 루트에서 모든 서브모듈에 대해 설정
sonar {
    properties {
        property("sonar.projectKey", "YAPP-Github_26th-App-Team-1-BE")
        property("sonar.organization", "yapp-github")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${layout.buildDirectory.get()}/reports/jacoco/jacocoRootReport/jacocoRootReport.xml"
        )

        // property("sonar.sources", subprojects.joinToString(",") { "${it.projectDir}/src/main/kotlin" })
        // property("sonar.tests", subprojects.joinToString(",") { "${it.projectDir}/src/test/kotlin" })
        // property("sonar.java.binaries", subprojects.joinToString(",") { "${it.layout.buildDirectory.get()}/classes/kotlin/main" })
        // property("sonar.java.test.binaries", subprojects.joinToString(",") { "${it.layout.buildDirectory.get()}/classes/kotlin/test" })

        property("sonar.kotlin.version", Versions.KOTLIN)
        property("sonar.exclusions", sonarGlobalExclusions.joinToString(","))
        property("sonar.cpd.exclusions", testExclusionPatterns.joinToString(","))
        property("sonar.coverage.exclusions", testExclusionPatterns.joinToString(","))
    }
}

// SonarQube 태스크가 통합 JaCoCo 리포트에 의존하도록 설정
tasks.named("sonar") {
    dependsOn("jacocoRootReport")
}

/**
 * CI용 - 전체 품질 검증 파이프라인을 실행합니다. (테스트, 커버리지, SonarQube 분석)
 * GitHub Actions에서 이 태스크 하나만 호출합니다.
 * 사용 예: ./gradlew fullCheck
 */
tasks.register("fullCheck") {
    description = "Runs all tests, generates reports, and performs SonarQube analysis"
    group = "Verification"
    dependsOn("testAll", "jacocoTestReportAll")
    finalizedBy("sonar")
}

/**
 * 로컬용 - SonarQube 분석 없이 빠르게 테스트 커버리지만 확인합니다.
 * 사용 예: ./gradlew checkCoverage
 */
tasks.register("checkCoverage") {
    description = "Runs tests and generates coverage reports without SonarQube analysis"
    group = "Verification"
    dependsOn("testAll", "jacocoTestReportAll")
}

/**
 * 로컬용 - 빌드 과정에서 생성된 모든 리포트를 삭제합니다.
 * 사용 예: ./gradlew cleanReports
 */
tasks.register("cleanReports") {
    description = "Cleans all generated reports"
    group = "Cleanup"
    doLast {
        subprojects.forEach { subproject ->
            delete(subproject.layout.buildDirectory.dir("reports"))
        }
        delete(layout.buildDirectory.dir("reports"))
    }
}

tasks.register("testAll") {
    description = "Runs tests in all subprojects"
    group = "Verification"
    dependsOn(subprojects.map { it.tasks.named("test") })
}

tasks.register("jacocoTestReportAll") {
    description = "Generates JaCoCo test reports for all subprojects and creates aggregate report"
    group = "Verification"
    dependsOn(subprojects.map { it.tasks.named("jacocoTestReport") })
    finalizedBy("jacocoRootReport")
}
