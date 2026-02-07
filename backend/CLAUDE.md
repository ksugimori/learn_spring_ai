# Backend - Spring Boot Kotlin

## 構成

```
backend/
├── todo-domain/              # ドメイン層
│   └── src/main/kotlin/com/example/todo/domain/
│       ├── model/           # User.kt, Todo.kt
│       ├── repository/      # UserRepository.kt, TodoRepository.kt
│       └── service/         # UserService.kt, TodoService.kt, TodoFilter.kt, TodoSort.kt
└── todo-api/                # API層 + MCP Server
    └── src/main/kotlin/com/example/todo/
        ├── TodoApiApplication.kt
        └── api/
            ├── config/      # SecurityConfig.kt, GlobalExceptionHandler.kt
            ├── controller/  # AuthController.kt, TodoController.kt
            ├── dto/         # Request/Response DTO
            ├── mcp/         # TodoTools.kt (MCP Tools)
            └── security/    # JwtTokenProvider.kt, JwtAuthenticationFilter.kt, CustomUserDetailsService.kt
```

## 主要クラス

**エンティティ:**
- `User`: id, username, password(BCrypt), createdAt, updatedAt
- `Todo`: id, title, dueDate, completed, user(ManyToOne), createdAt, updatedAt

**サービス:**
- `UserService`: ユーザーCRUD、重複チェック
- `TodoService`: Todo CRUD、権限チェック、`findWithFiltersAndSort()`で高度な検索
- `TodoFilter`: completed, dueDateFrom/To, keyword, hasNoDueDate
- `TodoSort`: field(TITLE/DUE_DATE/CREATED_AT/UPDATED_AT/COMPLETED), direction(ASC/DESC)

**セキュリティ:**
- `SecurityConfig`: JWT設定、CORS、エンドポイント認可
  - 公開: /api/auth/**, /h2-console/**, /mcp/**
  - 認証必須: 上記以外の全エンドポイント
- `JwtTokenProvider`: トークン生成・検証、HS256、有効期限24時間
- `JwtAuthenticationFilter`: リクエストヘッダーからトークン抽出・検証

**コントローラー:**
- `AuthController`: POST /api/auth/register, /login, /logout
- `TodoController`: /api/todos で全CRUD + フィルタ・ソート対応

**MCP (Model Context Protocol):**
- `TodoTools`: MCPツール実装（@McpToolアノテーション）
  - `get_todos`: Todo一覧取得
- Streaming-HTTP通信方式を使用（エンドポイント: `/mcp`）
- Spring AI MCP Server 1.1.2を統合

## 設定

**application.yml:**
- H2インメモリDB（jdbc:h2:mem:tododb）
- JWT secret/expiration設定
- JPA: ddl-auto=create-drop（開発用）
- MCP: protocol=STREAMABLE, transport=WEBMVC
- ポート8080

**build.gradle.kts:**
- JVMターゲット: Java 17
- マルチモジュール: todo-domainとtodo-api
