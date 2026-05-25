package com.expensetracker.app.domain.repository

import com.expensetracker.app.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll(): Flow<List<CategoryEntity>>
    fun getByType(type: String): Flow<List<CategoryEntity>>
    suspend fun getByTypeOnce(type: String): List<CategoryEntity>
    suspend fun addCategory(name: String, displayName: String, type: String)
    suspend fun deleteCategory(name: String)
    suspend fun seedDefaults()
}
