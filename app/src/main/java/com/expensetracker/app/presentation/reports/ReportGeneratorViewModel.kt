package com.expensetracker.app.presentation.reports

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.core.utils.PdfReportGenerator
import com.expensetracker.app.core.utils.ReportFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class ReportGeneratorUiState(
    val startDate: Long = LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    val endDate: Long = LocalDate.now().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    val isGenerating: Boolean = false,
    val showResult: Boolean = false,
    val resultMessage: String = ""
)

@HiltViewModel
class ReportGeneratorViewModel @Inject constructor(
    private val pdfReportGenerator: PdfReportGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportGeneratorUiState())
    val uiState: StateFlow<ReportGeneratorUiState> = _uiState.asStateFlow()

    fun setStartDate(millis: Long) {
        _uiState.value = _uiState.value.copy(startDate = millis)
    }

    fun setEndDate(millis: Long) {
        _uiState.value = _uiState.value.copy(endDate = millis)
    }

    fun dismissResult() {
        _uiState.value = _uiState.value.copy(showResult = false)
    }

    fun generatePdf(uri: Uri) {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true)
            try {
                pdfReportGenerator.generate(uri, ReportFilter(
                    startDate = state.startDate,
                    endDate = state.endDate
                ))
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    showResult = true,
                    resultMessage = "PDF report generated successfully!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    showResult = true,
                    resultMessage = "Failed to generate PDF: ${e.message}"
                )
            }
        }
    }
}
