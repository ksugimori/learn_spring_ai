package com.example.todo.api.config

import com.example.todo.domain.service.TodoService
import com.example.todo.domain.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class DataInitializer {
    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)

    @Bean
    fun initData(
        userService: UserService,
        todoService: TodoService,
    ) = CommandLineRunner {
        logger.info("Starting sample data initialization...")

        // サンプルユーザーの作成
        val demoUser = userService.createUser(name = "demo")
        logger.info("Created demo user: ${demoUser.name}")

        val testUser = userService.createUser(name = "test")
        logger.info("Created test user: ${testUser.name}")

        // demoユーザーのサンプルTodo
        todoService.createTodo(
            user = demoUser,
            title = "Spring Bootの学習",
            dueDate = LocalDate.now().plusDays(3),
        )

        todoService.createTodo(
            user = demoUser,
            title = "Reactの学習",
            dueDate = LocalDate.now().plusDays(5),
        )

        todoService.createTodo(
            user = demoUser,
            title = "買い物に行く",
            dueDate = LocalDate.now().plusDays(1),
        )

        val completedTodo = todoService.createTodo(
            user = demoUser,
            title = "プロジェクトセットアップ",
            dueDate = LocalDate.now().minusDays(2),
        )
        // 完了済みに設定
        todoService.toggleTodo(completedTodo.id!!, demoUser)

        todoService.createTodo(
            user = demoUser,
            title = "ドキュメント作成",
            dueDate = null,
        )

        // testユーザーのサンプルTodo
        todoService.createTodo(
            user = testUser,
            title = "テストケース作成",
            dueDate = LocalDate.now().plusDays(2),
        )

        todoService.createTodo(
            user = testUser,
            title = "コードレビュー",
            dueDate = LocalDate.now(),
        )

        logger.info("Sample data initialization completed!")
        logger.info("Demo user - name: demo")
        logger.info("Test user - name: test")
    }
}
