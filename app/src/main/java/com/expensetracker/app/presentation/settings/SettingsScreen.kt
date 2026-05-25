package com.expensetracker.app.presentation.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expensetracker.app.core.theme.CharcoalGray
import com.expensetracker.app.core.theme.DarkCardElevated
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.MatteBlack
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToRecurring: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToReportGenerator: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        if (uri != null) viewModel.exportToUri(uri)
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) viewModel.importFromUri(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatteBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = SoftWhite)
            }
            Text(
                text = "Settings",
                color = SoftWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$",
                        color = EmeraldGreen,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "ExpenseTracker",
                        color = SoftWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "v${state.appVersion}",
                        color = MutedWhite,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsMenuItem(
                icon = Icons.Filled.Category,
                label = "Manage Categories",
                onClick = onNavigateToCategories
            )
            SettingsMenuItem(
                icon = Icons.Filled.Repeat,
                label = "Recurring Payments",
                onClick = onNavigateToRecurring
            )
            SettingsMenuItem(
                icon = Icons.Filled.Description,
                label = "Reports",
                onClick = onNavigateToReports
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "DATA MANAGEMENT",
                color = MutedWhite.copy(alpha = 0.6f),
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingsMenuItem(
                icon = Icons.Filled.FileDownload,
                label = if (state.isExporting) "Exporting..." else "Export Backup",
                onClick = { exportLauncher.launch("expense_tracker_backup.json") },
                enabled = !state.isExporting && !state.isImporting
            )
            SettingsMenuItem(
                icon = Icons.Filled.FileUpload,
                label = if (state.isImporting) "Importing..." else "Import Backup",
                onClick = { importLauncher.launch(arrayOf("application/json", "*/*")) },
                enabled = !state.isExporting && !state.isImporting
            )
            SettingsMenuItem(
                icon = Icons.Filled.Backup,
                label = "Share Backup",
                onClick = { viewModel.shareBackup() },
                enabled = !state.isExporting
            )
            SettingsMenuItem(
                icon = Icons.Filled.Restore,
                label = "Generate PDF Report",
                onClick = onNavigateToReportGenerator
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PRIVACY",
                color = MutedWhite.copy(alpha = 0.6f),
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingsMenuItem(
                icon = Icons.Filled.Shield,
                label = "Privacy Policy",
                onClick = { }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "All data is stored locally on your device.\nNo internet connection required.\n100% private and secure.",
                color = MutedWhite.copy(alpha = 0.6f),
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }
    }

    if (state.showBackupResult) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissBackupResult() },
            containerColor = DarkCardElevated,
            title = {
                Text(
                    if (state.backupMessage.startsWith("Restored") || state.backupMessage.contains("success"))
                        "Success" else "Error",
                    color = SoftWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = { Text(state.backupMessage, color = MutedWhite) },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissBackupResult() }) {
                    Text("OK", color = EmeraldGreen)
                }
            }
        )
    }
}

@Composable
private fun SettingsMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCardElevated)
            .then(
                if (enabled) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EmeraldGreen,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = if (enabled) SoftWhite else MutedWhite.copy(alpha = 0.5f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (enabled) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MutedWhite,
                modifier = Modifier.size(20.dp)
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = EmeraldGreen,
                strokeWidth = 2.dp
            )
        }
    }
}
