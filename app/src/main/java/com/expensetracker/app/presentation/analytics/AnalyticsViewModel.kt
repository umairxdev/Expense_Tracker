package com.expensetracker.app.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.model.CategorySummary
import com.expensetracker.app.domain.model.SpendingTrend
import com.expensetracker.app.domain.usecase.GetReportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0,
    val categorySummaries: List<CategorySummary> = emptyList(),
    val spendingTrends: List<SpendingTrend> = emptyList(),
    val selectedMonth: YearMonth = YearMonth.now()
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getReportsUseCase: GetReportsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val currentMonth = YearMonth.now()
                val report = getReportsUseCase.getMonthlyReport(
                    currentMonth.monthValue, currentMonth.year
                )
                val trends = getReportsUseCase.getSpendingTrends(6)

                _uiState.value = AnalyticsUiState(
                    isLoading = false,
                    totalIncome = report.totalIncome,
                    totalExpense = report.totalExpense,
                    balance = report.balance,
                    categorySummaries = report.categorySummaries,
                    spendingTrends = trends,
                    selectedMonth = currentMonth
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
