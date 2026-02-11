.PHONY: help dev start backend frontend stop clean install lint lint-fix

# デフォルトターゲット
.DEFAULT_GOAL := help

help: ## ヘルプを表示
	@echo "使用可能なコマンド:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

dev: ## フロントエンドとバックエンドを並列起動（開発モード）
	@echo "📦 アプリケーションを起動中..."
	@echo ""
	@echo "🎨 フロントエンド: http://localhost:5173/"
	@echo "🚀 バックエンド MCP サーバー: http://localhost:8080/"
	@echo ""
	@echo "停止するには Ctrl+C を押してください"
	@echo ""
	@trap 'kill 0' INT; \
	(cd backend && ./gradlew :todo-api:bootRun) & \
	(cd frontend && npm run dev) & \
	wait

start: dev ## devのエイリアス

backend: ## バックエンドのみ起動
	@echo "🚀 バックエンドを起動中..."
	cd backend && ./gradlew :todo-api:bootRun

frontend: ## フロントエンドのみ起動
	@echo "🎨 フロントエンドを起動中..."
	cd frontend && npm run dev

install: ## 依存関係をインストール
	@echo "📥 フロントエンド依存関係をインストール中..."
	cd frontend && npm install
	@echo "✅ インストール完了"

build: ## プロジェクトをビルド
	@echo "🔨 バックエンドをビルド中..."
	cd backend && ./gradlew build
	@echo "🔨 フロントエンドをビルド中..."
	cd frontend && npm run build
	@echo "✅ ビルド完了"

test: ## テストを実行
	@echo "🧪 バックエンドテストを実行中..."
	cd backend && ./gradlew test
	@echo "🧪 フロントエンドテストを実行中..."
	cd frontend && npm test
	@echo "✅ テスト完了"

lint: ## Lintチェックを実行（バックエンド: ktlint、フロントエンド: ESLint）
	@echo "🔍 バックエンドのLintチェックを実行中..."
	cd backend && ./gradlew ktlintCheck
	@echo "🔍 フロントエンドのLintチェックを実行中..."
	cd frontend && npm run lint
	@echo "✅ Lintチェック完了"

lint-fix: ## Lintエラーを自動修正（バックエンド: ktlint format、フロントエンド: ESLint fix）
	@echo "🔧 バックエンドのLintエラーを修正中..."
	cd backend && ./gradlew ktlintFormat
	@echo "🔧 フロントエンドのLintエラーを修正中..."
	cd frontend && npm run lint -- --fix
	@echo "✅ Lint修正完了"

clean: ## ビルド成果物をクリーンアップ
	@echo "🧹 クリーンアップ中..."
	cd backend && ./gradlew clean
	cd frontend && rm -rf dist node_modules/.vite
	@echo "✅ クリーンアップ完了"

stop: ## 起動中のプロセスを停止
	@echo "🛑 アプリケーションを停止中..."
	@pkill -f "todo-api:bootRun" || true
	@pkill -f "vite" || true
	@echo "✅ 停止完了"

status: ## 起動中のプロセスを確認
	@echo "📊 起動中のプロセス:"
	@pgrep -fl "todo-api:bootRun" || echo "  バックエンド: 停止中"
	@pgrep -fl "vite" || echo "  フロントエンド: 停止中"
