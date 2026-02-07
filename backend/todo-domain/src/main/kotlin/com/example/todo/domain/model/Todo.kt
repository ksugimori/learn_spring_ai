package com.example.todo.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Todoドメインモデル（Pure Domain Model）
 * インフラストラクチャ（JPA等）に依存しない
 */
data class Todo(
    val id: Long? = null,
    var title: String,
    var dueDate: LocalDate? = null,
    var completed: Boolean = false,
    val user: User,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    /**
     * Todoの完了状態を切り替える
     */
    fun toggle() {
        completed = !completed
    }

    /**
     * 期限切れかどうかを判定
     */
    fun isOverdue(): Boolean {
        val due = dueDate ?: return false
        return !completed && due.isBefore(LocalDate.now())
    }
}
