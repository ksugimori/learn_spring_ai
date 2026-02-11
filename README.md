# Learn Spring AI - Todo Application

Spring AI学習用リポジトリ。Spring Boot (Kotlin) + React (TypeScript)によるフルスタックTodoアプリケーション。

## 🎯 概要

Spring AI の使い方を学ぶために作成した Todo アプリケーションです。

## 🛠️ 技術スタック

### バックエンド
- **言語**: Kotlin 1.9.22
- **フレームワーク**: Spring Boot 3.2.2
- **データベース**: H2 Database (インメモリ)
- **MCP**: Spring AI MCP Server 1.1.2
- **ビルドツール**: Gradle
- **コード品質**: ktlint
- **アーキテクチャ**: ドメイン駆動設計 (DDD) - 3層構成

### フロントエンド
- **言語**: TypeScript
- **フレームワーク**: React 18
- **ビルドツール**: Vite
- **ルーティング**: React Router
- **HTTP クライアント**: Axios

## 📁 プロジェクト構成

詳細なアーキテクチャとディレクトリ構成については、以下を参照してください:

- [プロジェクト全体の設計](CLAUDE.md)
- [バックエンド](backend/CLAUDE.md)
- [フロントエンド](frontend/CLAUDE.md)

## 🚀 セットアップと起動

### 必要な環境
- Java 17以上
- Node.js 16以上
- npm または yarn
- make（オプション）

### 簡単な起動方法（Makefileを使用）

```bash
# 依存関係をインストール（初回のみ）
make install

# フロントエンドとバックエンドを同時に起動
make dev
# または
make start

# 停止するには Ctrl+C を押す

# ヘルプを表示
make help
```

### 開発ツール

```bash
# テストを実行
make test

# Lintチェックを実行
make lint

# Lintエラーを自動修正
make lint-fix

# ビルドを実行
make build

# クリーンアップ
make clean
```

### 手動での起動

#### バックエンドの起動

```bash
cd backend
./gradlew :todo-api:bootRun
```

バックエンドは `http://localhost:8080` で起動します。

#### フロントエンドの起動

```bash
cd frontend
npm install
npm run dev
```

フロントエンドは `http://localhost:5173` で起動します。

### MCP サーバーに Claude Desktop から接続する場合

`claude_desktop_config.json` の `mcpServers` セクションに以下を追加してください:

```json
"mcpServers": {
  "learn_spring_ai": {
    "command": "npx",
    "args": [
      "mcp-remote",
      "http://localhost:8080/mcp"
    ]
  }
}
```

## ✨ 主要機能

### ユーザー管理
- ユーザー作成（名前のみ、重複チェックあり）
- ユーザー一覧表示
- ユーザー名変更
- ユーザー削除（TODO存在時は削除不可）

### TODO 管理
- TODO 作成・更新・削除（ユーザー選択式）
- TODO 完了/未完了の切り替え
- ユーザーフィルタ
- キーワード検索
- 完了状態フィルタ
- 複数フィールドでのソート（作成日時、更新日時、期限、タイトル）
- 期限管理と期限切れ表示

### MCP Server 統合
- Spring AI MCP Server による Model Context Protocol サポート
- Claude Desktop からの接続が可能
- TODO 管理機能の MCP ツール化

