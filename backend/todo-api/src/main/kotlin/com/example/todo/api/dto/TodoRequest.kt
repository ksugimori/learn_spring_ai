package com.example.todo.api.dto

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class TodoRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,
    val dueDate: LocalDate? = null,
)
