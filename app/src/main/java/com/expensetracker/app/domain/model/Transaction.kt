package com.expensetracker.app.domain.model

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val date: Long,
    val note: String = "",
    val isRecurring: Boolean = false,
    val recurringId: Long? = null
)
