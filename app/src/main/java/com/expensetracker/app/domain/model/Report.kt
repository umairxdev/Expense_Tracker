package com.expensetracker.app.domain.model

data class DailyReport(
    val date: String,
    val income: Double,
    val expense: Double,
    val balance: Double
)

data class CategorySummary(
    val category: String,
    val amount: Double,
    val percentage: Float,
    val transactionCount: Int
)

data class MonthlyReport(
    val month: String,
    val year: Int,
    val monthValue: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double,
    val categorySummaries: List<CategorySummary>,
    val transactionCount: Int
)

data class SpendingTrend(
    val month: String,
    val monthValue: Int,
    val year: Int,
    val income: Double,
    val expense: Double
)

data class DashboardData(
    val balance: Double,
    val totalIncome: Double,
    val totalExpense: Double,
    val monthlyIncome: Double,
    val monthlyExpense: Double,
    val recentTransactions: List<Transaction>,
    val categorySummaries: List<CategorySummary>,
    val spendingTrends: List<SpendingTrend>,
    val budgetStatus: List<Budget>
)
