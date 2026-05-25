package com.expensetracker.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.expensetracker.app.core.constants.Constants
import com.expensetracker.app.data.local.dao.BudgetDao
import com.expensetracker.app.data.local.dao.RecurringExpenseDao
import com.expensetracker.app.data.local.dao.TransactionDao
import com.expensetracker.app.data.local.entity.BudgetEntity
import com.expensetracker.app.data.local.entity.RecurringExpenseEntity
import com.expensetracker.app.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        RecurringExpenseEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun recurringExpenseDao(): RecurringExpenseDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
