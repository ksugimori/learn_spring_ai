package com.example.todo.infrastructure.jpa

import com.example.todo.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByName(name: String): UserEntity?
}
