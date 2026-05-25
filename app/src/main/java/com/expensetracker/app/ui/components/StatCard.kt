package com.expensetracker.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite
import com.expensetracker.app.core.utils.CurrencyUtils

@Composable
fun StatCard(
    label: String,
    amount: Double,
    color: Color = SoftWhite,
    modifier: Modifier = Modifier
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600),
        label = "statAlpha"
    )

    AnimatedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(animatedAlpha)
        ) {
            Text(
                text = label,
                color = MutedWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = CurrencyUtils.format(amount),
                    color = color,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
