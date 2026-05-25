package com.expensetracker.app.presentation.recurring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expensetracker.app.core.theme.CharcoalGray
import com.expensetracker.app.core.theme.DarkCard
import com.expensetracker.app.core.theme.DarkCardElevated
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.ExpenseRed
import com.expensetracker.app.core.theme.MatteBlack
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite
import com.expensetracker.app.core.utils.CurrencyUtils
import com.expensetracker.app.ui.components.AnimatedCard
import com.expensetracker.app.ui.components.CategoryIcon
import com.expensetracker.app.ui.components.EmptyState
import kotlin.math.roundToInt

@Composable
fun RecurringScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecurringViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

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
                fontSize = 20.sp,
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
                subtitle = "Add recurring expenses for bills, rent, and subscriptions"
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.expenses, key = { it.id }) { expense ->
                    AnimatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.toggleActive(expense.id, !expense.isActive) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CategoryIcon(category = expense.category)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = expense.title,
                                    color = SoftWhite,
                                    fontSize = 15.sp,
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
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (expense.isActive) "Active" else "Paused",
                                    color = if (expense.isActive) EmeraldGreen else MutedWhite,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    // Add Dialog
    if (state.showAddDialog) {
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
}
