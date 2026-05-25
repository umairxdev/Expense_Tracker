package com.expensetracker.app.domain.repository

import com.expensetracker.app.domain.model.RecurringExpense
import kotlinx.coroutines.flow.Flow

interface RecurringExpenseRepository {
    fun getAllRecurringExpenses(): Flow<List<RecurringExpense>>
    fun getActiveRecurringExpenses(): Flow<List<RecurringExpense>>
    suspend fun getRecurringExpenseById(id: Long): RecurringExpense?
    suspend fun addRecurringExpense(expense: RecurringExpense)
    suspend fun updateRecurringExpense(expense: RecurringExpense)
    suspend fun deleteRecurringExpense(expense: RecurringExpense)
    suspend fun toggleActive(id: Long, isActive: Boolean)
    suspend fun getDueExpenses(currentDay: Int): List<RecurringExpense>
}
