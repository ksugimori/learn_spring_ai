package com.example.todo.infrastructure.repository

import com.example.todo.domain.model.User
import com.example.todo.domain.repository.UserRepository
import com.example.todo.infrastructure.jpa.UserJpaRepository
import com.example.todo.infrastructure.mapper.UserMapper
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    private val mapper: UserMapper
) : UserRepository {

    override fun findById(id: Long): User? {
        return jpaRepository.findById(id)
            .map { mapper.toDomain(it) }
            .orElse(null)
    }

    override fun findByUsername(username: String): User? {
        return jpaRepository.findByUsername(username)
            ?.let { mapper.toDomain(it) }
    }

    override fun existsByUsername(username: String): Boolean {
        return jpaRepository.existsByUsername(username)
    }

    override fun save(user: User): User {
        val entity = mapper.toEntity(user)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findAll(): List<User> {
        return mapper.toDomainList(jpaRepository.findAll())
    }

    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }
}
