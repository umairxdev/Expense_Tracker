package com.expensetracker.app.domain.repository

import com.expensetracker.app.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllBudgets(): Flow<List<Budget>>
    fun getBudgetsByMonth(month: Int, year: Int): Flow<List<Budget>>
    suspend fun getBudgetByCategory(category: String, month: Int, year: Int): Budget?
    suspend fun addBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
    suspend fun updateSpent(id: Long, spent: Double)
}
