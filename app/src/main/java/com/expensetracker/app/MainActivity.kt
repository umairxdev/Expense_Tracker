package com.expensetracker.app

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expensetracker.app.core.theme.PockitTheme
import com.expensetracker.app.core.theme.ThemeMode
import com.expensetracker.app.navigation.NavGraph
import com.expensetracker.app.navigation.Screen
import com.expensetracker.app.ui.components.BottomNavBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val prefs = remember {
                getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            }
            var themeModeStr by remember { mutableStateOf(
                prefs.getString("theme_mode", "SYSTEM") ?: "SYSTEM"
            ) }

            DisposableEffect(Unit) {
                val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    if (key == "theme_mode") {
                        themeModeStr = prefs.getString("theme_mode", "SYSTEM") ?: "SYSTEM"
                    }
                }
                prefs.registerOnSharedPreferenceChangeListener(listener)
                onDispose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
            }

            val themeMode = remember(themeModeStr) {
                try { ThemeMode.valueOf(themeModeStr) }
                catch (_: Exception) { ThemeMode.SYSTEM }
            }

            PockitTheme(themeMode = themeMode) {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isOnTabScreen = currentRoute == Screen.Dashboard.route

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (isOnTabScreen) {
                BottomNavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { index ->
                        selectedTab = index
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavGraph(
                navController = navController,
                startDestination = Screen.Splash.route,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}
