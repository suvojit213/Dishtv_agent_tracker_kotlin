package com.suvojeet.issuetracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.rounded.Storage
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suvojeet.issuetracker.data.UserPreferencesRepository
import com.suvojeet.issuetracker.data.dataStore
import com.suvojeet.issuetracker.ui.theme.Poppins
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    var showRawSharedPreferencesDialog by remember { mutableStateOf(false) }
    val rawSharedPreferencesData by context.dataStore.data.map { preferences ->
        preferences.asMap().entries.joinToString("\n") { "${it.key.name}: ${it.value}" }
    }.collectAsState(initial = "Loading...")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Admin Settings",
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
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    SettingsTile(
                        icon = Icons.Rounded.Storage,
                        title = "View Raw SharedPreferences",
                        subtitle = "Display all locally stored key-value pairs",
                        onClick = { showRawSharedPreferencesDialog = true }
                    )
                }
            }
        }
    }

    if (showRawSharedPreferencesDialog) {
        AlertDialog(
            onDismissRequest = { showRawSharedPreferencesDialog = false },
            title = { Text("Raw SharedPreferences Data", fontFamily = Poppins, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = if (rawSharedPreferencesData.isNotEmpty()) rawSharedPreferencesData else "No data found.", fontFamily = Poppins)
                }
            },
            confirmButton = {
                TextButton(onClick = { showRawSharedPreferencesDialog = false }) {
                    Text("Close", fontFamily = Poppins, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun SettingsTile(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            }
        },
        headlineContent = {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                )
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = Color.Gray,
                )
            )
        },
        trailingContent = {
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "Go", modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
    )
}