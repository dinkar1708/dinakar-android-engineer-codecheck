# Feature Specification: Localization & Accessibility

## 1. Bilingual Localization (English & Japanese)

The application provides native bilingual localization across all user-facing strings, adhering to Android resource catalog standards:
- Default / English: `app/src/main/res/values/strings.xml`
- Japanese: `app/src/main/res/values-ja/strings.xml`

### String Resource Mapping:

| Key Name | English (`values/strings.xml`) | Japanese (`values-ja/strings.xml`) | Usage Context |
| :--- | :--- | :--- | :--- |
| `app_name` | GitHub Repository Explorer | GitHub リポジトリ検索 | App launcher & header |
| `search_hint` | Search repositories&#8230; | リポジトリを検索&#8230; | Search bar placeholder |
| `search_welcome` | Enter a search term to find GitHub repositories | 検索キーワードを入力してリポジトリを検索してください | Cold start idle state |
| `no_results` | No repositories found | リポジトリが見つかりませんでした | Empty results state |
| `retry` | Retry | 再試行 | Error state action button |
| `stars` | Stars | スター数 | Detail metric card |
| `watchers` | Watchers | 閲覧者数 | Detail metric card |
| `forks` | Forks | フォーク数 | Detail metric card |
| `open_issues` | Open Issues | 未解決の課題数 | Detail metric card |
| `language` | Language | 言語 | Detail metric card |
| `open_in_browser` | Open in GitHub | GitHub で開く | Browser button |

---

## 2. Accessibility & Typography Standards

### 1. Typography Ellipsis Standard (`&#8230;`):
- To satisfy Android Lint rule `TypographyEllipsis` and maintain proper typography rendering, ellipsis strings use the Unicode character entity `&#8230;` instead of three consecutive periods `...`.

### 2. TalkBack Semantics & Screen Readers:
- **Image Content Descriptions**:
  - Pure decorative elements (e.g., star icons, fork icons) have `contentDescription = null` to avoid redundant auditory clutter.
  - Informative elements (e.g., owner avatars, navigation back button, clear search input) declare explicit localized descriptions.
- **Card Semantics**:
  - `RepositoryCard` combines child text nodes into a cohesive semantic item so screen readers read: *"google/android, Kotlin, 1000 stars, double tap to view details"*.

### 3. High Contrast & Dynamic Type:
- All text styles utilize SP (Scale-independent Pixels) units, scaling smoothly when system accessibility font size or display size is enlarged.
- Minimum touch target sizing conforms to WCAG 2.1 AA recommendations (&ge; 48x48dp for buttons and interactive icons).
