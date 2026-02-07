package com.example.todo.api.dto

import com.example.todo.domain.model.Todo
import java.time.LocalDate
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val dueDate: LocalDate?,
    val completed: Boolean,
    val userId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(todo: Todo): TodoResponse {
            return TodoResponse(
                id = todo.id!!,
                title = todo.title,
                dueDate = todo.dueDate,
                completed = todo.completed,
                userId = todo.user.id!!,
                createdAt = todo.createdAt,
                updatedAt = todo.updatedAt,
            )
        }
    }
}
