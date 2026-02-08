# 実装TODO

認証機能削除とユーザー選択式TODO管理への移行タスク

---

## Phase 1: バックエンド - 認証削除

### 1.1 ドメイン層 (todo-domain)
- [x] `User.kt` - パスワード関連フィールドを削除
  - [x] `password` フィールド削除
  - [x] `email` フィールド削除（使用していない場合）
  - [x] `username`を`name`に変更
- [x] `UserRepository.kt` - メソッド名変更
  - [x] `findByUsername`を`findByName`に変更
  - [x] `existsByUsername`削除（findByNameで代用）

### 1.2 インフラストラクチャ層 (todo-infrastructure)
- [x] `UserEntity.kt` - パスワード関連フィールドを削除
  - [x] `password` フィールド削除
  - [x] `email` フィールド削除（使用していない場合）
  - [x] `username`を`name`に変更
  - [x] `name` に `unique = true` 制約追加済み
- [x] `UserJpaRepository.kt` - 簡素化
  - [x] `findByUsername`を`findByName`に変更
  - [x] `existsByUsername`削除
- [x] `UserRepositoryImpl.kt` - 簡素化
  - [x] `findByUsername`を`findByName`に変更
  - [x] `existsByUsername`削除
- [x] `UserMapper.kt` - マッピング更新（パスワードフィールド除外）

### 1.3 API層 (todo-api)
- [x] JWT認証関連クラスの削除
  - [x] `JwtTokenProvider.kt` 削除
  - [x] `JwtAuthenticationFilter.kt` 削除
  - [x] `CustomUserDetailsService.kt` 削除
- [x] `AuthController.kt` - 削除
- [x] `SecurityConfig.kt` - 簡素化
  - [x] JWT認証フィルターの削除
  - [x] すべてのエンドポイントを `permitAll()` に変更
  - [x] CORS設定は維持
- [x] `TodoController.kt` - 認証チェック削除
  - [x] `Authentication`パラメータ削除
  - [x] 暫定的に`userId`をリクエストパラメータで受け取るように変更
- [x] 認証関連DTO削除
  - [x] `LoginRequest`, `RegisterRequest`, `AuthResponse` 削除
- [x] `UserService.kt` - 簡素化
  - [x] `findByUsername`を`findByName`に変更
  - [x] `createUser`メソッド簡素化（名前のみ）
- [x] `DataInitializer.kt` - 修正
  - [x] `PasswordEncoder`依存削除
  - [x] `createUser`呼び出し更新
- [x] `application.yml` - JWT設定削除

### 1.4 Phase 1 テスト
- [x] バックエンド起動確認
  - [x] Spring Boot起動エラーがないこと
  - [x] コンパイルエラーがないこと
- [x] 既存のTODO API動作確認（認証なしでアクセス可能）
  - [x] GET /api/todos?userId=1 でアクセス可能（認証なし）
  - [x] POST /api/todos?userId=1 でアクセス可能（認証なし）

---

## Phase 2: バックエンド - ユーザー管理API

### 2.1 ドメイン層 (todo-domain)
- [ ] `UserService.kt` - サービス層実装
  - [ ] `createUser(name: String): User` - ユーザー作成
  - [ ] `getAllUsers(): List<User>` - 全ユーザー取得
  - [ ] `getUserById(id: Long): User` - ID指定取得
  - [ ] `updateUser(id: Long, name: String): User` - ユーザー名更新
  - [ ] `deleteUser(id: Long)` - ユーザー削除
  - [ ] 名前重複チェックロジック
  - [ ] TODO存在チェックロジック（削除時）

### 2.2 インフラストラクチャ層 (todo-infrastructure)
- [ ] `TodoRepository` - メソッド追加
  - [ ] `countByUserId(userId: Long): Long` 追加（ユーザー削除チェック用）

### 2.3 API層 (todo-api)
- [ ] `UserController.kt` - 新規作成
  - [ ] `POST /api/users` - ユーザー作成
  - [ ] `GET /api/users` - ユーザー一覧取得
  - [ ] `GET /api/users/{id}` - ユーザー詳細取得
  - [ ] `PUT /api/users/{id}` - ユーザー名変更
  - [ ] `DELETE /api/users/{id}` - ユーザー削除
- [ ] DTO作成
  - [ ] `CreateUserRequest.kt` - `name: String`
  - [ ] `UpdateUserRequest.kt` - `name: String`
  - [ ] `UserResponse.kt` - `id: Long, name: String`
- [ ] 例外処理
  - [ ] 名前重複時のエラー処理（409 Conflict）
  - [ ] TODO存在時の削除エラー処理（400 Bad Request）
  - [ ] ユーザーが見つからない場合のエラー処理（404 Not Found）

### 2.4 Phase 2 テスト
- [ ] ユーザーAPI動作確認
  - [ ] POST /api/users - ユーザー作成成功
  - [ ] GET /api/users - ユーザー一覧取得成功
  - [ ] GET /api/users/{id} - ユーザー詳細取得成功
  - [ ] PUT /api/users/{id} - ユーザー名更新成功
  - [ ] DELETE /api/users/{id} - ユーザー削除成功
  - [ ] 名前重複時に409エラー
  - [ ] TODOが存在するユーザー削除時に400エラー
- [ ] ktlintチェック実行（必要に応じて）

---

## Phase 3: フロントエンド - 認証削除

### 3.1 認証関連ファイルの削除
- [ ] `src/contexts/AuthContext.tsx` - 削除
- [ ] `src/pages/LoginPage.tsx` - 削除
- [ ] `src/pages/RegisterPage.tsx` - 削除
- [ ] `src/types/auth.ts` - 削除（存在する場合）

### 3.2 API設定の更新
- [ ] `src/api/axios.ts` または類似ファイル - 認証ヘッダー削除
  - [ ] Axiosインターセプターの認証ヘッダー設定を削除
  - [ ] LocalStorageからのトークン取得処理を削除
  - [ ] 401エラーハンドリングの削除

### 3.3 ルーティング設定の更新
- [ ] `src/App.tsx` - ルート更新
  - [ ] `/login` ルート削除
  - [ ] `/register` ルート削除
  - [ ] AuthContext.Providerの削除
  - [ ] デフォルトルートを `/todos` に変更
  - [ ] 認証ガードの削除（あれば）

### 3.4 ナビゲーション・ヘッダーの更新
- [ ] ログイン/ログアウトボタンの削除
- [ ] ユーザー表示の削除

### 3.5 Phase 3 テスト
- [ ] フロントエンド起動確認
  - [ ] npm run dev で起動成功
  - [ ] コンパイルエラーがないこと
  - [ ] デフォルトルートが /todos であること
- [ ] 認証関連のコードが完全に削除されていること確認

---

## Phase 4: フロントエンド - ユーザー管理UI

### 4.1 型定義
- [ ] `src/types/user.ts` - 新規作成
  - [ ] `User` インターフェース定義
  - [ ] `CreateUserRequest` インターフェース定義
  - [ ] `UpdateUserRequest` インターフェース定義

### 4.2 APIクライアント
- [ ] `src/api/users.ts` - 新規作成
  - [ ] `getUsers(): Promise<User[]>` - ユーザー一覧取得
  - [ ] `getUser(id: number): Promise<User>` - ユーザー詳細取得
  - [ ] `createUser(name: string): Promise<User>` - ユーザー作成
  - [ ] `updateUser(id: number, name: string): Promise<User>` - ユーザー更新
  - [ ] `deleteUser(id: number): Promise<void>` - ユーザー削除

### 4.3 コンポーネント
- [ ] `src/components/UserForm.tsx` - 新規作成
  - [ ] ユーザー作成フォーム（名前のみ）
  - [ ] ユーザー編集フォーム（名前変更）
  - [ ] バリデーション（名前が空でないこと）
  - [ ] エラー表示（重複エラーなど）

- [ ] `src/components/UserList.tsx` - 新規作成
  - [ ] ユーザー一覧表示
  - [ ] 編集ボタン
  - [ ] 削除ボタン
  - [ ] 削除確認ダイアログ
  - [ ] エラー表示（TODOが存在する場合）

### 4.4 ページ
- [ ] `src/pages/UsersPage.tsx` - 新規作成
  - [ ] UserFormコンポーネントの配置
  - [ ] UserListコンポーネントの配置
  - [ ] ユーザー作成・更新・削除の処理
  - [ ] ローディング状態の管理
  - [ ] エラーハンドリング

### 4.5 ルーティング
- [ ] `src/App.tsx` - ルート追加
  - [ ] `/users` ルート追加
  - [ ] ナビゲーションにユーザー管理リンク追加

### 4.6 Phase 4 テスト
- [ ] ユーザー管理画面の動作確認
  - [ ] /users にアクセス可能
  - [ ] ユーザー作成成功
  - [ ] ユーザー一覧表示成功
  - [ ] ユーザー編集成功
  - [ ] ユーザー削除成功
  - [ ] 名前重複時のエラー表示
  - [ ] TODOが存在するユーザー削除時のエラー表示
- [ ] ESLintチェック実行（必要に応じて）

---

## Phase 5: フロントエンド - TODO機能統合

### 5.1 型定義の更新
- [ ] `src/types/todo.ts` - Todo型にユーザー情報追加
  - [ ] `userId: number` フィールド確認（既存）
  - [ ] `userName?: string` フィールド追加（オプショナル、バックエンドが返す場合）

### 5.2 APIクライアントの確認
- [ ] `src/api/todos.ts` - レスポンス型の確認
  - [ ] TodoレスポンスにuserIdが含まれることを確認
  - [ ] 必要に応じてuserName取得ロジック追加

### 5.3 コンポーネントの更新
- [ ] `src/components/TodoForm.tsx` - ユーザー選択追加
  - [ ] ユーザー一覧取得処理追加
  - [ ] ユーザー選択ドロップダウン追加
  - [ ] 必須フィールドとして設定
  - [ ] デフォルト値設定（編集時は既存ユーザー）
  - [ ] 送信時にuserIdを含める

### 5.4 TODO一覧画面の更新
- [ ] `src/pages/TodosPage.tsx` - ユーザーフィルタ追加
  - [ ] ユーザー一覧取得処理追加
  - [ ] ユーザーフィルタドロップダウン追加
  - [ ] フィルタ適用ロジック
  - [ ] 「すべてのユーザー」オプション追加

- [ ] `src/components/TodoList.tsx` - ユーザー名表示追加
  - [ ] 各TODOにユーザー名を表示
  - [ ] ユーザー名のスタイリング

### 5.5 Phase 5 テスト
- [ ] TODO管理画面の動作確認
  - [ ] ユーザー選択ドロップダウン表示
  - [ ] ユーザー選択なしで作成不可
  - [ ] TODO作成時にユーザーが設定される
  - [ ] TODO編集時にユーザー変更可能
  - [ ] TODO一覧でユーザー名表示（またはユーザーID表示）
  - [ ] ユーザーフィルタが動作
- [ ] 既存機能の動作確認
  - [ ] 検索機能が正常動作
  - [ ] 完了状態フィルタが正常動作
  - [ ] ソート機能が正常動作
  - [ ] 期限管理・期限切れ表示が正常動作

---

## Phase 6: 統合テスト・クリーンアップ

### 6.1 統合テスト
- [ ] エンドツーエンドの動作確認
  - [ ] ユーザー作成 → TODO作成 → フィルタ → 完了までの一連の流れ
  - [ ] ユーザー削除時のTODO存在チェック
  - [ ] TODOのユーザー変更
  - [ ] 複数ユーザーでのTODO管理

### 6.2 コードクリーンアップ
- [ ] 不要なimport文の削除
- [ ] 不要なDTO・型定義の削除
- [ ] デッドコードの削除
- [ ] コードフォーマット整形

### 6.3 コード品質確認
- [ ] ktlintチェック実行（バックエンド）
- [ ] ESLintチェック実行（フロントエンド）
- [ ] 警告の解消

### 6.4 ドキュメント更新
- [ ] README.md - 機能説明の更新
  - [ ] 認証機能削除を反映
  - [ ] ユーザー管理機能を追加
- [ ] CLAUDE.md - アーキテクチャ説明の更新
  - [ ] JWT認証の記載削除
  - [ ] ユーザー選択方式の説明追加

---

## 完了条件

- [ ] すべてのPhase 1〜6のタスクが完了
- [ ] バックエンドが正常に起動し、すべてのAPIが動作
- [ ] フロントエンドが正常に起動し、すべての画面が表示・操作可能
- [ ] 認証機能が完全に削除されている
- [ ] ユーザー管理機能が正常に動作
- [ ] TODO管理でユーザー選択・フィルタが動作
- [ ] 既存のTODO機能（検索、フィルタ、ソート、期限管理）が正常動作
- [ ] ドキュメントが最新状態に更新されている
- [ ] コード品質チェックに合格
