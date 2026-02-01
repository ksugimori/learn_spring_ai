package com.example.todo.api.controller

import com.example.todo.api.dto.RegisterRequest
import com.example.todo.api.dto.TodoRequest
import com.example.todo.api.dto.TodoUpdateRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var authToken: String

    @BeforeEach
    fun setup() {
        // Register and get token
        val registerRequest = RegisterRequest(username = "todouser_${System.currentTimeMillis()}", password = "password123")
        val result = mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        authToken = response.get("token").asText()
    }

    @Test
    fun `getAllTodos should return empty list initially`() {
        mockMvc.perform(
            get("/api/todos")
                .header("Authorization", "Bearer $authToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `createTodo should create and return todo`() {
        val request = TodoRequest(title = "Test Todo", dueDate = LocalDate.now())

        mockMvc.perform(
            post("/api/todos")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("Test Todo"))
            .andExpect(jsonPath("$.completed").value(false))
    }

    @Test
    fun `updateTodo should update and return todo`() {
        // Create todo first
        val createRequest = TodoRequest(title = "Original", dueDate = null)
        val createResult = mockMvc.perform(
            post("/api/todos")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        )
            .andReturn()

        val createResponse = objectMapper.readTree(createResult.response.contentAsString)
        val todoId = createResponse.get("id").asLong()

        // Update todo
        val updateRequest = TodoUpdateRequest(title = "Updated", dueDate = LocalDate.now())
        mockMvc.perform(
            put("/api/todos/$todoId")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Updated"))
    }

    @Test
    fun `toggleTodo should toggle completion status`() {
        // Create todo
        val createRequest = TodoRequest(title = "Toggle Test", dueDate = null)
        val createResult = mockMvc.perform(
            post("/api/todos")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        )
            .andReturn()

        val createResponse = objectMapper.readTree(createResult.response.contentAsString)
        val todoId = createResponse.get("id").asLong()

        // Toggle todo
        mockMvc.perform(
            patch("/api/todos/$todoId/toggle")
                .header("Authorization", "Bearer $authToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.completed").value(true))
    }

    @Test
    fun `deleteTodo should delete todo`() {
        // Create todo
        val createRequest = TodoRequest(title = "Delete Test", dueDate = null)
        val createResult = mockMvc.perform(
            post("/api/todos")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        )
            .andReturn()

        val createResponse = objectMapper.readTree(createResult.response.contentAsString)
        val todoId = createResponse.get("id").asLong()

        // Delete todo
        mockMvc.perform(
            delete("/api/todos/$todoId")
                .header("Authorization", "Bearer $authToken")
        )
            .andExpect(status().isNoContent)
    }

    @Test
    fun `getAllTodos should filter by completed status`() {
        // Create completed todo
        val createRequest1 = TodoRequest(title = "Completed Todo", dueDate = null)
        val createResult1 = mockMvc.perform(
            post("/api/todos")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest1))
        )
            .andReturn()

        val createResponse1 = objectMapper.readTree(createResult1.response.contentAsString)
        val todoId1 = createResponse1.get("id").asLong()

        // Toggle to complete
        mockMvc.perform(
            patch("/api/todos/$todoId1/toggle")
                .header("Authorization", "Bearer $authToken")
        )

        // Create active todo
        val createRequest2 = TodoRequest(title = "Active Todo", dueDate = null)
        mockMvc.perform(
            post("/api/todos")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest2))
        )

        // Get completed todos
        mockMvc.perform(
            get("/api/todos?completed=true")
                .header("Authorization", "Bearer $authToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].title").value("Completed Todo"))
    }

    @Test
    fun `getAllTodos should require authentication`() {
        mockMvc.perform(get("/api/todos"))
            .andExpect(status().isForbidden)
    }
}
