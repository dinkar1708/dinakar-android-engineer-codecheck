# API Specification: Starred Repositories Endpoints

## 1. Overview
This specification documents GitHub's official REST API endpoints for starring, unstarring, and listing starred repositories.

```text
Base URL: https://api.github.com
API Version: GitHub REST API v3
Format: JSON (application/vnd.github.v3+json)
```

---

## 2. Endpoints Reference

### 1. List Repositories Starred by Authenticated User
Retrieves a paginated list of repositories that the authenticated user has starred.

- **Method**: `GET`
- **Path**: `/user/starred`
- **URL**: `https://api.github.com/user/starred`
- **Authentication**: Required (`Bearer <token>`)

#### Request Parameters:
| Parameter | Type | In | Required | Default | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `sort` | `string` | Query | No | `created` | Sort field: `created` (date starred) or `updated` (date repo was last pushed to). |
| `direction` | `string` | Query | No | `desc` | Sort direction: `asc` or `desc`. |
| `per_page` | `integer` | Query | No | `30` | Number of results per page (max 100). |
| `page` | `integer` | Query | No | `1` | Page number of results. |

#### Response (`200 OK`):
Returns an array of complete repository objects:
```json
[
  {
    "id": 24195230,
    "name": "flutter",
    "full_name": "flutter/flutter",
    "owner": {
      "login": "flutter",
      "avatar_url": "https://avatars.githubusercontent.com/u/14101776?v=4"
    },
    "html_url": "https://github.com/flutter/flutter",
    "description": "Flutter makes it easy and fast to build beautiful apps for mobile and beyond",
    "language": "Dart",
    "stargazers_count": 160000,
    "forks_count": 26000,
    "open_issues_count": 5200
  }
]
```

---

### 2. Check if a Repository is Starred
Checks whether a specific repository is starred by the authenticated user.

- **Method**: `GET`
- **Path**: `/user/starred/{owner}/{repo}`
- **URL**: `https://api.github.com/user/starred/{owner}/{repo}`
- **Authentication**: Required (`Bearer <token>`)

#### Response Status Codes:
- `204 No Content`: The repository is starred by the user.
- `404 Not Found`: The repository is not starred by the user.
- `401 Unauthorized`: No authentication credentials provided.

---

### 3. Star a Repository
Stars a repository on GitHub on behalf of the authenticated user.

- **Method**: `PUT`
- **Path**: `/user/starred/{owner}/{repo}`
- **URL**: `https://api.github.com/user/starred/{owner}/{repo}`
- **Authentication**: Required (`Bearer <token>`)

#### Response Status:
- `204 No Content`: Repository successfully starred.

---

### 4. Unstar a Repository
Removes a star from a repository on GitHub.

- **Method**: `DELETE`
- **Path**: `/user/starred/{owner}/{repo}`
- **URL**: `https://api.github.com/user/starred/{owner}/{repo}`
- **Authentication**: Required (`Bearer <token>`)

#### Response Status:
- `204 No Content`: Repository successfully unstarred.

---

## 3. Client Architecture & Unauthenticated Mode Strategy

1. **Unauthenticated Public Access (Coding Challenge Mode)**:
   - Without an OAuth token, requests to `/user/starred` return `401 Unauthorized`.
   - To respect GitHub's 60 req/hr unauthenticated rate limit and avoid individual N+1 calls (`GET /repositories/{id}`), the application uses **Local-First Caching**:
     - Starred repositories are stored locally upon user interaction.
     - The Starred tab displays immediately with zero network latency and full offline support.
2. **Production OAuth Upgrade**:
   - In a production release, signing in with GitHub OAuth seamlessly maps the local cache to `GET /user/starred` with HTTP ETag conditional requests (`If-None-Match`).
