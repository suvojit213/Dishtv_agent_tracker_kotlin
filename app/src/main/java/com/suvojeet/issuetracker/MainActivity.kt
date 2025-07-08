package com.suvojeet.issuetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.suvojeet.issuetracker.ui.theme.IssueTrackerTheme
import com.suvojeet.issuetracker.screens.SplashScreen
import com.suvojeet.issuetracker.screens.MainAppScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IssueTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(navController = navController)
                        }
                        composable("main_app") {
                            MainAppScreen(navController = navController)
                        }
                        composable("initial_setup") {
                            InitialSetupScreen(navController = navController)
                        }
                        composable("developer_info") {
                            DeveloperInfoScreen(navController = navController)
                        }
                        composable("edit_profile") {
                            EditProfileScreen(navController = navController)
                        }
                        composable("google_form_webview/{formUrl}") {
                            val formUrl = it.arguments?.getString("formUrl") ?: ""
                            GoogleFormWebviewScreen(navController = navController, formUrl = formUrl)
                        }
                        composable("admin_settings") {
                            AdminSettingsScreen(navController = navController)
                        }
                        composable("about_app") {
                            AboutAppScreen(navController = navController)
                        }
                        composable("credits") {
                            CreditsScreen(navController = navController)
                        }
                        composable("feedback") {
                            FeedbackScreen(navController = navController)
                        }
                        // Define other routes here as you convert screens
                    }
                }
            }
        }
    }
}
