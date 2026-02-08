package com.example.todo.infrastructure.mapper

import com.example.todo.domain.model.User
import com.example.todo.infrastructure.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun toEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            name = domain.name,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    fun toDomainList(entities: List<UserEntity>): List<User> {
        return entities.map { toDomain(it) }
    }
}
