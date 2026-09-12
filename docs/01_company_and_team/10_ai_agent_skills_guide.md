# 🤖 AI Pairing & Agent Skills Framework Standard

> [!NOTE]
> **Living Documentation Standard:**
> In this repository, AI agent skills are treated as **executable standards and living documentation**. Whenever new architectural patterns, module structures, class responsibilities, or conventions are introduced, corresponding AI skill files (`.agents/skills/`, `.claude/skills/`) and documentation (`docs/`) **must** be updated.

---

## 🎯 Purpose & Philosophy

As software engineering evolves toward human-AI collaborative development, maintaining consistency across AI-generated code is critical. Without centralized skills and instructions, AI assistants easily hallucinate deprecated patterns, bypass architectural boundaries, or produce non-idiomatic code.

This project implements an **AI Agent Skills Framework** to achieve:
1. **Zero Architecture Drift**: Guarantee that AI pairs adhere to Clean Architecture, Unidirectional Data Flow (UDF), and SOLID principles.
2. **Automated Compliance with Yumemi Evaluation Standards**: Enforce Yumemi's Qiita review criteria, review badges (`[must]`, `[imo]`, `[nits]`, `[memo]`), and memory leak prevention.
3. **Reproducible Excellence**: Provide deterministic instructions for drafting Architecture Decision Records (ADRs), Conventional Commits, and bilingual Pull Request descriptions.

---

## 🛠️ Currently Supported AI Tooling

The repository provides first-class, standardized skill configurations for:

### 1. Google Gemini & Antigravity CLI (`.agents/skills/`)
- **Format**: Official Google Gemini Agent Skill standard (`SKILL.md` with YAML frontmatter).
- **Core Guidelines**: Root [`GEMINI.md`](../../GEMINI.md).
- **Active Skills**:
  - [`yumemi-issue-workflow`](../../.agents/skills/yumemi-issue-workflow/SKILL.md): End-to-end TDD implementation guide with atomic PR sizing (<300 lines).
  - [`yumemi-code-review`](../../.agents/skills/yumemi-code-review/SKILL.md): Comprehensive review checklist against Yumemi Qiita evaluation criteria.

### 2. Anthropic Claude Code (`.claude/skills/`)
- **Format**: Official Claude Code Skill standard (`SKILL.md` with YAML frontmatter).
- **Skill Documentation**: [`.claude/README.md`](../../.claude/README.md).
- **Active Skills**:
  - [`create-adr`](../../.claude/skills/create-adr/SKILL.md): Template and workflow for drafting numbered ADRs in `docs/02_project_architecture/adr/`.
  - [`create-documentation`](../../.claude/skills/create-documentation/SKILL.md): Rules for concise, problem-focused technical docs.
  - [`create-pr`](../../.claude/skills/create-pr/SKILL.md): Standardized Japanese PR descriptions adhering to [`.github/pull_request_template.md`](../../.github/pull_request_template.md).
  - [`review-code`](../../.claude/skills/review-code/SKILL.md): Quality gate verifying SOLID principles, lifecycle cleanup, and badge categorization.
  - [`write-commit`](../../.claude/skills/write-commit/SKILL.md): Conventional Commits authoring with before/after rationale.

---

## 🌐 Using Other AI Assistants (Cursor, Copilot, Windsurf, ChatGPT, Roo Code)

We **strongly encourage** all engineers to utilize AI pairing tools of their choice while working on this codebase. If you use an AI tool other than Gemini or Claude:

### How to Integrate with Other Tools
1. **Cursor / Windsurf**:
   - Reference or mirror the skills in `.cursorrules` or `.windsurfrules`.
   - Direct the editor's system prompt to index `GEMINI.md`, `docs/01_company_and_team/`, and `.claude/skills/`.
2. **GitHub Copilot**:
   - Add workspace instructions in `.github/copilot-instructions.md` referencing `GEMINI.md` and the skill files.
3. **OpenAI ChatGPT / Web LLMs / Custom Agents**:
   - Copy the relevant `SKILL.md` directly into the chat prompt or custom GPT instructions before asking the model to write code, review a diff, or generate an ADR.

### 🔄 The Living Documentation Requirement: "Keep Skills Updated"
Whenever you implement changes that introduce:
- A new module or layer (e.g. `:core:domain`, `:core:data`, `:feature:search`)
- A new dependency injection pattern (e.g. Hilt modules)
- A new architectural pattern (e.g. `StateFlow<SearchUiState>` MVI)
- A new convention or naming scheme

> [!IMPORTANT]
> **Mandatory Rule:** You **must** update the corresponding skill files in `.agents/skills/` and `.claude/skills/`. If you introduce a new AI tool configuration (such as `.cursorrules`), check it in alongside the code so the entire team benefits.
>
> Our Pull Request template ([`.github/pull_request_template.md`](../../.github/pull_request_template.md)) includes an explicit verification checkbox:
> `- [ ] 新しい設計・アーキテクチャ・命名規則・パターンを追加した場合、AIスキル（.agents/skills/, .claude/skills/）および関連ドキュメント（docs/）を更新したか`

---

## 🗺️ AI Skills to Codebase Relative Path Mapping

All AI skills reference concrete locations in the codebase. When referencing or updating skills, consult this relative path map:

| Resource / Layer | Concrete File / Directory Relative Path | Primary Associated Skill(s) |
|:---|:---|:---|
| **Network Service (Ktor)** | [`core/network/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/network/GitHubApiService.kt`](../../core/network/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/network/GitHubApiService.kt) | `review-code`, `yumemi-code-review` |
| **Domain Layer (Entities & Repos)** | [`core/domain/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/domain/`](../../core/domain/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/domain/) | `review-code`, `yumemi-issue-workflow` |
| **Data Layer (Caching & Impl)** | [`core/data/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/data/repository/GitHubRepositoryImpl.kt`](../../core/data/src/commonMain/kotlin/jp/co/yumemi/android/codecheck/core/data/repository/GitHubRepositoryImpl.kt) | `review-code`, `yumemi-issue-workflow` |
| **Search Feature (UI & ViewModel)** | [`feature/search/src/main/kotlin/jp/co/yumemi/android/codecheck/feature/search/`](../../feature/search/src/main/kotlin/jp/co/yumemi/android/codecheck/feature/search/) | `review-code`, `yumemi-issue-workflow` |
| **Detail Feature (UI & ViewModel)** | [`feature/detail/src/main/kotlin/jp/co/yumemi/android/codecheck/feature/detail/`](../../feature/detail/src/main/kotlin/jp/co/yumemi/android/codecheck/feature/detail/) | `review-code`, `yumemi-issue-workflow` |
| **Design System & AI Workflow** | [`docs/02_project_architecture/design/ai_assisted_design_workflow.md`](../02_project_architecture/design/ai_assisted_design_workflow.md) | `review-code`, `yumemi-code-review` |
| **Application Shell & DI** | [`app/src/main/kotlin/jp/co/yumemi/android/codecheck/`](../../app/src/main/kotlin/jp/co/yumemi/android/codecheck/) | `create-pr`, `review-code` |
| **ADR Storage** | [`docs/02_project_architecture/adr/`](../02_project_architecture/adr/) | `create-adr`, `create-documentation` |
| **Sprint & Issue Roadmap**| [`docs/03_sprint_execution/`](../03_sprint_execution/) | `yumemi-issue-workflow`, `create-documentation` |
| **Review Guidelines** | [`docs/01_company_and_team/04_code_review_guidelines.md`](./04_code_review_guidelines.md) | `review-code`, `yumemi-code-review` |
| **Engineering Guardrails**| [`docs/01_company_and_team/06_engineering_guardrails.md`](./06_engineering_guardrails.md) | `write-commit`, `review-code` |
| **Definition of Done** | [`docs/01_company_and_team/05_definition_of_done.md`](./05_definition_of_done.md) | `create-pr`, `yumemi-issue-workflow` |
| **PR Template** | [`.github/pull_request_template.md`](../../.github/pull_request_template.md) | `create-pr`, `yumemi-issue-workflow` |

---

## 🚀 How Developers Should Use AI Skills

1. **When Starting an Issue**:
   Ask the AI assistant: *"Follow the `yumemi-issue-workflow` skill to plan and implement Issue #N."*
2. **When Writing an Architecture Decision**:
   Ask the AI assistant: *"Follow the `create-adr` skill to document our decision on [topic]."*
3. **When Reviewing Code or Preparing a PR**:
   Ask the AI assistant: *"Follow the `review-code` skill to perform a Yumemi code review on the latest git diff."*
4. **When Drafting Commits & PRs**:
   Ask the AI assistant: *"Follow `write-commit` and `create-pr` to prepare the commit message and PR description."*
