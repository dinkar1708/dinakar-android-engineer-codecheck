# Design Specifications

This directory contains all UI/UX design specifications for the application.

## How to Use

1. Open `00 Index.html` in your browser to navigate all designs
2. All design files are interlinked - keep them together in this directory
3. Use these specifications when implementing features

## Design Files

### Screens
- `01 SplashScreen.html` - App launch screen
- `02 MainScreen.html` - Main repository list screen
- `03 SearchScreen.html` - Repository search interface
- `04 StarredScreen.html` - Starred repositories view
- `05 DetailScreen.html` - Repository details view
- `06 SettingsScreen.html` - Application settings

### Common Components
- `Common_AppIcon.html` - App icon specifications
- `Common_Chip.html` - Chip component
- `Common_IconRow.html` - Icon row component
- `Common_MetaRow.html` - Metadata row component
- `Common_Palette.html` - Color palette and design system
- `Common_RepoCard.html` - Repository card component
- `Common_StatCard.html` - Statistics card component

## For Developers

When implementing features, reference the corresponding design file:
- Feature specifications in [`docs/02_project_architecture/features/`](../features/readme.md) reference these design files
- Example: "See design: `docs/02_project_architecture/design/html/02 MainScreen.html`"

## Workflow

Designer → Export HTML → Commit to this folder (`html/`) → Developer implements from specs

---

## 🔗 Related Documentation
- [UI/UX Design Specification & Architecture](../ui_ux_design_specification.md)
- [AI-Assisted Design-to-Code Workflow](../ai_assisted_design_workflow.md)
- [Design System & Material 3 Theming Specification](../../features/03_design_system_and_theming.md)
