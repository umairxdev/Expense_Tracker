package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    fun getAllTransactions(): Flow<List<Transaction>> = repository.getAllTransactions()

    fun getTransactionsByMonth(month: Int, year: Int): Flow<List<Transaction>> =
        repository.getMonthlyTransactions(month, year)

    fun getTransactionsByCategory(category: String): Flow<List<Transaction>> =
        repository.getTransactionsByCategory(category)
}
