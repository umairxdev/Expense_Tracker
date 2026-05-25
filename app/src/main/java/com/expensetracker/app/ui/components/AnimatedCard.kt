package com.expensetracker.app.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val bg = if (backgroundColor == Color.Transparent) {
        MaterialTheme.colorScheme.surfaceVariant
    } else backgroundColor

    val shape = RoundedCornerShape(16.dp)
    val borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)

    if (onClick != null) {
        ClickableGlassCard(
            modifier = modifier,
            background = bg,
            borderColor = borderColor,
            shape = shape,
            onClick = onClick,
            content = content
        )
    } else {
        StaticGlassCard(
            modifier = modifier,
            background = bg,
            borderColor = borderColor,
            shape = shape,
            content = content
        )
    }
}

@Composable
private fun StaticGlassCard(
    modifier: Modifier,
    background: Color,
    borderColor: Color,
    shape: RoundedCornerShape,
    content: @Composable () -> Unit
) {
    val elevation by animateDpAsState(
        targetValue = 4.dp,
        animationSpec = spring(),
        label = "elevation"
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(0.5.dp, borderColor, shape)
    ) {
        GlassSheen(shape = shape)
        Box(
            modifier = Modifier
                .padding(12.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun ClickableGlassCard(
    modifier: Modifier,
    background: Color,
    borderColor: Color,
    shape: RoundedCornerShape,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 1.dp else 4.dp,
        animationSpec = spring(),
        label = "elevation"
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(0.5.dp, borderColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        GlassSheen(shape = shape)
        Box(
            modifier = Modifier
                .padding(12.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun GlassSheen(shape: RoundedCornerShape) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.04f),
                        Color.Transparent,
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.02f)
                    )
                )
            )
    )
}
