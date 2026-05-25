package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.addTransaction(transaction)
    }
}
