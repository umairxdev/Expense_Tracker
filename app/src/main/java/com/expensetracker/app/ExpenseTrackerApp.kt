package com.expensetracker.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.expensetracker.app.core.utils.CurrencyUtils
import com.expensetracker.app.core.utils.NotificationUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExpenseTrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        loadCurrencyPreference()
    }

    private fun loadCurrencyPreference() {
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        CurrencyUtils.currencyCode = prefs.getString("currency_code", "PKR") ?: "PKR"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationUtils.CHANNEL_ID,
                NotificationUtils.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for recurring bills and budget warnings"
                enableVibration(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
