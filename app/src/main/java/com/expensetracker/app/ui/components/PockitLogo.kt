package com.expensetracker.app.ui.components

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.expensetracker.app.core.theme.EmeraldGreen

@Composable
fun PockitLogo(
    modifier: Modifier = Modifier,
    sizeDp: Int = 80
) {
    Canvas(
        modifier = modifier.size(sizeDp.dp)
    ) {
        val canvasSize = this.size.width
        val pad = canvasSize * 0.1f
        val rectSize = canvasSize - pad * 2
        val cornerR = rectSize * 0.25f

        drawRoundRect(
            color = Color.Black.copy(alpha = 0.08f),
            topLeft = Offset(pad + rectSize * 0.02f, pad + rectSize * 0.04f),
            size = Size(rectSize, rectSize),
            cornerRadius = CornerRadius(cornerR, cornerR)
        )

        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(EmeraldGreen, EmeraldGreen.copy(alpha = 0.85f))
            ),
            topLeft = Offset(pad, pad),
            size = Size(rectSize, rectSize),
            cornerRadius = CornerRadius(cornerR, cornerR)
        )

        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.2f),
                    Color.White.copy(alpha = 0f)
                ),
                startY = pad,
                endY = pad + rectSize * 0.45f
            ),
            topLeft = Offset(pad, pad),
            size = Size(rectSize, rectSize),
            cornerRadius = CornerRadius(cornerR, cornerR)
        )

        drawLetterP(canvasSize, rectSize, pad)
    }
}

private fun DrawScope.drawLetterP(canvasSize: Float, rectSize: Float, pad: Float) {
    val textPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = rectSize * 0.72f
        typeface = Typeface.create("sans-serif-medium", Typeface.BOLD)
        isAntiAlias = true
        isSubpixelText = true
        isLinearText = true
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val centerX = canvasSize / 2f
    val centerY = canvasSize / 2f

    val fontMetrics = textPaint.fontMetrics
    val textY = centerY - (fontMetrics.ascent + fontMetrics.descent) / 2f

    drawContext.canvas.nativeCanvas.drawText("P", centerX, textY, textPaint)
}
