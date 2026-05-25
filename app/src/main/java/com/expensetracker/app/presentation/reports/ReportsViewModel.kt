package com.expensetracker.app.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.model.MonthlyReport
import com.expensetracker.app.domain.model.SpendingTrend
import com.expensetracker.app.domain.usecase.GetReportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class ReportsUiState(
    val isLoading: Boolean = true,
    val reports: List<MonthlyReport> = emptyList(),
    val trends: List<SpendingTrend> = emptyList()
)

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getReportsUseCase: GetReportsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        loadReports()
    }

    fun loadReports() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val current = YearMonth.now()
                val reports = mutableListOf<MonthlyReport>()

                for (i in 0 until 6) {
                    val ym = current.minusMonths(i.toLong())
                    val report = getReportsUseCase.getMonthlyReport(
                        ym.monthValue, ym.year
                    )
                    reports.add(report)
                }

                val trends = getReportsUseCase.getSpendingTrends(12)

                _uiState.value = ReportsUiState(
                    isLoading = false,
                    reports = reports,
                    trends = trends
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
