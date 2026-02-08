package com.example.todo.api.dto

import com.example.todo.domain.model.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id ?: throw IllegalArgumentException("User id must not be null"),
                name = user.name,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
        }
    }
}
