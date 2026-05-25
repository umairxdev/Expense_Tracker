package com.expensetracker.app.presentation.reports

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.utils.CurrencyUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportGeneratorScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportGeneratorViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri: Uri? ->
        if (uri != null) viewModel.generatePdf(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
            }
            Text(
                text = "Generate Report",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select Date Range",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )

            DateSelector(
                label = "Start Date",
                date = state.startDate,
                dateFormat = dateFormat,
                onClick = { showStartPicker = true }
            )

            DateSelector(
                label = "End Date",
                date = state.endDate,
                dateFormat = dateFormat,
                onClick = { showEndPicker = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { pdfLauncher.launch("expense_report.pdf") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldGreen,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                enabled = !state.isGenerating
            ) {
                if (state.isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.background,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Filled.PictureAsPdf,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Generate PDF Report",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "The report includes income & expense summary, daily trend chart, and all transactions within the selected date range.",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }

    if (showStartPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.startDate
        )
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { viewModel.setStartDate(it) }
                    showStartPicker = false
                }) { Text("Set", color = EmeraldGreen) }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            },
            colors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                headlineContentColor = MaterialTheme.colorScheme.onSurface,
                weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                yearContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                currentYearContentColor = EmeraldGreen,
                selectedYearContentColor = MaterialTheme.colorScheme.background,
                selectedDayContentColor = MaterialTheme.colorScheme.background,
                selectedDayContainerColor = EmeraldGreen,
                dayContentColor = MaterialTheme.colorScheme.onSurface,
                todayContentColor = EmeraldGreen,
                todayDateBorderColor = EmeraldGreen
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.endDate
        )
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { viewModel.setEndDate(it) }
                    showEndPicker = false
                }) { Text("Set", color = EmeraldGreen) }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            },
            colors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                headlineContentColor = MaterialTheme.colorScheme.onSurface,
                weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                yearContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                currentYearContentColor = EmeraldGreen,
                selectedYearContentColor = MaterialTheme.colorScheme.background,
                selectedDayContentColor = MaterialTheme.colorScheme.background,
                selectedDayContainerColor = EmeraldGreen,
                dayContentColor = MaterialTheme.colorScheme.onSurface,
                todayContentColor = EmeraldGreen,
                todayDateBorderColor = EmeraldGreen
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (state.showResult) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissResult() },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    if (state.resultMessage.contains("success")) "Success" else "Error",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            },
            text = { Text(state.resultMessage, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissResult() }) {
                    Text("OK", color = EmeraldGreen)
                }
            }
        )
    }
}

@Composable
private fun DateSelector(
    label: String,
    date: Long,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.CalendarMonth,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = dateFormat.format(Date(date)),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
