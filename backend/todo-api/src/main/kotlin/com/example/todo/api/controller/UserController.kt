package com.example.todo.api.controller

import com.example.todo.api.dto.CreateUserRequest
import com.example.todo.api.dto.UpdateUserRequest
import com.example.todo.api.dto.UserResponse
import com.example.todo.domain.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userService.findAll()
        val response = users.map { UserResponse.from(it) }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
    ): ResponseEntity<UserResponse> {
        val user = userService.findById(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(UserResponse.from(user))
    }

    @PostMapping
    fun createUser(
        @Valid @RequestBody request: CreateUserRequest,
    ): ResponseEntity<UserResponse> {
        return try {
            val user = userService.createUser(request.name)
            ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user))
        } catch (e: IllegalArgumentException) {
            // Name already exists
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest,
    ): ResponseEntity<UserResponse> {
        return try {
            val user = userService.updateUser(id, request.name)
            ResponseEntity.ok(UserResponse.from(user))
        } catch (e: IllegalArgumentException) {
            when {
                e.message?.contains("not found") == true -> ResponseEntity.notFound().build()
                e.message?.contains("already exists") == true -> ResponseEntity.status(HttpStatus.CONFLICT).build()
                else -> ResponseEntity.badRequest().build()
            }
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        return try {
            userService.deleteUser(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            // User not found
            ResponseEntity.notFound().build()
        } catch (e: IllegalStateException) {
            // User has todos
            ResponseEntity.badRequest().build()
        }
    }
}
