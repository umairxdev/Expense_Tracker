package com.expensetracker.app.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.ExpenseCategory
import com.expensetracker.app.domain.model.IncomeCategory
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class AddTransactionUiState(
    val amount: String = "",
    val selectedCategory: String = "",
    val note: String = "",
    val date: Long = System.currentTimeMillis(),
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    val expenseCategories: List<Category> = ExpenseCategory.entries.toList()
    val incomeCategories: List<Category> = IncomeCategory.entries.toList()

    fun setType(type: TransactionType) {
        _uiState.value = _uiState.value.copy(
            transactionType = type,
            selectedCategory = ""
        )
    }

    fun setAmount(amount: String) {
        if (amount.length <= 12) {
            _uiState.value = _uiState.value.copy(amount = amount)
        }
    }

    fun setCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setNote(note: String) {
        _uiState.value = _uiState.value.copy(note = note)
    }

    fun setDate(dateMillis: Long) {
        _uiState.value = _uiState.value.copy(date = dateMillis)
    }

    fun save() {
        val state = _uiState.value
        val amountValue = state.amount.toDoubleOrNull()

        if (amountValue == null || amountValue <= 0) {
            _uiState.value = state.copy(error = "Please enter a valid amount")
            return
        }

        if (state.selectedCategory.isEmpty()) {
            _uiState.value = state.copy(error = "Please select a category")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, error = null)

            val transaction = Transaction(
                amount = amountValue,
                type = state.transactionType,
                category = state.selectedCategory,
                date = state.date,
                note = state.note
            )

            try {
                addTransactionUseCase(transaction)
                _uiState.value = _uiState.value.copy(isSaving = false, isSaved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Failed to save transaction"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
