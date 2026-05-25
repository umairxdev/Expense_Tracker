package com.expensetracker.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensetracker.app.core.theme.EmeraldGreen
import com.expensetracker.app.navigation.BottomNavItem
import com.expensetracker.app.navigation.bottomNavItems

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) EmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(200),
                    label = "navColor"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { onItemSelected(item.route) }
                ) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = item.label,
                        color = iconColor,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
