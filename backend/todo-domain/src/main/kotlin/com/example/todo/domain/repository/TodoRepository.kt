package com.example.todo.domain.repository

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.User
import java.time.LocalDate

/**
 * Todoリポジトリインターフェース（ポート）
 * 実装はインフラストラクチャ層で行う
 */
interface TodoRepository {
    fun findById(id: Long): Todo?

    fun findByUser(user: User): List<Todo>

    fun findByUserAndCompleted(
        user: User,
        completed: Boolean,
    ): List<Todo>

    fun findByUserAndTitleContaining(
        user: User,
        keyword: String,
    ): List<Todo>

    fun findOverdueTodos(
        user: User,
        date: LocalDate,
    ): List<Todo>

    fun findTodosByDueDate(
        user: User,
        date: LocalDate,
    ): List<Todo>

    fun save(todo: Todo): Todo

    fun findAll(): List<Todo>

    fun deleteById(id: Long)

    fun countByUserId(userId: Long): Long
}
