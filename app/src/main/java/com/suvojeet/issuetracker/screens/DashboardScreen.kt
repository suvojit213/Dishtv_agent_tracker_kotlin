package com.suvojeet.issuetracker.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suvojeet.issuetracker.data.UserPreferencesRepository
import com.suvojeet.issuetracker.ui.theme.Poppins
import com.suvojeet.issuetracker.utils.parseHistoryEntry
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }

    var crmId by remember { mutableStateOf("") }
    var tlName by remember { mutableStateOf("") }
    var advisorName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var totalIssues by remember { mutableIntStateOf(0) }
    var issuesPerDay by remember { mutableStateOf(emptyMap<String, Int>()) }
    var issueTypeBreakdown by remember { mutableStateOf(emptyMap<String, Int>()) }

    val scrollState = rememberScrollState()
    var isFabExtended by remember { mutableStateOf(true) }

    LaunchedEffect(scrollState.value) {
        if (scrollState.value > 0 && isFabExtended) {
            isFabExtended = false
        } else if (scrollState.value == 0 && !isFabExtended) {
            isFabExtended = true
        }
    }

    LaunchedEffect(key1 = true) {
        isLoading = true
        // Load User Data
        crmId = userPreferencesRepository.crmId.first() ?: ""
        // For tlName and advisorName, you'd need to add them to UserPreferencesRepository
        // For now, using placeholders or assuming they are not stored in prefs
        tlName = "Your TL Name"
        advisorName = "Your Advisor Name"

        // Load Analytics Data
        // Assuming issueHistory is stored as a list of strings in SharedPreferences/DataStore
        // You'll need to add a method to UserPreferencesRepository to get issue history
        val issueHistory = listOf(
            "Fill Time: 2025-07-07T10:00:00 | Issue Explanation: Bug Fix",
            "Fill Time: 2025-07-07T11:00:00 | Issue Explanation: Feature Request",
            "Fill Time: 2025-07-08T09:00:00 | Issue Explanation: Bug Fix",
            "Fill Time: 2025-07-08T14:30:00 | Issue Explanation: UI Improvement"
        ) // Placeholder data

        val total = issueHistory.size
        val issuesPerDayMap = mutableMapOf<String, Int>()
        val issueTypeBreakdownMap = mutableMapOf<String, Int>()

        for (entry in issueHistory) {
            val parsedEntry = parseHistoryEntry(entry)
            val fillTime = parsedEntry["Fill Time"]
            val issueType = parsedEntry["Issue Explanation"]

            if (fillTime != null) {
                val date = LocalDate.parse(fillTime.substring(0, 10)) // Assuming YYYY-MM-DD format
                val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                issuesPerDayMap[formattedDate] = (issuesPerDayMap[formattedDate] ?: 0) + 1
            }

            if (issueType != null) {
                issueTypeBreakdownMap[issueType] = (issueTypeBreakdownMap[issueType] ?: 0) + 1
            }
        }

        totalIssues = total
        issuesPerDay = issuesPerDayMap
        issueTypeBreakdown = issueTypeBreakdownMap
        isLoading = false
    }

    val fadeAnimation by animateFloatAsState(
        targetValue = if (isLoading) 0f else 1f,
        animationSpec = tween(durationMillis = 400)
    )
    val slideAnimation by animateFloatAsState(
        targetValue = if (isLoading) 0.3f else 1f,
        animationSpec = tween(durationMillis = 400)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1E3A8A),
                                Color(0xFF3B82F6),
                                Color(0xFFF8FAFC),
                            ),
                            startY = 0.0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
                    .verticalScroll(scrollState)
                    .alpha(fadeAnimation)
                    .scale(slideAnimation)
                    .padding(24.dp)
            ) {
                // Custom App Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Issue Tracker App",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                        )
                    )
                    IconButton(
                        onClick = { navController.navigate("developer_info") }
                    ) {
                        Icon(Icons.Outlined.Info, contentDescription = "Info", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hero Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.95f),
                                    Color.White.copy(alpha = 0.85f),
                                ),
                                start = Offset(0.0f, 0.0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back, ${advisorName.split(" ").first()}!",
                        style = TextStyle(
                            color = Color(0xFF1E3A8A),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Track and manage your issues with precision",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            fontFamily = Poppins,
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Advisor Information Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF1E3A8A),
                                            Color(0xFF3B82F6)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Person, contentDescription = "Person", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Advisor Profile",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E3A8A),
                                fontFamily = Poppins,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    InfoRow(Icons.Outlined.Badge, "CRM ID", crmId)
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(Icons.Outlined.SupervisorAccount, "Team Leader", tlName)
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(Icons.Outlined.Person, "Advisor Name", advisorName)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { navController.navigate("edit_profile") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Edit Profile",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins,
                                color = Color.White,
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Analytics Section
                if (totalIssues > 0) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Your Activity",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E3A8A),
                                fontFamily = Poppins,
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AnalyticsCard(
                            title = "Total Issues Recorded",
                            value = "$totalIssues",
                            icon = Icons.Rounded.TaskAlt,
                            color = Color(0xFF059669)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AnalyticsCard(
                            title = "Issues Today",
                            value = "${issuesPerDay[LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)] ?: 0}",
                            icon = Icons.Rounded.CalendarToday,
                            color = Color(0xFF3B82F6)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (issueTypeBreakdown.isNotEmpty()) {
                            IssueTypeBreakdownCard(issueTypeBreakdown)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (isFabExtended) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("issue_tracker") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    icon = { Icon(Icons.Rounded.AddTask, contentDescription = "Fill Issue Tracker") },
                    text = { Text("Fill Issue Tracker") }
                )
            } else {
                FloatingActionButton(
                    onClick = { navController.navigate("issue_tracker") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Rounded.AddTask, contentDescription = "Fill Issue Tracker")
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1E3A8A).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = Color(0xFF3B82F6), modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.W600,
                    letterSpacing = 0.5.sp,
                    fontFamily = Poppins,
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (value.isNotEmpty()) value else "Not set",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF1E3A8A),
                    fontFamily = Poppins,
                )
            )
        }
    }
}

@Composable
fun AnalyticsCard(title: String, value: String, icon: ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.W500,
                    fontFamily = Poppins,
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A),
                    fontFamily = Poppins,
                )
            )
        }
    }
}

@Composable
fun IssueTypeBreakdownCard(issueTypeBreakdown: Map<String, Int>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Issue Type Breakdown",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins,
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        issueTypeBreakdown.forEach { (type, count) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = type,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        fontFamily = Poppins,
                    )
                )
                Text(
                    text = "$count",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A),
                        fontFamily = Poppins,
                    )
                )
            }
        }
    }
}
