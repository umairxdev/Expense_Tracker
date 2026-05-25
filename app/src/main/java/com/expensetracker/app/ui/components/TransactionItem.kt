package com.expensetracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensetracker.app.core.theme.CharcoalGray
import com.expensetracker.app.core.theme.DarkCard
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.ExpenseRed
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite
import com.expensetracker.app.core.utils.CurrencyUtils
import com.expensetracker.app.core.utils.DateUtils
import com.expensetracker.app.domain.model.ExpenseCategory
import com.expensetracker.app.domain.model.IncomeCategory
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import java.time.Instant
import java.time.ZoneId

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null
) {
    val isExpense = transaction.type == TransactionType.EXPENSE
    val amountColor = if (isExpense) ExpenseRed else EmeraldGreen
    val icon = if (isExpense) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward
    val categoryName = if (isExpense) {
        try {
            ExpenseCategory.valueOf(transaction.category).displayName
        } catch (e: IllegalArgumentException) {
            transaction.category
        }
    } else {
        try {
            IncomeCategory.valueOf(transaction.category).displayName
        } catch (e: IllegalArgumentException) {
            transaction.category
        }
    }

    val date = Instant.ofEpochMilli(transaction.date)
        .atZone(ZoneId.systemDefault()).toLocalDate()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isExpense) ExpenseRed.copy(alpha = 0.15f) else EmeraldGreen.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = amountColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = categoryName,
                color = SoftWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            if (transaction.note.isNotEmpty()) {
                Text(
                    text = transaction.note,
                    color = MutedWhite,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = DateUtils.formatDisplay(date),
                color = MutedWhite,
                fontSize = 11.sp
            )
        }

        Text(
            text = "${if (isExpense) "-" else "+"}${CurrencyUtils.format(transaction.amount)}",
            color = amountColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        if (onDelete != null) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MutedWhite.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
