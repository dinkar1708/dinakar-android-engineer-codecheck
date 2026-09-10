---
name: write-commit
description: Use this skill when authoring Conventional Commit messages aligned with our project history, SOLID principles, and before/after metrics.
---

# Write Commit Message

## Format Standards

Follow [Conventional Commits](https://www.conventionalcommits.org/) format:

```text
type: imperative concise description in English

[optional body explaining WHAT and WHY]
- Detail 1
- Detail 2

Before: [previous state or violation]
After: [new compliant state]

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>
```

---

## Allowed Types
- `feat` &mdash; New feature or user capability
- `fix` &mdash; Bug fix (e.g., typo, memory leak, crash)
- `refactor` &mdash; Code restructuring without changing behavior (e.g., SOLID extraction)
- `docs` &mdash; Documentation only changes
- `test` &mdash; Adding or updating unit/UI tests
- `chore` &mdash; Build scripts, dependencies, tooling, or CI updates

---

## Real Codebase Examples

### 1. Architectural Refactoring (SOLID Principles)
```text
refactor: extract API Client layer to enforce Single Responsibility

- Create GitHubApiClient interface and GitHubApiClientImpl
- Move HTTP Ktor execution logic out of RepositorySearchViewModel
- Implement Closeable for clean HTTP client teardown

Before: RepositorySearchViewModel made raw HTTP Ktor calls directly
After: Clean separation between presentation and network client layers

Co-Authored-By: Claude <noreply@anthropic.com>
```

### 2. Eliminating Mutable Globals
```text
refactor: remove mutable static state to follow Principle of Least Surprise

- Delete TopActivity.lastSearchDate global static variable
- Remove hidden side-effects from ViewModel search flow

Before: TopActivity held mutable static state accessed across components
After: Fully stateless Activity; predictable ViewModel execution

Co-Authored-By: Claude <noreply@anthropic.com>
```

### 3. Resource Management
```text
refactor: implement proper resource management with Closeable interfaces

- Implement java.io.Closeable on GitHubApiClient and GitHubRepository
- Cascade resource cleanup in ViewModel onCleared()

Before: Ktor HttpClient remained open indefinitely causing connection leaks
After: Client closed deterministically when ViewModel is destroyed

Co-Authored-By: Claude <noreply@anthropic.com>
```

---

## Authoring Rules
1. **Imperative Mood**: Use "extract", "implement", "resolve", "remove" (never "extracted" or "removes").
2. **First Line Limit**: Under 72 characters.
3. **Traceability**: Reference specific classes (`GitHubApiClient`, `RepositoryMapper`, `RepositorySearchViewModel`).

## 📂 Related Relative Paths
- **Contributing Guide**: `docs/CONTRIBUTING.md`
- **Developer Workflow**: `docs/01_company_and_team/08_developer_workflow.md`
- **Engineering Guardrails**: `docs/01_company_and_team/06_engineering_guardrails.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`

