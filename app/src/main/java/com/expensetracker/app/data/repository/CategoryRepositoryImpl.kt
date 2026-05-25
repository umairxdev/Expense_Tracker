package com.expensetracker.app.data.repository

import com.expensetracker.app.data.local.dao.CategoryDao
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.domain.model.ExpenseCategory
import com.expensetracker.app.domain.model.IncomeCategory
import com.expensetracker.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository {

    override fun getAll(): Flow<List<CategoryEntity>> = dao.getAll()

    override fun getByType(type: String): Flow<List<CategoryEntity>> = dao.getByType(type)

    override suspend fun getByTypeOnce(type: String): List<CategoryEntity> = dao.getByTypeOnce(type)

    override suspend fun addCategory(name: String, displayName: String, type: String) {
        dao.insert(CategoryEntity(name = name, displayName = displayName, type = type, isDefault = false))
    }

    override suspend fun deleteCategory(name: String) {
        dao.deleteByName(name)
    }

    override suspend fun seedDefaults() {
        if (dao.count() > 0) return
        val defaults = ExpenseCategory.entries.map {
            CategoryEntity(name = it.name, displayName = it.displayName, type = "EXPENSE", isDefault = true)
        } + IncomeCategory.entries.map {
            CategoryEntity(name = it.name, displayName = it.displayName, type = "INCOME", isDefault = true)
        }
        dao.insertAll(defaults)
    }
}
