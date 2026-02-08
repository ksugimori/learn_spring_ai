package com.example.todo.domain.model

import java.time.LocalDateTime

/**
 * ユーザードメインモデル（Pure Domain Model）
 * インフラストラクチャ（JPA等）に依存しない
 */
data class User(
    val id: Long? = null,
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
