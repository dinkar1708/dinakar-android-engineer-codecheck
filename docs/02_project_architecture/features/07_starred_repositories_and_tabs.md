# Starred Repositories Feature

## 1. Overview
The **Starred Repositories** feature allows users to star repositories from the Repository Detail screen and view their starred list in a dedicated **Starred Tab**.

---

## 2. Features

### A. Repository Detail Screen
- Users can star or unstar a repository by tapping the star icon (`★` / `☆`) in the top app bar.
- Tapping the icon toggles the star state with instant visual feedback.

### B. Starred Tab
- Displays the list of repositories that the user has starred.
- Each repository card displays its basic information and star icon.
- Tapping any card opens the Repository Detail screen.
- An empty state is displayed when no repositories have been starred yet.

---

## 3. Implementation Note

> [!NOTE]
> **API & Cache Note**:
> The app does not call the actual GitHub Star API (`GET /user/starred`) for now because the app operates without user login/OAuth. 
> Starred repositories are stored in an in-memory session cache while the app is running. In a future production release with GitHub user authentication, this will sync directly with GitHub's `GET /user/starred` and `PUT /user/starred` endpoints.
