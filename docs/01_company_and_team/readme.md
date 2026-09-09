# 🏢 Mobile Engineering Operating Model & Team Governance

> [!NOTE]
> **Enterprise Confluence & Notion Ready:**
> This directory constitutes a turnkey, repository-agnostic **Mobile Engineering Operating Model**. In corporate operations, these standards are published directly to the company's internal wiki (Confluence / Notion) to govern all mobile projects (Android, iOS, KMP, Flutter) and scale engineering teams uniformly.

---

## 🎯 Purpose & Scope

The standards defined in this handbook establish a high-performance engineering culture focused on:
1. **Developer Velocity & Happiness:** Frictionless onboarding, rapid feedback loops, and contract-first workflows.
2. **Predictable Delivery:** Strict 24h review SLAs, unambiguous Definition of Done, and DORA metrics tracking.
3. **Enterprise Security & Quality:** Zero secrets in code, automated CI gates, and strict branch protection policies.

---

## 📚 Handbook Table of Contents

| Standard | Document | Target Scope & Core Policy | Confluence Space |
|:---|:---|:---|:---:|
| **1. EM Strategy & Metrics** | [`01_engineering_leadership.md`](./01_engineering_leadership.md) | DORA metrics (Lead Time, MTTR, CFR), velocity tracking, hiring criteria & ROI | `ENG-MGMT` |
| **2. People & Mentorship** | [`02_mentorship_and_career_growth.md`](./02_mentorship_and_career_growth.md) | 1-on-1 cadences, IC vs EM career ladders, SBI feedback model & coaching | `PEOPLE-OPS` |
| **3. Team Onboarding** | [`03_team_onboarding.md`](./03_team_onboarding.md) | 30-60-90 Day Ramp-Up Plan (First commit on Day 2, feature ownership by Day 30) | `ONBOARDING` |
| **4. Code Review Charter** | [`04_code_review_guidelines.md`](./04_code_review_guidelines.md) | 24-hour review SLA, 4-hour hotfix SLA, Conventional Comments (`nit:`, `suggestion:`) | `CODE-QUALITY` |
| **5. Definition of Done** | [`05_definition_of_done.md`](./05_definition_of_done.md) | Enterprise DoD quality gates: Unit test coverage, zero Detekt warnings, APK verification | `STANDARDS` |
| **6. Security & Guardrails** | [`06_engineering_guardrails.md`](./06_engineering_guardrails.md) | Secret scanning, branch protection, auto-delete merged branches & AI governance | `SECURITY` |
| **7. CI/CD & Delivery** | [`07_cicd_and_delivery_standards.md`](./07_cicd_and_delivery_standards.md) | 3-tier promotion (`dev` -> `stg` -> `main`), Firebase QA distribution & keystores | `DEVOPS` |
| **8. Developer Workflow** | [`08_developer_workflow.md`](./08_developer_workflow.md) | Contract-first development (Domain -> Data -> UI) enabling parallel engineering | `DEV-GUIDES` |
| **9. Engineering Culture** | [`09_yumemi_review_culture.md`](./09_yumemi_review_culture.md) | Psychological safety, blameless retrospectives & collaborative code review ethics | `CULTURE` |

---

## 🔄 Relationship to Project Repositories

Individual project repositories across the mobile organization **implement and adhere to** these company-level standards:
- Each project's technical architecture satisfies the company Definition of Done ([`05_definition_of_done.md`](./05_definition_of_done.md)).
- Project CI/CD workflows implement the automated delivery pipelines defined in [`07_cicd_and_delivery_standards.md`](./07_cicd_and_delivery_standards.md).
- Squad agile milestones and team velocity are tracked according to [`01_engineering_leadership.md`](./01_engineering_leadership.md).
