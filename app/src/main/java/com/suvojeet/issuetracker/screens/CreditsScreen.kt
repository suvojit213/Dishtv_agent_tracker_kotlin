package com.suvojeet.issuetracker.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suvojeet.issuetracker.ui.theme.Poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Credits",
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Project Lead",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ContributorTile(
                        name = "Suvojeet Sengupta",
                        role = "App Developer, App Idea & Concept Designer",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Contributors for Ideas & Improvements",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ContributorTile(
                        name = "Dheeraj Ravidas",
                        role = "Contributor (Ideas & Improvements)",
                    )
                    ContributorTile(
                        name = "Mouma Sengupta",
                        role = "Contributor (Ideas & Improvements)",
                    )
                    ContributorTile(
                        name = "Sudhanshu",
                        role = "Contributor (Ideas & Improvements)",
                    )
                    ContributorTile(
                        name = "Monika",
                        role = "Contributor (Ideas & Improvements)",
                    )
                    ContributorTile(
                        name = "Rimmi",
                        role = "Contributor (Ideas & Improvements)",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Team Leadership",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ContributorTile(
                        name = "Manish Sir",
                        role = "Team Leader",
                    )
                }
            }

            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Special Thanks",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "A heartfelt thank you to everyone who contributed their time, effort, and valuable insights to make this project a success.",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ContributorTile(name: String, role: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.Person, contentDescription = "Person", tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = name,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                )
            )
            Text(
                text = role,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = Color.Gray,
                )
            )
        }
    }
}