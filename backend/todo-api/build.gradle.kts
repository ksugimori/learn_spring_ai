plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.10"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    // todo-domain モジュールへの依存
    implementation(project(":todo-domain"))

    // todo-infrastructure モジュールへの依存（リポジトリ実装）
    implementation(project(":todo-infrastructure"))

    // Spring Data JPA (for annotations and configuration)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Spring AI MCP Server with WebMVC (Streaming-HTTP) support
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webmvc:1.1.2")
}
