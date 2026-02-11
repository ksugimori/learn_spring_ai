# ブランチ保護ルールの設定

PR のマージ前に CI チェックを必須にするため、以下の手順でブランチ保護ルールを設定してください。

## 設定手順

1. **リポジトリの Settings に移動**
   - GitHub リポジトリページで `Settings` タブをクリック

2. **Branches セクションに移動**
   - 左サイドバーの `Branches` をクリック

3. **Branch protection rule を追加**
   - `Add branch protection rule` ボタンをクリック

4. **以下の設定を行う**

   ### Branch name pattern
   ```
   main
   ```

   ### Protect matching branches の設定

   #### ✅ Require status checks to pass before merging
   - このオプションを有効化
   - **Status checks that are required:** で `test-and-lint` を選択
     - まだ表示されない場合は、一度 PR を作成してワークフローを実行すると選択可能になります

   #### ✅ Require branches to be up to date before merging
   - このオプションも有効化（推奨）
   - main ブランチの最新の変更を含んだ状態でないとマージできなくなります

   #### ✅ その他の推奨設定
   - `Require a pull request before merging` - PR 必須
   - `Require approvals` - レビュー承認が必要な場合（オプション）
   - `Dismiss stale pull request approvals when new commits are pushed` - 新しいコミット後に再承認が必要（推奨）

5. **Create ボタンをクリックして保存**

## 設定後の動作

- PR を作成すると、CI ワークフローが自動実行されます
- `make test` または `make lint` が失敗すると、PR のマージがブロックされます
- すべてのチェックが成功するまで、マージボタンが無効化されます

## 確認方法

1. 適当な変更を含む PR を作成
2. PR ページの下部に「Required status checks」が表示される
3. ✅ test-and-lint のステータスが表示される
4. チェックが失敗している場合、マージボタンが無効になっていることを確認
