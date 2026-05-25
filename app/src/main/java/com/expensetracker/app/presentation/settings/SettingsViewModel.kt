package com.expensetracker.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.local.dao.BudgetDao
import com.expensetracker.app.data.local.dao.CategoryDao
import com.expensetracker.app.data.local.dao.RecurringExpenseDao
import com.expensetracker.app.data.local.dao.TransactionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val appVersion: String = "1.0.0",
    val showResetConfirm: Boolean = false,
    val isResetting: Boolean = false,
    val showResetResult: Boolean = false,
    val resetMessage: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val transactionDao: TransactionDao,
    private val recurringExpenseDao: RecurringExpenseDao,
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun showResetConfirm() {
        _uiState.value = _uiState.value.copy(showResetConfirm = true)
    }

    fun dismissResetConfirm() {
        _uiState.value = _uiState.value.copy(showResetConfirm = false)
    }

    fun dismissResetResult() {
        _uiState.value = _uiState.value.copy(showResetResult = false)
    }

    fun confirmReset() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isResetting = true, showResetConfirm = false)
            try {
                transactionDao.deleteAll()
                recurringExpenseDao.deleteAll()
                budgetDao.deleteAll()
                categoryDao.deleteNonDefault()
                _uiState.value = _uiState.value.copy(
                    isResetting = false,
                    showResetResult = true,
                    resetMessage = "All app data has been cleared."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isResetting = false,
                    showResetResult = true,
                    resetMessage = "Failed to reset: ${e.message}"
                )
            }
        }
    }
}
