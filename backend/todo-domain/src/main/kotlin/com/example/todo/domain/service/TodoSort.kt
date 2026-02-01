package com.example.todo.domain.service

enum class TodoSortField {
    TITLE,
    DUE_DATE,
    CREATED_AT,
    UPDATED_AT,
    COMPLETED
}

enum class SortDirection {
    ASC,
    DESC
}

data class TodoSort(
    val field: TodoSortField = TodoSortField.CREATED_AT,
    val direction: SortDirection = SortDirection.DESC
)
