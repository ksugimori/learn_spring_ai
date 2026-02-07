plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.10"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    // todo-domain モジュールへの依存
    implementation(project(":todo-domain"))

    // Spring Data JPA (for annotations and configuration)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Spring AI MCP Server with WebMVC (Streaming-HTTP) support
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webmvc:1.1.2")

    // Test
    testImplementation("org.springframework.security:spring-security-test")
}
