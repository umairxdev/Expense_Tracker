package com.expensetracker.app.presentation.recurring

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expensetracker.app.core.theme.CharcoalGray
import com.expensetracker.app.core.theme.DarkCardElevated
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.ExpenseRed
import com.expensetracker.app.core.theme.MatteBlack
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite
import com.expensetracker.app.core.utils.CurrencyUtils
import com.expensetracker.app.domain.model.RecurringExpense
import com.expensetracker.app.domain.model.RecurringFrequency
import com.expensetracker.app.ui.components.AnimatedCard
import com.expensetracker.app.ui.components.CategoryIcon
import com.expensetracker.app.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecurringViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var insufficientPair by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { message ->
            if (message.startsWith("INSUFFICIENT_BALANCE:")) {
                val parts = message.split(":")
                if (parts.size >= 3) {
                    val amount = parts[1].toDoubleOrNull() ?: return@collect
                    val balance = parts[2].toDoubleOrNull() ?: return@collect
                    insufficientPair = Pair(amount, balance)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MatteBlack)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = SoftWhite)
            }
            Text(
                text = "Recurring Payments",
                color = SoftWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { viewModel.showAddDialog() }) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = EmeraldGreen)
            }
        }

        if (state.expenses.isEmpty()) {
            EmptyState(
                title = "No recurring payments",
                subtitle = "Tap + to add bills, rent, or subscriptions"
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.expenses, key = { it.id }) { expense ->
                    RecurringCard(
                        expense = expense,
                        onTap = { viewModel.quickAddTransaction(expense) },
                        onToggleActive = { viewModel.toggleActive(expense.id, !expense.isActive) },
                        onDelete = { viewModel.deleteExpense(expense) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    if (state.showAddDialog) {
        AddRecurringDialog(viewModel = viewModel)
    }

    if (insufficientPair != null) {
        AlertDialog(
            onDismissRequest = { insufficientPair = null },
            containerColor = DarkCardElevated,
            icon = {
                Icon(Icons.Filled.Warning, contentDescription = null, tint = ExpenseRed, modifier = Modifier.size(32.dp))
            },
            title = {
                Text("Insufficient Balance", color = SoftWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                val (amount, balance) = insufficientPair!!
                Column {
                    Text(
                        "${CurrencyUtils.format(amount)} exceeds your available balance of ${CurrencyUtils.format(balance)}.",
                        color = MutedWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Balance cannot go negative. Add income first.",
                        color = MutedWhite.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { insufficientPair = null }) {
                    Text("OK", color = EmeraldGreen)
                }
            }
        )
    }
}

@Composable
private fun RecurringCard(
    expense: RecurringExpense,
    onTap: () -> Unit,
    onToggleActive: () -> Unit,
    onDelete: () -> Unit
) {
    AnimatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onTap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(category = expense.category)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    color = SoftWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${expense.frequency.displayName} \u2022 Due day ${expense.dueDay}",
                    color = MutedWhite,
                    fontSize = 12.sp
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyUtils.format(expense.amount),
                    color = ExpenseRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (expense.isActive) "Active" else "Paused",
                    color = if (expense.isActive) EmeraldGreen else MutedWhite,
                    fontSize = 11.sp
                )
            }
            IconButton(
                onClick = onToggleActive,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (expense.isActive) Icons.Filled.PauseCircle else Icons.Filled.CheckCircle,
                    contentDescription = if (expense.isActive) "Pause" else "Activate",
                    tint = if (expense.isActive) MutedWhite else EmeraldGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddRecurringDialog(viewModel: RecurringViewModel) {
    val state by viewModel.uiState.collectAsState()
    var categoryExpanded by remember { androidx.compose.runtime.mutableStateOf(false) }
    var frequencyExpanded by remember { androidx.compose.runtime.mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { viewModel.hideAddDialog() },
        containerColor = DarkCardElevated,
        title = {
            Text("Add Recurring", color = SoftWhite, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = state.editTitle,
                    onValueChange = { viewModel.setEditTitle(it) },
                    label = { Text("Title", color = MutedWhite) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGreen,
                        unfocusedBorderColor = CharcoalGray,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.editAmount,
                    onValueChange = { viewModel.setEditAmount(it) },
                    label = { Text("Amount", color = MutedWhite) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGreen,
                        unfocusedBorderColor = CharcoalGray,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = state.editCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category", color = MutedWhite) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = CharcoalGray,
                            focusedTextColor = SoftWhite,
                            unfocusedTextColor = SoftWhite
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        viewModel.categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName, color = SoftWhite) },
                                onClick = {
                                    viewModel.setEditCategory(cat.name)
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = frequencyExpanded,
                    onExpandedChange = { frequencyExpanded = it }
                ) {
                    OutlinedTextField(
                        value = state.editFrequency.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Frequency", color = MutedWhite) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = CharcoalGray,
                            focusedTextColor = SoftWhite,
                            unfocusedTextColor = SoftWhite
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = frequencyExpanded,
                        onDismissRequest = { frequencyExpanded = false }
                    ) {
                        viewModel.frequencies.forEach { freq ->
                            DropdownMenuItem(
                                text = { Text(freq.displayName, color = SoftWhite) },
                                onClick = {
                                    viewModel.setEditFrequency(freq)
                                    frequencyExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.editDueDay.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { day ->
                            viewModel.setEditDueDay(day)
                        }
                    },
                    label = { Text("Due Day (1-31)", color = MutedWhite) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGreen,
                        unfocusedBorderColor = CharcoalGray,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.saveExpense() },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save", color = MatteBlack)
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.hideAddDialog() }) {
                Text("Cancel", color = MutedWhite)
            }
        }
    )
}
