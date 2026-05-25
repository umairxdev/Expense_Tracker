package com.expensetracker.app.core.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyUtils {
    private val formatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    fun format(amount: Double): String {
        return formatter.format(amount)
    }

    fun format(amount: Double, showSymbol: Boolean): String {
        return if (showSymbol) format(amount)
        else NumberFormat.getNumberInstance(Locale.US).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }.format(amount)
    }

    fun parseCurrencyString(amount: String): Double {
        return try {
            amount.replace("$", "").replace(",", "").toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
}
