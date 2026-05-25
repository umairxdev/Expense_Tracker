package com.expensetracker.app.core.utils

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {
    private val displayFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    private val fullFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")
    private val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    private val shortMonthFormatter = DateTimeFormatter.ofPattern("MMM")
    private val dayFormatter = DateTimeFormatter.ofPattern("dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    fun formatDisplay(date: LocalDate): String = date.format(displayFormatter)

    fun formatFull(date: LocalDate): String = date.format(fullFormatter)

    fun formatMonthYear(date: LocalDate): String = date.format(monthYearFormatter)

    fun formatShortMonth(date: LocalDate): String = date.format(shortMonthFormatter)

    fun formatDay(date: LocalDate): String = date.format(dayFormatter)

    fun formatTime(dateTime: LocalDateTime): String = dateTime.format(timeFormatter)

    fun formatMonthYear(year: Int, month: Int): String {
        return YearMonth.of(year, month).format(monthYearFormatter)
    }

    fun getDayOfWeek(date: LocalDate): String {
        return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    fun isToday(date: LocalDate): Boolean = date == LocalDate.now()

    fun isCurrentMonth(date: LocalDate): Boolean {
        val now = LocalDate.now()
        return date.year == now.year && date.month == now.month
    }

    fun getDaysInMonth(year: Int, month: Int): Int {
        return YearMonth.of(year, month).lengthOfMonth()
    }

    fun getMonthRange(year: Int, month: Int): Pair<LocalDate, LocalDate> {
        val ym = YearMonth.of(year, month)
        return Pair(ym.atDay(1), ym.atEndOfMonth())
    }

    fun getCurrentMonthRange(): Pair<LocalDate, LocalDate> {
        val now = LocalDate.now()
        return getMonthRange(now.year, now.monthValue)
    }

    fun getPreviousMonths(n: Int): List<YearMonth> {
        val current = YearMonth.now()
        return (0 until n).map { current.minusMonths(it.toLong()) }
    }
}
