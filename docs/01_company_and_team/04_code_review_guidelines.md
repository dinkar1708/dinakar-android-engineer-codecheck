# Code Review Guidelines & Peer Feedback Standards

**Scope:** Development & Code Review Governance (Phase 2 Inception & Delivery)  
**Target:** Mobile Engineering Team & Technical Reviewers  

---

## 1. Philosophy & Objectives

Code reviews serve as a primary quality assurance gate and knowledge-sharing mechanism. They are designed to:
- **Ensure Architectural Integrity:** Prevent architectural drift and enforce consistent design patterns (MVVM, Clean Architecture, Unidirectional Data Flow).
- **Prevent Defect Leakage:** Detect edge-case bugs, race conditions, memory leaks, and regressions before code reaches staging or production.
- **Maintain High Maintainability:** Keep the codebase clean, documented, and approachable for all team members.
- **Promote Constructive Mentorship:** Share domain knowledge and best practices across experience levels.

---

## 2. Author Responsibilities: Preparing a Pull Request

### Pre-Flight Verification Checklist
Before requesting peer review, authors must complete the following checks:
1. **Self-Review:** Inspect your own PR diff on GitHub to eliminate temporary debug logs, stray comments, or accidental modifications.
2. **Automated Quality Gates:** Ensure local checks pass prior to opening PR:
   - `./gradlew testDevDebugUnitTest` (Unit tests pass with 80%+ coverage on new logic)
   - `./gradlew lintDebug` (Zero Android Lint errors)
   - `./gradlew detekt` (Zero static analysis violations)
3. **Green CI Pipeline:** Ensure all automated GitHub Actions checks complete successfully. Reviewers should never be asked to review broken builds.

### Sizing & Context
- **Atomic PR Size:** PRs should ideally span **100 to 300 lines of code (LOC)**. PRs exceeding 500 lines must be decomposed into sequential, logically independent PRs (e.g., Step 1: Domain/Interface contracts; Step 2: Data layer implementation; Step 3: UI layer).
- **Comprehensive PR Description:** Detail the *Why* (business context or issue reference), *What* (technical solution summary), and *How to Test* (reproduction or validation steps).
- **Visual Proof:** Attach before/after screenshots or screen recordings for all UI and styling changes.

---

## 3. Reviewer Responsibilities & Evaluation Criteria

Reviewers evaluate changes across five critical dimensions:

| Dimension | Key Evaluation Questions |
|:---|:---|
| **Architecture & Patterns** | Does the change follow MVVM + Repository patterns? Is business logic kept out of the UI layer? Are dependencies properly injected via Hilt? |
| **Correctness & Safety** | Are edge cases handled (null values, network failures, empty lists, rate limits)? Are coroutine scopes properly bounded (`viewModelScope`)? Are ViewBinding references cleared in `onDestroyView()`? |
| **Testing & Coverage** | Are meaningful unit tests included? Do tests assert state transitions and error conditions rather than trivial property access? |
| **Performance** | Are there main thread I/O operations, redundant allocations, or unnecessary Jetpack Compose recompositions? |
| **Clarity & Documentation** | Are public interfaces and non-obvious algorithms documented with KDoc? Are naming conventions expressive and consistent? |

*Note: Automated tools (KtLint, Detekt, Android Lint) enforce formatting and syntax. Human reviewers focus on architecture, logic, security, and edge cases.*

---

## 4. Standardized Feedback Taxonomy

To eliminate ambiguity and establish clear expectations, reviewers must prefix inline comments with standard categories:

| Tag | Severity | Action Required |
|:---|:---|:---|
| **`[MUST]`** | Blocker | Critical defect, crash risk, security flaw, or architectural violation. Must be resolved before merge. |
| **`[NITS]`** | Non-blocking | Minor polish, typo, or stylistic preference. Author may address or defer to a follow-up ticket. |
| **`[IMO]`** | Discussion | "In My Opinion." Subjective architectural or design suggestion open for discussion. Non-blocking. |
| **`[FYI]`** | Informational | Contextual information, reference link, or documentation pointer. No action required. |
| **`[MEMO]`** | Context | Author note explaining the rationale behind a non-obvious implementation detail. |

---

## 5. Review Turnaround & SLAs

- **First Review SLA:** Initial review completed within **24 hours** of PR submission.
- **Re-review SLA:** Follow-up verification on addressed feedback completed within **4 hours**.
- **Author Response SLA:** Feedback acknowledged or addressed within **24 hours**.

---

## 6. Conflict Resolution & Escalation

1. **Direct Collaborative Discussion:** When perspectives diverge, authors and reviewers discuss technical trade-offs directly in the PR thread with concrete examples.
2. **Escalation to Lead:** If consensus is not reached after two review cycles, the PR is escalated to the Tech Lead for final architectural determination.

---

## 7. Cross-References

- [Definition of Done](./05_definition_of_done.md)
- [Engineering Guardrails & Repository Policies](./06_engineering_guardrails.md)
- [Collaborative Code Review Culture](./09_collaborative_review_culture.md)
- [Code Commenting & Documentation Standards](./12_code_commenting_and_documentation_standards.md)
- [Developer Workflow & Ticket Guide](./08_developer_workflow.md)
