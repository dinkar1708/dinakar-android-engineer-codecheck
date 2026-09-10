---
name: create-documentation
description: Use this skill when authoring, structuring, or updating technical documentation, ADRs, or issue summaries.
---

# Documentation Standards

## 📱 Current Codebase Context
- **Current Stack:** ViewBinding + LiveData + Manual DI + Single-module
- **Target Stack:** Jetpack Compose + StateFlow + Hilt + Multi-module (aspirational)
- When documenting, be clear about what is **current** vs. **planned**

## Core Principles
1. **Single Source of Truth**: Issues documented in `03_issues_summary.md` only.
2. **Concise Code**: 5–10 lines max in docs; explain concepts, not huge implementations.
3. **Problem-Focused**: Describe WHAT and WHY, not low-level HOW.
4. **No Duplication**: Link to existing documents instead of copying text.
5. **Clickable Links**: Always use markdown links `[Text](path)`.

## ADR Format
- **Location**: `docs/02_project_architecture/adr/NNN_brief_title.md`
- **Structure**:
  - Title with ADR-NNN
  - Status, Deciders, Technical Story
  - Context & Problem Statement
  - Decision & Solution
  - Consequences (Positive & Negative)
  - Alternatives Considered
  - References

## Issue Summary Format
- **Location**: `docs/03_sprint_execution/03_issues_summary.md`
- **Format**:
  - `Issue #N: [GitHub #M - 日本語](url)`
  - Brief descriptive title
  - 2–4 sentences describing the problem
  - No redundant "Problems to fix" or "Solution" headers

## PR Description (Japanese)
- **Branch**: `type/brief-description`
- **Title (English)**: `type: brief description`
- **Template Sections**:
  - `## issue`
  - `## 概要 (Summary)`
  - `## 変更内容 (Changes)`
  - `## スクリーンショット (Screenshots)`
  - `## テスト (Testing)`
  - `## レビューレベルの設定 (Review Level)`
  - `## レビュー観点 (Review Points)`
  - `## 参考 (Reference)`

## 📂 Related Relative Paths
- **Documentation Root**: `docs/readme.md`
- **Company Handbook**: `docs/01_company_and_team/readme.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`
- **Architecture Blueprints**: `docs/02_project_architecture/readme.md`
- **Sprint Execution & Issues**: `docs/03_sprint_execution/03_issues_summary.md`
- **PR Template**: `.github/pull_request_template.md`

