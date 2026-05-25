package com.expensetracker.app.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensetracker.app.core.theme.CharcoalGray
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.core.theme.MatteBlack
import com.expensetracker.app.core.theme.MutedWhite
import com.expensetracker.app.core.theme.SoftWhite

data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val description: String
)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Filled.AccountBalanceWallet,
        title = "Track Your Expenses",
        description = "Log every expense effortlessly and stay on top of your spending with beautiful visual insights."
    ),
    OnboardingPage(
        icon = Icons.Filled.Analytics,
        title = "Smart Analytics",
        description = "Understand your spending patterns with clear charts, trends, and monthly reports."
    ),
    OnboardingPage(
        icon = Icons.Filled.Security,
        title = "100% Private & Offline",
        description = "All your data stays on your device. No accounts, no cloud, no tracking."
    )
)

@Composable
fun OnboardingScreen(
    onNavigateToDashboard: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatteBlack)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
            },
            label = "onboarding"
        ) { page ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = pages[page].icon,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(100.dp)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = pages[page].title,
                    color = SoftWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pages[page].description,
                    color = MutedWhite,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pages.indices.forEach { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentPage) 24.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == currentPage) EmeraldGreen
                            else CharcoalGray
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (currentPage < pages.size - 1) {
                    currentPage++
                } else {
                    onNavigateToDashboard()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EmeraldGreen,
                contentColor = MatteBlack
            )
        ) {
            Text(
                text = if (currentPage < pages.size - 1) "Continue" else "Get Started",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (currentPage < pages.size - 1) {
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onNavigateToDashboard) {
                Text(
                    text = "Skip",
                    color = MutedWhite,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
