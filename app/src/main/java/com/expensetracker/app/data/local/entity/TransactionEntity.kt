package com.expensetracker.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: String,
    val category: String,
    val date: Long,
    val note: String = "",
    val isRecurring: Boolean = false,
    val recurringId: Long? = null
)
