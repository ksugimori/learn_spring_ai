package com.example.todo.api.mcp

import io.modelcontextprotocol.spec.McpSchema.GetPromptResult
import io.modelcontextprotocol.spec.McpSchema.PromptMessage
import io.modelcontextprotocol.spec.McpSchema.Role
import io.modelcontextprotocol.spec.McpSchema.TextContent
import org.springaicommunity.mcp.annotation.McpArg
import org.springaicommunity.mcp.annotation.McpPrompt
import org.springframework.stereotype.Component

@Component
class TodoPrompts {
    @McpPrompt(name = "remain_todos", description = "未完了の Todo を知らせるメッセージを作成します")
    fun remainTodos(
        @McpArg(description = "ユーザー名", required = true)
        name: String,
    ): GetPromptResult {
        val propmt = """
            タスクの進捗状況をユーザーに報告するメッセージを作成してください。

            # 対象ユーザー
            - $name さん

            # 全タスクの参照元

            - learn-spring-ai://todos/all

            # 報告内容

            - 進捗状況を `完了タスク数/全タスク数 (ddd%)` というフォーマットで表示
            - 期限が近い未完了タスクのリストアップ（期限日、タイトル）
            - 次に着手するべきタスクの提案
        """.trimIndent()

        return GetPromptResult(
            "Remain Todos Prompt",
            listOf(
                PromptMessage(Role.ASSISTANT, TextContent(propmt)),
            ),
        )
    }
}
