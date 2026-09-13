# Claude Code Skills

Project-specific skills for Android documentation and development, structured according to the official Claude Code skills standard.

## Directory Structure

```text
.claude/
├── README.md
├── settings.local.json
└── skills/
    ├── create-adr/
    │   └── SKILL.md
    ├── create-documentation/
    │   └── SKILL.md
    ├── create-pr/
    │   └── SKILL.md
    ├── review-code/
    │   └── SKILL.md
    └── write-commit/
        └── SKILL.md
```

## Available Skills

| Skill | Description | Location |
| :--- | :--- | :--- |
| **`create-adr`** | Guidance and template for Architecture Decision Records. | `skills/create-adr/SKILL.md` |
| **`create-documentation`** | General documentation principles and standard formats. | `skills/create-documentation/SKILL.md` |
| **`create-pr`** | Standard Japanese PR template and guidelines. | `skills/create-pr/SKILL.md` |
| **`review-code`** | Code review checklist (Clean Architecture, memory leaks, Kotlin idioms). | `skills/review-code/SKILL.md` |
| **`write-commit`** | Conventional Commit message generator with stats and attribution. | `skills/write-commit/SKILL.md` |

## Usage
Claude Code automatically discovers skills located in `.claude/skills/<name>/SKILL.md` based on the frontmatter `name` and `description` triggers.

## Living Documentation Rule
Whenever new code designs, architecture patterns, or class responsibilities are added to the codebase, developers must update these skill files. For complete instructions and multi-agent pairing standards, see [`docs/01_company_and_team/10_ai_agent_skills_guide.md`](../docs/01_company_and_team/10_ai_agent_skills_guide.md).

