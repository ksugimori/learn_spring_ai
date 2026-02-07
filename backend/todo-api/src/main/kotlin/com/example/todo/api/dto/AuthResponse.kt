package com.example.todo.api.dto

data class AuthResponse(
    val token: String,
    val username: String,
    val message: String? = null,
)
