package com.expensetracker.app.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Dashboard : Screen("dashboard")
    data object AddTransaction : Screen("add_transaction?type={type}") {
        fun createRoute(type: String) = "add_transaction?type=$type"
    }
    data object Analytics : Screen("analytics")
    data object Reports : Screen("reports")
    data object History : Screen("history")
    data object Categories : Screen("categories")
    data object Recurring : Screen("recurring")
    data object Settings : Screen("settings")
}
