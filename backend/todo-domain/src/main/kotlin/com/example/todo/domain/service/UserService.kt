package com.example.todo.domain.service

import com.example.todo.domain.model.User
import com.example.todo.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
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
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}
