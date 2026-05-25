package com.expensetracker.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.model.ExpenseCategory
import com.expensetracker.app.domain.model.IncomeCategory
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val transactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val isLoading: Boolean = true
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    val filterOptions = listOf("All") +
            ExpenseCategory.entries.map { it.displayName } +
            IncomeCategory.entries.map { it.displayName }

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val transactions = transactionRepository.getAllTransactions().first()
            _uiState.value = HistoryUiState(
                transactions = transactions,
                filteredTransactions = transactions,
                isLoading = false
            )
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun setFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        var filtered = state.transactions

        if (state.selectedFilter != "All") {
            filtered = filtered.filter { transaction ->
                try {
                    val expenseCat = ExpenseCategory.valueOf(transaction.category.uppercase())
                    expenseCat.displayName == state.selectedFilter
                } catch (e: IllegalArgumentException) {
                    try {
                        val incomeCat = IncomeCategory.valueOf(transaction.category.uppercase())
                        incomeCat.displayName == state.selectedFilter
                    } catch (e2: IllegalArgumentException) {
                        false
                    }
                }
            }
        }

        if (state.searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.note.contains(state.searchQuery, ignoreCase = true) ||
                        it.amount.toString().contains(state.searchQuery) ||
                        it.category.contains(state.searchQuery, ignoreCase = true)
            }
        }

        _uiState.value = state.copy(filteredTransactions = filtered)
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
            loadTransactions()
        }
    }
}
