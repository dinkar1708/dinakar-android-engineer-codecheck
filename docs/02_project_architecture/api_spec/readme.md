# API Specification Index & Overview

## 1. Overview
The application connects to the **GitHub REST API v3** to query public repositories, inspect metrics, and display owner metadata.

- **Base URL**: `https://api.github.com`
- **Protocol**: HTTPS / REST
- **Payload Format**: JSON (`application/vnd.github.v3+json`)
- **Transport Security**: TLS 1.3

---

## 2. Authentication & Rate Limiting

### Unauthenticated Rate Limits:
- **Rate Limit**: 60 requests per hour per IP address.
- **Header Metadata**:
  - `X-RateLimit-Limit`: Maximum requests permitted per window (60).
  - `X-RateLimit-Remaining`: Number of remaining requests available in window.
  - `X-RateLimit-Reset`: UTC epoch timestamp indicating when current window resets.

### Handling 403 Forbidden Rate Limits:
When an IP exceeds 60 requests/hr, GitHub responds with HTTP status `403 Forbidden` and a JSON error payload:
```json
{
  "message": "API rate limit exceeded for 192.0.2.1. (But here's the good news: Authenticated requests get a higher rate limit. Check out the documentation for more details.)",
  "documentation_url": "https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting"
}
```

The client network layer intercepts non-2xx statuses and translates them into user-facing error notifications with retry capabilities.

---

## 3. API Specification Documents

| Document | Description |
|:---------|:------------|
| [01_search_repositories_api.md](./01_search_repositories_api.md) | GitHub Search API endpoint specification, request/response schemas |
| [02_api_versioning_guide.md](./02_api_versioning_guide.md) | API versioning strategy, backward compatibility, version negotiation |
| [04_repository_detail_api.md](./04_repository_detail_api.md) | GitHub Repository Detail API (Screen 04) endpoint specification, schemas & UI contracts |

---

## Related Documentation

- **ADR:** [007_api_versioning_strategy.md](../adr/007_api_versioning_strategy.md)
- **Architecture:** [Clean Architecture & UDF](../architecture/01_clean_architecture_and_udf.md)
- **CI/CD:** [API Version Alignment](../../01_company_and_team/07_cicd_and_delivery_standards.md)
