package com.example.todo.api.mcp

import com.example.todo.domain.service.TodoService
import org.springaicommunity.mcp.annotation.McpResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class TodoResources(private val todoService: TodoService) {
    @McpResource(
        uri = "learn-spring-ai://todos/all",
        description = "現在の全 Todo のリスト",
        mimeType = MediaType.APPLICATION_JSON_VALUE,
    )
    fun allTodos() = todoService.findAll()
}

