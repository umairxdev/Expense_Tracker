package com.expensetracker.app.presentation.categories

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.material3.MaterialTheme
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.ExpenseRed
import com.expensetracker.app.ui.components.CategoryIcon

@Composable
fun CategoriesScreen(
    onNavigateBack: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                    text = "Categories",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Expense Categories",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(state.categories.filter { it.isExpense }) { item ->
                    CategoryRow(item = item, onDelete = {
                        if (!item.isDefault) viewModel.requestDelete(item)
                    })
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Income Categories",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(state.categories.filter { !it.isExpense }) { item ->
                    CategoryRow(item = item, onDelete = {
                        if (!item.isDefault) viewModel.requestDelete(item)
                    })
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.showAddDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = EmeraldGreen,
            contentColor = MaterialTheme.colorScheme.background
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Category")
        }
    }

    if (state.showAddDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideAddDialog() },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text("Add Category", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = state.addName,
                        onValueChange = { viewModel.setAddName(it) },
                        label = { Text("Category Name", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.addDisplayName,
                        onValueChange = { viewModel.setAddDisplayName(it) },
                        label = { Text("Display Name (optional)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = state.addType == "EXPENSE",
                            onClick = { viewModel.setAddType("EXPENSE") },
                            colors = RadioButtonDefaults.colors(selectedColor = ExpenseRed)
                        )
                        Text("Expense", color = ExpenseRed, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = state.addType == "INCOME",
                            onClick = { viewModel.setAddType("INCOME") },
                            colors = RadioButtonDefaults.colors(selectedColor = EmeraldGreen)
                        )
                        Text("Income", color = EmeraldGreen, fontSize = 14.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.addCategory() }) {
                    Text("Add", color = EmeraldGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideAddDialog() }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    if (state.deleteConfirm != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelDelete() },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text("Delete Category", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Delete \"${state.deleteConfirm!!.displayName}\"? Existing transactions won't be affected.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDelete() }) {
                    Text("Delete", color = ExpenseRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelDelete() }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
private fun CategoryRow(item: CategoryItem, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryIcon(category = item.name, size = 40.dp, iconSize = 20.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.displayName,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (item.isExpense) "Expense" else "Income",
                color = if (item.isExpense) ExpenseRed else EmeraldGreen,
                fontSize = 12.sp
            )
        }
        if (!item.isDefault) {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = ExpenseRed.copy(alpha = 0.7f))
            }
        }
    }
}
