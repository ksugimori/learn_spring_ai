package com.example.todo.domain.repository

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TodoRepository : JpaRepository<Todo, Long> {
    fun findByUser(user: User): List<Todo>
    fun findByUserAndCompleted(user: User, completed: Boolean): List<Todo>
    fun findByUserAndTitleContaining(user: User, keyword: String): List<Todo>

    @Query("SELECT t FROM Todo t WHERE t.user = :user AND t.dueDate < :date AND t.completed = false")
    fun findOverdueTodos(@Param("user") user: User, @Param("date") date: LocalDate): List<Todo>

    @Query("SELECT t FROM Todo t WHERE t.user = :user AND t.dueDate = :date")
    fun findTodosByDueDate(@Param("user") user: User, @Param("date") date: LocalDate): List<Todo>
}
