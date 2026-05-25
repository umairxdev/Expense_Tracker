package com.expensetracker.app.presentation.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.model.ExpenseCategory
import com.expensetracker.app.domain.model.RecurringExpense
import com.expensetracker.app.domain.model.RecurringFrequency
import com.expensetracker.app.domain.usecase.ManageRecurringExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecurringUiState(
    val expenses: List<RecurringExpense> = emptyList(),
    val showAddDialog: Boolean = false,
    val isSaving: Boolean = false,
    val editTitle: String = "",
    val editAmount: String = "",
    val editCategory: String = ExpenseCategory.entries.first().name,
    val editFrequency: RecurringFrequency = RecurringFrequency.MONTHLY,
    val editDueDay: Int = 1,
    val editNote: String = ""
)

@HiltViewModel
class RecurringViewModel @Inject constructor(
    private val manageRecurringExpensesUseCase: ManageRecurringExpensesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecurringUiState())
    val uiState: StateFlow<RecurringUiState> = _uiState.asStateFlow()

    val categories = ExpenseCategory.entries.toList()
    val frequencies = RecurringFrequency.entries.toList()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            val expenses = manageRecurringExpensesUseCase.getAll().first()
            _uiState.value = _uiState.value.copy(expenses = expenses)
        }
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(
            showAddDialog = true,
            editTitle = "",
            editAmount = "",
            editCategory = ExpenseCategory.entries.first().name,
            editFrequency = RecurringFrequency.MONTHLY,
            editDueDay = 1,
            editNote = ""
        )
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun setEditTitle(title: String) {
        _uiState.value = _uiState.value.copy(editTitle = title)
    }

    fun setEditAmount(amount: String) {
        _uiState.value = _uiState.value.copy(editAmount = amount)
    }

    fun setEditCategory(category: String) {
        _uiState.value = _uiState.value.copy(editCategory = category)
    }

    fun setEditFrequency(frequency: RecurringFrequency) {
        _uiState.value = _uiState.value.copy(editFrequency = frequency)
    }

    fun setEditDueDay(day: Int) {
        _uiState.value = _uiState.value.copy(editDueDay = day.coerceIn(1, 31))
    }

    fun setEditNote(note: String) {
        _uiState.value = _uiState.value.copy(editNote = note)
    }

    fun saveExpense() {
        val state = _uiState.value
        val amount = state.editAmount.toDoubleOrNull() ?: return
        if (state.editTitle.isBlank()) return

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            val expense = RecurringExpense(
                title = state.editTitle,
                amount = amount,
                category = state.editCategory,
                frequency = state.editFrequency,
                dueDay = state.editDueDay,
                note = state.editNote
            )
            manageRecurringExpensesUseCase.add(expense)
            _uiState.value = _uiState.value.copy(
                showAddDialog = false,
                isSaving = false
            )
            loadExpenses()
        }
    }

    fun toggleActive(id: Long, isActive: Boolean) {
        viewModelScope.launch {
            manageRecurringExpensesUseCase.toggleActive(id, isActive)
            loadExpenses()
        }
    }

    fun deleteExpense(expense: RecurringExpense) {
        viewModelScope.launch {
            manageRecurringExpensesUseCase.delete(expense)
            loadExpenses()
        }
    }
}
