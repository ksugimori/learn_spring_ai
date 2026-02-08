# Todo Application

Spring Boot (Kotlin) + React (TypeScript)のフルスタックTodoアプリケーション

## 構成

```
learn_spring_ai/
├── backend/                # Spring Boot Kotlinバックエンド（DDD設計）
│   ├── todo-domain/       # ドメイン層（Pure Domain Model）
│   ├── todo-infrastructure/ # インフラストラクチャ層（データアクセス実装）
│   └── todo-api/          # アプリケーション層（REST API + MCP）
└── frontend/              # React TypeScriptフロントエンド
    └── src/
        ├── api/           # APIクライアント
        ├── components/    # 再利用可能コンポーネント
        ├── pages/         # ページコンポーネント
        └── types/         # TypeScript型定義
```

## 技術スタック

**バックエンド:** Kotlin 1.9.22, Spring Boot 3.2.2, Spring AI MCP Server 1.1.2, H2 Database, Gradle
**フロントエンド:** TypeScript, React 18, Vite, React Router, Axios

## 起動

**バックエンド (port 8080):**
```bash
cd backend && ./gradlew :todo-api:bootRun
```

**フロントエンド (port 5173):**
```bash
cd frontend && npm install && npm run dev
```

## 主要機能

- ユーザー管理（作成、更新、削除、名前重複チェック、TODO存在チェック）
- Todo CRUD操作（ユーザー選択式）
- 検索・フィルタ・ソート（キーワード、完了状態、ユーザー、複数フィールド）
- 期限管理と期限切れ表示
- MCP (Model Context Protocol) サーバー統合（Streaming-HTTP）

## 重要な設計判断

- **DDD設計**: ドメイン駆動設計に基づく3層アーキテクチャ
  - Pure Domain Model（JPAアノテーションなし）
  - リポジトリパターン（ポート&アダプター）
  - 依存性逆転原則（インフラ→ドメイン）
- **マルチモジュール構成**: domain, infrastructure, api の3モジュール分離
- **ユーザー選択方式**: 認証なし、TODO操作時にユーザーをドロップダウンで選択
- **インラインスタイル**: CSS-in-JSで型安全なスタイリング
- **H2インメモリDB**: 開発用、起動時にスキーマ自動生成（create-drop）
