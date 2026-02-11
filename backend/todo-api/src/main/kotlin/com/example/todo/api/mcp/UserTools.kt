package com.example.todo.api.mcp

import com.example.todo.domain.model.User
import com.example.todo.domain.service.UserService
import org.springaicommunity.mcp.annotation.McpTool
import org.springframework.stereotype.Component

@Component
class UserTools(private val userService: UserService) {
    @McpTool(name = "get_all_users", description = "すべてのユーザーを取得します")
    fun getAllUsers(): List<User> {
        return userService.findAll()
    }
}
