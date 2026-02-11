package com.example.todo.api.mcp

import com.example.todo.domain.model.User
import com.example.todo.domain.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDateTime

class UserToolsTest {
    private lateinit var userService: UserService
    private lateinit var userTools: UserTools

    @BeforeEach
    fun setup() {
        userService = mock()
        userTools = UserTools(userService)
    }

    @Test
    fun `getAllUsers should return all users from userService`() {
        // Arrange
        val expectedUsers = listOf(
            User(
                id = 1L,
                name = "Alice",
                createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2026, 1, 1, 0, 0),
            ),
            User(
                id = 2L,
                name = "Bob",
                createdAt = LocalDateTime.of(2026, 1, 2, 0, 0),
                updatedAt = LocalDateTime.of(2026, 1, 2, 0, 0),
            ),
            User(
                id = 3L,
                name = "Charlie",
                createdAt = LocalDateTime.of(2026, 1, 3, 0, 0),
                updatedAt = LocalDateTime.of(2026, 1, 3, 0, 0),
            ),
        )

        whenever(userService.findAll()).thenReturn(expectedUsers)

        // Act
        val result = userTools.getAllUsers()

        // Assert
        verify(userService).findAll()
        assertEquals(3, result.size)
        assertEquals("Alice", result[0].name)
        assertEquals("Bob", result[1].name)
        assertEquals("Charlie", result[2].name)
    }

    @Test
    fun `getAllUsers should return empty list when no users exist`() {
        // Arrange
        whenever(userService.findAll()).thenReturn(emptyList())

        // Act
        val result = userTools.getAllUsers()

        // Assert
        verify(userService).findAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllUsers should return single user`() {
        // Arrange
        val expectedUsers = listOf(
            User(
                id = 1L,
                name = "Alice",
                createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2026, 1, 1, 0, 0),
            ),
        )

        whenever(userService.findAll()).thenReturn(expectedUsers)

        // Act
        val result = userTools.getAllUsers()

        // Assert
        verify(userService).findAll()
        assertEquals(1, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("Alice", result[0].name)
    }
}
