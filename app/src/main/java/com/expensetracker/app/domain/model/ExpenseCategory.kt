package com.expensetracker.app.domain.model

enum class ExpenseCategory(override val displayName: String, override val icon: String) : Category {
    GROCERIES("Groceries", "cart"),
    BILLS("Bills", "receipt"),
    FOOD("Food", "restaurant"),
    SHOPPING("Shopping", "shopping_bag"),
    TRANSPORT("Transport", "directions_car"),
    RENT("Rent", "home"),
    ENTERTAINMENT("Entertainment", "movie"),
    HEALTHCARE("Healthcare", "local_hospital"),
    TRAVEL("Travel", "flight"),
    EDUCATION("Education", "school"),
    UTILITIES("Utilities", "bolt"),
    OTHER("Other", "more_horiz")
}
