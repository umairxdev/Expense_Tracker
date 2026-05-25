package com.expensetracker.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensetracker.app.core.theme.CharcoalGray
import com.expensetracker.app.core.theme.DarkCard
import com.expensetracker.app.core.theme.DimWhite
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.ExpenseRed
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite
import com.expensetracker.app.domain.model.CategorySummary
import com.expensetracker.app.domain.model.SpendingTrend
import com.expensetracker.app.core.utils.CurrencyUtils

@Composable
fun PieChart(
    data: List<CategorySummary>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.amount }
    if (total <= 0) return

    val animatedPercentages = data.map { summary ->
        val target = (summary.amount / total).toFloat()
        animateFloatAsState(
            targetValue = target,
            animationSpec = tween(1000),
            label = "pie_${summary.category}"
        )
    }

    val chartColors = listOf(
        EmeraldGreen, Color(0xFF4CAF50), Color(0xFFFFCA28),
        Color(0xFFFF7043), Color(0xFFE040FB), Color(0xFF29B6F6),
        Color(0xFF7E57C2), Color(0xFFFF4081), Color(0xFF26C6DA),
        Color(0xFFFF6F00), Color(0xFF42A5F5), Color(0xFF78909C)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(120.dp)) {
            Canvas(modifier = Modifier.size(120.dp)) {
                val strokeWidth = 32.dp.toPx()
                var startAngle = -90f

                data.forEachIndexed { index, summary ->
                    val sweepAngle = 360f * animatedPercentages[index].value
                    drawArc(
                        color = chartColors[index % chartColors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth)
                    )
                    startAngle += sweepAngle
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            data.take(6).forEachIndexed { index, summary ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .padding(0.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = summary.category.take(10),
                            color = MutedWhite,
                            fontSize = 11.sp
                        )
                    }
                    Text(
                        text = CurrencyUtils.format(summary.amount),
                        color = SoftWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun BarChart(
    data: List<SpendingTrend>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOfOrNull {
        maxOf(it.income, it.expense)
    } ?: 1.0

    Column(modifier = modifier.fillMaxWidth()) {
        data.forEach { trend ->
            val incomeFraction = (trend.income / maxValue).toFloat().coerceIn(0f, 1f)
            val expenseFraction = (trend.expense / maxValue).toFloat().coerceIn(0f, 1f)

            val animatedIncome by animateFloatAsState(
                targetValue = incomeFraction,
                animationSpec = tween(800),
                label = "bar_income_${trend.month}"
            )
            val animatedExpense by animateFloatAsState(
                targetValue = expenseFraction,
                animationSpec = tween(800),
                label = "bar_expense_${trend.month}"
            )

            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = trend.month,
                    color = DimWhite,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$", color = EmeraldGreen, fontSize = 9.sp)
                    Box(
                        modifier = Modifier
                            .width((animatedIncome * 200).dp)
                            .height(6.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        CurrencyUtils.format(trend.income),
                        color = EmeraldGreen,
                        fontSize = 9.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$", color = ExpenseRed, fontSize = 9.sp)
                    Box(
                        modifier = Modifier
                            .width((animatedExpense * 200).dp)
                            .height(6.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        CurrencyUtils.format(trend.expense),
                        color = ExpenseRed,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LineChart(
    data: List<SpendingTrend>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    val maxValue = data.maxOfOrNull { maxOf(it.income, it.expense) } ?: 1.0

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val padding = 40f
        val chartWidth = canvasWidth - padding * 2
        val chartHeight = canvasHeight - padding * 2

        if (data.size < 2) return@Canvas

        val stepX = chartWidth / (data.size - 1)

        // Draw grid lines
        for (i in 0..4) {
            val y = padding + (chartHeight * i / 4)
            drawLine(
                color = Color(0x1AFFFFFF),
                start = Offset(padding, y),
                end = Offset(canvasWidth - padding, y),
                strokeWidth = 1f
            )
        }

        // Draw income line
        val incomePath = androidx.compose.ui.graphics.Path()
        data.forEachIndexed { index, trend ->
            val x = padding + index * stepX
            val y = padding + chartHeight * (1f - (trend.income / maxValue).toFloat())
            if (index == 0) incomePath.moveTo(x, y) else incomePath.lineTo(x, y)
        }
        drawPath(
            path = incomePath,
            color = EmeraldGreen,
            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
        )

        // Draw expense line
        val expensePath = androidx.compose.ui.graphics.Path()
        data.forEachIndexed { index, trend ->
            val x = padding + index * stepX
            val y = padding + chartHeight * (1f - (trend.expense / maxValue).toFloat())
            if (index == 0) expensePath.moveTo(x, y) else expensePath.lineTo(x, y)
        }
        drawPath(
            path = expensePath,
            color = ExpenseRed,
            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
        )

        // Draw labels
        data.forEachIndexed { index, trend ->
            val x = padding + index * stepX
            drawContext.canvas.nativeCanvas.drawText(
                trend.month,
                x - 10f,
                canvasHeight - 5f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 24f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}
