<div align="center">
  <br/>
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="Pockit" width="96" height="96" style="border-radius: 22px;">
  <br/>
  <h1>Pockit</h1>
  <p>
    <strong>A premium, offline-first personal finance tracker for Android.</strong>
  </p>
  <p>
    Built with Kotlin · Jetpack Compose · Room · Hilt · Material Design 3
  </p>
  <br/>
  <p>
    <img src="https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
    <img src="https://img.shields.io/badge/Compose-BOM_2024.06-4285F4?logo=jetpackcompose&logoColor=white" alt="Compose">
    <img src="https://img.shields.io/badge/Room-2.6.1-FF6F00?logo=sqlite&logoColor=white" alt="Room">
    <img src="https://img.shields.io/badge/Hilt-2.51.1-009688?logo=dagger&logoColor=white" alt="Hilt">
    <img src="https://img.shields.io/badge/Min_SDK-26-34A853" alt="Min SDK">
    <img src="https://img.shields.io/badge/Target_SDK-34-34A853" alt="Target SDK">
    <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
  </p>
  <br/>
</div>

---

## Overview

Pockit is a beautifully crafted, **100% offline** expense tracking application built with financial privacy at its core. No accounts, no cloud sync, no tracking — every transaction stays exclusively on your device.

The app combines **Clean Architecture** with an iOS-inspired design language — dark mode first, emerald green accents, and fluid animations throughout — delivering a premium financial experience.

---

## Features

### Dashboard
- Real-time balance overview with animated counters
- Monthly income vs. expense summary
- Category spending breakdown via interactive pie chart
- Quick-action buttons for recurring payments, reports, and history
- Recent transaction preview
- Floating action button for one-tap income/expense entry

### Transaction Management
- Add income or expense in seconds
- 12 expense categories and 6 income categories with distinct semantic colors
- Optional notes for every transaction
- Amount validation and error handling

### Recurring Expenses
- Daily, weekly, biweekly, monthly, quarterly, and yearly frequencies
- Set due dates and toggle active/paused status
- Notification reminders for upcoming bills

### Analytics & Reports
- Monthly income/expense trends via animated line chart
- Category distribution via donut chart
- Bar chart comparing income vs. expenses over 12 months
- Scrollable monthly summary cards with transaction counts
- PDF report generation for any date range

### Transaction History
- Full search across amounts, notes, and categories
- Quick-filter chips (All, Food, Bills, Shopping, Salary)
- Chronological list with category icons and color coding

### Privacy-First Design
- 100% offline — no internet permission required
- No authentication, no accounts, no sign-up
- All data stored locally via Room database
- No analytics SDKs, no crash reporting, no telemetry

---

## Screenshots

<div align="center">
  <table>
    <tr>
      <td><img src="screenshots/splash.png" alt="Splash" width="200"/></td>
      <td><img src="screenshots/dashboard.png" alt="Dashboard" width="200"/></td>
      <td><img src="screenshots/analytics.png" alt="Analytics" width="200"/></td>
      <td><img src="screenshots/history.png" alt="History" width="200"/></td>
    </tr>
  </table>
</div>

> Screenshots will be added after the initial release. The app currently ships with a polished splash animation, onboarding flow, and full feature set.

---

## Tech Stack

| Layer             | Technology                         |
|-------------------|------------------------------------|
| **Language**      | Kotlin 2.0.0                      |
| **UI**            | Jetpack Compose (BOM 2024.06)     |
| **Architecture**  | MVVM + Clean Architecture          |
| **Database**      | Room 2.6.1                         |
| **DI**            | Hilt 2.51.1                        |
| **Navigation**    | Navigation Compose 2.7.7           |
| **Async**         | Kotlin Coroutines + Flow           |
| **Data Persist.** | DataStore Preferences               |
| **Animations**    | Compose Animation                   |
| **Charts**        | Custom Canvas-based components      |
| **Min SDK**       | 26 (Android 8.0)                   |
| **Target SDK**    | 34 (Android 14)                    |

---

## Architecture

The project follows **Clean Architecture** with strict layer separation:

```
com.expensetracker.app/
├── core/          # Theme, constants, extensions, utilities
├── data/          # Room database, DAOs, entities, repository implementations
├── domain/        # Domain models, repository interfaces, use cases
├── di/            # Hilt dependency injection modules
├── navigation/    # NavGraph, screen routes, bottom nav items
├── presentation/  # ViewModels and Compose screens
│   ├── splash/        # Animated splash with auto-transition
│   ├── onboarding/    # 3-page introduction carousel
│   ├── dashboard/     # Home screen with balance, charts, quick actions
│   ├── transaction/   # Add income/expense form
│   ├── analytics/     # Charts and spending trends
│   ├── reports/       # Monthly reports and PDF generation
│   ├── history/       # Searchable, filterable transaction list
│   ├── recurring/     # Recurring expense management
│   ├── categories/    # Browse expense and income categories
│   └── settings/      # App info, theme, currency, data management
└── ui/
    └── components/  # Reusable composables (cards, charts, logo, etc.)
```

### Key Principles
- **Separation of concerns** — UI logic, business logic, and data access are fully isolated
- **Repository pattern** — Domain layer never depends on data layer implementations
- **Unidirectional data flow** — ViewModels expose `StateFlow` consumed by Compose screens
- **Immutable UI state** — Every screen has a single, immutable data class representing its state

---

## Screens

| Screen              | Description                                     |
|---------------------|-------------------------------------------------|
| **Splash**          | Animated logo with auto-transition              |
| **Onboarding**      | 3-page introduction carousel with skip option   |
| **Dashboard**       | Balance card, stats, pie chart, recent txns    |
| **Add Transaction** | Type toggle, amount input, category grid, notes |
| **Analytics**       | Line chart, category breakdown, monthly summary |
| **Reports**         | Bar chart, monthly report cards, PDF export     |
| **History**         | Searchable, filterable transaction list          |
| **Recurring**       | Recurring expense list with add dialog           |
| **Categories**      | Browse all expense and income categories         |
| **Settings**        | Theme, currency, data management, privacy info   |

---

## Design System

### Colors
- **Background:** Matte Black (`#0D0D0D`), Dark Surface (`#121212`)
- **Cards:** Dark Card (`#1A1A1E`), Elevated (`#222228`)
- **Accent:** Emerald Green (`#059669`), Emerald Glow (`#33059669`)
- **Semantic:** Income Green (`#059669`), Expense Red (`#FF5252`)
- **Text:** Soft White (`#F5F5F5`), Muted White (`#A0A0A8`), Dim White (`#6B6B75`)
- **12 distinct category colors** for visual differentiation
- Full **light mode** palette with matching semantics

### Typography
- System font family (San Francisco on iOS, Roboto on Android)
- Strong hierarchy with large display heads
- Consistent letter spacing on labels
- Premium cash amount styling

### Animations
- Spring-based card press interactions
- Fade + slide navigation transitions (300ms)
- Animated value counters (800–1000ms)
- Chart rendering animations (800–1000ms)
- Staggered card entrance on dashboard

---

## Getting Started

### Prerequisites
- [Android Studio Hedgehog (2023.1.1)](https://developer.android.com/studio) or later
- JDK 17
- Android SDK 34

### Setup

```bash
# Clone the repository
git clone https://github.com/umairxdev/Expense_Tracker.git

# Open in Android Studio
# File > Open > select the project directory

# Sync Gradle and run
./gradlew assembleDebug
```

### Build Variants

```bash
# Debug build (unoptimized, suitable for development)
./gradlew assembleDebug

# Release build (minified with R8, ready for distribution)
./gradlew assembleRelease

# Release App Bundle
./gradlew bundleRelease
```

> **Note:** Ensure your Android SDK path does not contain spaces. Configure it via `File > Settings > Appearance & Behavior > System Settings > Android SDK`.

---

## Permissions

| Permission                  | Purpose                               |
|-----------------------------|---------------------------------------|
| `POST_NOTIFICATIONS`        | Recurring bill reminders (Android 13+) |
| `SCHEDULE_EXACT_ALARM`      | Precise bill due-date notifications    |
| `USE_EXACT_ALARM`           | Alarm scheduling for reminders          |

No internet or network permissions are declared. The app functions entirely offline.

---

## Contributing

Contributions are welcome and appreciated. Here's how to get started:

1. **Fork** the repository
2. **Create a feature branch** (`git checkout -b feature/amazing-feature`)
3. **Commit your changes** (`git commit -m 'Add amazing feature'`)
4. **Push to the branch** (`git push origin feature/amazing-feature`)
5. **Open a Pull Request**

### Style Guide
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use Compose best practices (state hoisting, stable types, composable previews)
- Maintain clean architecture boundaries
- Write meaningful commit messages
- Add unit tests for new business logic

---

## License

Distributed under the **MIT License**. See `LICENSE` for more information.

---

<div align="center">
  <br/>
  <p>
    Built with craftsmanship in Kotlin · 100% offline · 100% private
  </p>
  <p>
    <sub>No Firebase. No servers. No tracking. Just you and your money.</sub>
  </p>
  <br/>
</div>
