package com.expensetracker.app.data.repository

import com.expensetracker.app.data.local.dao.BudgetDao
import com.expensetracker.app.data.local.entity.BudgetEntity
import com.expensetracker.app.domain.model.Budget
import com.expensetracker.app.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {

    private fun BudgetEntity.toDomain() = Budget(
        id = id,
        category = category,
        amount = amount,
        spent = spent,
        month = month,
        year = year
    )

    private fun Budget.toEntity() = BudgetEntity(
        id = id,
        category = category,
        amount = amount,
        spent = spent,
        month = month,
        year = year
    )

    override fun getAllBudgets(): Flow<List<Budget>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getBudgetsByMonth(month: Int, year: Int): Flow<List<Budget>> =
        dao.getByMonth(month, year).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getBudgetByCategory(category: String, month: Int, year: Int): Budget? =
        dao.getByCategory(category, month, year)?.toDomain()

    override suspend fun addBudget(budget: Budget) {
        dao.insert(budget.toEntity())
    }

    override suspend fun updateBudget(budget: Budget) {
        dao.update(budget.toEntity())
    }

    override suspend fun deleteBudget(budget: Budget) {
        dao.delete(budget.toEntity())
    }

    override suspend fun updateSpent(id: Long, spent: Double) {
        dao.updateSpent(id, spent)
    }
}
