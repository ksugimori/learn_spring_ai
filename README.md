# Learn Spring AI - Todo Application

Spring AI学習用リポジトリ。Spring Boot (Kotlin) + React (TypeScript)によるフルスタックTodoアプリケーション。

## 🎯 概要

Spring AI の使い方を学ぶために作成した Todo アプリケーションです。

## 🛠️ 技術スタック

### バックエンド
- **言語**: Kotlin 1.9.22
- **フレームワーク**: Spring Boot 3.2.2
- **認証**: Spring Security + JWT
- **データベース**: H2 Database (インメモリ)
- **MCP**: Spring AI MCP Server 1.1.2
- **ビルドツール**: Gradle
- **コード品質**: ktlint

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

### バックエンドの起動

```bash
cd backend
./gradlew :todo-api:bootRun
```

バックエンドは `http://localhost:8080` で起動します。

### フロントエンドの起動

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
