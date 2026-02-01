# Backend - Spring Boot Kotlin

## 構成

```
backend/
├── todo-domain/              # ドメイン層
│   └── src/main/kotlin/com/example/todo/domain/
│       ├── model/           # User.kt, Todo.kt
│       ├── repository/      # UserRepository.kt, TodoRepository.kt
│       └── service/         # UserService.kt, TodoService.kt, TodoFilter.kt, TodoSort.kt
└── todo-api/                # API層
    └── src/main/kotlin/com/example/todo/
        ├── TodoApplication.kt
        └── api/
            ├── config/      # SecurityConfig.kt, GlobalExceptionHandler.kt
            ├── controller/  # AuthController.kt, TodoController.kt
            ├── dto/         # Request/Response DTO
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
- `SecurityConfig`: JWT設定、CORS、エンドポイント認可（/api/auth/**は公開、他は認証必須）
- `JwtTokenProvider`: トークン生成・検証、HS256、有効期限24時間
- `JwtAuthenticationFilter`: リクエストヘッダーからトークン抽出・検証

**コントローラー:**
- `AuthController`: POST /api/auth/register, /login, /logout
- `TodoController`: /api/todos で全CRUD + フィルタ・ソート対応

## 設定

**application.yml:**
- H2インメモリDB（jdbc:h2:mem:tododb）
- JWT secret/expiration設定
- JPA: ddl-auto=create-drop（開発用）
- ポート8080

**build.gradle.kts:**
- JVMターゲット: Java 17
- マルチモジュール: todo-domainとtodo-api
