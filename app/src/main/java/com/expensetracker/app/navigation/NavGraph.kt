package com.expensetracker.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.expensetracker.app.presentation.analytics.AnalyticsScreen
import com.expensetracker.app.presentation.categories.CategoriesScreen
import com.expensetracker.app.presentation.dashboard.DashboardScreen
import com.expensetracker.app.presentation.history.HistoryScreen
import com.expensetracker.app.presentation.onboarding.OnboardingScreen
import com.expensetracker.app.presentation.recurring.RecurringScreen
import com.expensetracker.app.presentation.reports.ReportsScreen
import com.expensetracker.app.presentation.settings.SettingsScreen
import com.expensetracker.app.presentation.splash.SplashScreen
import com.expensetracker.app.presentation.transaction.AddTransactionScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(200))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(200))
        }
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToAddTransaction = { type ->
                    navController.navigate(Screen.AddTransaction.createRoute(type))
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.Analytics.route)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                onNavigateToRecurring = {
                    navController.navigate(Screen.Recurring.route)
                },
                onNavigateToReports = {
                    navController.navigate(Screen.Reports.route)
                }
            )
        }
        composable(
            route = Screen.AddTransaction.route,
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                    defaultValue = "EXPENSE"
                }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "EXPENSE"
            AddTransactionScreen(
                initialType = type,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Reports.route) {
            ReportsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Categories.route) {
            CategoriesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Recurring.route) {
            RecurringScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCategories = {
                    navController.navigate(Screen.Categories.route)
                },
                onNavigateToRecurring = {
                    navController.navigate(Screen.Recurring.route)
                },
                onNavigateToReports = {
                    navController.navigate(Screen.Reports.route)
                }
            )
        }
    }
}
