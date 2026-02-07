package com.example.todo.api.controller

import com.example.todo.api.dto.TodoRequest
import com.example.todo.api.dto.TodoResponse
import com.example.todo.api.dto.TodoUpdateRequest
import com.example.todo.domain.service.*
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService,
    private val userService: UserService,
) {
    @GetMapping
    fun getAllTodos(
        @RequestParam(required = false) completed: Boolean?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) dueDateFrom: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) dueDateTo: LocalDate?,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) hasNoDueDate: Boolean?,
        @RequestParam(required = false, defaultValue = "CREATED_AT") sortBy: String?,
        @RequestParam(required = false, defaultValue = "DESC") sortDirection: String?,
        authentication: Authentication,
    ): ResponseEntity<List<TodoResponse>> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        // フィルタ条件の構築
        val filter = TodoFilter(
            completed = completed,
            dueDateFrom = dueDateFrom,
            dueDateTo = dueDateTo,
            keyword = keyword,
            hasNoDueDate = hasNoDueDate,
        )

        // ソート条件の構築
        val sortField = try {
            TodoSortField.valueOf(sortBy ?: "CREATED_AT")
        } catch (e: IllegalArgumentException) {
            TodoSortField.CREATED_AT
        }

        val direction = try {
            SortDirection.valueOf(sortDirection ?: "DESC")
        } catch (e: IllegalArgumentException) {
            SortDirection.DESC
        }

        val sort = TodoSort(field = sortField, direction = direction)

        // フィルタ・ソート適用
        val todos = todoService.findWithFiltersAndSort(user, filter, sort)
        val response = todos.map { TodoResponse.from(it) }

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getTodoById(
        @PathVariable id: Long,
        authentication: Authentication,
    ): ResponseEntity<TodoResponse> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todo = todoService.findById(id)
            ?: return ResponseEntity.notFound().build()

        // Check if the todo belongs to the authenticated user
        if (todo.user.id != user.id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        return ResponseEntity.ok(TodoResponse.from(todo))
    }

    @PostMapping
    fun createTodo(
        @Valid @RequestBody request: TodoRequest,
        authentication: Authentication,
    ): ResponseEntity<TodoResponse> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todo = todoService.createTodo(user, request.title, request.dueDate)
        return ResponseEntity.status(HttpStatus.CREATED).body(TodoResponse.from(todo))
    }

    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Long,
        @Valid @RequestBody request: TodoUpdateRequest,
        authentication: Authentication,
    ): ResponseEntity<TodoResponse> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todo = todoService.updateTodo(id, user, request.title, request.dueDate)
        return ResponseEntity.ok(TodoResponse.from(todo))
    }

    @PatchMapping("/{id}/toggle")
    fun toggleTodo(
        @PathVariable id: Long,
        authentication: Authentication,
    ): ResponseEntity<TodoResponse> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todo = todoService.toggleTodo(id, user)
        return ResponseEntity.ok(TodoResponse.from(todo))
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(
        @PathVariable id: Long,
        authentication: Authentication,
    ): ResponseEntity<Void> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        todoService.deleteTodo(id, user)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/completed")
    fun getCompletedTodos(authentication: Authentication): ResponseEntity<List<TodoResponse>> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todos = todoService.findByUserAndCompleted(user, true)
        val response = todos.map { TodoResponse.from(it) }

        return ResponseEntity.ok(response)
    }

    @GetMapping("/active")
    fun getActiveTodos(authentication: Authentication): ResponseEntity<List<TodoResponse>> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todos = todoService.findByUserAndCompleted(user, false)
        val response = todos.map { TodoResponse.from(it) }

        return ResponseEntity.ok(response)
    }

    @GetMapping("/overdue")
    fun getOverdueTodos(authentication: Authentication): ResponseEntity<List<TodoResponse>> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todos = todoService.findOverdueTodos(user)
        val response = todos.map { TodoResponse.from(it) }

        return ResponseEntity.ok(response)
    }

    @GetMapping("/search")
    fun searchTodos(
        @RequestParam keyword: String,
        authentication: Authentication,
    ): ResponseEntity<List<TodoResponse>> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todos = todoService.searchByKeyword(user, keyword)
        val response = todos.map { TodoResponse.from(it) }

        return ResponseEntity.ok(response)
    }
}
