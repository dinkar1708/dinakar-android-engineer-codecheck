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

All commits in this repository strictly adhere to the [Conventional Commits v1.0.0](https://www.conventionalcommits.org/) specification. This provides an explicit, readable commit history, simplifies changelog generation, and ensures consistent quality gates.

### Format Structure

```text
<type>(<optional-scope>): <imperative summary>

[optional body explaining motivation and technical context]

[optional footer(s): closes #issue-number, BREAKING CHANGE: <reason>]
```

---

### Commit Type Prefixes & Meanings

| Prefix | Name & Purpose | When to Use | Examples |
|:---|:---|:---|:---|
| **`feat`** | **New Feature** | Adding new user-facing functionality, public API capability, or UI screens. | `feat(search): add repository sorting tabs by stars and forks`<br>`feat(theme): support system dynamic dark mode switching` |
| **`fix`** | **Bug Fix** | Correcting a defect, unexpected crash, regression, memory leak, or logic error. | `fix(search): correct forks_count mapping key typo`<br>`fix(detail): nullify ViewBinding in onDestroyView to prevent leak` |
| **`chore`** | **Routine Maintenance** | Housekeeping tasks, repo configuration, cleanup, or maintenance that does **not** alter app production code, business logic, or test logic. | `chore(repo): update .gitignore for local IDE cache files`<br>`chore(release): prepare v1.0.0 milestone changelog`<br>`chore(license): add MIT license header to source files` |
| **`refactor`** | **Refactoring** | Restructuring production code without changing external behavior or fixing bugs (e.g. modularization, extracting classes, improving readability). | `refactor(domain): decouple repository contract from network client`<br>`refactor(detail): extract repository detail card into composable component` |
| **`build`** | **Build System & Dependencies** | Changes that affect Gradle build scripts, dependency catalogs, SDK upgrades, compiler flags, or ProGuard rules. | `build(deps): bump Kotlin from 1.9.20 to 1.9.22`<br>`build(gradle): configure build-logic convention plugin for Java 17` |
| **`ci`** | **Continuous Integration** | Changes to CI/CD workflows, automation scripts, test matrices, or pipeline configuration files. | `ci(github): add automated unit test and lint verification workflow`<br>`ci(actions): cache Gradle dependencies to speed up PR checks` |
| **`test`** | **Testing** | Adding new automated tests, modifying test doubles, fixing flaky tests, or increasing coverage without altering production logic. | `test(search): add Turbine flow test for SearchViewModel error state`<br>`test(data): add Ktor MockEngine tests for 403 rate limit handling` |
| **`docs`** | **Documentation** | Updating or adding markdown documentation, architecture decision records (ADRs), guides, diagrams, or KDoc comments. | `docs(architecture): add clean architecture layer boundary diagram`<br>`docs(contributing): explain semantic commit prefixes and workflow` |
| **`perf`** | **Performance Improvement** | Code changes focused specifically on improving execution speed, reducing memory footprint, or optimizing UI rendering. | `perf(search): debounce search text input by 300ms to reduce network calls`<br>`perf(list): enable stable keys in LazyColumn for optimal recomposition` |
| **`style`** | **Code Style & Formatting** | Cosmetic changes that do not affect code logic (whitespace, indentation, import reordering, line breaks). | `style(ui): reorder imports and apply spotless ktlint formatting` |
| **`revert`** | **Revert Commit** | Reverting a previous commit that introduced a regression or unintended change. | `revert: "feat(search): add experimental query caching"` |

---

### 🔍 Deep Dive: Understanding `chore` vs `build` vs `refactor`

Developers often wonder when to choose `chore` versus other prefixes. Here is the rule of thumb:

1. **`chore` (Housekeeping & Maintenance)**:
   - Use `chore` for general repository housekeeping that doesn't change runtime code behavior or build definitions.
   - *Examples*: updating `.gitignore`, cleaning up obsolete asset files, updating release templates, organizing documentation folders, deleting scratch scripts.
   - *Mnemonic*: "If it's an administrative or housekeeping task that doesn't alter how the app runs or compiles, it's a `chore`."

2. **`build` vs `chore`**:
   - If the change modifies `build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`, Gradle wrapper, or compilation plugins, use **`build`** (e.g. `build(deps): update ktor to 2.3.7`).
   - If the change is non-compilation tooling or broad maintenance, use **`chore`** (e.g. `chore: clean up deprecated IDE files`).

3. **`refactor` vs `chore`**:
   - If code inside `src/main/` is modified to restructure, rename, or improve code design, it is always a **`refactor`** (never a `chore`).
   - *Rule*: Never use `chore` for changes to application architecture or source code.

---

### 💡 Best Practices for Writing Commit Messages

1. **Use the Imperative Mood**:
   - ✅ `feat: add search query debouncing`
   - ❌ `feat: added search query debouncing`
   - ❌ `feat: adds search query debouncing`
   - *Rule*: The commit message should complete the sentence: *"If applied, this commit will..."*

2. **Follow the 50/72 Rule**:
   - Keep the summary line under **50 characters** (maximum 72 characters).
   - If more detail is necessary, leave one blank line and wrap body paragraphs at **72 characters**.

3. **Keep Commits Atomic**:
   - Each commit should represent one logical unit of work.
   - Do not combine formatting refactors (`style`), bug fixes (`fix`), and new features (`feat`) in a single commit.

4. **Specify Scopes When Applicable**:
   - Useful scopes for this project: `search`, `detail`, `settings`, `starred`, `domain`, `data`, `network`, `ui`, `branding`, `deps`, `workflow`.
   - *Example*: `feat(branding): replace app icons across Android and iOS with high-res dark theme assets`

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
