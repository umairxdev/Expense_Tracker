package com.expensetracker.app.core.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyUtils {
    var currencyCode: String = "PKR"

    fun getSymbol(): String {
        val code = if (currencyCode.isBlank()) "PKR" else currencyCode
        return try {
            Currency.getInstance(code).symbol
        } catch (_: Exception) {
            Currency.getInstance("USD").symbol
        }
    }

    fun getCode(): String {
        return if (currencyCode.isBlank()) "PKR" else currencyCode
    }

    private fun getNumberFormatter(): NumberFormat {
        return NumberFormat.getNumberInstance(Locale.US).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
    }

    fun format(amount: Double): String {
        val code = if (currencyCode.isBlank()) "PKR" else currencyCode
        val symbol = try {
            Currency.getInstance(code).symbol
        } catch (_: Exception) {
            Currency.getInstance("USD").symbol
        }
        return "$symbol ${getNumberFormatter().format(amount)}"
    }

    fun formatCompact(amount: Double): String {
        return getNumberFormatter().format(amount)
    }

    fun format(amount: Double, showSymbol: Boolean): String {
        return if (showSymbol) format(amount)
        else getNumberFormatter().format(amount)
    }

    val availableCurrencies: List<Pair<String, String>> get() = listOf(
        "PKR" to "₨ - Pakistani Rupee",
        "USD" to "\$ - US Dollar",
        "EUR" to "€ - Euro",
        "GBP" to "£ - British Pound Sterling",
        "JPY" to "¥ - Japanese Yen",
        "INR" to "₹ - Indian Rupee",
        "CNY" to "¥ - Chinese Yuan",
        "KRW" to "₩ - South Korean Won",
        "CAD" to "CA\$ - Canadian Dollar",
        "AUD" to "A\$ - Australian Dollar",
        "NZD" to "NZ\$ - New Zealand Dollar",
        "SGD" to "S\$ - Singapore Dollar",
        "HKD" to "HK\$ - Hong Kong Dollar",
        "MYR" to "RM - Malaysian Ringgit",
        "IDR" to "Rp - Indonesian Rupiah",
        "PHP" to "₱ - Philippine Peso",
        "THB" to "฿ - Thai Baht",
        "VND" to "₫ - Vietnamese Dong",
        "TWD" to "NT\$ - New Taiwan Dollar",
        "AED" to "د.إ - UAE Dirham",
        "SAR" to "﷼ - Saudi Riyal",
        "QAR" to "﷼ - Qatari Riyal",
        "KWD" to "د.ك - Kuwaiti Dinar",
        "OMR" to "﷼ - Omani Rial",
        "BHD" to "د.ب - Bahraini Dinar",
        "EGP" to "E£ - Egyptian Pound",
        "TRY" to "₺ - Turkish Lira",
        "ILS" to "₪ - Israeli Shekel",
        "RUB" to "₽ - Russian Ruble",
        "CHF" to "CHF - Swiss Franc",
        "SEK" to "kr - Swedish Krona",
        "NOK" to "kr - Norwegian Krone",
        "DKK" to "kr - Danish Krone",
        "MXN" to "MX\$ - Mexican Peso",
        "BRL" to "R\$ - Brazilian Real",
        "ARS" to "\$ - Argentine Peso",
        "CLP" to "\$ - Chilean Peso",
        "COP" to "\$ - Colombian Peso",
        "ZAR" to "R - South African Rand",
        "NGN" to "₦ - Nigerian Naira",
        "KES" to "KSh - Kenyan Shilling",
        "BDT" to "৳ - Bangladeshi Taka",
        "LKR" to "Rs - Sri Lankan Rupee",
        "NPR" to "Rs - Nepalese Rupee",
        "AFN" to "؋ - Afghan Afghani",
        "IRR" to "﷼ - Iranian Rial",
        "IQD" to "د.ع - Iraqi Dinar",
        "JOD" to "د.ا - Jordanian Dinar",
        "LBP" to "ل.ل - Lebanese Pound",
        "MAD" to "د.م. - Moroccan Dirham",
        "TND" to "د.ت - Tunisian Dinar",
        "DZD" to "د.ج - Algerian Dinar",
        "UAH" to "₴ - Ukrainian Hryvnia",
        "PLN" to "zł - Polish Zloty",
        "CZK" to "Kč - Czech Koruna",
        "HUF" to "Ft - Hungarian Forint",
        "RON" to "lei - Romanian Leu",
        "BGN" to "лв - Bulgarian Lev",
        "HRK" to "kn - Croatian Kuna",
        "ISK" to "kr - Icelandic Krona",
        "RSD" to "дин. - Serbian Dinar"
    )
}
