package com.expensetracker.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring_expenses")
data class RecurringExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val frequency: String,
    val dueDay: Int,
    val isActive: Boolean = true,
    val note: String = "",
    val lastProcessedDate: Long? = null
)
