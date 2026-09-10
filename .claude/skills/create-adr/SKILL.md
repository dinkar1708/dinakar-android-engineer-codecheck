---
name: create-adr
description: Use this skill when creating or updating Architecture Decision Records (ADRs) in docs/02_project_architecture/adr/.
---

# Create ADR (Architecture Decision Record)

When creating an ADR, follow this structure:

## File Naming
- Sequential number: 007, 008, 009
- Format: `NNN_brief_title.md`
- Example: `007_api_versioning_strategy.md`

## Structure Template

```markdown
# ADR-NNN: Title

**Status:** 📋 Proposed | ✅ Implemented | 🔄 Superseded by ADR-XXX
**Deciders:** Name
**Technical Story:** Brief context

---

## Context
What problem? Why does it matter?

## Decision
What solution chosen?

## Consequences

### Positive ✅
- Benefit 1
- Benefit 2

### Negative ⚠️
- Drawback 1

## Alternatives Considered
1. **Alternative 1:** Why rejected
2. **Alternative 2:** Why rejected

## References
- [Link](url)
```

## After Creating ADR
1. Update `docs/02_project_architecture/adr/readme.md`
2. Add entry to ADR index table
3. Link from related docs
4. Check status field matches reality

## 📂 Related Relative Paths
- **ADR Directory**: `docs/02_project_architecture/adr/`
- **ADR Index Table**: `docs/02_project_architecture/adr/readme.md`
- **Architecture Overview**: `docs/02_project_architecture/architecture/01_clean_architecture_and_udf.md`
- **AI Agent Skills Guide**: `docs/01_company_and_team/10_ai_agent_skills_guide.md`
- **PR Template**: `.github/pull_request_template.md`

