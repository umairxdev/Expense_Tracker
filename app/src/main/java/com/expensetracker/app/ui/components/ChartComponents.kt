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
import androidx.compose.foundation.background
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

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Canvas(modifier = Modifier.size(140.dp)) {
                val strokeWidth = 36.dp.toPx()
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

        Spacer(modifier = Modifier.height(16.dp))

        data.take(6).forEachIndexed { index, summary ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(chartColors[index % chartColors.size])
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = summary.category,
                        color = MutedWhite,
                        fontSize = 13.sp
                    )
                }
                Text(
                    text = CurrencyUtils.format(summary.amount),
                    color = SoftWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
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

    if (maxValue <= 0) return

    Column(modifier = modifier.fillMaxWidth()) {
        data.take(12).forEach { trend ->
            val incomeFraction = (trend.income / maxValue).toFloat().coerceIn(0f, 0.85f)
            val expenseFraction = (trend.expense / maxValue).toFloat().coerceIn(0f, 0.85f)

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trend.month.take(3),
                    color = DimWhite,
                    fontSize = 10.sp,
                    modifier = Modifier.width(28.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedIncome)
                                .height(7.dp)
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                .background(EmeraldGreen.copy(alpha = 0.75f))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            CurrencyUtils.format(trend.income),
                            color = EmeraldGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedExpense)
                                .height(7.dp)
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                .background(ExpenseRed.copy(alpha = 0.65f))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            CurrencyUtils.format(trend.expense),
                            color = ExpenseRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
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
                x,
                canvasHeight - 8f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 22f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}
