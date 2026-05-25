package com.expensetracker.app.domain.model

data class RecurringExpense(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val frequency: RecurringFrequency,
    val dueDay: Int,
    val isActive: Boolean = true,
    val note: String = "",
    val lastProcessedDate: Long? = null
)

enum class RecurringFrequency(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    BIWEEKLY("Biweekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly")
}
