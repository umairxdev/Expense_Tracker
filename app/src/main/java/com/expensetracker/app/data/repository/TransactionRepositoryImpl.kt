package com.expensetracker.app.data.repository

import com.expensetracker.app.data.local.dao.TransactionDao
import com.expensetracker.app.data.local.entity.TransactionEntity
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    private fun TransactionEntity.toDomain() = Transaction(
        id = id,
        amount = amount,
        type = TransactionType.valueOf(type),
        category = category,
        date = date,
        note = note,
        isRecurring = isRecurring,
        recurringId = recurringId
    )

    private fun Transaction.toEntity() = TransactionEntity(
        id = id,
        amount = amount,
        type = type.name,
        category = category,
        date = date,
        note = note,
        isRecurring = isRecurring,
        recurringId = recurringId
    )

    override fun getAllTransactions(): Flow<List<Transaction>> =
        dao.getAllTransactions().map { entities -> entities.map { it.toDomain() } }

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        dao.getTransactionsByType(type.name).map { entities -> entities.map { it.toDomain() } }

    override fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        dao.getTransactionsByDateRange(startDate, endDate).map { entities -> entities.map { it.toDomain() } }

    override fun getTransactionsByCategory(category: String): Flow<List<Transaction>> =
        dao.getTransactionsByCategory(category).map { entities -> entities.map { it.toDomain() } }

    override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> =
        dao.getRecentTransactions(limit).map { entities -> entities.map { it.toDomain() } }

    override fun getMonthlyTransactions(month: Int, year: Int): Flow<List<Transaction>> {
        val ym = YearMonth.of(year, month)
        val startDate = ym.atDay(1)
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endDate = ym.atEndOfMonth()
            .atTime(23, 59, 59)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return dao.getMonthlyTransactions(startDate, endDate).map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getTotalIncome(): Double = dao.getTotalIncome() ?: 0.0

    override suspend fun getTotalExpense(): Double = dao.getTotalExpense() ?: 0.0

    override suspend fun getBalance(): Double = (dao.getTotalIncome() ?: 0.0) - (dao.getTotalExpense() ?: 0.0)

    override suspend fun getMonthlyIncome(month: Int, year: Int): Double {
        val range = getMonthRange(month, year)
        return dao.getMonthlyIncome(range.first, range.second) ?: 0.0
    }

    override suspend fun getMonthlyExpense(month: Int, year: Int): Double {
        val range = getMonthRange(month, year)
        return dao.getMonthlyExpense(range.first, range.second) ?: 0.0
    }

    override suspend fun getCategorySummary(month: Int, year: Int): List<Pair<String, Double>> {
        val range = getMonthRange(month, year)
        return dao.getCategorySummary(range.first, range.second).map {
            Pair(it.category, it.total)
        }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        dao.insert(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.update(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.delete(transaction.toEntity())
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return dao.getTransactionById(id)?.toDomain()
    }

    private fun getMonthRange(month: Int, year: Int): Pair<Long, Long> {
        val ym = YearMonth.of(year, month)
        val start = ym.atDay(1)
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = ym.atEndOfMonth()
            .atTime(23, 59, 59)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return Pair(start, end)
    }
}
