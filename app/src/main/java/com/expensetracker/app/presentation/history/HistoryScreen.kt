package com.expensetracker.app.presentation.history

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.ui.components.EmptyState
import com.expensetracker.app.ui.components.TransactionItem

@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var deleteTarget by remember { mutableStateOf<Transaction?>(null) }

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
                text = "History",
                color = SoftWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            // Search
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Search transactions...", color = MutedWhite.copy(alpha = 0.5f))
                },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = MutedWhite)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreen,
                    unfocusedBorderColor = CharcoalGray,
                    cursorColor = EmeraldGreen,
                    focusedTextColor = SoftWhite,
                    unfocusedTextColor = SoftWhite
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val chips = listOf("All", "Food", "Bills", "Shopping", "Salary")
                chips.forEach { chip ->
                    val isSelected = state.selectedFilter == chip
                    Text(
                        text = chip,
                        color = if (isSelected) MatteBlack else MutedWhite,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) EmeraldGreen else CharcoalGray
                            )
                            .clickable { viewModel.setFilter(chip) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.filteredTransactions.isEmpty()) {
                EmptyState(
                    title = "No transactions found",
                    subtitle = "Try adjusting your search or filters",
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.filteredTransactions,
                        key = { it.id }
                    ) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            modifier = Modifier.fillMaxWidth(),
                            onDelete = { deleteTarget = transaction }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (deleteTarget != null) {
        val txn = deleteTarget!!
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            containerColor = DarkCardElevated,
            title = {
                Text("Delete Transaction", color = SoftWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Delete ${CurrencyUtils.format(txn.amount)} ${txn.category.lowercase().replace("_", " ")} from ${txn.type.name.lowercase().replaceFirstChar { it.uppercase() }}? This cannot be undone.",
                    color = MutedWhite
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTransaction(txn)
                    deleteTarget = null
                }) {
                    Text("Delete", color = ExpenseRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("Cancel", color = MutedWhite)
                }
            }
        )
    }
}
