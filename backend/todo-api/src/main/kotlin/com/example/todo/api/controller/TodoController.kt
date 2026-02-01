package com.example.todo.api.controller

import com.example.todo.api.dto.TodoRequest
import com.example.todo.api.dto.TodoResponse
import com.example.todo.api.dto.TodoUpdateRequest
import com.example.todo.domain.service.TodoService
import com.example.todo.domain.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService,
    private val userService: UserService
) {

    @GetMapping
    fun getAllTodos(authentication: Authentication): ResponseEntity<List<TodoResponse>> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todos = todoService.findByUser(user)
        val response = todos.map { TodoResponse.from(it) }

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getTodoById(
        @PathVariable id: Long,
        authentication: Authentication
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
        authentication: Authentication
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
        authentication: Authentication
    ): ResponseEntity<TodoResponse> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todo = todoService.updateTodo(id, user, request.title, request.dueDate)
        return ResponseEntity.ok(TodoResponse.from(todo))
    }

    @PatchMapping("/{id}/toggle")
    fun toggleTodo(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<TodoResponse> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todo = todoService.toggleTodo(id, user)
        return ResponseEntity.ok(TodoResponse.from(todo))
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(
        @PathVariable id: Long,
        authentication: Authentication
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
        authentication: Authentication
    ): ResponseEntity<List<TodoResponse>> {
        val user = userService.findByUsername(authentication.name)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val todos = todoService.searchByKeyword(user, keyword)
        val response = todos.map { TodoResponse.from(it) }

        return ResponseEntity.ok(response)
    }
}
