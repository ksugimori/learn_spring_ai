package com.example.todo.api.controller

import com.example.todo.api.dto.AuthResponse
import com.example.todo.api.dto.LoginRequest
import com.example.todo.api.dto.RegisterRequest
import com.example.todo.api.security.JwtTokenProvider
import com.example.todo.domain.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest,
    ): ResponseEntity<AuthResponse> {
        // Check if username already exists
        if (userService.existsByUsername(request.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                AuthResponse(
                    token = "",
                    username = request.username,
                    message = "Username already exists",
                ),
            )
        }

        // Create new user
        val encodedPassword = passwordEncoder.encode(request.password)
        val user = userService.createUser(request.username, encodedPassword)

        // Generate JWT token
        val token = jwtTokenProvider.generateToken(user.username)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            AuthResponse(
                token = token,
                username = user.username,
                message = "User registered successfully",
            ),
        )
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<AuthResponse> {
        // Authenticate user
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password),
        )

        // Generate JWT token
        val token = jwtTokenProvider.generateToken(request.username)

        return ResponseEntity.ok(
            AuthResponse(
                token = token,
                username = request.username,
                message = "Login successful",
            ),
        )
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Map<String, String>> {
        // With JWT, logout is handled on the client side by removing the token
        return ResponseEntity.ok(mapOf("message" to "Logout successful"))
    }
}
