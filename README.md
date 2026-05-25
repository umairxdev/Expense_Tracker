# ExpenseTracker

<div align="center">
  <p>
    <strong>A premium, offline-first personal finance tracker for Android.</strong>
  </p>
  <p>
    Built with Kotlin &middot; Jetpack Compose &middot; Room &middot; Hilt &middot; Material Design 3
  </p>
  <br>
  <p>
    <img src="https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
    <img src="https://img.shields.io/badge/Compose-BOM%202024.06-4285F4?logo=jetpackcompose&logoColor=white" alt="Compose">
    <img src="https://img.shields.io/badge/Room-2.6.1-FF6F00?logo=sqlite&logoColor=white" alt="Room">
    <img src="https://img.shields.io/badge/Hilt-2.51.1-009688?logo=dagger&logoColor=white" alt="Hilt">
    <img src="https://img.shields.io/badge/Min%20SDK-26-34A853" alt="Min SDK">
    <img src="https://img.shields.io/badge/Target%20SDK-34-34A853" alt="Target SDK">
    <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
  </p>
</div>

---

## Overview

ExpenseTracker is a beautifully crafted, 100% offline expense tracking application that puts your financial privacy first. No accounts. No cloud. No tracking. Every transaction stays exclusively on your device.

The app combines clean architecture with an elegant, iOS-inspired design language — dark mode first, emerald green accents, and smooth animations throughout — to deliver a premium financial experience that feels like Apple Wallet meets Copilot Money.

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
- 12 expense categories: Groceries, Bills, Food, Shopping, Transport, Rent, Entertainment, Healthcare, Travel, Education, Utilities, Other
- 6 income categories: Salary, Freelance, Business, Investments, Rent, Other
- Category icons with distinct semantic colors
- Optional notes for every transaction
- Amount validation and error handling

### Recurring Expenses
- Support for daily, weekly, biweekly, monthly, quarterly, and yearly recurring payments
- Set due dates and track active/paused status
- Tap to toggle payment active state
- Notification reminders for upcoming bills

### Analytics & Reports
- Monthly income/expense trends via animated line chart
- Category distribution via donut chart
- Bar chart comparing income vs. expenses over 12 months
- Scrollable monthly summary cards with transaction counts
- Spending trend analysis across 6 months

### Transaction History
- Full search across amounts, notes, and categories
- Quick-filter chips (All, Food, Bills, Shopping, Salary)
- Chronological list with category icons and color coding
- Red for expenses, green for income

### Privacy-First Design
- 100% offline — no internet permission required
- No authentication, no accounts, no sign-up
- All data stored locally via Room database
- No analytics SDKs, no crash reporting, no telemetry

---

## Tech Stack

| Layer         | Technology                          |
|---------------|-------------------------------------|
| Language      | Kotlin 2.0.0                       |
| UI            | Jetpack Compose (BOM 2024.06)      |
| Architecture  | MVVM + Clean Architecture           |
| Database      | Room 2.6.1                         |
| DI            | Hilt 2.51.1                        |
| Navigation    | Navigation Compose 2.7.7           |
| Async         | Kotlin Coroutines + Flow           |
| Data Persist. | DataStore Preferences               |
| Animations    | Compose Animation                   |
| Charts        | Custom Canvas-based components      |
| Min SDK       | 26 (Android 8.0)                   |
| Target SDK    | 34 (Android 14)                    |

---

## Architecture

The project follows **Clean Architecture** with three distinct layers:

```
app/
├── core/          # Theme, constants, extensions, utilities
├── data/          # Room database, DAOs, entities, repository implementations
├── domain/        # Domain models, repository interfaces, use cases
├── di/            # Hilt dependency injection modules
├── navigation/    # NavGraph, screen routes, bottom nav items
├── presentation/  # ViewModels and Compose screens
│   ├── splash/
│   ├── onboarding/
│   ├── dashboard/
│   ├── transaction/
│   ├── analytics/
│   ├── reports/
│   ├── history/
│   ├── recurring/
│   ├── categories/
│   └── settings/
└── ui/
    └── components/  # Reusable composables (cards, charts, etc.)
```

### Key Principles
- **Separation of concerns** — UI logic, business logic, and data access are fully isolated
- **Repository pattern** — Domain layer never depends on data layer implementations
- **Unidirectional data flow** — ViewModels expose `StateFlow` consumed by Compose screens
- **Immutable UI state** — Every screen has a single, immutable data class representing its state

---

## Screens

| Screen            | Description                                       |
|-------------------|---------------------------------------------------|
| Splash            | Animated logo with auto-transition               |
| Onboarding        | 3-page introduction carousel with skip option    |
| Dashboard         | Balance card, stats, pie chart, recent transactions |
| Add Transaction   | Type toggle, amount input, category grid, notes  |
| Analytics         | Line chart, category breakdown, monthly summary  |
| Reports           | Bar chart, monthly report cards                  |
| History           | Searchable, filterable transaction list          |
| Recurring         | Recurring expense list with add dialog           |
| Categories        | Browse all expense and income categories         |
| Settings          | App info, navigation links                       |

---

## Design System

### Colors
- **Background:** Matte Black (`#0D0D0D`), Dark Surface (`#121212`)
- **Cards:** Dark Card (`#1A1A1E`), Elevated (`#222228`)
- **Accent:** Emerald Green (`#00E676`), Emerald Glow (`#3300E676`)
- **Semantic:** Income Green (`#00E676`), Expense Red (`#FF5252`)
- **Text:** Soft White (`#F5F5F5`), Muted White (`#A0A0A8`)
- **12 distinct category colors** for visual differentiation

### Typography
- System font family (San Francisco-style on iOS, Roboto on Android)
- Strong hierarchy with large display heads
- Consistent 0.5px letter spacing on labels
- Premium cash amount styling

### Animations
- Spring-based card press animations
- Fade + slide navigation transitions (300ms)
- Animated value counters (800–1000ms)
- Chart rendering animations (800–1000ms)
- Staggered card entrance on dashboard

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Setup

```bash
# Clone the repository
git clone https://github.com/umairxdev/Expense_Tracker.git

# Open in Android Studio
cd Expense_Trackerracker
./gradlew assembleDebug
```

> **Important:** Ensure your Android SDK path does not contain spaces. Configure it via  
> `File > Settings > Appearance & Behavior > System Settings > Android SDK`.

### Build Variants
- `debug` — Unoptimized, suitable for development
- `release` — Minified with R8, ready for distribution

```bash
# Build release APK
./gradlew assembleRelease

# Build release App Bundle
./gradlew bundleRelease
```

---

## Permissions

| Permission                     | Purpose                               |
|-------------------------------|---------------------------------------|
| `POST_NOTIFICATIONS`          | Recurring bill reminders (Android 13+) |
| `SCHEDULE_EXACT_ALARM`        | Precise bill due-date notifications    |
| `USE_EXACT_ALARM`             | Alarm scheduling for reminders         |

No internet or network permissions are declared. The app functions entirely offline.

---

## Roadmap

- [ ] Budget management with spending alerts
- [ ] Export data to CSV/PDF
- [ ] Custom category creation
- [ ] Biometric app lock
- [ ] Widget support (Android 12+)
- [ ] Multi-currency support
- [ ] Dark/Light theme toggle
- [ ] Data backup to local file

---

## Contributing

Contributions are welcome. Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Style Guide
- Follow Kotlin coding conventions
- Use Compose best practices (state hoisting, stable types)
- Maintain clean architecture boundaries
- Write meaningful commit messages

---

## License

Distributed under the MIT License. See `LICENSE` for more information.

---

<div align="center">
  <br>
  <p>
    Built with craftsmanship in Kotlin &bull; 100% offline &bull; 100% private
  </p>
  <p>
    <sub>No Firebase. No servers. No tracking. Just you and your money.</sub>
  </p>
</div>
