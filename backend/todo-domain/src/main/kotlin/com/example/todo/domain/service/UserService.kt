package com.example.todo.domain.service

import com.example.todo.domain.model.User
import com.example.todo.domain.repository.TodoRepository
import com.example.todo.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val todoRepository: TodoRepository,
) {
    fun findById(id: Long): User? {
        return userRepository.findById(id)
    }

    fun findByName(name: String): User? {
        return userRepository.findByName(name)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    @Transactional
    fun createUser(name: String): User {
        if (userRepository.findByName(name) != null) {
            throw IllegalArgumentException("User name already exists: $name")
        }

        val user = User(name = name)
        return userRepository.save(user)
    }

    @Transactional
    fun updateUser(
        id: Long,
        name: String,
    ): User {
        val user = userRepository.findById(id)
            ?: throw IllegalArgumentException("User not found: $id")

        // Check if new name already exists (excluding current user)
        val existingUser = userRepository.findByName(name)
        if (existingUser != null && existingUser.id != id) {
            throw IllegalArgumentException("User name already exists: $name")
        }

        val updatedUser = user.copy(name = name)
        return userRepository.save(updatedUser)
    }

    @Transactional
    fun deleteUser(id: Long) {
        // Check if user exists
        userRepository.findById(id)
            ?: throw IllegalArgumentException("User not found: $id")

        // Check if user has any todos
        val todoCount = todoRepository.countByUserId(id)
        if (todoCount > 0) {
            throw IllegalStateException("Cannot delete user with existing todos")
        }

        userRepository.deleteById(id)
    }
}
