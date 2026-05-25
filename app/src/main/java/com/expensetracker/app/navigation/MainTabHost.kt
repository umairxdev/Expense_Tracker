package com.expensetracker.app.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.expensetracker.app.presentation.analytics.AnalyticsScreen
import com.expensetracker.app.presentation.dashboard.DashboardScreen
import com.expensetracker.app.presentation.history.HistoryScreen
import com.expensetracker.app.presentation.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainTabHost(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onNavigateToAddTransaction: (String) -> Unit,
    onNavigateToRecurring: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToReportGenerator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        if (selectedTab != pagerState.currentPage) {
            pagerState.animateScrollToPage(selectedTab)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onTabSelected(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        when (page) {
            0 -> DashboardScreen(
                onNavigateToAddTransaction = onNavigateToAddTransaction,
                onNavigateToAnalytics = { scope.launch { pagerState.animateScrollToPage(1) } },
                onNavigateToHistory = { scope.launch { pagerState.animateScrollToPage(2) } },
                onNavigateToRecurring = onNavigateToRecurring,
                onNavigateToReports = onNavigateToReports
            )
            1 -> AnalyticsScreen(
                onNavigateBack = { scope.launch { pagerState.animateScrollToPage(0) } }
            )
            2 -> HistoryScreen(
                onNavigateBack = { scope.launch { pagerState.animateScrollToPage(0) } }
            )
            3 -> SettingsScreen(
                onNavigateBack = { scope.launch { pagerState.animateScrollToPage(0) } },
                onNavigateToCategories = onNavigateToCategories,
                onNavigateToRecurring = onNavigateToRecurring,
                onNavigateToReports = onNavigateToReports,
                onNavigateToReportGenerator = onNavigateToReportGenerator
            )
        }
    }
}
