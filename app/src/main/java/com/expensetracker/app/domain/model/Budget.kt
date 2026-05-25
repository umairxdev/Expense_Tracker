package com.expensetracker.app.domain.model

data class Budget(
    val id: Long = 0,
    val category: String,
    val amount: Double,
    val spent: Double = 0.0,
    val month: Int,
    val year: Int
) {
    val percentage: Double
        get() = if (amount > 0) (spent / amount).coerceAtMost(1.0) else 0.0

    val remaining: Double
        get() = (amount - spent).coerceAtLeast(0.0)

    val isExceeded: Boolean
        get() = spent > amount
}
