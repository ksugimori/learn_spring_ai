package com.example.todo.api.mcp

import com.example.todo.domain.model.Todo
import com.example.todo.domain.service.TodoFilter
import com.example.todo.domain.service.TodoService
import com.example.todo.domain.service.UserService
import org.springaicommunity.mcp.annotation.McpTool
import org.springaicommunity.mcp.annotation.McpToolParam
import org.springframework.stereotype.Component
import java.time.LocalDate
import kotlin.Boolean

@Component
class TodoTools(
    private val todoService: TodoService,
    private val userService: UserService,
) {
    @McpTool(name = "search_todos", description = "条件を指定して Todo を検索します")
    fun searchTodos(
        @McpToolParam(description = "完了しているか？", required = false)
        completed: Boolean?,
        @McpToolParam(description = "dueDate 検索範囲の下限", required = false)
        dueDateFrom: LocalDate?,
        @McpToolParam(description = "dueDate 検索範囲の上限", required = false)
        dueDateTo: LocalDate?,
        @McpToolParam(description = "title に含む文字列", required = false)
        keyword: String?,
        @McpToolParam(description = "dueDate が未指定か？", required = false)
        hasNoDueDate: Boolean?,
    ): List<Todo> {
        val todoFilter = TodoFilter(
            completed,
            dueDateFrom,
            dueDateTo,
            keyword,
            hasNoDueDate,
        )

        return todoService.findWithFilters(todoFilter)
    }

    @McpTool(name = "create_todo", description = "新しい Todo を作成します")
    fun createTodo(
        @McpToolParam(description = "Todo を作成するユーザーの ID", required = true)
        userId: Long,
        @McpToolParam(description = "Todo のタイトル", required = true)
        title: String,
        @McpToolParam(description = "Todo の期限（任意）", required = false)
        dueDate: LocalDate?,
    ): Todo {
        val user = userService.findById(userId)
            ?: throw IllegalArgumentException("User with id $userId not found")

        return todoService.createTodo(
            user = user,
            title = title,
            dueDate = dueDate,
        )
    }
}
