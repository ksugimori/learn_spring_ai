package com.example.todo.domain.service

import java.time.LocalDate

data class TodoFilter(
    val completed: Boolean? = null,
    val dueDateFrom: LocalDate? = null,
    val dueDateTo: LocalDate? = null,
    val keyword: String? = null,
    val hasNoDueDate: Boolean? = null
)
