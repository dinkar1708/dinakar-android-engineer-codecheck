# Architecture Decision Records (ADR)

## What are ADRs?

Architecture Decision Records (ADRs) document important architectural decisions made during the project lifecycle. Each ADR captures:
- **Context:** The problem or requirement
- **Decision:** The solution chosen
- **Consequences:** Trade-offs and implications
- **Alternatives Considered:** Other options and why they were rejected

## Why Use ADRs?

1. **Knowledge Transfer:** New team members understand "why" not just "what"
2. **Decision History:** Track evolution of architecture over time
3. **Prevent Rework:** Avoid revisiting settled decisions
4. **Accountability:** Clear ownership of technical choices

## ADR Format

We use the format popularized by Michael Nygard:

```markdown
# ADR-###: Title

**Status:** Proposed | Under Review | Superseded by ADR-###
**Deciders:** Name(s)
**Technical Story:** Link to Issue/PR

## Context

What is the issue or problem we're trying to solve?

## Decision

What solution did we choose?

## Consequences

### Positive
- Benefit 1
- Benefit 2

### Negative
- Drawback 1
- Drawback 2

### Neutral
- Change 1
- Change 2

## Alternatives Considered

### Alternative 1
Why it was rejected.

### Alternative 2
Why it was rejected.

## References
- Link 1
- Link 2
```

---

## Index of ADRs

| ADR | Title | Status | Architectural Scope |
|:----|:------|:-------|:--------------------|
| [001](./001_multi_module_architecture.md) | Multi-Module Architecture | Proposed | Project Structure & Modularity |
| [002](./002_gradle_convention_plugins.md) | Gradle Convention Plugins | Proposed | Build Infrastructure (`build-logic`) |
| [003](./003_detekt_static_analysis.md) | Detekt Static Analysis | Proposed | Quality Gates & Linting |
| [004](./004_headless_kmp_boundary.md) | Headless KMP Boundary | Proposed | Cross-Platform Domain Logic |
| [005](./005_product_flavors_strategy.md) | Product Flavors Strategy | Implemented | Multi-Environment Isolation |
| [006](./006_jetpack_compose_migration.md) | Declarative Jetpack Compose Migration | Proposed | Presentation & UI Modernization |
| [007](./007_api_versioning_strategy.md) | API Versioning & Contract Management | Proposed | API Evolution & Backward Compatibility |

---

## Decision Process

### When to Create an ADR
Create an ADR when making decisions that:
- Affect overall architecture
- Impact multiple modules
- Have long-term consequences
- Require significant effort to reverse
- Involve trade-offs between competing concerns

### ADR Workflow
1. **Propose:** Create ADR with "Proposed" status for architectural review
2. **Discuss:** Share with team and client evaluators, gather feedback
3. **Decide:** Refine architecture based on technical evaluation
4. **Implement:** Deliver via atomic pull requests and verification

### Updating ADRs
- **Never delete or modify** baseline ADRs
- If decision changes, create new ADR that supersedes the old one
- Mark old ADR as "Superseded by ADR-XXX"

---

## References
- [Documenting Architecture Decisions](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions) by Michael Nygard
- [ADR GitHub Organization](https://adr.github.io/)
- [Architecture Decision Records in Action](https://www.youtube.com/watch?v=41NVge3_cYo)
