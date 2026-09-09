# ADR-003: Detekt Static Analysis and Code Quality Automation

**Status:** 📋 Proposed  
**Deciders:** Dinakar Prasad Maurya  
**Technical Story:** Issue #1, Issue #2, Quality Engineering & CI/CD  

---

## Context

In Android codebases, maintaining consistent code quality, idiomatic Kotlin standards, and preventing code smells (such as God classes, complex cyclomatic paths, unhandled exceptions, and magic numbers) across a multi-module project can degrade rapidly without automated tooling. 

Manual code review alone:
1. Wastes reviewer time on cosmetic lint issues rather than architectural decisions.
2. Leads to bikeshedding and subjective style disagreements.
3. Fails to prevent regressions in complexity metrics and safety rules.

---

## Decision

We adopt **Detekt** as the static code analysis engine for Kotlin across all modules in the project, automated via our custom Gradle convention plugin (`DetektConventionPlugin.kt`).

Key elements of this decision:
1. **Centralized Configuration:** All rules are governed by a single configuration file at `config/detekt/detekt.yml`.
2. **Custom Convention Plugin:** Every Android library, feature, and application module automatically inherits the Detekt task through `codecheck.detekt`.
3. **CI Quality Gate:** GitHub Actions CI executes `./gradlew detekt` on every pull request, failing the build on critical threshold violations.
4. **Baseline Support:** Existing legacy code can be baselined so technical debt is contained without blocking active feature PRs.

---

## Consequences

### Positive ✅
- **Automated Consistency:** Formats, naming conventions, and complexity limits are mechanically enforced.
- **Faster Code Reviews:** Reviewers focus on architectural intent and business logic while Detekt enforces idiomatic Kotlin syntax.
- **Multi-Module Scalability:** Adding a new module automatically equips it with the organization's quality rules without duplicate configuration.
- **Fail Fast:** Local and CI validation stops issues before human reviewers review the code.

### Negative ⚠️
- **Build Overhead:** Detekt task adds a small amount of time (~5-10 seconds) to full CI pipeline runs. Mitigated by Gradle build cache.
- **Rule Adjustments:** Certain Compose-specific patterns (e.g. `@Composable` functions uppercase naming) require explicit Detekt rule configuration (`naming.FunctionNaming.ignoreAnnotated: ['Composable']`).

---

## Alternatives Considered

1. **Android Lint Alone:**
   - *Rejected:* Android Lint is excellent for Android-specific resource and API checks, but lacks Kotlin-first AST analysis and configurable cyclomatic complexity thresholds. Detekt complements Android Lint.
2. **ktlint Only:**
   - *Rejected:* ktlint strictly focuses on formatting and styling, but does not provide deep static analysis (dead code detection, complex methods, potential bugs).
3. **SonarQube / SonarCloud:**
   - *Rejected:* Requires external server infrastructure and subscription overhead; unnecessary for this repository scale when Detekt runs zero-cost in Gradle.

---

## References
- [Detekt Official Documentation](https://detekt.dev/)
- [Google Now in Android Static Analysis](https://github.com/android/nowinandroid)
- [ADR-002: Gradle Convention Plugins](./002_gradle_convention_plugins.md)
