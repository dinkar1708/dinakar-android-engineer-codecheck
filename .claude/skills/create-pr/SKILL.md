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
- [ ] ビルドが成功することを確認 (`./gradlew clean assembleDebug`)
- [ ] 単体テストがパスすることを確認 (`./gradlew test`)
- [x] 動作確認済み (該当項目のみチェック)

## レビューレベルの設定 (Review Level)
- [ ] まったく見ないでApproveする(基本的には非推奨。)
- [x] ぱっとみて違和感がないかチェックしてApproveする
- [ ] 仕様レベルまで理解して、仕様通りに動くかある程度検証、動作確認までしてApproveする
- [ ] その他 (以下にコメント記載)

## レビュー観点 (Review Points)
- [x] マージ先のブランチの設定が正しいか (開発: `dev` / 検証: `stg` / 本番: `main`)
- [x] 紐付いているチケットと変更内容との間に相違がないか
- [ ] 考慮漏れなどないか (エッジケース、Null安全性、ライフサイクル、例外処理)
- [ ] テストおよびビルドが正常に通過しているか (`./gradlew test`)
- [ ] 新しい設計・アーキテクチャ・命名規則・パターンを追加した場合、AIスキル（`.agents/skills/`、`.claude/skills/`）および関連ドキュメント（`docs/`）を更新したか

## 参考 (Reference)
<!-- 関連するドキュメント、記事、ガイドラインへのリンク -->
- [Link](./path/to/doc.md)
- [AIスキル活用ガイド](docs/01_company_and_team/10_ai_agent_skills_guide.md)
```

## Guidelines
- Check only relevant items (don't check build/test for docs-only changes)
- Use clickable links: `[Text](path)`
- Keep description concise
- For documentation changes: lighter review level is acceptable

## 📂 Related Relative Paths
- **PR Template Source**: `.github/pull_request_template.md`
- **Code Review Guidelines**: `docs/01_company_and_team/04_code_review_guidelines.md`
- **Yumemi Review Culture**: `docs/01_company_and_team/09_yumemi_review_culture.md`
- **Definition of Done**: `docs/01_company_and_team/05_definition_of_done.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`
- **Issue Summary**: `docs/03_sprint_execution/03_issues_summary.md`

