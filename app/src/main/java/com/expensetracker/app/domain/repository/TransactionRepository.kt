package com.expensetracker.app.domain.repository

import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>
    fun getMonthlyTransactions(month: Int, year: Int): Flow<List<Transaction>>
    suspend fun getTotalIncome(): Double
    suspend fun getTotalExpense(): Double
    suspend fun getBalance(): Double
    suspend fun getMonthlyIncome(month: Int, year: Int): Double
    suspend fun getMonthlyExpense(month: Int, year: Int): Double
    suspend fun getCategorySummary(month: Int, year: Int): List<Pair<String, Double>>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun getTransactionById(id: Long): Transaction?
}
