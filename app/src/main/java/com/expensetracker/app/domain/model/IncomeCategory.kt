package com.expensetracker.app.domain.model

enum class IncomeCategory(override val displayName: String, override val icon: String) : Category {
    SALARY("Salary", "work"),
    FREELANCE("Freelance", "laptop"),
    BUSINESS("Business", "business"),
    INVESTMENTS("Investments", "trending_up"),
    RENT("Rent", "home"),
    OTHER("Other", "more_horiz")
}
