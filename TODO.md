# 実装TODOリスト

このファイルは、Todoアプリケーション実装までのタスクリストです。

## フェーズ1: バックエンド基盤構築（マルチプロジェクト設定）

- [ ] Gradleマルチプロジェクト設定（`settings.gradle.kts`）
- [ ] `todo-domain`モジュール作成
- [ ] `todo-api`モジュール作成
- [ ] 各モジュールの`build.gradle.kts`設定
- [ ] H2データベース設定

## フェーズ2: ドメイン層の実装（`todo-domain`）

- [ ] UserエンティティとRepository
- [ ] TodoエンティティとRepository
- [ ] UserServiceの実装
- [ ] TodoServiceの実装（基本CRUD）

## フェーズ3: API層と認証機能（`todo-api`）

- [ ] Spring Security設定
- [ ] JWT認証実装（JwtTokenProvider等）
- [ ] 認証用DTO（LoginRequest, RegisterRequest等）
- [ ] AuthController（ユーザー登録・ログインAPI）

## フェーズ4: Todo REST API実装（`todo-api`）

- [ ] TodoController実装
- [ ] Todo用DTO（TodoRequest, TodoResponse等）
- [ ] 基本的なCRUD API実装

## フェーズ5: 検索・フィルタ・ソート機能

- [ ] `todo-domain`: TodoServiceに検索・フィルタ・ソートロジック追加
- [ ] `todo-api`: TodoControllerにクエリパラメータ対応追加

## フェーズ6: フロントエンド開発

- [ ] React + TypeScriptプロジェクトセットアップ
- [ ] APIクライアント実装
- [ ] ログイン・登録画面
- [ ] Todoリスト画面
- [ ] CRUD操作の実装
- [ ] 検索・フィルタUI

## フェーズ7: テストとリファイニング

- [ ] `todo-domain`: ユニットテスト
- [ ] `todo-api`: 統合テスト
- [ ] フロントエンド: コンポーネントテスト
- [ ] バグ修正
- [ ] UI/UX改善
