package com.example.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.example.todo"])
@EnableJpaRepositories(basePackages = ["com.example.todo.domain.repository"])
@EntityScan(basePackages = ["com.example.todo.domain.model"])
class TodoMcpApplication

fun main(args: Array<String>) {
    runApplication<TodoMcpApplication>(*args)
}
