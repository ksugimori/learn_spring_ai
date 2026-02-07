package com.example.todo.mcp

import com.example.todo.domain.model.Todo
import com.example.todo.domain.repository.TodoRepository
import org.springaicommunity.mcp.annotation.McpTool
import org.springframework.stereotype.Component


@Component
class TodoTools(private val todoRepository: TodoRepository) {

    @McpTool(name = "get_todos", description = "Todo の一覧を取得します")
    fun getTodos(): List<Todo> {
        return todoRepository.findAll()
    }
}
