# 実装TODOリスト

このファイルは、Todoアプリケーション実装までのタスクリストです。

## フェーズ1: バックエンド基盤構築（マルチプロジェクト設定）

- [x] ディレクトリ構造の作成（`backend/`、`frontend/`）
- [x] `backend/settings.gradle.kts`でマルチプロジェクト設定
- [x] `backend/build.gradle.kts`（ルートプロジェクト設定）
- [x] `backend/todo-domain`モジュール作成
- [x] `backend/todo-api`モジュール作成
- [x] 各モジュールの`build.gradle.kts`設定
- [x] H2データベース設定

## フェーズ2: ドメイン層の実装（`backend/todo-domain`）

- [x] UserエンティティとRepository（`domain/model/User.kt`、`domain/repository/UserRepository.kt`）
- [x] TodoエンティティとRepository（`domain/model/Todo.kt`、`domain/repository/TodoRepository.kt`）
- [x] UserServiceの実装（`domain/service/UserService.kt`）
- [x] TodoServiceの実装（基本CRUD）（`domain/service/TodoService.kt`）

## フェーズ3: API層と認証機能（`backend/todo-api`）

- [x] Spring Boot起動クラス（`TodoApplication.kt`）
- [x] Spring Security設定（`api/config/SecurityConfig.kt`）
- [x] JWT認証実装（`api/security/JwtTokenProvider.kt`等）
- [x] 認証用DTO（`api/dto/LoginRequest.kt`、`api/dto/RegisterRequest.kt`等）
- [x] AuthController（`api/controller/AuthController.kt`）

## フェーズ4: Todo REST API実装（`backend/todo-api`）

- [ ] TodoController実装（`api/controller/TodoController.kt`）
- [ ] Todo用DTO（`api/dto/TodoRequest.kt`、`api/dto/TodoResponse.kt`等）
- [ ] 基本的なCRUD API実装

## フェーズ5: 検索・フィルタ・ソート機能

- [ ] `backend/todo-domain`: TodoServiceに検索・フィルタ・ソートロジック追加
- [ ] `backend/todo-api`: TodoControllerにクエリパラメータ対応追加

## フェーズ6: フロントエンド開発（`frontend/`）

- [ ] React + TypeScriptプロジェクトセットアップ（Vite等）
- [ ] APIクライアント実装（`src/api/`）
- [ ] TypeScript型定義（`src/types/`）
- [ ] ログイン・登録画面（`src/pages/`、`src/components/`）
- [ ] Todoリスト画面（`src/pages/`、`src/components/`）
- [ ] CRUD操作の実装
- [ ] 検索・フィルタUI

## フェーズ7: テストとリファイニング

- [ ] `backend/todo-domain`: ユニットテスト
- [ ] `backend/todo-api`: 統合テスト
- [ ] `frontend/`: コンポーネントテスト
- [ ] バグ修正
- [ ] UI/UX改善
