package com.example.todo.infrastructure.repository

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.User
import com.example.todo.domain.repository.TodoRepository
import com.example.todo.infrastructure.jpa.TodoJpaRepository
import com.example.todo.infrastructure.jpa.UserJpaRepository
import com.example.todo.infrastructure.mapper.TodoMapper
import com.example.todo.infrastructure.mapper.UserMapper
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class TodoRepositoryImpl(
    private val todoJpaRepository: TodoJpaRepository,
    private val userJpaRepository: UserJpaRepository,
    private val todoMapper: TodoMapper,
    private val userMapper: UserMapper,
) : TodoRepository {
    override fun findById(id: Long): Todo? {
        return todoJpaRepository.findById(id)
            .map { todoMapper.toDomain(it) }
            .orElse(null)
    }

    override fun findByUser(user: User): List<Todo> {
        val userEntity = user.id?.let { userJpaRepository.findById(it).orElse(null) }
            ?: return emptyList()
        return todoMapper.toDomainList(todoJpaRepository.findByUser(userEntity))
    }

    override fun findByUserAndCompleted(
        user: User,
        completed: Boolean,
    ): List<Todo> {
        val userEntity = user.id?.let { userJpaRepository.findById(it).orElse(null) }
            ?: return emptyList()
        return todoMapper.toDomainList(
            todoJpaRepository.findByUserAndCompleted(userEntity, completed),
        )
    }

    override fun findByUserAndTitleContaining(
        user: User,
        keyword: String,
    ): List<Todo> {
        val userEntity = user.id?.let { userJpaRepository.findById(it).orElse(null) }
            ?: return emptyList()
        return todoMapper.toDomainList(
            todoJpaRepository.findByUserAndTitleContaining(userEntity, keyword),
        )
    }

    override fun findOverdueTodos(
        user: User,
        date: LocalDate,
    ): List<Todo> {
        val userEntity = user.id?.let { userJpaRepository.findById(it).orElse(null) }
            ?: return emptyList()
        return todoMapper.toDomainList(
            todoJpaRepository.findOverdueTodos(userEntity, date),
        )
    }

    override fun findTodosByDueDate(
        user: User,
        date: LocalDate,
    ): List<Todo> {
        val userEntity = user.id?.let { userJpaRepository.findById(it).orElse(null) }
            ?: return emptyList()
        return todoMapper.toDomainList(
            todoJpaRepository.findTodosByDueDate(userEntity, date),
        )
    }

    override fun save(todo: Todo): Todo {
        val userEntity = todo.user.id?.let { userJpaRepository.findById(it).orElse(null) }
            ?: throw IllegalArgumentException("User not found")
        val entity = todoMapper.toEntity(todo, userEntity)
        val saved = todoJpaRepository.save(entity)
        return todoMapper.toDomain(saved)
    }

    override fun findAll(): List<Todo> {
        return todoMapper.toDomainList(todoJpaRepository.findAll())
    }

    override fun deleteById(id: Long) {
        todoJpaRepository.deleteById(id)
    }

    override fun countByUserId(userId: Long): Long {
        return todoJpaRepository.countByUserId(userId)
    }
}
