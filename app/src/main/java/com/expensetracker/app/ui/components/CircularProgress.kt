package com.expensetracker.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensetracker.app.core.theme.CharcoalGray
import com.expensetracker.app.core.theme.EmeraldGreen

@Composable
fun CircularProgressIndicator(
    percentage: Float,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    strokeWidth: Dp = 6.dp,
    progressColor: Color = EmeraldGreen,
    trackColor: Color = CharcoalGray,
    showPercentage: Boolean = true,
    label: String = ""
) {
    val animatedProgress by animateFloatAsState(
        targetValue = percentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = strokeWidth.toPx()
            val canvasSize = size.toPx() - stroke
            val topLeft = Offset(stroke / 2, stroke / 2)

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(canvasSize, canvasSize),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = Size(canvasSize, canvasSize),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        if (showPercentage) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(percentage * 100).toInt()}%",
                    color = Color.White,
                    fontSize = (size.value / 5).sp,
                    fontWeight = FontWeight.Bold
                )
                if (label.isNotEmpty()) {
                    Text(
                        text = label,
                        color = Color.Gray,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}
