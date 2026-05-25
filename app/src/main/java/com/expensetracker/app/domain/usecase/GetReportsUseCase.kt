package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.CategorySummary
import com.expensetracker.app.domain.model.MonthlyReport
import com.expensetracker.app.domain.model.SpendingTrend
import com.expensetracker.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GetReportsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend fun getMonthlyReport(month: Int, year: Int): MonthlyReport {
        val income = transactionRepository.getMonthlyIncome(month, year)
        val expense = transactionRepository.getMonthlyExpense(month, year)
        val transactions = transactionRepository.getMonthlyTransactions(month, year).first()
        val categoryPairs = transactionRepository.getCategorySummary(month, year)
        val totalExpense = categoryPairs.sumOf { it.second }

        val categorySummaries = categoryPairs.map { (category, amount) ->
            CategorySummary(
                category = category,
                amount = amount,
                percentage = if (totalExpense > 0) (amount / totalExpense).toFloat() else 0f,
                transactionCount = transactions.count { it.category == category }
            )
        }

        return MonthlyReport(
            month = YearMonth.of(year, month).month.name,
            year = year,
            monthValue = month,
            totalIncome = income,
            totalExpense = expense,
            balance = income - expense,
            categorySummaries = categorySummaries,
            transactionCount = transactions.size
        )
    }

    suspend fun getSpendingTrends(months: Int = 6): List<SpendingTrend> {
        val trends = mutableListOf<SpendingTrend>()
        val current = YearMonth.now()

        for (i in (months - 1) downTo 0) {
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
