# ADR-007: API Versioning & Contract Management Strategy

**Status:** 📋 Proposed
**Deciders:** Dinakar Prasad Maurya
**Technical Story:** API Evolution, Backward Compatibility, Client-Server Contract Management

---

## Context

Mobile applications face unique challenges with API versioning due to update latency:
1. **Deployment Asymmetry:** Backend services can deploy instantly, but mobile clients remain on older versions until users update via app stores (approval delays, user reluctance).
2. **Breaking Changes Risk:** Unversioned API changes (field renames, type changes, endpoint removals) crash older clients still in production.
3. **Multi-Environment Complexity:** Different app flavors (`dev`, `stg`, `prod`) may target different backend API versions, requiring clear version alignment.
4. **Third-Party API Evolution:** External APIs like GitHub REST API evolve (v3 → v4), requiring explicit version negotiation.

---

## Decision

We adopt a multi-layered API versioning strategy:

### 1. External API Version Pinning

**GitHub REST API v3:**
- Explicit version header: `Accept: application/vnd.github.v3+json`
- Prevents implicit upgrades to v4 that could introduce breaking changes
- Documents required API version in `api_spec/01_search_repositories_api.md`

### 2. Internal Backend API Versioning (If Applicable)

For custom backend services (beyond GitHub API):
- **URL-based versioning:** `/api/v1/repositories`, `/api/v2/repositories`
- **Header-based versioning:** `API-Version: 2024-09-10`
- **Semantic versioning:** Major.Minor.Patch for breaking vs non-breaking changes

### 3. Client-Server Contract Enforcement

**Contract-First Development:**
- OpenAPI/Swagger 3.0 schemas define API contracts before implementation
- Protocol Buffers for strongly-typed gRPC services
- GraphQL schemas for flexible query-based APIs

**Version Alignment Matrix:**
```
Mobile Flavor → Backend Tier Mapping
├── dev flavor    → Development backend (latest unstable API)
├── stg flavor    → Staging backend (release candidate API)
└── prod flavor   → Production backend (stable versioned API)
```

### 4. Backward Compatibility Rules

**Non-Breaking Changes (Minor version bump):**
- Adding new optional fields
- Adding new endpoints
- Deprecation warnings (with migration timeline)

**Breaking Changes (Major version bump):**
- Removing fields or endpoints
- Changing field types
- Renaming fields
- Requires maintaining old version in parallel

---

## Implementation Guidelines

### API Version Detection

```kotlin
// Example: Version negotiation header
private const val GITHUB_API_VERSION = "application/vnd.github.v3+json"
private const val INTERNAL_API_VERSION = "2024-09-10"

@Provides
fun provideHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest {
            header("Accept", GITHUB_API_VERSION)
            header("API-Version", INTERNAL_API_VERSION)
        }
    }
}
```

### Version Mismatch Handling

Clients should gracefully handle version incompatibilities:
- Parse API version from response headers
- Display upgrade prompt if minimum version requirement not met
- Log version mismatches for debugging

---

## Consequences

### Positive ✅
- **Predictable Behavior:** Explicit versioning prevents surprise breakages
- **Independent Deployment:** Backend teams can evolve APIs without coordinating mobile releases
- **Clear Migration Path:** Version deprecation timelines allow gradual client migration
- **Testing Confidence:** Different environments can test different API versions in isolation

### Negative ⚠️
- **Maintenance Overhead:** Supporting multiple API versions requires parallel implementation
- **Version Tracking:** Requires documentation and monitoring of version compatibility matrix
- **Client Complexity:** Clients need version detection and fallback logic

---

## Alternatives Considered

1. **No Versioning (Breaking Changes Anytime):**
   - *Rejected:* Crashes production clients, poor user experience, no rollback path.

2. **Client-Side Feature Flags:**
   - *Rejected:* Bloats client code with conditional logic, doesn't solve server-side contract breaking.

3. **Single Evergreen API (Only Additive Changes):**
   - *Partially Adopted:* Good for minor versions, but insufficient for major architectural changes.

---

## References

- [GitHub API Versioning](https://docs.github.com/en/rest/overview/api-versions)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Semantic Versioning](https://semver.org/)
- [API Specification: Search Repositories](../api_spec/01_search_repositories_api.md)
- [CI/CD Standards: API Version Alignment](../../01_company_and_team/07_cicd_and_delivery_standards.md)
- [Developer Workflow: Contract-First Development](../../01_company_and_team/08_developer_workflow.md)
