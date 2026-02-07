plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.10"
    id("io.spring.dependency-management") version "1.1.7"
}

// ライブラリモジュールとして使用するため、bootJarを無効化
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    // Spring Core (DI/Transaction for Service layer)
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")

    // Stereotype annotations (@Service, etc.)
    implementation("org.springframework:spring-core")

    // Test dependencies (Kotlin-specific)
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
}
