package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.RecurringExpense
import com.expensetracker.app.domain.repository.RecurringExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageRecurringExpensesUseCase @Inject constructor(
    private val repository: RecurringExpenseRepository
) {
    fun getAll(): Flow<List<RecurringExpense>> = repository.getAllRecurringExpenses()

    fun getActive(): Flow<List<RecurringExpense>> = repository.getActiveRecurringExpenses()

    suspend fun add(expense: RecurringExpense) = repository.addRecurringExpense(expense)

    suspend fun update(expense: RecurringExpense) = repository.updateRecurringExpense(expense)

    suspend fun delete(expense: RecurringExpense) = repository.deleteRecurringExpense(expense)

    suspend fun toggleActive(id: Long, isActive: Boolean) = repository.toggleActive(id, isActive)
}
