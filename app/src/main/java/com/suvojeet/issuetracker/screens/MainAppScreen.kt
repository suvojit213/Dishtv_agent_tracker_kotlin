package com.suvojeet.issuetracker.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.suvojeet.issuetracker.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first

@Composable
fun MainAppScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Tracker", "History", "Settings")
    val icons = listOf(Icons.Rounded.Home, Icons.Rounded.AddTask, Icons.Rounded.History, Icons.Rounded.Settings)

    LaunchedEffect(key1 = true) {
        val onboardingComplete = userPreferencesRepository.interactiveOnboardingComplete.first()
        if (!onboardingComplete) {
            // TODO: Implement interactive onboarding tour here
            userPreferencesRepository.setInteractiveOnboardingComplete(true)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> DashboardScreen(navController = navController)
                1 -> IssueTrackerScreen(navController = navController)
                2 -> HistoryScreen(navController = navController)
                3 -> SettingsScreen(navController = navController)
            }
        }
    }
}