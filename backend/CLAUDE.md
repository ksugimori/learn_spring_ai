# Backend - Spring Boot Kotlin (DDD設計)

## 構成

```
backend/
├── todo-domain/              # ドメイン層（Pure Domain Model）
│   └── src/main/kotlin/com/example/todo/domain/
│       ├── model/           # User.kt, Todo.kt (JPAアノテーションなし)
│       ├── repository/      # UserRepository.kt, TodoRepository.kt (インターフェース)
│       └── service/         # UserService.kt, TodoService.kt, TodoFilter.kt, TodoSort.kt
│
├── todo-infrastructure/      # インフラストラクチャ層（データアクセス実装）
│   └── src/main/kotlin/com/example/todo/infrastructure/
│       ├── entity/          # UserEntity.kt, TodoEntity.kt (JPAエンティティ)
│       ├── jpa/            # UserJpaRepository.kt, TodoJpaRepository.kt (Spring Data JPA)
│       ├── repository/      # UserRepositoryImpl.kt, TodoRepositoryImpl.kt (実装)
│       └── mapper/          # UserMapper.kt, TodoMapper.kt (Domain ⇔ Entity変換)
│
└── todo-api/                # アプリケーション層（REST API + MCP Server）
    └── src/main/kotlin/com/example/todo/
        ├── TodoApiApplication.kt
        └── api/
            ├── config/      # SecurityConfig.kt, GlobalExceptionHandler.kt, DataInitializer.kt
            ├── controller/  # AuthController.kt, TodoController.kt
            ├── dto/         # Request/Response DTO
            ├── mcp/         # TodoTools.kt (MCP Tools)
            └── security/    # JwtTokenProvider.kt, JwtAuthenticationFilter.kt, CustomUserDetailsService.kt
```

## アーキテクチャ

### DDD（ドメイン駆動設計）

**依存関係（依存性逆転原則）:**
```
todo-api ───→ todo-domain ←─── todo-infrastructure
                 ↑                      ↓
            (インターフェース)      (実装)
```

**レイヤー構成:**
- **Domain Layer**: ビジネスロジックとルール（インフラ非依存）
- **Infrastructure Layer**: データアクセス、外部サービス連携
- **Application Layer**: ユースケース、REST API、MCP Server

## 主要クラス

### ドメイン層（todo-domain）

**ドメインモデル（Pure Domain Model）:**
- `User`: id, username, password, createdAt, updatedAt
  - JPAアノテーションなし、純粋なKotlin data class
- `Todo`: id, title, dueDate, completed, user, createdAt, updatedAt
  - ドメインロジック: `toggle()`, `isOverdue()`

**リポジトリインターフェース（ポート）:**
- `UserRepository`: ユーザーデータアクセスのインターフェース
- `TodoRepository`: Todoデータアクセスのインターフェース

**ドメインサービス:**
- `UserService`: ユーザーCRUD、重複チェック
- `TodoService`: Todo CRUD、権限チェック、`findWithFiltersAndSort()`で高度な検索
- `TodoFilter`: completed, dueDateFrom/To, keyword, hasNoDueDate
- `TodoSort`: field(TITLE/DUE_DATE/CREATED_AT/UPDATED_AT/COMPLETED), direction(ASC/DESC)

### インフラストラクチャ層（todo-infrastructure）

**JPAエンティティ:**
- `UserEntity`: @Entity, @Table等のJPAアノテーション付き
- `TodoEntity`: JPA関連の設定（@ManyToOne, @JoinColumn等）

**Spring Data JPAリポジトリ:**
- `UserJpaRepository`: JpaRepository<UserEntity, Long>
- `TodoJpaRepository`: カスタムクエリ（@Query）を含む

**リポジトリ実装（アダプター）:**
- `UserRepositoryImpl`: UserRepositoryインターフェースの実装
- `TodoRepositoryImpl`: TodoRepositoryインターフェースの実装

**マッパー:**
- `UserMapper`: User（ドメイン） ⇔ UserEntity（永続化）
- `TodoMapper`: Todo（ドメイン） ⇔ TodoEntity（永続化）

### アプリケーション層（todo-api）

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
- EntityScan: com.example.todo.infrastructure.entity
- EnableJpaRepositories: com.example.todo.infrastructure.jpa
- ポート8080

**build.gradle.kts:**
- JVMターゲット: Java 17
- マルチモジュール: todo-domain, todo-infrastructure, todo-api
- 依存関係:
  - todo-infrastructure → todo-domain
  - todo-api → todo-domain
  - todo-api → todo-infrastructure

## 設計のメリット

1. **テスタビリティ**: ドメインロジックをインフラなしでテスト可能
2. **保守性**: 関心の分離により変更の影響範囲が明確
3. **拡張性**: データストアの切り替えが容易（JPA→別のORMなど）
4. **ビジネスロジックの独立性**: ドメインモデルが純粋なビジネスルールに集中
