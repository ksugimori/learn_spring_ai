package com.example.todo.api.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    val name: String,
)
