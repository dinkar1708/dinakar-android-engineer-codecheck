## issue
refs #9 (Issue #9: テストを追加 - PR 1/4: ドメイン・データ・通信層の単体テスト)

## 概要 (Summary)
Issue #9（テストを追加）の第1弾（PR 1/4）として、マルチモジュール Clean Architecture の基盤となる **`:core:domain`（ドメイン層）**、**`:core:data`（データ・キャッシュ層）**、および **`:core:network`（通信・マッパー層）** の単体テストハーネスを整備し、全23件のテストケースを追加しました。

外部通信や Android フレームワークに依存せず、KMP の `commonTest` において純粋な Kotlin と Ktor `MockEngine` を用いて JVM 上で高速（ミリ秒単位）に実行できるテスト構成を実現しています。
※ なお、ViewModel の単体テスト（PR 2/4）、Compose UI テスト（PR 3/4）、E2E 結合テスト（PR 4/4）は後続の各アトミック PR にて段階的に追加します。

## 変更内容 (Changes)

### 1. `:core:domain`（ドメイン層の単体テスト）
- `RepositoryItemTest`:
  - `ownerIconUrl` が `Owner.avatarUrl` に正しく委譲されることの検証
  - デフォルト引数（0L, null 等）の適用確認
  - `copy` による不変データ更新の確認
- `SearchRepositoriesUseCaseTest`:
  - 空文字または空白文字クエリ時にリポジトリを呼び出さず即座に空リストを返却する入力検証
  - 正常な検索クエリの前後の余白をトリムしてリポジトリに正しく委譲されることの確認
  - リポジトリ層で発生した例外が適切に呼び出し元へ伝播することの検証
- `GetRepositoryDetailsUseCaseTest`:
  - オーナー名またはリポジトリ名が空白の場合に `IllegalArgumentException` を送出する防御的検証
  - 正常引数時のトリムおよび詳細データ取得の確認

### 2. `:core:data`（データ・キャッシュ層の単体テスト）
- `InMemoryCacheTest`:
  - 未登録キーに対する `get` が `null` を返却することの確認
  - キャッシュ登録後の正常な値取得
  - `maxCapacity` 超過時に最も古いエントリが自動削除されることの検証
  - `clear` による全エントリ消去の確認
- `DefaultGitHubRepositoryTest`:
  - 初回検索時に API を呼び出し、結果をドメインモデルへマッピングしてインメモリキャッシュに保存することの確認
  - 同一クエリでの2回目の検索時に、ネットワーク通信を行わずキャッシュから即座に応答することの確認（API 呼び出し回数: 1回）
  - レートリミットエラー（HTTP 403 / `RateLimitExceededException`）発生時に不要なリトライを行わず即座に失敗することの検証
  - リポジトリ詳細検索（`getRepositoryDetails`）の正常系およびキャッシュ動作の確認

### 3. `:core:network`（通信・マッパー層の単体テスト）
- `GitHubApiServiceTest` (Ktor `MockEngine`):
  - 正常系レスポンス（HTTP 200）の JSON デシリアライズ検証
  - レートリミットエラー（HTTP 403）が `RateLimitExceededException` に正しくマッピングされることの検証
  - 存在しないリポジトリ（HTTP 404）が `NotFoundException` にマッピングされることの検証
- `DtoMappersTest`:
  - `RepositoryItemDto` から `RepositoryItem` へのドメインマッピングの検証
  - `fullName` が null の場合に `name` へフォールバックする安全性の確認
  - `OwnerDto` が null の場合に空文字のアバターURLで安全にドメイン変換されることの確認

### 4. ビルド設定
- `:core:domain`, `:core:data`, `:core:network` の `commonTest` ソースセットに `kotlin("test")`, `libs.kotlinx.coroutines.test`, `libs.ktor.client.mock` を追加

## スクリーンショット (Screenshots)
単体テストの追加のため見た目の変更はありません。

## テスト (Testing)
- [x] ドメイン層の単体テスト成功 (`./gradlew :core:domain:testDebugUnitTest`) - 9/9 passed
- [x] データ層の単体テスト成功 (`./gradlew :core:data:testDebugUnitTest`) - 7/7 passed
- [x] ネットワーク層の単体テスト成功 (`./gradlew :core:network:testDebugUnitTest`) - 7/7 passed
- [x] 全体単体テスト成功 (`./gradlew testDebugUnitTest`) - 23/23 passed
- [x] 静的解析パス (`./gradlew lintDebug`)
- [x] ビルド成功 (`./gradlew clean assembleDebug`)
- [ ] ViewModel 単体テスト（後続 PR 9.2 にて実施）
- [ ] Compose UI テスト（後続 PR 9.3 にて実施）
- [ ] E2E 結合テスト（後続 PR 9.4 にて実施）

## レビューレベルの設定 (Review Level)
- [ ] まったく見ないでApproveする(基本的には非推奨。)
- [ ] ぱっとみて違和感がないかチェックしてApproveする
- [x] 仕様レベルまで理解して、仕様通りに動くかある程度検証、動作確認までしてApproveする
- [ ] その他 (以下にコメント記載)

## レビュー観点 (Review Points)
- [x] マージ先のブランチの設定が正しいか (開発: `dev`)
- [x] 紐付いているチケットと変更内容との間に相違がないか (PR 1/4)
- [ ] 考慮漏れなどないか (エッジケース、Null安全性、ライフサイクル、例外処理)
- [ ] テストおよびビルドが正常に通過しているか (`./gradlew testDebugUnitTest`)
- [ ] 新しい設計・アーキテクチャ・命名規則・パターンを追加した場合、AIスキルおよび関連ドキュメントを更新したか

## 参考 (Reference)
- Android Testing Guide: https://developer.android.com/training/testing
- Kotlinx Coroutines Test: https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/
- Ktor Client Testing: https://ktor.io/docs/client-testing.html
- 関連Issue: refs #9 (PR 1/4)
