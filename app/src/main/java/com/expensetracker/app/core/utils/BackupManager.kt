package com.expensetracker.app.core.utils

import android.content.Context
import android.net.Uri
import com.expensetracker.app.data.local.dao.BudgetDao
import com.expensetracker.app.data.local.dao.CategoryDao
import com.expensetracker.app.data.local.dao.RecurringExpenseDao
import com.expensetracker.app.data.local.dao.TransactionDao
import com.expensetracker.app.data.local.entity.BudgetEntity
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.data.local.entity.RecurringExpenseEntity
import com.expensetracker.app.data.local.entity.TransactionEntity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

data class BackupData(
    val version: Int = 1,
    val transactions: List<TransactionEntity> = emptyList(),
    val recurringExpenses: List<RecurringExpenseEntity> = emptyList(),
    val budgets: List<BudgetEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList()
)

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val transactionDao: TransactionDao,
    private val recurringExpenseDao: RecurringExpenseDao,
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao
) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun exportToUri(uri: Uri) {
        val data = BackupData(
            version = 1,
            transactions = kotlinx.coroutines.runBlocking { transactionDao.getAllTransactionsOnce() },
            recurringExpenses = kotlinx.coroutines.runBlocking { recurringExpenseDao.getAllOnce() },
            budgets = kotlinx.coroutines.runBlocking { budgetDao.getAllOnce() },
            categories = kotlinx.coroutines.runBlocking { categoryDao.getAllOnce() }
        )
        val json = gson.toJson(data)
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(json.toByteArray())
        }
    }

    suspend fun importFromUri(uri: Uri): Result<String> {
        return try {
            val json = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).readText()
            } ?: return Result.failure(Exception("Could not read file"))

            val data = gson.fromJson(json, BackupData::class.java)

            transactionDao.deleteAll()
            recurringExpenseDao.deleteAll()
            budgetDao.deleteAll()
            categoryDao.deleteNonDefault()

            data.transactions.chunked(100).forEach { chunk -> transactionDao.insertAll(chunk) }
            data.recurringExpenses.forEach { recurringExpenseDao.insert(it) }
            data.budgets.forEach { budgetDao.insert(it) }
            data.categories.filter { !it.isDefault }.forEach { categoryDao.insert(it) }

            val summary = buildString {
                append("Restored ${data.transactions.size} transactions")
                if (data.recurringExpenses.isNotEmpty()) append(", ${data.recurringExpenses.size} recurring")
                if (data.budgets.isNotEmpty()) append(", ${data.budgets.size} budgets")
                if (data.categories.isNotEmpty()) append(", ${data.categories.filter { !it.isDefault }.size} categories")
            }
            Result.success(summary)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun createLocalBackup(): Uri {
        val file = java.io.File(context.cacheDir, "expense_tracker_backup.json")
        val data = BackupData(
            version = 1,
            transactions = kotlinx.coroutines.runBlocking { transactionDao.getAllTransactionsOnce() },
            recurringExpenses = kotlinx.coroutines.runBlocking { recurringExpenseDao.getAllOnce() },
            budgets = kotlinx.coroutines.runBlocking { budgetDao.getAllOnce() },
            categories = kotlinx.coroutines.runBlocking { categoryDao.getAllOnce() }
        )
        val json = gson.toJson(data)
        file.writeText(json)
        return Uri.fromFile(file)
    }
}
