package com.expensetracker.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.expensetracker.app.data.local.entity.RecurringExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringExpenseDao {

    @Query("SELECT * FROM recurring_expenses ORDER BY dueDay ASC")
    fun getAll(): Flow<List<RecurringExpenseEntity>>

    @Query("SELECT * FROM recurring_expenses WHERE isActive = 1 ORDER BY dueDay ASC")
    fun getActive(): Flow<List<RecurringExpenseEntity>>

    @Query("SELECT * FROM recurring_expenses WHERE id = :id")
    suspend fun getById(id: Long): RecurringExpenseEntity?

    @Query("SELECT * FROM recurring_expenses WHERE isActive = 1 AND dueDay = :day")
    suspend fun getDueByDay(day: Int): List<RecurringExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: RecurringExpenseEntity): Long

    @Update
    suspend fun update(expense: RecurringExpenseEntity)

    @Delete
    suspend fun delete(expense: RecurringExpenseEntity)

    @Query("UPDATE recurring_expenses SET isActive = :isActive WHERE id = :id")
    suspend fun toggleActive(id: Long, isActive: Boolean)
}
