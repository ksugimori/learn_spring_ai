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
            ├── config/      # WebConfig.kt, GlobalExceptionHandler.kt, DataInitializer.kt
            ├── controller/  # UserController.kt, TodoController.kt
            ├── dto/         # Request/Response DTO
            └── mcp/         # TodoTools.kt (MCP Tools)
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
- `User`: id, name, createdAt, updatedAt
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

**設定:**
- `WebConfig`: CORS設定（WebMvcConfigurer）
  - 許可オリジン: localhost:3000, localhost:5173
  - 全HTTPメソッド、全ヘッダーを許可

**コントローラー:**
- `UserController`: /api/users で全CRUD操作（作成、更新、削除、名前重複チェック）
- `TodoController`: /api/todos で全CRUD + フィルタ・ソート対応

**MCP (Model Context Protocol):**
- `TodoTools`: MCPツール実装（@McpToolアノテーション）
  - `get_todos`: Todo一覧取得
- Streaming-HTTP通信方式を使用（エンドポイント: `/mcp`）
- Spring AI MCP Server 1.1.2を統合

## 設定

**application.yml:**
- H2インメモリDB（jdbc:h2:mem:tododb）
- H2 Console有効化（/h2-console）
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
