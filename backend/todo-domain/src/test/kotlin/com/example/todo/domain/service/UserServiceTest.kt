package com.example.todo.domain.service

import com.example.todo.domain.model.User
import com.example.todo.domain.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class UserServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        userRepository = mock()
        userService = UserService(userRepository)
    }

    @Test
    fun `findById should return user when exists`() {
        val user = User(id = 1L, username = "testuser", password = "password")
        whenever(userRepository.findById(1L)).thenReturn(user)

        val result = userService.findById(1L)

        assertNotNull(result)
        assertEquals("testuser", result?.username)
    }

    @Test
    fun `findById should return null when not exists`() {
        whenever(userRepository.findById(1L)).thenReturn(null)

        val result = userService.findById(1L)

        assertNull(result)
    }

    @Test
    fun `findByUsername should return user when exists`() {
        val user = User(id = 1L, username = "testuser", password = "password")
        whenever(userRepository.findByUsername("testuser")).thenReturn(user)

        val result = userService.findByUsername("testuser")

        assertNotNull(result)
        assertEquals("testuser", result?.username)
    }

    @Test
    fun `existsByUsername should return true when exists`() {
        whenever(userRepository.existsByUsername("testuser")).thenReturn(true)

        val result = userService.existsByUsername("testuser")

        assertTrue(result)
    }

    @Test
    fun `createUser should throw exception when username exists`() {
        whenever(userRepository.existsByUsername("testuser")).thenReturn(true)

        assertThrows<IllegalArgumentException> {
            userService.createUser("testuser", "encodedPassword")
        }
    }

    @Test
    fun `createUser should save and return user when username is unique`() {
        val user = User(id = 1L, username = "newuser", password = "encodedPassword")
        whenever(userRepository.existsByUsername("newuser")).thenReturn(false)
        whenever(userRepository.save(any())).thenReturn(user)

        val result = userService.createUser("newuser", "encodedPassword")

        assertNotNull(result)
        assertEquals("newuser", result.username)
        verify(userRepository, times(1)).save(any())
    }
}
