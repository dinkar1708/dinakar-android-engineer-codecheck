# Team Onboarding Guide

Welcome to the Mobile Engineering team! This guide will help you get started and make your first contributions.

## Day 1: Environment Setup

### Install Required Tools

1. **Android Studio** (Hedgehog or newer)
   - Download from: https://developer.android.com/studio
   - Choose "Android Studio with bundled SDK"

2. **Java Development Kit (JDK)**
   - The project works with Java 17 or Java 21
   - Android Studio includes JDK 17 by default

3. **Git**
   - Install from: https://git-scm.com/
   - Configure your name and email:
     ```bash
     git config --global user.name "Your Name"
     git config --global user.email "your.email@company.com"
     ```

### Clone and Build the Project

1. Clone your assigned repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```

2. Open in Android Studio:
   - File → Open → Select the project folder
   - Wait for Gradle sync to complete (may take 5-10 minutes first time)

3. Run the build to verify everything works:
   ```bash
   ./gradlew assembleDevDebug
   ```

   Expected output: BUILD SUCCESSFUL

4. Run tests:
   ```bash
   ./gradlew testDevDebugUnitTest
   ```

   Expected output: All tests passing

## Week 1: Understanding the Codebase

### Read the Documentation (in this order)

1. **Engineering Operating Model** ([`readme.md`](./readme.md)) - Mobile platform governance and team structure
2. **Engineering Leadership Strategy** ([`01_engineering_leadership.md`](./01_engineering_leadership.md)) - Decision framework and delivery philosophy
3. **Engineering Guardrails & Security** ([`06_engineering_guardrails.md`](./06_engineering_guardrails.md)) - Architectural boundaries, secrets, and AI coding policy
4. **CI/CD & Delivery Standards** ([`07_cicd_and_delivery_standards.md`](./07_cicd_and_delivery_standards.md)) - Promotion pipelines, testing gates, and release rules
5. **Project Architecture Blueprints** - Consult the assigned project's specific architecture specifications and ADRs

### Explore the Code Structure *(Example: GitHub Search Client Application)*

Across our mobile projects, code is structured following modular Clean Architecture. Below is an illustrative reference structure (using our GitHub Repository Search application as a concrete example):

```
app/src/main/kotlin/jp/co/yumemi/android/code_check/  # Example Reference Module
├── ui/                      # Declarative Presentation Layer (Compose / SwiftUI)
│   ├── features/            # Feature screens & ViewModels
│   │   ├── search/          # Search feature (query, debouncing, results list)
│   │   ├── detail/          # Detail feature (repository statistics, web link)
│   │   └── settings/        # Settings feature (language, theme toggles)
│   ├── theme/               # Design System tokens, typography, and color schemes
│   └── navigation/          # Jetpack Navigation graph and routing
├── di/                      # Dependency Injection modules (Hilt)
└── TopActivity.kt           # Single-Activity entry point
```

### Run the App *(Example Validation Workflow)*

1. Start an Android Emulator or connect a physical device
2. In Android Studio: Run → Run 'app'
3. Verify basic application flow (for example, in the GitHub Search app, test typing a query such as `"kotlin"` to observe network loading, success, and error state transitions)

### Watch for These Patterns *(Found in All Projects)*

As you explore any mobile project across our organization, notice our standard architectural patterns:

- **MVVM / MVI Architecture**: Each screen has a ViewModel managing UI state and intent
- **StateFlow for UI State**: ViewModels expose immutable `StateFlow` that declarative UI observes
- **Hilt / DI for Dependency Injection**: `@HiltViewModel` and `@Inject` constructor injection (e.g. `di/RepositoryModule.kt`)
- **Declarative UI**: 100% Jetpack Compose (Android) and SwiftUI (iOS), with zero legacy XML view inflation
- **Repository Pattern**: Clean interface separation between domain use cases and data sources (e.g. `GitHubRepository` contract)

### Cross-Functional Communication & Alignment

Mobile engineers operate in close alignment with backend, platform, and QA teams:
1. **Join Team Communication Channels:**
   - `#mobile-engineering` — General mobile announcements, architecture discussions, and brown bag sessions.
   - `#mobile-backend-alignment` — Direct communication with the **Dedicated Backend Platform Squad** for API contracts, schema changes, endpoint SLAs, and mock servers.
   - `#qa-releases` — Build distribution notices and Firebase QA verification status.
2. **Attend Recurring Alignment Sync-ups:**
   - **Mobile ↔ Backend Alignment Sync:** Bi-weekly session to align on upcoming API schemas, breaking change notices, and backend staging deployment schedules.
   - **Sprint Planning & Retrospectives:** Agile ceremonies following Yumemi collaborative review and retrospective ethics.

## Week 2: Your First Contribution

### Start Small: Fix a Typo or Improve Documentation

1. Find a typo or confusing sentence in any .md file
2. Create a branch:
   ```bash
   git checkout -b docs/improve-readme-clarity
   ```

3. Make your change
4. Test that markdown renders correctly
5. Commit with a clear message:
   ```bash
   git add docs/readme.md
   git commit -m "docs: clarify environment setup instructions"
   ```

6. Push and create a Pull Request:
   ```bash
   git push origin docs/improve-readme-clarity
   ```

7. Fill out the PR template
8. Request review from your mentor

### Next: Small Code Change

Pick one of these beginner-friendly tasks on your assigned project (illustrated using our GitHub Search app as an example):

**Option A: Add or update a string resource**
- Add a new message to `res/values/strings.xml`
- Add localized translation in `res/values-ja/strings.xml` (Japanese)
- Connect it in a Composable screen

**Option B: Improve error message or validation**
- Find an error message or validation banner in a feature ViewModel (e.g., `SearchViewModel` or `DetailScreen`)
- Make it more user-friendly and defensive
- Write a unit test to verify it displays correctly

**Option C: Add a new test**
- Pick any ViewModel, Repository, or UseCase (e.g., `SearchViewModelTest`)
- Add a test case for an edge scenario that isn't covered (e.g., network timeout, empty list, rate limiting)
- Make sure it passes locally and in CI

### Checklist for Your First Code PR

- [ ] I read and understood the code I'm changing
- [ ] I tested my change manually on the emulator
- [ ] I ran `./gradlew testDebugUnitTest` and all tests pass
- [ ] I ran `./gradlew lintDebug` with no new warnings
- [ ] I filled out the PR template completely
- [ ] I tagged my mentor for review

## Month 1: Building Confidence

### Learn by Doing

**Week 3: Feature Addition**
- Implement a small feature (e.g., add an action button or formatting helper)
- Follow standard patterns (ViewModel + StateFlow + Compose)
- Write unit tests for your feature
- Present your feature in team meeting

**Week 4: Bug Fix**
- Pick a bug from the backlog (marked "good first issue")
- Reproduce the bug
- Fix it and add a test that would have caught it
- Document the fix in your PR description

### Growing Your Skills
 
**Study One Pattern Per Week (Using the GitHub Search App as Reference Example):**

- **Week 1**: How does Dependency Injection work?
  - Read the project's DI modules (e.g. `di/RepositoryModule.kt`)
  - Trace how repository interfaces (`GitHubRepository`) are bound to implementations and injected into ViewModels (`SearchViewModel`)
  - Draw a diagram of the dependency graph

- **Week 2**: How does StateFlow manage UI state?
  - Read a feature ViewModel (e.g., `SearchViewModel.kt`)
  - Understand Loading → Success → Empty → Error state transitions
  - Practice adding a new UI state or action end-to-end

- **Week 3**: How does the Repository pattern work?
  - Read repository interfaces (`GitHubRepository`) and their data-layer implementations
  - Understand why interfaces decouple business logic from networking libraries (e.g. Ktor, Retrofit)
  - Practice explaining this to another junior engineer

- **Week 4**: How does Declarative UI work?
  - Read a feature screen Composable line by line (e.g., `SearchScreen.kt`)
  - Understand `@Composable` functions, state hoisting, and preview parameters
  - Create your own reusable UI component

### Get Involved in Code Reviews

- Review PRs from other engineers (even if you're learning)
- Ask questions: "Why did you choose this approach?"
- Look for things you understand: "Does this test cover all cases?"
- Build confidence by reviewing simpler PRs first

## Month 2-3: Becoming Productive

### Take Ownership of Features

- Lead development of a medium-sized feature
- Write the technical design before coding
- Break work into small PRs (3-5 per feature)
- Mentor another new joiner

### Expand Your Knowledge

- Read all Architectural Decision Records (ADRs) in the project's architecture documentation
- Understand our cross-platform and headless multiplatform strategies
- Learn about native client integrations (e.g. `iosApp/`)
- Present a technical topic in a team brown bag session

### Contribute to Process Improvement

- Identify something that slows you down
- Propose an improvement (new doc, script, or tool)
- Implement it and share with the team

## Common Gotchas and Solutions

### Problem: "Gradle sync failed"

**Solution:**
1. Check you're using Java 17 or Java 21
2. In Android Studio: File → Settings → Build, Execution, Deployment → Build Tools → Gradle
3. Set "Gradle JDK" to "Embedded JDK 17" or "jbr-21"
4. Click "Sync Project with Gradle Files"

### Problem: "Tests are failing on CI but passing locally"

**Solution:**
- Clean build: `./gradlew clean`
- Run tests exactly as CI does: `./gradlew testDevDebugUnitTest`
- Check if you have uncommitted files affecting tests

### Problem: "My PR is too large and reviewers are overwhelmed"

**Solution:**
- Break it into smaller PRs
- First PR: Data layer changes only
- Second PR: ViewModel changes using new data
- Third PR: UI changes using new ViewModel
- Reference the feature in each PR description

### Problem: "I don't understand why my code was rejected"

**Solution:**
- Don't take it personally; code review is about learning
- Ask the reviewer: "Can you explain why this approach is better?"
- Pair with a senior to understand the feedback
- Update your code and learn the pattern for next time

## Getting Help

### When You're Stuck

1. **Try for 30 minutes first**
   - Read the error message carefully
   - Search the codebase for similar patterns
   - Check team documentation and technical reference guides

2. **Ask for Help**
   - Reach out in the team communication channel with:
     - What you're trying to do
     - What you've tried
     - The error message or unexpected behavior
   - Tag your mentor if it's urgent

3. **Pair Programming**
   - Schedule a 30-minute session with a senior engineer
   - Share your screen and work through the problem together
   - Take notes on what you learned

### Resources

- **Team Chat**: #mobile-engineering (Slack / Teams)
- **Code Review Channel**: #mobile-code-reviews
- **Weekly 1-on-1**: With your manager every week
- **Team Meeting**: Wednesdays 2pm (present your work, ask questions)
- **Office Hours**: Fridays 3-4pm (any engineer available for questions)

## Success Metrics

### After 1 Month

- [ ] At least 3 PRs merged
- [ ] Can explain MVVM architecture
- [ ] Can run tests and fix failing tests
- [ ] Comfortable using Git and GitHub
- [ ] Know who to ask for help

### After 3 Months

- [ ] Led development of at least one feature
- [ ] Contributed to architectural discussions
- [ ] Mentored a newer team member
- [ ] Presented a technical topic to the team
- [ ] Independently resolved bugs and added features

### After 6 Months

- [ ] Designed and implemented a complex feature
- [ ] Contributed to technical documentation (wrote an ADR)
- [ ] Conducting code reviews regularly
- [ ] Identified and fixed technical debt
- [ ] Recognized as a productive team member

## Welcome to the Team!

Remember: Everyone was new once. Ask questions, make mistakes, and learn. We're here to help you grow.

Your mentor: [Name]
Team lead: [Name]
Engineering manager: [Name]

First day checklist:
- [ ] Environment setup complete
- [ ] Project builds successfully
- [ ] Tests run and pass
- [ ] Introduced yourself in team chat channel
- [ ] Read this entire guide
- [ ] Scheduled your first 1-on-1 with mentor

Let's build great software together!
