plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
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
    // todo-domain モジュールへの依存（依存性逆転）
    implementation(project(":todo-domain"))

    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // H2 Database
    runtimeOnly("com.h2database:h2")
}
