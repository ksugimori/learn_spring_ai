package com.example.todo.infrastructure.jpa

import com.example.todo.infrastructure.entity.TodoEntity
import com.example.todo.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface TodoJpaRepository : JpaRepository<TodoEntity, Long> {
    fun findByUser(user: UserEntity): List<TodoEntity>

    fun findByUserAndCompleted(
        user: UserEntity,
        completed: Boolean,
    ): List<TodoEntity>

    fun findByUserAndTitleContaining(
        user: UserEntity,
        keyword: String,
    ): List<TodoEntity>

    @Query("SELECT t FROM TodoEntity t WHERE t.user = :user AND t.dueDate < :date AND t.completed = false")
    fun findOverdueTodos(
        @Param("user") user: UserEntity,
        @Param("date") date: LocalDate,
    ): List<TodoEntity>

    @Query("SELECT t FROM TodoEntity t WHERE t.user = :user AND t.dueDate = :date")
    fun findTodosByDueDate(
        @Param("user") user: UserEntity,
        @Param("date") date: LocalDate,
    ): List<TodoEntity>

    fun countByUserId(userId: Long): Long
}
