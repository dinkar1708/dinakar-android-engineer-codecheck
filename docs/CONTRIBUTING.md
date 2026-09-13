# Contributing Guidelines

**Project**: Android Engineer Code Check Solution
**Author**: Dinakar Prasad Maurya

---

## 🌿 Branch Naming Strategy

This project follows **industry-standard semantic branch prefixes** for consistent Git workflow.

### Format

```
<type>/<descriptive-name>
```

### Branch Type Prefixes

| Prefix | Purpose | Example |
|:-------|:--------|:--------|
| `chore/` | Build system, dependencies, tooling, config changes | `chore/gradle-8.5-upgrade` |
| `docs/` | Documentation-only changes (no code) | `docs/complete-documentation` |
| `feature/` | New features or major functionality additions | `feature/multi-module-architecture` |
| `fix/` | Bug fixes | `fix/memory-leaks-viewbinding` |
| `refactor/` | Code improvements without changing behavior | `refactor/naming-conventions` |
| `test/` | Adding or improving tests | `test/comprehensive-testing` |
| `release/` | Release preparation and versioning | `release/v1.0.0` |

### Naming Rules

- ✅ Use lowercase only
- ✅ Use hyphens to separate words (not underscores or spaces)
- ✅ Keep under 50 characters
- ✅ Be descriptive and specific
- ✅ Include issue number when relevant: `fix/issue-3-memory-leaks`

---

## 📝 Commit Message Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/) format:

```
<type>: <description>

[optional body]

[optional footer]
```

**Types:** `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `perf`, `ci`, `build`, `revert`

**Examples:**
```bash
chore: upgrade to Gradle 8.5

docs: add architecture overview

feat: implement multi-module architecture

fix: resolve memory leak in ViewBinding cleanup

refactor: extract ViewModel logic from Fragment

test: add unit tests for Repository layer
```

---

### 🔀 Branching Strategy: Multi-Environment Branching Model

This project follows an **Enterprise Multi-Environment Git Workflow** structured around three persistent branch tiers: `dev`, `stg`, and `main`.

### Branch Hierarchy

| Branch | Environment | Purpose | Target for PRs |
|:---|:---|:---|:---|
| **`main`** | **Production** | Production-ready code and release milestone tags (`v1.0.0`). Only accepts PRs from `stg`. | PRs from `stg` |
| **`stg`** | **Staging / QA** | Pre-release validation, staging builds, and E2E regression testing. | PRs from `dev` |
| **`dev`** | **Development** | Main integration branch for active development. | PRs from topic branches (`feature/*`, `fix/*`, etc.) |
| **Topic Branches** | **Working** | Day-to-day work branches (`feature/*`, `fix/*`, `refactor/*`, `chore/*`, `docs/*`). | Branch from `dev`, merge into `dev` |

### Promotion Pipeline Diagram

```
Topic Branches
(feature/*, fix/*, refactor/*, chore/*, docs/*)
      │
      └─── (PR) ───► dev (Development Integration)
                      │
                      └─── (Release PR) ───► stg (Staging / Pre-Release Testing)
                                              │
                                              └─── (Production PR) ───► main (Production Release & Tags)
```

---

## 🔄 Development Workflow (Step-by-Step Lifecycle)

All code changes follow this 3-phase promotion lifecycle:

### Phase 1: Feature & Bugfix Development (Topic Branch → `dev`)

All day-to-day work (features, bug fixes, refactorings, tests) branches from and merges into `dev`:

1. **Update local `dev` branch**:
   ```bash
   git checkout dev
   git pull origin dev
   ```

2. **Create topic branch from `dev`**:
   ```bash
   git checkout -b <type>/<descriptive-name>
   # Examples:
   #   git checkout -b refactor/naming-conventions
   #   git checkout -b fix/memory-leaks
   #   git checkout -b feature/compose-ui
   ```

3. **Implement & verify locally**:
   ```bash
   # Verify build
   ./gradlew clean assembleDebug

   # Run tests
   ./gradlew test
   ```

4. **Commit with Conventional Commits**:
   ```bash
   git add .
   git commit -m "<type>: <concise description>"
   ```

5. **Push and create PR targeting `dev`**:
   ```bash
   git push -u origin <type>/<descriptive-name>

   # Create PR targeting dev
   gh pr create --base dev --title "<type>: <concise description>"
   ```

6. **Complete review checklist and merge into `dev`**.

---

### Phase 2: Staging Promotion (`dev` → `stg`)

When a milestone or sprint of features in `dev` is ready for pre-release validation:

1. Ensure all feature PRs are merged into `dev`.
2. Open a Pull Request from `dev` targeting `stg`:
   ```bash
   gh pr create --base stg --head dev --title "chore: promote dev to staging for QA validation"
   ```
3. Run comprehensive staging builds and integration tests on `stg`.
4. Merge `dev` into `stg`.

---

### Phase 3: Production Release (`stg` → `main`)

When staging verification passes and the release criteria are met:

1. Open a Pull Request from `stg` targeting `main`:
   ```bash
   gh pr create --base main --head stg --title "release: v1.0.0 production release"
   ```
2. Verify all CI quality gates pass.
3. Merge `stg` into `main`.
4. Tag the production release milestone:
   ```bash
   git checkout main
   git pull origin main
   git tag -a v1.0.0 -m "Release v1.0.0"
   git push origin v1.0.0
   ```

---

## ✅ Pull Request Guidelines

### PR Title Format

Follow the same convention as commit messages:

```
<type>: <description>
```

**Examples:**
- `chore: upgrade to Gradle 8.5`
- `feat: implement Clean Architecture with Hilt`
- `fix: resolve forks_count typo`
- `docs: add complete documentation`

### PR Description Should Include:

- **What**: Summary of changes
- **Why**: Reason for changes
- **How**: Implementation approach (if complex)
- **Testing**: How changes were verified
- **Closes**: Link to issues (e.g., "Closes #1, #2")

---

## 🏗️ Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use `.editorconfig` for consistent formatting
- Run `./gradlew detekt` before committing (when available)
- No wildcard imports
- Meaningful variable/function names (no Hungarian notation)

---

## 🧪 Testing

- Write unit tests for new features
- Ensure existing tests pass: `./gradlew test`
- Add UI tests for critical user flows (when applicable)

---

## 🚀 Release Process

Releases use the `release/` branch prefix:

```bash
# Create release branch
git checkout -b release/v1.0.0

# Update version numbers, changelog
# Create tag
git tag -a v1.0.0 -m "Release v1.0.0"

# Push tag
git push origin v1.0.0
```

---

**Why These Conventions?**

1. **Industry Standard**: Used by Google, major open-source projects
2. **Clear Intent**: Semantic prefixes show purpose at a glance
3. **Automation Ready**: Works well with CI/CD tools
4. **Evaluation Benchmark**: Demonstrates professional Git discipline ✅

---

**Questions?** See [Getting Started](./GETTING_STARTED.md) or [Architecture Overview](./ARCHITECTURE_OVERVIEW.md)
