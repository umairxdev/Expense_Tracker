package com.expensetracker.app.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expensetracker.app.core.theme.CharcoalGray
import com.expensetracker.app.core.theme.DarkCard
import com.expensetracker.app.core.theme.DarkCardElevated
import com.expensetracker.app.core.theme.DimWhite
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.EmeraldGlow
import com.expensetracker.app.core.theme.ExpenseRed
import com.expensetracker.app.core.theme.MatteBlack
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite
import com.expensetracker.app.core.utils.CurrencyUtils
import com.expensetracker.app.core.utils.DateUtils
import com.expensetracker.app.ui.components.AnimatedCard
import com.expensetracker.app.ui.components.CircularProgressIndicator
import com.expensetracker.app.ui.components.EmptyState
import com.expensetracker.app.ui.components.PieChart
import com.expensetracker.app.ui.components.TransactionItem

@Composable
fun DashboardScreen(
    onNavigateToAddTransaction: (String) -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToRecurring: () -> Unit,
    onNavigateToReports: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MatteBlack,
        floatingActionButton = {
            Box {
                var expanded by remember { mutableStateOf(false) }
                FloatingActionButton(
                    onClick = { expanded = !expanded },
                    containerColor = EmeraldGreen,
                    contentColor = MatteBlack,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp)
                    )
                }

                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = 72.dp)
                            .width(160.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        DashboardFloatingOption(
                            label = "Add Income",
                            color = EmeraldGreen,
                            onClick = {
                                expanded = false
                                onNavigateToAddTransaction("INCOME")
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DashboardFloatingOption(
                            label = "Add Expense",
                            color = ExpenseRed,
                            onClick = {
                                expanded = false
                                onNavigateToAddTransaction("EXPENSE")
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Text(
                text = "Overview",
                color = SoftWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = DateUtils.formatMonthYear(
                    java.time.LocalDate.now().year,
                    java.time.LocalDate.now().monthValue
                ),
                color = MutedWhite,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Balance Card
            if (uiState.data != null) {
                BalanceCard(
                    balance = uiState.data!!.balance,
                    income = uiState.data!!.monthlyIncome,
                    expense = uiState.data!!.monthlyExpense,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Quick Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickStatCard(
                        label = "Income",
                        amount = uiState.data!!.monthlyIncome,
                        color = EmeraldGreen,
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatCard(
                        label = "Expenses",
                        amount = uiState.data!!.monthlyExpense,
                        color = ExpenseRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Category Summary
                if (uiState.data!!.categorySummaries.isNotEmpty()) {
                    AnimatedCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                text = "Spending by Category",
                                color = SoftWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            PieChart(
                                data = uiState.data!!.categorySummaries,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Quick Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Filled.Repeat,
                        label = "Recurring",
                        onClick = onNavigateToRecurring,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = Icons.Filled.TrendingUp,
                        label = "Reports",
                        onClick = onNavigateToReports,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = Icons.Filled.CalendarMonth,
                        label = "History",
                        onClick = onNavigateToHistory,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Recent Transactions
                AnimatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent Transactions",
                                color = SoftWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "See All",
                                color = EmeraldGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { onNavigateToHistory() }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        if (uiState.data!!.recentTransactions.isEmpty()) {
                            EmptyState(
                                title = "No transactions yet",
                                subtitle = "Tap + to add your first transaction"
                            )
                        } else {
                            uiState.data!!.recentTransactions.forEach { transaction ->
                                TransactionItem(
                                    transaction = transaction,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun BalanceCard(
    balance: Double,
    income: Double,
    expense: Double,
    modifier: Modifier = Modifier
) {
    val animatedBalance by animateFloatAsState(
        targetValue = balance.toFloat(),
        animationSpec = tween(1000),
        label = "balance"
    )

    Box(
        modifier = modifier
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        DarkCardElevated,
                        DarkCard
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(EmeraldGlow, RoundedCornerShape(20.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Current Balance",
                color = MutedWhite,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = CurrencyUtils.format(animatedBalance.toDouble()),
                color = SoftWhite,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Income",
                        color = MutedWhite,
                        fontSize = 11.sp
                    )
                    Text(
                        text = CurrencyUtils.format(income),
                        color = EmeraldGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Expenses",
                        color = MutedWhite,
                        fontSize = 11.sp
                    )
                    Text(
                        text = CurrencyUtils.format(expense),
                        color = ExpenseRed,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickStatCard(
    label: String,
    amount: Double,
    color: Color,
    modifier: Modifier = Modifier
) {
    AnimatedCard(modifier = modifier) {
        Column {
            Text(
                text = label,
                color = MutedWhite,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = CurrencyUtils.format(amount),
                color = color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = EmeraldGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = SoftWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DashboardFloatingOption(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCardElevated)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
