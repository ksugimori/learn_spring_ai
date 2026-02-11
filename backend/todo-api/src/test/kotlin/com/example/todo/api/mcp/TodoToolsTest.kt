package com.example.todo.api.mcp

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.User
import com.example.todo.domain.service.TodoFilter
import com.example.todo.domain.service.TodoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate

class TodoToolsTest {
    private lateinit var todoService: TodoService
    private lateinit var todoTools: TodoTools
    private lateinit var testUser: User

    @BeforeEach
    fun setup() {
        todoService = mock()
        todoTools = TodoTools(todoService)
        testUser = User(id = 1L, name = "testuser")
    }

    @Test
    fun `searchTodos should call todoService with all parameters`() {
        // Arrange
        val completed = true
        val dueDateFrom = LocalDate.of(2026, 1, 1)
        val dueDateTo = LocalDate.of(2026, 12, 31)
        val keyword = "test"
        val hasNoDueDate = false

        val expectedFilter = TodoFilter(
            completed = completed,
            dueDateFrom = dueDateFrom,
            dueDateTo = dueDateTo,
            keyword = keyword,
            hasNoDueDate = hasNoDueDate,
        )

        val expectedTodos = listOf(
            Todo(
                id = 1L,
                title = "Test Todo",
                dueDate = LocalDate.of(2026, 6, 1),
                completed = true,
                user = testUser,
            ),
        )

        whenever(todoService.findWithFilters(expectedFilter)).thenReturn(expectedTodos)

        // Act
        val result = todoTools.searchTodos(
            completed = completed,
            dueDateFrom = dueDateFrom,
            dueDateTo = dueDateTo,
            keyword = keyword,
            hasNoDueDate = hasNoDueDate,
        )

        // Assert
        verify(todoService).findWithFilters(expectedFilter)
        assertEquals(1, result.size)
        assertEquals("Test Todo", result[0].title)
        assertEquals(true, result[0].completed)
    }

    @Test
    fun `searchTodos should handle all null parameters`() {
        // Arrange
        val expectedFilter = TodoFilter(
            completed = null,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = null,
            hasNoDueDate = null,
        )

        whenever(todoService.findWithFilters(expectedFilter)).thenReturn(emptyList())

        // Act
        val result = todoTools.searchTodos(
            completed = null,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = null,
            hasNoDueDate = null,
        )

        // Assert
        verify(todoService).findWithFilters(expectedFilter)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `searchTodos should handle partial parameters - completed only`() {
        // Arrange
        val expectedFilter = TodoFilter(
            completed = false,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = null,
            hasNoDueDate = null,
        )

        val expectedTodos = listOf(
            Todo(id = 1L, title = "Active Todo 1", completed = false, user = testUser),
            Todo(id = 2L, title = "Active Todo 2", completed = false, user = testUser),
        )

        whenever(todoService.findWithFilters(expectedFilter)).thenReturn(expectedTodos)

        // Act
        val result = todoTools.searchTodos(
            completed = false,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = null,
            hasNoDueDate = null,
        )

        // Assert
        verify(todoService).findWithFilters(expectedFilter)
        assertEquals(2, result.size)
        assertFalse(result[0].completed)
        assertFalse(result[1].completed)
    }

    @Test
    fun `searchTodos should handle partial parameters - keyword only`() {
        // Arrange
        val keyword = "buy"
        val expectedFilter = TodoFilter(
            completed = null,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = keyword,
            hasNoDueDate = null,
        )

        val expectedTodos = listOf(
            Todo(id = 1L, title = "Buy groceries", user = testUser),
        )

        whenever(todoService.findWithFilters(expectedFilter)).thenReturn(expectedTodos)

        // Act
        val result = todoTools.searchTodos(
            completed = null,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = keyword,
            hasNoDueDate = null,
        )

        // Assert
        verify(todoService).findWithFilters(expectedFilter)
        assertEquals(1, result.size)
        assertEquals("Buy groceries", result[0].title)
    }

    @Test
    fun `searchTodos should handle partial parameters - date range only`() {
        // Arrange
        val dueDateFrom = LocalDate.of(2026, 2, 1)
        val dueDateTo = LocalDate.of(2026, 2, 28)
        val expectedFilter = TodoFilter(
            completed = null,
            dueDateFrom = dueDateFrom,
            dueDateTo = dueDateTo,
            keyword = null,
            hasNoDueDate = null,
        )

        val expectedTodos = listOf(
            Todo(id = 1L, title = "February Todo", dueDate = LocalDate.of(2026, 2, 15), user = testUser),
        )

        whenever(todoService.findWithFilters(expectedFilter)).thenReturn(expectedTodos)

        // Act
        val result = todoTools.searchTodos(
            completed = null,
            dueDateFrom = dueDateFrom,
            dueDateTo = dueDateTo,
            keyword = null,
            hasNoDueDate = null,
        )

        // Assert
        verify(todoService).findWithFilters(expectedFilter)
        assertEquals(1, result.size)
        assertEquals("February Todo", result[0].title)
        assertEquals(LocalDate.of(2026, 2, 15), result[0].dueDate)
    }

    @Test
    fun `searchTodos should handle hasNoDueDate parameter`() {
        // Arrange
        val expectedFilter = TodoFilter(
            completed = null,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = null,
            hasNoDueDate = true,
        )

        val expectedTodos = listOf(
            Todo(id = 1L, title = "No due date todo", dueDate = null, user = testUser),
        )

        whenever(todoService.findWithFilters(expectedFilter)).thenReturn(expectedTodos)

        // Act
        val result = todoTools.searchTodos(
            completed = null,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = null,
            hasNoDueDate = true,
        )

        // Assert
        verify(todoService).findWithFilters(expectedFilter)
        assertEquals(1, result.size)
        assertNull(result[0].dueDate)
    }

    @Test
    fun `searchTodos should return multiple todos`() {
        // Arrange
        val expectedFilter = TodoFilter(
            completed = null,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = null,
            hasNoDueDate = null,
        )

        val expectedTodos = listOf(
            Todo(id = 1L, title = "Todo 1", user = testUser),
            Todo(id = 2L, title = "Todo 2", user = testUser),
            Todo(id = 3L, title = "Todo 3", user = testUser),
        )

        whenever(todoService.findWithFilters(expectedFilter)).thenReturn(expectedTodos)

        // Act
        val result = todoTools.searchTodos(
            completed = null,
            dueDateFrom = null,
            dueDateTo = null,
            keyword = null,
            hasNoDueDate = null,
        )

        // Assert
        verify(todoService).findWithFilters(expectedFilter)
        assertEquals(3, result.size)
    }
}