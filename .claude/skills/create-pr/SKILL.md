---
name: create-pr
description: Use this skill when generating Pull Request descriptions following the project's Japanese PR template standard.
---

# Create PR Description

## Branch Naming
Format: `type/brief-description`  
Types: `docs`, `feat`, `fix`, `refactor`, `test`, `chore`, `ci`  
Example: `docs/add-api-versioning-adr`

## PR Title (English)
Format: `type: brief description`  
Example: `docs: add API versioning strategy (ADR-007)`

## PR Description (Japanese)

```markdown
## issue
<!-- 例: close #1 または closes #2, #3 -->
close #

## 概要 (Summary)
<!-- 変更内容の概要を簡潔に記載してください -->

## 変更内容 (Changes)
- 変更1
- 変更2

## スクリーンショット (Screenshots)
見た目の変更はありませんが、念の為動作確認しました。
<!-- UI変更がある場合: 画像または動画を添付してください -->

## テスト (Testing)
- [ ] 全体単体テスト成功 (`./gradlew testDebugUnitTest`)
- [ ] 静的解析パス (`./gradlew lintDebug`)
- [ ] デバッグビルド成功 (`./gradlew clean assembleDebug`)
- [ ] 実機 / エミュレータ E2E テスト成功 (`./gradlew :app:connectedAndroidTest` ※該当時)
- [ ] コードカバレッジ確認 (`./gradlew koverHtmlReportDebug` ※目標80%以上維持)

## テスト配置・命名規則の遵守 (Testing Architecture Compliance)
- [ ] **Guide 1 (Unit & ViewModel)**: 単体テスト・ViewModel テストが `src/test/`（Android）または `src/commonTest/`（KMP）に正しく配置されているか ([01_unit_testing.md](docs/02_project_architecture/testing/01_unit_testing.md))
- [ ] **Guide 2 (Compose View & Screen)**: Compose UI テストが `feature/*/src/test/` および `core/ui/src/test/` の Robolectric JVM テストとして配置されているか（※feature モジュール配下に不要な `androidTest` を作成していないか） ([02_compose_ui_testing.md](docs/02_project_architecture/testing/02_compose_ui_testing.md))
- [ ] **Guide 3 (Integration & E2E)**: 実機/エミュレータ E2E テストが `app/src/androidTest/` のみに配置され、通信層統合テストが `core/network/src/commonTest/` に配置されているか ([03_integration_and_e2e_testing.md](docs/02_project_architecture/testing/03_integration_and_e2e_testing.md))

## レビューレベルの設定 (Review Level)
- [ ] まったく見ないでApproveする(基本的には非推奨。)
- [ ] ぱっとみて違和感がないかチェックしてApproveする
- [x] 仕様レベルまで理解して、仕様通りに動くかある程度検証、動作確認までしてApproveする
- [ ] その他 (以下にコメント記載)

## レビュー観点 (Review Points)
- [x] マージ先のブランチの設定が正しいか (開発: `dev` / 検証: `stg` / 本番: `main`)
- [x] 紐付いているチケットと変更内容との間に相違がないか
- [ ] テストディレクトリ配置・命名規則（上記 3 ガイド）に 100% 厳格に従っているか
- [ ] 考慮漏れなどないか (エッジケース、Null安全性、ライフサイクル、例外処理)
- [ ] テストおよびビルドが正常に通過しているか (`./gradlew testDebugUnitTest`)
- [ ] 新しい設計・アーキテクチャ・命名規則・パターンを追加した場合、AIスキル（`.agents/skills/`、`.claude/skills/`）および関連ドキュメント（`docs/`）を更新したか

## 参考 (Reference)
<!-- 関連するドキュメント、ガイドライン、Issue等へのリンクを記載してください -->
- [例: 関連Issue / PR / 技術ドキュメントへのリンク](https://example.com)
```

## Guidelines
- Check only relevant items (don't check build/test for docs-only changes)
- Use clickable links: `[Text](path)`
- Keep description concise
- For documentation changes: lighter review level is acceptable

## 📂 Related Relative Paths
- **PR Template Source**: `.github/pull_request_template.md`
- **Master Testing Strategy**: `docs/02_project_architecture/testing/readme.md`
- **Guide 1: Unit & ViewModel Testing**: `docs/02_project_architecture/testing/01_unit_testing.md`
- **Guide 2: Compose View Testing**: `docs/02_project_architecture/testing/02_compose_ui_testing.md`
- **Guide 3: Integration & E2E Testing**: `docs/02_project_architecture/testing/03_integration_and_e2e_testing.md`
- **Code Review Guidelines**: `docs/01_company_and_team/04_code_review_guidelines.md`
- **Yumemi Review Culture**: `docs/01_company_and_team/09_yumemi_review_culture.md`
- **Definition of Done**: `docs/01_company_and_team/05_definition_of_done.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`
- **Issue Summary**: `docs/03_sprint_execution/03_issues_summary.md`

