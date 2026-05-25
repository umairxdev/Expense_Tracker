package com.expensetracker.app.presentation.transaction

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.ui.components.CategoryIcon

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTransactionScreen(
    initialType: String = "EXPENSE",
    onNavigateBack: () -> Unit,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val incomeCategories by viewModel.incomeCategories.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(initialType) {
        val type = try {
            TransactionType.valueOf(initialType)
        } catch (e: Exception) {
            TransactionType.EXPENSE
        }
        viewModel.setType(type)
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatteBlack)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = SoftWhite
                )
            }
            Text(
                text = if (state.transactionType == TransactionType.EXPENSE) "Add Expense" else "Add Income",
                color = SoftWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { viewModel.save() },
                enabled = !state.isSaving
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                    tint = EmeraldGreen
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkCardElevated)
                    .padding(4.dp)
            ) {
                val types = listOf(TransactionType.EXPENSE, TransactionType.INCOME)
                types.forEach { type ->
                    val isSelected = state.transactionType == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) EmeraldGreen else Color.Transparent)
                            .clickable { viewModel.setType(type) },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (type == TransactionType.EXPENSE)
                                    Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = if (isSelected) MatteBlack else MutedWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                    text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                    color = if (isSelected) MatteBlack else MutedWhite,
                    fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Amount",
                color = MutedWhite,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.setAmount(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "0.00",
                        color = MutedWhite.copy(alpha = 0.5f),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = SoftWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreen,
                    unfocusedBorderColor = CharcoalGray,
                    cursorColor = EmeraldGreen
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Category",
                color = MutedWhite,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            val categories = if (state.transactionType == TransactionType.EXPENSE)
                expenseCategories else incomeCategories

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = state.selectedCategory == category.name
                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) EmeraldGreen.copy(alpha = 0.15f) else DarkCardElevated,
                        animationSpec = tween(200),
                        label = "catBg"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) EmeraldGreen.copy(alpha = 0.5f) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.setCategory(category.name) }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CategoryIcon(
                                category = category.name,
                                size = 28.dp,
                                iconSize = 14.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                text = category.displayName,
                color = if (isSelected) EmeraldGreen else SoftWhite,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Note (optional)",
                color = MutedWhite,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.note,
                onValueChange = { viewModel.setNote(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = {
                    Text("Add a note...", color = MutedWhite.copy(alpha = 0.5f))
                },
                textStyle = androidx.compose.ui.text.TextStyle(color = SoftWhite),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreen,
                    unfocusedBorderColor = CharcoalGray,
                    cursorColor = EmeraldGreen
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.save() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldGreen,
                    contentColor = MatteBlack
                ),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MatteBlack,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                text = "Save Transaction",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                    )
                }
            }

            if (state.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.error!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    if (state.showLowBalanceWarning) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLowBalanceWarning() },
            containerColor = DarkCardElevated,
            icon = {
                Icon(Icons.Filled.Warning, contentDescription = null, tint = ExpenseRed, modifier = Modifier.size(32.dp))
            },
            title = {
                Text("Low Balance Warning", color = SoftWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        "This expense of ${CurrencyUtils.format(state.amount.toDoubleOrNull() ?: 0.0)} exceeds your available balance of ${CurrencyUtils.format(state.currentBalance)}.",
                        color = MutedWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your balance may go negative. Save anyway?",
                        color = MutedWhite.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.saveAfterWarning() }) {
                    Text("Save Anyway", color = ExpenseRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissLowBalanceWarning() }) {
                    Text("Cancel", color = MutedWhite)
                }
            }
        )
    }
}
