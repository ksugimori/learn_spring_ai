# Frontend - React TypeScript

## 構成

```
frontend/src/
├── api/
│   ├── client.ts     # Axios設定（JWT自動付与、401エラー処理）
│   ├── auth.ts       # authApi: login, register, logout
│   └── todos.ts      # todosApi: CRUD + フィルタ対応
├── components/
│   ├── TodoItem.tsx  # Todo表示（チェックボックス、編集・削除ボタン、期限切れハイライト）
│   └── TodoForm.tsx  # Todo作成・編集フォーム
├── contexts/
│   └── AuthContext.tsx # 認証状態管理（isAuthenticated, username, login/register/logout）
├── pages/
│   ├── LoginPage.tsx    # ログイン画面
│   ├── RegisterPage.tsx # 登録画面（パスワード確認付き）
│   └── TodoListPage.tsx # Todo一覧・CRUD・検索・フィルタ・ソート
├── types/
│   └── index.ts      # TypeScript型定義（User, Todo, Request/Response型）
└── App.tsx           # ルーティング（PrivateRoute, PublicRoute）
```

## 主要コンポーネント

**認証:**
- `AuthContext`: LocalStorageでトークン永続化、グローバル認証状態
- `PrivateRoute`: 認証済みのみアクセス可、未認証は/loginへ
- `PublicRoute`: 未認証のみアクセス可、認証済みは/へ

**API通信:**
- `client.ts`: Axiosインターセプターで全リクエストにJWT自動付与、401エラーで自動ログアウト
- `auth.ts`, `todos.ts`: 型安全なAPI関数

**ページ:**
- `TodoListPage`: useState でローカル状態管理、useEffect でデータフェッチ
  - フィルタ: completed, keyword
  - ソート: sortBy, sortDirection

**型定義:**
- バックエンドDTOと一致する型定義（Todo, User, AuthResponse等）
- `import type { ... }` で型インポート（verbatimModuleSyntax対応）

## 環境変数

`.env`:
```
VITE_API_BASE_URL=http://localhost:8080
```

## スタイリング

インラインスタイル（CSS-in-JS）を使用。動的スタイル適用が容易。
