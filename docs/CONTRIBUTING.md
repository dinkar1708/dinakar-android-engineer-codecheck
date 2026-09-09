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

## 🔄 Development Workflow

### 1. Create a Branch

```bash
# Create and checkout new branch
git checkout -b feature/your-feature-name
```

### 2. Make Changes

```bash
# Make your changes
# Add and commit
git add .
git commit -m "feat: add your feature"
```

### 3. Push to Remote

```bash
# Push branch to remote
git push -u origin feature/your-feature-name
```

### 4. Create Pull Request

```bash
# Using GitHub CLI
gh pr create --base main --title "feat: add your feature" --body "Description of changes"

# Or create PR via GitHub web interface
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
4. **Yumemi Evaluation**: Demonstrates professional Git discipline ✅

---

**Questions?** See [Getting Started](./GETTING_STARTED.md) or [Architecture Overview](./ARCHITECTURE_OVERVIEW.md)
