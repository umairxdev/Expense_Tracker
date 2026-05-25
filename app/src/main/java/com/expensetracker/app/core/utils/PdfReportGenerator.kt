package com.expensetracker.app.core.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
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
    private val monthDayFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

    private val margin = 44f
    private val pageWidth = 507f
    private val contentWidth = pageWidth - 2 * margin

    private val emeraldColor = Color.rgb(5, 150, 105)
    private val sectionFont = Paint().apply {
        color = Color.rgb(50, 50, 50)
        textSize = 14f
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        isAntiAlias = true
    }
    private val summaryLabelFont = Paint().apply {
        color = Color.rgb(140, 140, 140)
        textSize = 10f
        typeface = Typeface.DEFAULT
        isAntiAlias = true
    }
    private val summaryAmountFont = Paint().apply {
        color = Color.rgb(30, 30, 30)
        textSize = 18f
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        isAntiAlias = true
    }
    private val tableHeaderFont = Paint().apply {
        color = Color.rgb(100, 100, 100)
        textSize = 9f
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        isAntiAlias = true
    }
    private val tableRowFont = Paint().apply {
        color = Color.rgb(50, 50, 50)
        textSize = 10f
        typeface = Typeface.DEFAULT
        isAntiAlias = true
    }
    private val greenFont = Paint().apply {
        color = Color.rgb(5, 150, 105)
        textSize = 10f
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        isAntiAlias = true
    }
    private val redFont = Paint().apply {
        color = Color.rgb(210, 60, 50)
        textSize = 10f
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        isAntiAlias = true
    }
    private val noteFont = Paint().apply {
        color = Color.rgb(160, 160, 160)
        textSize = 8f
        typeface = Typeface.DEFAULT
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

        canvas.drawColor(Color.rgb(248, 248, 248))

        var y = 36f

        y = drawHeader(canvas, y, filter)
        y += 8f
        y = drawDivider(canvas, y)
        y += 24f

        y = drawSummary(canvas, y, totalIncome, totalExpense, balance)
        y += 8f
        y = drawDivider(canvas, y)
        y += 24f

        y = drawBarChart(canvas, y, transactions)
        y += 8f
        y = drawDivider(canvas, y)
        y += 24f

        drawTransactionTable(canvas, y, transactions)

        document.finishPage(page)
        context.contentResolver.openOutputStream(uri)?.use { outputStream: OutputStream ->
            document.writeTo(outputStream)
        }
        document.close()
    }

    private fun drawHeader(canvas: Canvas, y: Float, filter: ReportFilter): Float {
        var cy = y
        val headerBgPaint = Paint().apply {
            color = Color.rgb(25, 25, 30)
        }
        canvas.drawRect(0f, 0f, 595f, 74f, headerBgPaint)

        val accentPaint = Paint().apply {
            color = emeraldColor
        }
        canvas.drawRect(0f, 74f, 595f, 78f, accentPaint)

        val brandFont = Paint().apply {
            color = Color.WHITE
            textSize = 28f
            typeface = Typeface.create("sans-serif-medium", Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText("POCKIT", 44f, 32f, brandFont)

        val subHeaderFont = Paint().apply {
            color = Color.rgb(180, 180, 180)
            textSize = 11f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText("Financial Statement", 44f, 52f, subHeaderFont)

        val dateRangePaint = Paint().apply {
            color = Color.rgb(160, 160, 160)
            textSize = 9f
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
        val periodText = "${dateFormat.format(Date(filter.startDate))}  —  ${dateFormat.format(Date(filter.endDate))}"
        canvas.drawText(periodText, 595f - 44f, 66f, dateRangePaint)

        cy = 94f
        return cy
    }

    private fun drawDivider(canvas: Canvas, y: Float): Float {
        val paint = Paint().apply {
            color = Color.rgb(220, 220, 220)
            strokeWidth = 1f
        }
        canvas.drawLine(margin, y, 595f - margin, y, paint)
        return y + 2f
    }

    private fun drawSummary(
        canvas: Canvas, y: Float,
        totalIncome: Double, totalExpense: Double, balance: Double
    ): Float {
        var cy = y

        sectionFont.color = Color.rgb(50, 50, 50)
        canvas.drawText("Summary", margin, cy, sectionFont)
        cy += 20f

        val boxWidth = contentWidth / 3 - 8f
        val boxes = listOf(
            Triple("Total Income", CurrencyUtils.format(totalIncome), emeraldColor),
            Triple("Total Expenses", CurrencyUtils.format(totalExpense), Color.rgb(210, 60, 50)),
            Triple("Balance", CurrencyUtils.format(balance), if (balance >= 0) emeraldColor else Color.rgb(210, 60, 50))
        )

        val boxBgPaint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
        }
        val boxStrokePaint = Paint().apply {
            color = Color.rgb(230, 230, 230)
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        boxes.forEachIndexed { i, (label, amount, amountColor) ->
            val left = margin + i * (boxWidth + 8f)
            val top = cy
            val right = left + boxWidth
            val bottom = cy + 64f

            val r = RectF(left, top, right, bottom)
            canvas.drawRoundRect(r, 6f, 6f, boxBgPaint)
            canvas.drawRoundRect(r, 6f, 6f, boxStrokePaint)

            summaryLabelFont.textAlign = Paint.Align.CENTER
            canvas.drawText(label, left + boxWidth / 2, top + 18f, summaryLabelFont)

            summaryAmountFont.color = amountColor
            summaryAmountFont.textAlign = Paint.Align.CENTER
            canvas.drawText(amount, left + boxWidth / 2, top + 48f, summaryAmountFont)
        }

        summaryAmountFont.color = Color.rgb(30, 30, 30)
        cy += 80f
        return cy
    }

    private fun drawBarChart(
        canvas: Canvas, y: Float,
        transactions: List<TransactionEntity>
    ): Float {
        var cy = y

        val dailyMap = transactions
            .groupBy { monthDayFormat.format(Date(it.date)) }
            .mapValues { (_, txns) ->
                txns.filter { it.type == "EXPENSE" }.sumOf { it.amount } to
                    txns.filter { it.type == "INCOME" }.sumOf { it.amount }
            }
            .toList()
            .take(7)

        sectionFont.color = Color.rgb(50, 50, 50)
        canvas.drawText("Daily Trend", margin, cy, sectionFont)
        cy += 20f

        if (dailyMap.isEmpty()) return cy + 10f

        val chartHeight = 120f
        val chartLeft = margin
        val chartRight = 595f - margin
        val chartWidth = chartRight - chartLeft

        val maxVal = dailyMap.maxOf { maxOf(it.second.first, it.second.second) }
        if (maxVal <= 0) return cy + chartHeight + 30f

        val barGroupWidth = chartWidth / dailyMap.size
        val barWidth = barGroupWidth * 0.28f
        val barSpacing = barGroupWidth * 0.08f

        val gridPaint = Paint().apply {
            color = Color.rgb(235, 235, 235)
            strokeWidth = 1f
        }

        for (i in 0..4) {
            val yPos = cy + chartHeight - (chartHeight * i / 4f)
            canvas.drawLine(chartLeft, yPos, chartRight, yPos, gridPaint)
        }

        dailyMap.forEachIndexed { index, (date, amounts) ->
            val (expense, income) = amounts
            val x = chartLeft + index * barGroupWidth + barSpacing

            val expHeight = (expense / maxVal * chartHeight).toFloat()
            val incHeight = (income / maxVal * chartHeight).toFloat()

            val expensePaint = Paint().apply {
                color = Color.rgb(210, 60, 50)
            }
            canvas.drawRect(x, (cy + chartHeight - expHeight), x + barWidth, cy + chartHeight, expensePaint)

            val incomePaint = Paint().apply {
                color = emeraldColor
            }
            canvas.drawRect(x + barWidth + barSpacing * 2, (cy + chartHeight - incHeight), x + barWidth * 2 + barSpacing * 2, cy + chartHeight, incomePaint)

            val labelPaint = Paint().apply {
                color = Color.rgb(140, 140, 140)
                textSize = 8f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(date, x + barGroupWidth / 2, cy + chartHeight + 14f, labelPaint)
        }

        val legendY = cy + chartHeight + 34f
        val legendPaint = Paint().apply {
            color = Color.rgb(140, 140, 140)
            textSize = 9f
            isAntiAlias = true
        }

        val expenseLegendX = 595f / 2 - 60f
        canvas.drawRect(expenseLegendX, legendY - 7f, expenseLegendX + 12f, legendY + 1f, Paint().apply {
            color = Color.rgb(210, 60, 50)
        })
        canvas.drawText("Expense", expenseLegendX + 18f, legendY + 1f, legendPaint)

        val incomeLegendX = 595f / 2 + 20f
        canvas.drawRect(incomeLegendX, legendY - 7f, incomeLegendX + 12f, legendY + 1f, Paint().apply {
            color = Color.rgb(0, 160, 70)
        })
        canvas.drawText("Income", incomeLegendX + 18f, legendY + 1f, legendPaint)

        cy = legendY + 24f
        return cy
    }

    private fun drawTransactionTable(
        canvas: Canvas, y: Float,
        transactions: List<TransactionEntity>
    ) {
        var cy = y

        sectionFont.color = Color.rgb(50, 50, 50)
        canvas.drawText("Transactions", margin, cy, sectionFont)
        cy += 20f

        val colWidths = listOf(90, 130, 55, 90, contentWidth.toInt() - 90 - 130 - 55 - 90)
        val headers = listOf("DATE", "CATEGORY", "TYPE", "AMOUNT", "NOTE")
        val colStarts = colWidths.scan(margin.toInt()) { acc, w -> acc + w }

        val headerBgPaint = Paint().apply {
            color = Color.rgb(245, 245, 245)
        }
        canvas.drawRect(margin, cy, 595f - margin, cy + 24f, headerBgPaint)

        headers.forEachIndexed { i, header ->
            val align = if (i == 3) Paint.Align.RIGHT else Paint.Align.LEFT
            tableHeaderFont.textAlign = align
            val x = if (align == Paint.Align.RIGHT) colStarts[i] + colWidths[i] else colStarts[i]
            canvas.drawText(header, x.toFloat(), cy + 16f, tableHeaderFont)
        }

        cy += 24f

        val displayTransactions = transactions.take(50)
        val altRowPaint = Paint().apply {
            color = Color.rgb(252, 252, 252)
        }

        for ((rowIndex, txn) in displayTransactions.withIndex()) {
            if (rowIndex % 2 == 1) {
                canvas.drawRect(margin, cy, 595f - margin, cy + 22f, altRowPaint)
            }

            val dateStr = dateFormat.format(Date(txn.date))
            val categoryStr = txn.category.replace("_", " ")
                .lowercase()
                .replaceFirstChar { it.uppercase() }
            val typeStr = txn.type
            val amountStr = "${if (txn.type == "INCOME") "+" else "-"}${CurrencyUtils.format(txn.amount)}"
            val noteStr = txn.note.ifEmpty { "" }

            val rowData = listOf(dateStr, categoryStr, typeStr, amountStr, noteStr)
            val rowPaints = listOf(tableRowFont, tableRowFont, tableRowFont,
                if (txn.type == "INCOME") greenFont else redFont,
                noteFont
            )

            rowData.forEachIndexed { i, data ->
                val align = if (i == 3) Paint.Align.RIGHT else Paint.Align.LEFT
                val paint = rowPaints[i]
                paint.textAlign = align
                val x = if (align == Paint.Align.RIGHT) colStarts[i] + colWidths[i] else colStarts[i]
                val displayText = if (i == 4 && data.length > 16) data.take(16) + "..." else data
                canvas.drawText(displayText, x.toFloat(), cy + 15f, paint)
            }

            cy += 22f

            if (cy > 800f) {
                val morePaint = Paint().apply {
                    color = Color.rgb(140, 140, 140)
                    textSize = 10f
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText(
                    "+ ${transactions.size - rowIndex - 1} more transactions",
                    595f / 2, cy + 16f, morePaint
                )
                break
            }
        }
    }
}
