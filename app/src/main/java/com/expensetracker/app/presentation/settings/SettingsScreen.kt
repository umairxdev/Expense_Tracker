package com.expensetracker.app.presentation.settings

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.ExpenseRed
import com.expensetracker.app.ui.components.PockitLogo
import com.expensetracker.app.core.utils.CurrencyUtils

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
    var showPrivacy by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
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
                text = "Settings",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                PockitLogo(
                    modifier = Modifier,
                    sizeDp = 56
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Pockit",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "v${state.appVersion}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
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
                text = "THEME",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            val themeLabel = when (state.themeMode) {
                "SYSTEM" -> "System Default"
                "LIGHT" -> "Light"
                else -> "Dark"
            }
            SettingsMenuItem(
                icon = Icons.Filled.Palette,
                label = "App Theme: $themeLabel",
                onClick = { showThemePicker = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CURRENCY",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingsMenuItem(
                icon = Icons.Filled.AttachMoney,
                label = "Currency: ${state.selectedCurrency}",
                onClick = { viewModel.showCurrencyPicker() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "DATA MANAGEMENT",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingsMenuItem(
                icon = Icons.Filled.Description,
                label = "Generate PDF Report",
                onClick = onNavigateToReportGenerator
            )
            SettingsMenuItem(
                icon = Icons.Filled.DeleteForever,
                label = "Reset All Data",
                onClick = { viewModel.showResetConfirm() },
                tintColor = ExpenseRed
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PRIVACY",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingsMenuItem(
                icon = Icons.Filled.Shield,
                label = "Privacy Policy",
                onClick = { showPrivacy = true }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "All data is stored locally on your device.\nNo internet connection required.\n100% private and secure.",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }

    if (state.showResetConfirm) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissResetConfirm() },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text("Reset All Data", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "This will permanently delete all transactions, budgets, recurring payments, and custom categories. This action cannot be undone.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmReset() }) {
                    Text("Yes, Reset Everything", color = ExpenseRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissResetConfirm() }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    if (state.showResetResult) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissResetResult() },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text("Done", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            },
            text = { Text(state.resetMessage, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissResetResult() }) {
                    Text("OK", color = EmeraldGreen)
                }
            }
        )
    }

    if (showPrivacy) {
        AlertDialog(
            onDismissRequest = { showPrivacy = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text("Privacy Policy", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        "Your data belongs to you.",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Pockit does not collect, store, or transmit any personal data. " +
                                "All your financial information, transactions, budgets, and settings are stored " +
                                        "exclusively on your device in a local database.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                            "No internet connection is required to use this app. " +
                                        "We do not use analytics services, crash reporting, or third-party SDKs " +
                                        "that access your data.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                            "If you choose to export or share data (backup or PDF), " +
                                        "that action is initiated by you and controlled entirely by your device's share system.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacy = false }) {
                    Text("OK", color = EmeraldGreen)
                }
            }
        )
    }

    if (showThemePicker) {
        AlertDialog(
            onDismissRequest = { showThemePicker = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text("App Theme", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    listOf(
                        Triple("SYSTEM", Icons.Filled.SettingsBrightness, "Follow your device settings"),
                        Triple("LIGHT", Icons.Filled.LightMode, "Always light theme"),
                        Triple("DARK", Icons.Filled.DarkMode, "Always dark theme")
                    ).forEach { (mode, icon, desc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .then(
                                    if (state.themeMode == mode) Modifier.background(EmeraldGreen.copy(alpha = 0.1f))
                                    else Modifier
                                )
                                .clickable {
                                    viewModel.setThemeMode(mode)
                                    showThemePicker = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (state.themeMode == mode) EmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = mode.lowercase().replaceFirstChar { it.uppercase() },
                                    color = if (state.themeMode == mode) EmeraldGreen else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp,
                                    fontWeight = if (state.themeMode == mode) FontWeight.SemiBold else FontWeight.Normal
                                )
                                Text(
                                    text = desc,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemePicker = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    if (state.showCurrencyPicker) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCurrencyPicker() },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text("Select Currency", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            },
            text = {
                val currencies = CurrencyUtils.availableCurrencies
                Column(modifier = Modifier.fillMaxWidth()) {
                    currencies.forEach { pair ->
                        val code = pair.first
                        val label = pair.second
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .then(
                                    if (state.selectedCurrency == code) Modifier.background(EmeraldGreen.copy(alpha = 0.1f))
                                    else Modifier
                                )
                                .clickable { viewModel.setCurrency(code) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                color = if (state.selectedCurrency == code) EmeraldGreen else MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                fontWeight = if (state.selectedCurrency == code) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissCurrencyPicker() }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    enabled: Boolean = true,
    tintColor: androidx.compose.ui.graphics.Color = EmeraldGreen
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tintColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}
