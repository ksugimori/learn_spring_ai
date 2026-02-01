package com.example.todo.domain.service

import com.example.todo.domain.model.User
import com.example.todo.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {
    fun findById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    @Transactional
    fun createUser(username: String, encodedPassword: String): User {
        if (existsByUsername(username)) {
            throw IllegalArgumentException("Username already exists: $username")
        }

        val user = User(
            username = username,
            password = encodedPassword
        )

        return userRepository.save(user)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}
