package com.example.todo.domain.repository

import com.example.todo.domain.model.User

/**
 * ユーザーリポジトリインターフェース（ポート）
 * 実装はインフラストラクチャ層で行う
 */
interface UserRepository {
    fun findById(id: Long): User?

    fun findByName(name: String): User?

    fun save(user: User): User

    fun findAll(): List<User>

    fun deleteById(id: Long)
}
