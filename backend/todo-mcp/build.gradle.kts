plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.10"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    // todo-domain モジュールへの依存
    implementation(project(":todo-domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Data JPA (for annotations and configuration)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring AI MCP Server with WebMVC (SSE) support
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webmvc:1.1.2")

}
