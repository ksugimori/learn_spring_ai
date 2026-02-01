plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

// ライブラリモジュールとして使用するため、bootJarを無効化
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // H2 Database
    runtimeOnly("com.h2database:h2")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
}
