package com.expensetracker.app.presentation.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.ExpenseRed
import com.expensetracker.app.core.theme.MatteBlack
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite
import com.expensetracker.app.core.utils.CurrencyUtils
import com.expensetracker.app.ui.components.AnimatedCard
import com.expensetracker.app.ui.components.LineChart
import com.expensetracker.app.ui.components.PieChart
import com.expensetracker.app.ui.components.StatCard

@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatteBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = SoftWhite)
            }
            Text(
                text = "Analytics",
                color = SoftWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Summary cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Income",
                    amount = state.totalIncome,
                    color = EmeraldGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Expenses",
                    amount = state.totalExpense,
                    color = ExpenseRed,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatCard(
                label = "Balance",
                amount = state.balance,
                color = if (state.balance >= 0) EmeraldGreen else ExpenseRed,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Spending Trends
            if (state.spendingTrends.isNotEmpty()) {
                AnimatedCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Spending Trends",
                        color = SoftWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LineChart(
                        data = state.spendingTrends,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Category Breakdown
            if (state.categorySummaries.isNotEmpty()) {
                AnimatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = "Category Breakdown",
                            color = SoftWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        PieChart(
                            data = state.categorySummaries,
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}
