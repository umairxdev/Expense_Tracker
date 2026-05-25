package com.expensetracker.app.presentation.categories

import androidx.lifecycle.ViewModel
import com.expensetracker.app.domain.model.ExpenseCategory
import com.expensetracker.app.domain.model.IncomeCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class CategoryItem(
    val name: String,
    val displayName: String,
    val isExpense: Boolean
)

@HiltViewModel
class CategoriesViewModel @Inject constructor() : ViewModel() {

    val categories = ExpenseCategory.entries.map {
        CategoryItem(it.name, it.displayName, true)
    } + IncomeCategory.entries.map {
        CategoryItem(it.name, it.displayName, false)
    }
}
