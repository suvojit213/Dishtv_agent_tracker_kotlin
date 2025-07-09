package com.suvojeet.issuetracker.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.PeopleOutline
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suvojeet.issuetracker.ui.components.SettingsTile
import com.suvojeet.issuetracker.ui.theme.Poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    var showAdminPasswordDialog by remember { mutableStateOf(false) }
    var adminPasswordInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // General Settings Section
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    SettingsTile(
                        icon = Icons.Rounded.PersonOutline,
                        title = "Edit Profile",
                        subtitle = "Update your CRM ID, Team Leader, and Advisor Name",
                        onClick = { navController.navigate("edit_profile") }
                    )
                    SettingsTile(
                        icon = Icons.Rounded.AdminPanelSettings,
                        title = "Admin Settings",
                        subtitle = "Access administrative configurations",
                        onClick = { showAdminPasswordDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About Section
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    SettingsTile(
                        icon = Icons.Outlined.Info,
                        title = "About App",
                        subtitle = "Learn more about the Issue Tracker App",
                        onClick = { navController.navigate("about_app") }
                    )
                    SettingsTile(
                        icon = Icons.Rounded.Code,
                        title = "Developer Info",
                        subtitle = "Information about the app developer",
                        onClick = { navController.navigate("developer_info") }
                    )
                    SettingsTile(
                        icon = Icons.Rounded.DataObject,
                        title = "View App Source Code",
                        subtitle = "Explore the code on GitHub",
                        onClick = {
                            val url = "https://github.com/suvojit213/issue_tracker"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not launch $url. Please ensure you have a web browser installed and an active internet connection.", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                    SettingsTile(
                        icon = Icons.Rounded.Feedback,
                        title = "Feedback",
                        subtitle = "Share your thoughts and suggestions",
                        onClick = { navController.navigate("feedback") }
                    )
                    SettingsTile(
                        icon = Icons.Rounded.PeopleOutline,
                        title = "Credits",
                        subtitle = "Meet the team behind the app",
                        onClick = { navController.navigate("credits") }
                    )
                }
            }
        }
    }

    if (showAdminPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showAdminPasswordDialog = false },
            title = { Text("Admin Access", fontFamily = Poppins, fontWeight = FontWeight.Bold) },
            text = {
                TextField(
                    value = adminPasswordInput,
                    onValueChange = { adminPasswordInput = it },
                    label = { Text("Enter password") },
                    singleLine = true,
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (adminPasswordInput == "01082005") {
                            showAdminPasswordDialog = false
                            navController.navigate("admin_settings")
                        } else {
                            Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Enter", fontFamily = Poppins, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdminPasswordDialog = false }) {
                    Text("Cancel", fontFamily = Poppins)
                }
            }
        )
    }
}

