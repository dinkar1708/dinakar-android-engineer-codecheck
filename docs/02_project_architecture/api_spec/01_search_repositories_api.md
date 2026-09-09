# API Specification: Search Repositories Endpoint

## 1. Endpoint Overview
Searches for repositories on GitHub matching a given query string with optional sorting and pagination.

- **Method**: `GET`
- **Path**: `/search/repositories`
- **URL**: `https://api.github.com/search/repositories`

---

## 2. Request Parameters

| Parameter | Type | In | Required | Default | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `q` | `string` | Query | **Yes** | — | Search keyword or qualifier (e.g., `kotlin`, `android in:name`). |
| `sort` | `string` | Query | No | `best-match` | Sort field: `stars`, `forks`, `help-wanted-issues`, `updated`. |
| `order` | `string` | Query | No | `desc` | Sort direction: `desc`, `asc`. |
| `per_page`| `integer`| Query | No | `30` | Number of results per page (max 100). |
| `page` | `integer`| Query | No | `1` | Page number of results. |

---

## 3. Headers

| Header | Value | Description |
| :--- | :--- | :--- |
| `Accept` | `application/vnd.github.v3+json` | GitHub API version specification |
| `User-Agent` | `AndroidEngineerCodeCheck` | Client identification |

---

## 4. Response Schema (`200 OK`)

```json
{
  "total_count": 420000,
  "incomplete_results": false,
  "items": [
    {
      "id": 1234567,
      "name": "android-architecture",
      "full_name": "android/architecture-samples",
      "owner": {
        "login": "android",
        "avatar_url": "https://avatars.githubusercontent.com/u/32689599?v=4"
      },
      "html_url": "https://github.com/android/architecture-samples",
      "description": "A collection of samples to discuss different architectural approaches.",
      "language": "Kotlin",
      "stargazers_count": 43500,
      "watchers_count": 43500,
      "forks_count": 11800,
      "open_issues_count": 85
    }
  ]
}
```

---

## 5. Response Fields Dictionary

### Root Object

| Field | Type | Nullable | Description |
| :--- | :--- | :---: | :--- |
| `total_count` | `integer` | No | Total count of repositories matching the query. |
| `incomplete_results` | `boolean` | No | Flag indicating whether query timed out before searching all shards. |
| `items` | `array[object]` | No | Array of matched repository objects. |

### Repository Object (`items[]`)

| Field | Type | Nullable | Description |
| :--- | :--- | :---: | :--- |
| `id` | `integer` | No | Unique repository ID. |
| `name` | `string` | No | Short name of the repository. |
| `full_name` | `string` | No | Full name including owner (e.g., `owner/repo`). |
| `owner` | `object` | Yes | Owner object containing user or organization profile. |
| `owner.login` | `string` | No | GitHub handle of repository owner. |
| `owner.avatar_url` | `string` | Yes | HTTPS URL to the owner's avatar image. |
| `html_url` | `string` | Yes | Web URL to the GitHub repository page. |
| `description` | `string` | Yes | Plain text description of the repository. |
| `language` | `string` | Yes | Primary programming language identified by GitHub Linguist. |
| `stargazers_count` | `integer` | No | Total number of stargazers. |
| `watchers_count` | `integer` | No | Total number of watchers (subscribers). |
| `forks_count` | `integer` | No | Total number of forks. Note: historically corrected from legacy `forks_conut` bug. |
| `open_issues_count` | `integer` | No | Number of open issues and pull requests. |

---

## 6. HTTP Status Codes & Error Contracts

| HTTP Status | Reason | Payload Schema | Client Handling |
| :--- | :--- | :--- | :--- |
| `200 OK` | Request succeeded | `SearchRepositoriesResponse` | Render repository list |
| `304 Not Modified` | Cached content valid | Empty | Serve from in-memory cache |
| `403 Forbidden` | Rate limit exceeded | `{"message": "...", "documentation_url": "..."}` | Present rate limit warning banner |
| `422 Unprocessable Entity` | Query validation failed | `{"message": "...", "errors": [...]}` | Present invalid query prompt |
| `503 Service Unavailable` | GitHub API outage | `{"message": "Service Unavailable"}` | Trigger exponential backoff retry |
