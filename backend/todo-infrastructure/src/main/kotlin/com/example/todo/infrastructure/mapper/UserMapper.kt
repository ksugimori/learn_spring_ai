package com.example.todo.infrastructure.mapper

import com.example.todo.domain.model.User
import com.example.todo.infrastructure.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            password = entity.password,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            username = domain.username,
            password = domain.password,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomainList(entities: List<UserEntity>): List<User> {
        return entities.map { toDomain(it) }
    }
}
