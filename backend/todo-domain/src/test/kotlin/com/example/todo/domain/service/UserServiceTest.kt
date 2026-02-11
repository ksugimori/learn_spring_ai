package com.example.todo.domain.service

import com.example.todo.domain.model.User
import com.example.todo.domain.repository.TodoRepository
import com.example.todo.domain.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class UserServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var todoRepository: TodoRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        userRepository = mock()
        todoRepository = mock()
        userService = UserService(userRepository, todoRepository)
    }

    @Test
    fun `findById should return user when exists`() {
        val user = User(id = 1L, name = "testuser")
        whenever(userRepository.findById(1L)).thenReturn(user)

        val result = userService.findById(1L)

        assertNotNull(result)
        assertEquals("testuser", result?.name)
    }

    @Test
    fun `findById should return null when not exists`() {
        whenever(userRepository.findById(1L)).thenReturn(null)

        val result = userService.findById(1L)

        assertNull(result)
    }

    @Test
    fun `findByName should return user when exists`() {
        val user = User(id = 1L, name = "testuser")
        whenever(userRepository.findByName("testuser")).thenReturn(user)

        val result = userService.findByName("testuser")

        assertNotNull(result)
        assertEquals("testuser", result?.name)
    }

    @Test
    fun `findAll should return all users`() {
        val users = listOf(
            User(id = 1L, name = "user1"),
            User(id = 2L, name = "user2"),
        )
        whenever(userRepository.findAll()).thenReturn(users)

        val result = userService.findAll()

        assertEquals(2, result.size)
        assertEquals("user1", result[0].name)
        assertEquals("user2", result[1].name)
    }

    @Test
    fun `createUser should throw exception when name exists`() {
        val existingUser = User(id = 1L, name = "testuser")
        whenever(userRepository.findByName("testuser")).thenReturn(existingUser)

        assertThrows<IllegalArgumentException> {
            userService.createUser("testuser")
        }
    }

    @Test
    fun `createUser should save and return user when name is unique`() {
        val user = User(id = 1L, name = "newuser")
        whenever(userRepository.findByName("newuser")).thenReturn(null)
        whenever(userRepository.save(any())).thenReturn(user)

        val result = userService.createUser("newuser")

        assertNotNull(result)
        assertEquals("newuser", result.name)
        verify(userRepository, times(1)).save(any())
    }

    @Test
    fun `updateUser should update and return user`() {
        val existingUser = User(id = 1L, name = "oldname")
        val updatedUser = User(id = 1L, name = "newname")
        whenever(userRepository.findById(1L)).thenReturn(existingUser)
        whenever(userRepository.findByName("newname")).thenReturn(null)
        whenever(userRepository.save(any())).thenReturn(updatedUser)

        val result = userService.updateUser(1L, "newname")

        assertEquals("newname", result.name)
        verify(userRepository, times(1)).save(any())
    }

    @Test
    fun `updateUser should throw exception when user not found`() {
        whenever(userRepository.findById(1L)).thenReturn(null)

        assertThrows<IllegalArgumentException> {
            userService.updateUser(1L, "newname")
        }
    }

    @Test
    fun `updateUser should throw exception when new name already exists`() {
        val existingUser = User(id = 1L, name = "oldname")
        val otherUser = User(id = 2L, name = "existingname")
        whenever(userRepository.findById(1L)).thenReturn(existingUser)
        whenever(userRepository.findByName("existingname")).thenReturn(otherUser)

        assertThrows<IllegalArgumentException> {
            userService.updateUser(1L, "existingname")
        }
    }

    @Test
    fun `deleteUser should delete user when no todos exist`() {
        val user = User(id = 1L, name = "testuser")
        whenever(userRepository.findById(1L)).thenReturn(user)
        whenever(todoRepository.countByUserId(1L)).thenReturn(0)

        userService.deleteUser(1L)

        verify(userRepository, times(1)).deleteById(1L)
    }

    @Test
    fun `deleteUser should throw exception when user has todos`() {
        val user = User(id = 1L, name = "testuser")
        whenever(userRepository.findById(1L)).thenReturn(user)
        whenever(todoRepository.countByUserId(1L)).thenReturn(5)

        assertThrows<IllegalStateException> {
            userService.deleteUser(1L)
        }
    }

    @Test
    fun `deleteUser should throw exception when user not found`() {
        whenever(userRepository.findById(1L)).thenReturn(null)

        assertThrows<IllegalArgumentException> {
            userService.deleteUser(1L)
        }
    }
}
