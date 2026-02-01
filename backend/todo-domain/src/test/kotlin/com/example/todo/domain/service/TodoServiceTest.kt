package com.example.todo.domain.service

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.User
import com.example.todo.domain.repository.TodoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.LocalDate
import java.util.*

class TodoServiceTest {

    private lateinit var todoRepository: TodoRepository
    private lateinit var todoService: TodoService
    private lateinit var testUser: User

    @BeforeEach
    fun setup() {
        todoRepository = mock(TodoRepository::class.java)
        todoService = TodoService(todoRepository)
        testUser = User(id = 1L, username = "testuser", password = "password")
    }

    @Test
    fun `findById should return todo when exists`() {
        val todo = Todo(id = 1L, title = "Test Todo", user = testUser)
        `when`(todoRepository.findById(1L)).thenReturn(Optional.of(todo))

        val result = todoService.findById(1L)

        assertNotNull(result)
        assertEquals("Test Todo", result?.title)
    }

    @Test
    fun `findByUser should return todos for user`() {
        val todos = listOf(
            Todo(id = 1L, title = "Todo 1", user = testUser),
            Todo(id = 2L, title = "Todo 2", user = testUser)
        )
        `when`(todoRepository.findByUser(testUser)).thenReturn(todos)

        val result = todoService.findByUser(testUser)

        assertEquals(2, result.size)
    }

    @Test
    fun `createTodo should save and return todo`() {
        val todo = Todo(id = 1L, title = "New Todo", dueDate = LocalDate.now(), user = testUser)
        `when`(todoRepository.save(any(Todo::class.java))).thenReturn(todo)

        val result = todoService.createTodo(testUser, "New Todo", LocalDate.now())

        assertNotNull(result)
        assertEquals("New Todo", result.title)
        verify(todoRepository, times(1)).save(any(Todo::class.java))
    }

    @Test
    fun `updateTodo should throw exception when todo not found`() {
        `when`(todoRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<IllegalArgumentException> {
            todoService.updateTodo(1L, testUser, "Updated", null)
        }
    }

    @Test
    fun `updateTodo should throw exception when user not authorized`() {
        val otherUser = User(id = 2L, username = "otheruser", password = "password")
        val todo = Todo(id = 1L, title = "Todo", user = otherUser)
        `when`(todoRepository.findById(1L)).thenReturn(Optional.of(todo))

        assertThrows<IllegalArgumentException> {
            todoService.updateTodo(1L, testUser, "Updated", null)
        }
    }

    @Test
    fun `updateTodo should update and return todo when authorized`() {
        val todo = Todo(id = 1L, title = "Original", user = testUser)
        `when`(todoRepository.findById(1L)).thenReturn(Optional.of(todo))
        `when`(todoRepository.save(any(Todo::class.java))).thenReturn(todo)

        val result = todoService.updateTodo(1L, testUser, "Updated", LocalDate.now())

        assertEquals("Updated", result.title)
        verify(todoRepository, times(1)).save(any(Todo::class.java))
    }

    @Test
    fun `toggleTodo should toggle completion status`() {
        val todo = Todo(id = 1L, title = "Todo", completed = false, user = testUser)
        `when`(todoRepository.findById(1L)).thenReturn(Optional.of(todo))
        `when`(todoRepository.save(any(Todo::class.java))).thenReturn(todo)

        val result = todoService.toggleTodo(1L, testUser)

        assertTrue(result.completed)
        verify(todoRepository, times(1)).save(any(Todo::class.java))
    }

    @Test
    fun `deleteTodo should throw exception when user not authorized`() {
        val otherUser = User(id = 2L, username = "otheruser", password = "password")
        val todo = Todo(id = 1L, title = "Todo", user = otherUser)
        `when`(todoRepository.findById(1L)).thenReturn(Optional.of(todo))

        assertThrows<IllegalArgumentException> {
            todoService.deleteTodo(1L, testUser)
        }
    }

    @Test
    fun `deleteTodo should delete when authorized`() {
        val todo = Todo(id = 1L, title = "Todo", user = testUser)
        `when`(todoRepository.findById(1L)).thenReturn(Optional.of(todo))

        todoService.deleteTodo(1L, testUser)

        verify(todoRepository, times(1)).deleteById(1L)
    }

    @Test
    fun `findWithFiltersAndSort should apply filters correctly`() {
        val todos = listOf(
            Todo(id = 1L, title = "Completed Todo", completed = true, user = testUser),
            Todo(id = 2L, title = "Active Todo", completed = false, user = testUser)
        )
        `when`(todoRepository.findByUser(testUser)).thenReturn(todos)

        val filter = TodoFilter(completed = true)
        val result = todoService.findWithFiltersAndSort(testUser, filter)

        assertEquals(1, result.size)
        assertTrue(result[0].completed)
    }

    @Test
    fun `findWithFiltersAndSort should apply keyword filter`() {
        val todos = listOf(
            Todo(id = 1L, title = "Buy groceries", user = testUser),
            Todo(id = 2L, title = "Clean house", user = testUser)
        )
        `when`(todoRepository.findByUser(testUser)).thenReturn(todos)

        val filter = TodoFilter(keyword = "buy")
        val result = todoService.findWithFiltersAndSort(testUser, filter)

        assertEquals(1, result.size)
        assertEquals("Buy groceries", result[0].title)
    }

    @Test
    fun `findWithFiltersAndSort should apply sort by title`() {
        val todos = listOf(
            Todo(id = 1L, title = "Zebra", user = testUser),
            Todo(id = 2L, title = "Apple", user = testUser)
        )
        `when`(todoRepository.findByUser(testUser)).thenReturn(todos)

        val sort = TodoSort(field = TodoSortField.TITLE, direction = SortDirection.ASC)
        val result = todoService.findWithFiltersAndSort(testUser, TodoFilter(), sort)

        assertEquals("Apple", result[0].title)
        assertEquals("Zebra", result[1].title)
    }
}
