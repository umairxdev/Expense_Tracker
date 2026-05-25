package com.expensetracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.expensetracker.app.core.theme.CategoryBills
import com.expensetracker.app.core.theme.CategoryBusiness
import com.expensetracker.app.core.theme.CategoryEducation
import com.expensetracker.app.core.theme.CategoryEntertainment
import com.expensetracker.app.core.theme.CategoryFood
import com.expensetracker.app.core.theme.CategoryFreelance
import com.expensetracker.app.core.theme.CategoryGroceries
import com.expensetracker.app.core.theme.CategoryHealthcare
import com.expensetracker.app.core.theme.CategoryInvestments
import com.expensetracker.app.core.theme.CategoryOther
import com.expensetracker.app.core.theme.CategoryRent
import com.expensetracker.app.core.theme.CategorySalary
import com.expensetracker.app.core.theme.CategoryShopping
import com.expensetracker.app.core.theme.CategoryTransport
import com.expensetracker.app.core.theme.CategoryTravel
import com.expensetracker.app.core.theme.CategoryUtilities

@Composable
fun CategoryIcon(
    category: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    iconSize: Dp = 18.dp
) {
    val (icon, color) = getCategoryIconAndColor(category)

    Box(
        modifier = modifier
            .size(size)
            .background(color.copy(alpha = 0.15f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = category,
            tint = color,
            modifier = Modifier.size(iconSize)
        )
    }
}

fun getCategoryIconAndColor(category: String): Pair<ImageVector, Color> {
    return when (category.uppercase()) {
        "GROCERIES" -> Pair(Icons.Filled.ShoppingCart, CategoryGroceries)
        "BILLS" -> Pair(Icons.Filled.Receipt, CategoryBills)
        "FOOD" -> Pair(Icons.Filled.Restaurant, CategoryFood)
        "SHOPPING" -> Pair(Icons.Filled.ShoppingBag, CategoryShopping)
        "TRANSPORT" -> Pair(Icons.Filled.DirectionsCar, CategoryTransport)
        "RENT" -> Pair(Icons.Filled.Home, CategoryRent)
        "ENTERTAINMENT" -> Pair(Icons.Filled.Movie, CategoryEntertainment)
        "HEALTHCARE" -> Pair(Icons.Filled.LocalHospital, CategoryHealthcare)
        "TRAVEL" -> Pair(Icons.Filled.Flight, CategoryTravel)
        "EDUCATION" -> Pair(Icons.Filled.School, CategoryEducation)
        "UTILITIES" -> Pair(Icons.Filled.Bolt, CategoryUtilities)
        "SALARY" -> Pair(Icons.Filled.Work, CategorySalary)
        "FREELANCE" -> Pair(Icons.Filled.Laptop, CategoryFreelance)
        "BUSINESS" -> Pair(Icons.Filled.Business, CategoryBusiness)
        "INVESTMENTS" -> Pair(Icons.Filled.TrendingUp, CategoryInvestments)
        else -> Pair(Icons.Filled.MoreHoriz, CategoryOther)
    }
}
