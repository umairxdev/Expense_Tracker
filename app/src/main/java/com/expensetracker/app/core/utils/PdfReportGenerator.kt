package com.expensetracker.app.core.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.expensetracker.app.data.local.dao.TransactionDao
import com.expensetracker.app.data.local.entity.TransactionEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

data class ReportFilter(
    val startDate: Long,
    val endDate: Long,
    val type: String = "ALL"
)

@Singleton
class PdfReportGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val transactionDao: TransactionDao
) {
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val headerFont = Paint().apply {
        color = Color.rgb(0, 200, 83)
        textSize = 28f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    private val titleFont = Paint().apply {
        color = Color.WHITE
        textSize = 18f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    private val bodyFont = Paint().apply {
        color = Color.rgb(200, 200, 200)
        textSize = 12f
        isAntiAlias = true
    }
    private val smallFont = Paint().apply {
        color = Color.rgb(150, 150, 150)
        textSize = 10f
        isAntiAlias = true
    }
    private val greenPaint = Paint().apply {
        color = Color.rgb(0, 200, 83)
        textSize = 14f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    private val redPaint = Paint().apply {
        color = Color.rgb(244, 67, 54)
        textSize = 14f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    private val whitePaint = Paint().apply {
        color = Color.WHITE
        textSize = 12f
        isAntiAlias = true
    }

    fun generate(uri: Uri, filter: ReportFilter) {
        val transactions = kotlinx.coroutines.runBlocking {
            transactionDao.getTransactionsByDateRangeOnce(filter.startDate, filter.endDate)
        }

        val totalIncome = transactions.filter { it.type == "INCOME" }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }
        val balance = totalIncome - totalExpense

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        var y = 40f
        val margin = 40f
        val pageWidth = 515f

        drawHeader(canvas, y)
        y += 50f

        y = drawDateRange(canvas, y, margin, filter)
        y += 20f

        y = drawSummary(canvas, y, margin, pageWidth, totalIncome, totalExpense, balance)
        y += 30f

        y = drawBarChart(canvas, y, margin, pageWidth, transactions)
        y += 30f

        y = drawTransactionTable(canvas, y, margin, pageWidth, transactions)

        document.finishPage(page)
        context.contentResolver.openOutputStream(uri)?.use { outputStream: OutputStream ->
            document.writeTo(outputStream)
        }
        document.close()
    }

    private fun drawHeader(canvas: Canvas, y: Float) {
        headerFont.textAlign = Paint.Align.CENTER
        canvas.drawText("EXPENSE TRACKER", 595f / 2, y, headerFont)
        headerFont.textAlign = Paint.Align.LEFT
    }

    private fun drawDateRange(canvas: Canvas, y: Float, margin: Float, filter: ReportFilter): Float {
        var cy = y
        canvas.drawRect(margin, cy, 595f - margin, cy + 30f, Paint().apply {
            color = Color.rgb(30, 30, 30)
        })
        titleFont.textAlign = Paint.Align.CENTER
        canvas.drawText(
            "${dateFormat.format(Date(filter.startDate))} - ${dateFormat.format(Date(filter.endDate))}",
            595f / 2, cy + 20f, titleFont
        )
        titleFont.textAlign = Paint.Align.LEFT
        return cy + 40f
    }

    private fun drawSummary(
        canvas: Canvas, y: Float, margin: Float, pageWidth: Float,
        totalIncome: Double, totalExpense: Double, balance: Double
    ): Float {
        var cy = y
        titleFont.textAlign = Paint.Align.CENTER
        canvas.drawText("SUMMARY", 595f / 2, cy, titleFont)
        titleFont.textAlign = Paint.Align.LEFT
        cy += 25f

        val colWidth = pageWidth / 3
        val labels = listOf("Income", "Expenses", "Balance")
        val amounts = listOf(
            CurrencyUtils.format(totalIncome),
            CurrencyUtils.format(totalExpense),
            CurrencyUtils.format(balance)
        )
        val colors = listOf(greenPaint, redPaint,
            if (balance >= 0) greenPaint else redPaint)

        labels.forEachIndexed { i, label ->
            val cx = margin + colWidth * i
            smallFont.textAlign = Paint.Align.CENTER
            canvas.drawText(label, cx + colWidth / 2, cy, smallFont)
            smallFont.textAlign = Paint.Align.LEFT
            colors[i].textAlign = Paint.Align.CENTER
            canvas.drawText(amounts[i], cx + colWidth / 2, cy + 20f, colors[i])
            colors[i].textAlign = Paint.Align.LEFT
        }

        cy += 35f

        canvas.drawLine(margin, cy, 595f - margin, cy, Paint().apply {
            color = Color.rgb(60, 60, 60)
            strokeWidth = 1f
        })
        return cy + 15f
    }

    private fun drawBarChart(
        canvas: Canvas, y: Float, margin: Float, pageWidth: Float,
        transactions: List<TransactionEntity>
    ): Float {
        var cy = y

        val dailyMap = transactions
            .groupBy { dateFormat.format(Date(it.date)) }
            .mapValues { (_, txns) ->
                txns.filter { it.type == "EXPENSE" }.sumOf { it.amount } to
                    txns.filter { it.type == "INCOME" }.sumOf { it.amount }
            }
            .toList()
            .take(7)

        if (dailyMap.isEmpty()) return cy

        titleFont.textAlign = Paint.Align.CENTER
        canvas.drawText("DAILY TREND (Last 7 Days)", 595f / 2, cy, titleFont)
        titleFont.textAlign = Paint.Align.LEFT
        cy += 25f

        val chartHeight = 100f
        val maxVal = dailyMap.maxOf { maxOf(it.second.first, it.second.second) }
        if (maxVal <= 0) return cy + chartHeight + 20f

        val barWidth = (pageWidth - 40f) / dailyMap.size / 3
        val chartTop = cy
        val chartBottom = cy + chartHeight

        dailyMap.forEachIndexed { index, (date, amounts) ->
            val (expense, income) = amounts
            val x = margin + index * (barWidth * 3 + 4f)

            val expHeight = (expense / maxVal * chartHeight).toFloat()
            val incHeight = (income / maxVal * chartHeight).toFloat()

            val expensePaint = Paint().apply { color = Color.rgb(244, 67, 54) }
            canvas.drawRect(x, chartBottom - expHeight, x + barWidth, chartBottom, expensePaint)

            val incomePaint = Paint().apply { color = Color.rgb(0, 200, 83) }
            canvas.drawRect(x + barWidth + 2f, chartBottom - incHeight, x + barWidth * 2 + 2f, chartBottom, incomePaint)

            if (index % 2 == 0) {
                val dateLabel = date.takeLast(5)
                smallFont.textAlign = Paint.Align.CENTER
                canvas.drawText(dateLabel, x + barWidth, chartBottom + 12f, smallFont)
                smallFont.textAlign = Paint.Align.LEFT
            }
        }

        cy = chartBottom + 25f
        canvas.drawLine(margin, cy, 595f - margin, cy, Paint().apply {
            color = Color.rgb(60, 60, 60)
            strokeWidth = 1f
        })
        return cy + 15f
    }

    private fun drawTransactionTable(
        canvas: Canvas, y: Float, margin: Float, pageWidth: Float,
        transactions: List<TransactionEntity>
    ): Float {
        var cy = y
        titleFont.textAlign = Paint.Align.CENTER
        canvas.drawText("TRANSACTIONS", 595f / 2, cy, titleFont)
        titleFont.textAlign = Paint.Align.LEFT
        cy += 25f

        val cols = listOf(80f, 150f, 80f, 80f, 125f)
        val headers = listOf("Date", "Category", "Type", "Amount", "Note")

        val headerPaint = Paint().apply {
            color = Color.rgb(0, 200, 83)
            textSize = 11f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        var cx = margin
        headers.forEachIndexed { i, header ->
            headerPaint.textAlign = if (i == cols.size - 1) Paint.Align.RIGHT else Paint.Align.LEFT
            canvas.drawText(header, cx, cy, headerPaint)
            cx += cols[i]
        }

        cy += 8f
        canvas.drawLine(margin, cy, 595f - margin, cy, Paint().apply {
            color = Color.rgb(60, 60, 60)
            strokeWidth = 1f
        })
        cy += 10f

        val displayTransactions = transactions.take(50)

        for (txn in displayTransactions) {
            val dateStr = dateFormat.format(Date(txn.date))
            val typePaint = if (txn.type == "INCOME") greenPaint else redPaint
            val txnDatePaint = whitePaint

            val rowData = listOf(
                dateStr,
                txn.category.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                txn.type,
                "${if (txn.type == "INCOME") "+" else "-"}${CurrencyUtils.format(txn.amount)}",
                txn.note.ifEmpty { "" }
            )

            cx = margin
            rowData.forEachIndexed { i, data ->
                val p = if (i == 3) typePaint else if (i == 0) txnDatePaint else whitePaint
                p.textAlign = if (i == cols.size - 1) Paint.Align.RIGHT else Paint.Align.LEFT
                canvas.drawText(data, cx, cy, p)
                cx += cols[i]
            }
            cy += 16f

            if (cy > 800f) break
        }

        if (transactions.size > 50) {
            cy += 10f
            smallFont.textAlign = Paint.Align.CENTER
            canvas.drawText(
                "+ ${transactions.size - 50} more transactions",
                595f / 2, cy, smallFont
            )
            smallFont.textAlign = Paint.Align.LEFT
        }

        return cy + 20f
    }
}
