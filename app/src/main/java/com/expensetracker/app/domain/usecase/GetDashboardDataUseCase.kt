package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.CategorySummary
import com.expensetracker.app.domain.model.DashboardData
import com.expensetracker.app.domain.model.SpendingTrend
import com.expensetracker.app.domain.repository.BudgetRepository
import com.expensetracker.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(): DashboardData {
        val now = LocalDate.now()
        val currentMonth = now.monthValue
        val currentYear = now.year

        val totalIncome = transactionRepository.getTotalIncome()
        val totalExpense = transactionRepository.getTotalExpense()
        val balance = totalIncome - totalExpense
        val monthlyIncome = transactionRepository.getMonthlyIncome(currentMonth, currentYear)
        val monthlyExpense = transactionRepository.getMonthlyExpense(currentMonth, currentYear)

        val recentTransactions = transactionRepository.getRecentTransactions(5).first()

        val categoryPairs = transactionRepository.getCategorySummary(currentMonth, currentYear)
        val totalMonthlyExpense = categoryPairs.sumOf { it.second }
        val categorySummaries = categoryPairs.map { (category, amount) ->
            CategorySummary(
                category = category,
                amount = amount,
                percentage = if (totalMonthlyExpense > 0) (amount / totalMonthlyExpense).toFloat() else 0f,
                transactionCount = 0
            )
        }

        val trends = getSpendingTrends()

        val budgets = budgetRepository.getBudgetsByMonth(currentMonth, currentYear).first()

        return DashboardData(
            balance = balance,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            monthlyIncome = monthlyIncome,
            monthlyExpense = monthlyExpense,
            recentTransactions = recentTransactions,
            categorySummaries = categorySummaries,
            spendingTrends = trends,
            budgetStatus = budgets
        )
    }

    private suspend fun getSpendingTrends(): List<SpendingTrend> {
        val trends = mutableListOf<SpendingTrend>()
        val current = YearMonth.now()

        for (i in 5 downTo 0) {
            val ym = current.minusMonths(i.toLong())
            val income = transactionRepository.getMonthlyIncome(ym.monthValue, ym.year)
            val expense = transactionRepository.getMonthlyExpense(ym.monthValue, ym.year)
            trends.add(
                SpendingTrend(
                    month = ym.month.name.take(3),
                    monthValue = ym.monthValue,
                    year = ym.year,
                    income = income,
                    expense = expense
                )
            )
        }
        return trends
    }
}
