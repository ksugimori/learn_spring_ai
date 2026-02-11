# Frontend - React TypeScript

## 構成

```
frontend/src/
├── api/
│   ├── client.ts     # Axios設定
│   ├── users.ts      # usersApi: ユーザーCRUD操作
│   └── todos.ts      # todosApi: CRUD + フィルタ対応
├── components/
│   ├── TodoItem.tsx  # Todo表示（チェックボックス、編集・削除ボタン、期限切れハイライト）
│   ├── TodoForm.tsx  # Todo作成・編集フォーム（ユーザー選択付き）
│   ├── UserForm.tsx  # ユーザー作成・編集フォーム
│   ├── UserList.tsx  # ユーザー一覧・編集・削除
│   └── NavBar.tsx    # ナビゲーションバー
├── pages/
│   ├── TodoListPage.tsx # Todo一覧・CRUD・検索・フィルタ・ソート
│   └── UsersPage.tsx    # ユーザー管理画面
├── types/
│   └── index.ts      # TypeScript型定義（User, Todo, Request/Response型）
└── App.tsx           # ルーティング
```

## 主要コンポーネント

**ユーザー管理:**
- `UsersPage`: ユーザーの作成、編集、削除機能
- `UserForm`: ユーザー作成・編集フォーム（名前の重複チェック）
- `UserList`: ユーザー一覧表示、編集・削除アクション

**API通信:**
- `client.ts`: Axiosインスタンス設定
- `users.ts`: ユーザーCRUD API関数
- `todos.ts`: Todo CRUD + フィルタ API関数

**ページ:**
- `TodoListPage`: useState でローカル状態管理、useEffect でデータフェッチ
  - ユーザー選択: ドロップダウンでユーザーを選択してTodoを作成
  - フィルタ: completed, keyword, userId
  - ソート: sortBy, sortDirection
- `UsersPage`: ユーザー管理画面

**型定義:**
- バックエンドDTOと一致する型定義（Todo, User, Request/Response型）
- `import type { ... }` で型インポート（verbatimModuleSyntax対応）

## 環境変数

`.env`:
```
VITE_API_BASE_URL=http://localhost:8080
```

## スタイリング

インラインスタイル（CSS-in-JS）を使用。動的スタイル適用が容易。
