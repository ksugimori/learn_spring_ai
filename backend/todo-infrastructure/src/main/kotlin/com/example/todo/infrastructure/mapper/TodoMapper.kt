package com.example.todo.infrastructure.mapper

import com.example.todo.domain.model.Todo
import com.example.todo.infrastructure.entity.TodoEntity
import com.example.todo.infrastructure.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class TodoMapper(
    private val userMapper: UserMapper,
) {
    fun toDomain(entity: TodoEntity): Todo {
        return Todo(
            id = entity.id,
            title = entity.title,
            dueDate = entity.dueDate,
            completed = entity.completed,
            user = userMapper.toDomain(entity.user),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun toEntity(
        domain: Todo,
        userEntity: UserEntity,
    ): TodoEntity {
        return TodoEntity(
            id = domain.id,
            title = domain.title,
            dueDate = domain.dueDate,
            completed = domain.completed,
            user = userEntity,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    fun toDomainList(entities: List<TodoEntity>): List<Todo> {
        return entities.map { toDomain(it) }
    }
}
