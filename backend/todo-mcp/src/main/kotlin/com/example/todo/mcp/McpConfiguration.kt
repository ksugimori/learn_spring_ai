package com.example.todo.mcp

import com.example.todo.domain.repository.TodoRepository
import org.springaicommunity.mcp.annotation.McpTool
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Supplier


@Configuration
class McpConfig(private val todoRepository: TodoRepository) {

    @McpTool(description = "Todo の一覧を取得します")
    @Bean
    fun getTodos() = Supplier { todoRepository.findAll() }
}
