package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.components.BottomNav
import com.example.myapplication.ui.components.TopBar
import com.example.myapplication.ui.screens.AssistantScreen
import com.example.myapplication.ui.screens.AuthScreen
import com.example.myapplication.ui.screens.DashboardScreen
import com.example.myapplication.ui.screens.CrewScreen
import com.example.myapplication.ui.screens.MapScreen
import com.example.myapplication.ui.screens.LogScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LifelineApp()
                }
            }
        }
    }
}

@Composable
fun LifelineApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "auth"

    Scaffold(
        topBar = { TopBar(connectionStatus = "OFFLINE") },
        bottomBar = {
            if (currentRoute != "auth") {
                BottomNav(currentRoute = currentRoute) { route ->
                    navController.navigate(route) {
                        popUpTo("dashboard") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "auth",
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            composable("auth") {
                AuthScreen(onAuthenticate = {
                    navController.navigate("dashboard") {
                        popUpTo("auth") { inclusive = true }
                    }
                })
            }
            composable("dashboard") {
                DashboardScreen()
            }
            composable("procedures") {
                LogScreen()
            }
            composable("assistant") {
                AssistantScreen()
            }
            composable("crew") {
                CrewScreen()
            }
            composable("map") {
                MapScreen()
            }
        }
    }
}