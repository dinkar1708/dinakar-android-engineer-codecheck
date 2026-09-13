# API Specification: Repository Detail Endpoint (Screen 04)

## 1. Endpoint Overview
Retrieves comprehensive details for a single GitHub repository by its owner handle and repository name. This endpoint powers the **Repository Detail Screen (Screen 04)**, providing headline engagement metrics, extended metadata cards, and interactive actions.

- **Method**: `GET`
- **Path**: `/repos/{owner}/{repo}`
- **URL**: `https://api.github.com/repos/{owner}/{repo}`

> **Note on Architecture**: In the application's offline-first navigation flow, repository details are carried directly via navigation arguments from the search results (`RepositoryItem`), allowing instant screen transitions. When refreshing or deep-linking, this endpoint is fetched directly.

---

## 2. Request Parameters

### Path Parameters

| Parameter | Type | In | Required | Description |
| :--- | :--- | :--- | :--- | :--- |
| `owner` | `string` | Path | **Yes** | The account owner of the repository (case-insensitive handle, e.g. `android` or `dmtrKovalenko`). |
| `repo` | `string` | Path | **Yes** | The name of the repository (case-insensitive, e.g. `compose-samples` or `fff`). |

---

## 3. Headers

| Header | Value | Description |
| :--- | :--- | :--- |
| `Accept` | `application/vnd.github.v3+json` | GitHub API version specification. |
| `User-Agent` | `AndroidEngineerCodeCheck` | Client identification. |

---

## 4. Response Schema (`200 OK`)

```json
{
  "id": 1234567,
  "name": "fff",
  "full_name": "dmtrKovalenko/fff",
  "owner": {
    "login": "dmtrKovalenko",
    "avatar_url": "https://avatars.githubusercontent.com/u/16926049?v=4",
    "html_url": "https://github.com/dmtrKovalenko"
  },
  "description": "Fastest file finder, built for terminal workflows. MIT licensed.",
  "html_url": "https://github.com/dmtrKovalenko/fff",
  "language": "Rust",
  "stargazers_count": 10695,
  "watchers_count": 10695,
  "forks_count": 444,
  "open_issues_count": 87,
  "default_branch": "main",
  "pushed_at": "2026-09-02T14:32:00Z",
  "license": {
    "key": "mit",
    "name": "MIT License",
    "spdx_id": "MIT",
    "url": "https://api.github.com/licenses/mit"
  },
  "size": 4300,
  "updated_at": "2026-09-02T16:00:00Z"
}
```

---

## 5. Response Fields & UI Presentation Dictionary

### Identity & Header (Hero Navy Section `#2D3545`)

| Field | Type | Nullable | UI Component | Presentation Rules |
| :--- | :--- | :---: | :--- | :--- |
| `name` | `string` | No | Title (`displayName`) | Repository display name. If composite `owner/repo`, extracted to short name. |
| `owner.login` | `string` | No | Subtitle | Repository owner handle in `Slate400` (`#94A3B8`). |
| `owner.avatar_url` | `string` | Yes | Circle Avatar (`56dp`) | Loaded via Coil. Fallback monogram initials (`getMonogramInitials`). |
| `description` | `string` | Yes | Description Text | Displayed in `Slate300` (`#CBD5E1`), 13sp. Omitted if null or blank. |
| `language` | `string` | Yes | Language Chip | Pill chip container in `Slate600` (`#475569`). Omitted if null. |

### Headline Metrics (2x2 StatCard Grid)

| Field | Type | Nullable | UI Label | Value Color | Formatting |
| :--- | :--- | :---: | :--- | :--- | :--- |
| `stargazers_count` | `integer` | No | `STARS` | `Slate900` (`#0F172A`) | Decimal formatted with commas (e.g., `10,695`). |
| `forks_count` | `integer` | No | `FORKS` | `Slate900` (`#0F172A`) | Decimal formatted with commas (e.g., `444`). |
| `watchers_count` | `integer` | No | `WATCHERS` | `Slate900` (`#0F172A`) | Decimal formatted with commas (e.g., `10,695`). |
| `open_issues_count`| `integer` | No | `OPEN ISSUES` | `AppAmber` (`#B45309`) | Highlight color alert indicating items needing attention. |

### Extended Metadata Cards (4 MetaRows)

| Field | Type | Nullable | UI Label | Presentation & Formatting Rules |
| :--- | :--- | :---: | :--- | :--- |
| `default_branch` | `string` | Yes | `Default branch` | Styled with `FontFamily.Monospace`. Fallback: `N/A`. |
| `pushed_at` | `string` | Yes | `Last push` | Formatted via `DetailFormatters.formatPushDate` to `YYYY-MM-DD`. Fallback: `—`. |
| `license` | `object` | Yes | `License` | Displays `license.spdx_id` or `license.name`. Fallback: `None`. |
| `size` | `integer` | No | `Size` | Formatted via `DetailFormatters.formatSize` (KB, MB, GB). e.g., `4300` -> `4.2 MB`. |

---

## 6. Interactive Action Contracts

### 1. View on GitHub (`Chrome Custom Tabs`)
- **Trigger**: "View on GitHub" button click.
- **Target URL**: `repository.htmlUrl` (`https://github.com/{owner}/{repo}`).
- **Chrome Custom Tabs Specification**:
  - Toolbar Color: `#2D3545` (`AppNavy`).
  - `setShowTitle(true)`.
  - Fallback: System browser intent (`Intent.ACTION_VIEW`) if no Custom Tabs provider exists.

### 2. Companion Archive Download
- **Trigger**: 48dp square companion download button with `Icons.Default.Download`.
- **Target URL**:
  ```kotlin
  "${repository.htmlUrl}/archive/refs/heads/${repository.defaultBranch ?: "main"}.zip"
  ```
- **Action**: Dispatches URL to `onOpenBrowser` to trigger the browser's download manager.

---

## 7. HTTP Status Codes & Error Contracts

| HTTP Status | Meaning | Payload Schema | Client Handling |
| :--- | :--- | :--- | :--- |
| `200 OK` | Repository details retrieved successfully | `RepositoryItemDto` | Render full detail view. |
| `301 Moved Permanently` | Repository transferred or renamed | `{"message": "Moved Permanently", "url": "..."}` | Follow redirect or update local cache. |
| `304 Not Modified` | Cached version is current | Empty | Render from local repository cache. |
| `403 Forbidden` | Rate limit exceeded | `{"message": "API rate limit exceeded..."}` | Show error state with retry. |
| `404 Not Found` | Repository does not exist or private | `{"message": "Not Found"}` | Display error state with user notification. |
| `503 Service Unavailable` | GitHub API temporarily unavailable | `{"message": "Service Unavailable"}` | Offer retry button. |
