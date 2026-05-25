package com.expensetracker.app.data.repository

import com.expensetracker.app.data.local.dao.RecurringExpenseDao
import com.expensetracker.app.data.local.entity.RecurringExpenseEntity
import com.expensetracker.app.domain.model.RecurringExpense
import com.expensetracker.app.domain.model.RecurringFrequency
import com.expensetracker.app.domain.repository.RecurringExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecurringExpenseRepositoryImpl @Inject constructor(
    private val dao: RecurringExpenseDao
) : RecurringExpenseRepository {

    private fun RecurringExpenseEntity.toDomain() = RecurringExpense(
        id = id,
        title = title,
        amount = amount,
        category = category,
        frequency = RecurringFrequency.valueOf(frequency),
        dueDay = dueDay,
        isActive = isActive,
        note = note,
        lastProcessedDate = lastProcessedDate
    )

    private fun RecurringExpense.toEntity() = RecurringExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category,
        frequency = frequency.name,
        dueDay = dueDay,
        isActive = isActive,
        note = note,
        lastProcessedDate = lastProcessedDate
    )

    override fun getAllRecurringExpenses(): Flow<List<RecurringExpense>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getActiveRecurringExpenses(): Flow<List<RecurringExpense>> =
        dao.getActive().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getRecurringExpenseById(id: Long): RecurringExpense? =
        dao.getById(id)?.toDomain()

    override suspend fun addRecurringExpense(expense: RecurringExpense) {
        dao.insert(expense.toEntity())
    }

    override suspend fun updateRecurringExpense(expense: RecurringExpense) {
        dao.update(expense.toEntity())
    }

    override suspend fun deleteRecurringExpense(expense: RecurringExpense) {
        dao.delete(expense.toEntity())
    }

    override suspend fun toggleActive(id: Long, isActive: Boolean) {
        dao.toggleActive(id, isActive)
    }

    override suspend fun getDueExpenses(currentDay: Int): List<RecurringExpense> =
        dao.getDueByDay(currentDay).map { it.toDomain() }
}
