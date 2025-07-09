package com.suvojeet.issuetracker.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.suvojeet.issuetracker.data.UserPreferencesRepository
import com.suvojeet.issuetracker.ui.theme.Poppins
import com.suvojeet.issuetracker.utils.parseHistoryEntry
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.suvojeet.issuetracker.data.dataStore
import java.time.format.DateTimeParseException
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.rounded.Warning
import androidx.datastore.preferences.core.edit

@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val issueHistory by userPreferencesRepository.issueHistory.collectAsState(initial = emptySet())
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedStartTime by remember { mutableStateOf<LocalTime?>(null) }
    var selectedEndTime by remember { mutableStateOf<LocalTime?>(null) }

    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showDeleteEntryDialog by remember { mutableStateOf<Int?>(null) }

    val filteredHistory = remember(issueHistory, selectedDate, selectedStartTime, selectedEndTime) {
        issueHistory.filter { entry ->
            val parsedEntry = parseHistoryEntry(entry)
            val fillTimeStr = parsedEntry["Fill Time"]
            if (fillTimeStr == null) return@filter false

            try {
                val entryDateTime = LocalDateTime.parse(fillTimeStr)
                val entryDate = entryDateTime.toLocalDate()
                val entryTime = entryDateTime.toLocalTime()

                val dateMatches = selectedDate == null || entryDate == selectedDate

                val timeMatches = if (selectedDate != null && (selectedStartTime != null || selectedEndTime != null)) {
                    val issueStartTimeStr = parsedEntry["Start Time"]
                    val issueEndTimeStr = parsedEntry["End Time"]

                    if (issueStartTimeStr == null || issueEndTimeStr == null) false
                    else {
                        val issueStartTime = LocalDateTime.parse(issueStartTimeStr).toLocalTime()
                        val issueEndTime = LocalDateTime.parse(issueEndTimeStr).toLocalTime()

                        val startMatch = selectedStartTime == null ||
                                issueStartTime.isAfter(selectedStartTime) ||
                                issueStartTime == selectedStartTime ||
                                (issueStartTime.isBefore(selectedStartTime) &&
                                        java.time.Duration.between(issueStartTime, selectedStartTime).toMinutes() <= 15)

                        val endMatch = selectedEndTime == null ||
                                issueEndTime.isBefore(selectedEndTime) ||
                                issueEndTime == selectedEndTime ||
                                (issueEndTime.isAfter(selectedEndTime) &&
                                        java.time.Duration.between(selectedEndTime, issueEndTime).toMinutes() <= 15)
                        startMatch && endMatch
                    }
                } else true

                dateMatches && timeMatches
            } catch (e: DateTimeParseException) {
                false
            }
        }.sortedByDescending {
            // Sort by fill time for consistent order
            val fillTimeStr = parseHistoryEntry(it)["Fill Time"]
            try { LocalDateTime.parse(fillTimeStr) } catch (e: Exception) { LocalDateTime.MIN }
        }
    }.toList()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3A8A),
                            Color(0xFFF8FAFC),
                        ),
                        startY = 0.0f,
                        endY = 0.3f * context.resources.displayMetrics.density // Corresponds to 0.3 stop in Flutter
                    )
                )
                .padding(innerPadding)
        ) {
            // Custom App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Issue History",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins
                    ),
                    modifier = Modifier.weight(1f)
                )
                AnimatedVisibility(
                    visible = issueHistory.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { showClearHistoryDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = "Clear History",
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Content
            if (issueHistory.isEmpty()) {
                EmptyState(
                    onRecordFirstIssue = {
                        navController.navigate("issue_tracker") {
                            popUpTo("main_app") { inclusive = false }
                        }
                    }
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    val year = selectedDate?.year ?: LocalDate.now().year
                                    val month = selectedDate?.monthValue?.minus(1) ?: LocalDate.now().monthValue - 1
                                    val day = selectedDate?.dayOfMonth ?: LocalDate.now().dayOfMonth
                                    DatePickerDialog(context, { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                                        selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
                                        selectedStartTime = null
                                        selectedEndTime = null
                                    }, year, month, day).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Rounded.CalendarToday, contentDescription = "Select Date", tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "Select Date",
                                    color = Color.White,
                                    fontFamily = Poppins
                                )
                            }
                            AnimatedVisibility(
                                visible = selectedDate != null,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(
                                    onClick = {
                                        selectedDate = null
                                        selectedStartTime = null
                                        selectedEndTime = null
                                    }
                                ) {
                                    Icon(Icons.Rounded.Clear, contentDescription = "Clear Date", tint = Color.Gray)
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = selectedDate != null,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            val hour = selectedStartTime?.hour ?: 0
                                            val minute = selectedStartTime?.minute ?: 0
                                            TimePickerDialog(context, { _, h, m ->
                                                selectedStartTime = LocalTime.of(h, m)
                                            }, hour, minute, false).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Rounded.AccessTime, contentDescription = "Start Time", tint = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = selectedStartTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Start Time",
                                            color = Color.White,
                                            fontFamily = Poppins
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            val hour = selectedEndTime?.hour ?: 0
                                            val minute = selectedEndTime?.minute ?: 0
                                            TimePickerDialog(context, { _, h, m ->
                                                selectedEndTime = LocalTime.of(h, m)
                                            }, hour, minute, false).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Rounded.AccessTime, contentDescription = "End Time", tint = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = selectedEndTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "End Time",
                                            color = Color.White,
                                            fontFamily = Poppins
                                        )
                                    }
                                }
                                AnimatedVisibility(
                                    visible = selectedStartTime != null || selectedEndTime != null,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(
                                            onClick = {
                                                selectedStartTime = null
                                                selectedEndTime = null
                                            }
                                        ) {
                                            Icon(Icons.Rounded.Clear, contentDescription = "Clear Times", tint = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (filteredHistory.isEmpty()) {
                        NoResultsState()
                    } else {
                        HistoryList(filteredHistory, navController, userPreferencesRepository, snackbarHostState, context)
                    }
                }
            }
        }
    }

    if (showClearHistoryDialog) {
        ClearHistoryDialog(
            onConfirm = {
                coroutineScope.launch {
                    userPreferencesRepository.clearIssueHistory()
                    showClearHistoryDialog = false
                    snackbarHostState.showSnackbar(
                        message = "History cleared successfully",
                        withDismissAction = true
                    )
                }
            },
            onDismiss = { showClearHistoryDialog = false }
        )
    }

    showDeleteEntryDialog?.let { indexToDelete ->
        DeleteEntryDialog(
            onConfirm = {
                coroutineScope.launch {
                    val currentHistory = issueHistory.toMutableSet()
                    val entryToDelete = filteredHistory[indexToDelete]
                    currentHistory.remove(entryToDelete)
                    userPreferencesRepository.clearIssueHistory() // Clear and re-add to update the set
                    currentHistory.forEach { entry -> launch { userPreferencesRepository.addIssueToHistory(entry) } }
                    showDeleteEntryDialog = null
                    snackbarHostState.showSnackbar(
                        message = "Entry deleted successfully",
                        withDismissAction = true
                    )
                }
            },
            onDismiss = { showDeleteEntryDialog = null }
        )
    }
}

@Composable
fun EmptyState(onRecordFirstIssue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(60.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1E3A8A).copy(alpha = 0.1f),
                            Color(0xFF3B82F6).copy(alpha = 0.05f),
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.History, contentDescription = "No History", tint = Color(0xFF1E3A8A), modifier = Modifier.size(60.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "No History Yet",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Your issue tracking history will appear here once you start recording issues.",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                fontFamily = Poppins
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onRecordFirstIssue,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Icon(Icons.Rounded.AddTask, contentDescription = "Record Issue", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Record First Issue",
                color = Color.White,
                fontFamily = Poppins
            )
        }
    }
}

@Composable
fun NoResultsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1E3A8A).copy(alpha = 0.1f),
                            Color(0xFF3B82F6).copy(alpha = 0.05f),
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.SearchOff, contentDescription = "No Results", tint = Color(0xFF1E3A8A), modifier = Modifier.size(50.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Matching Results",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Try adjusting your search terms or clearing the search bar.",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                fontFamily = Poppins
            )
        )
    }
}

@Composable
fun HistoryList(
    filteredHistory: List<String>,
    navController: NavController,
    userPreferencesRepository: UserPreferencesRepository,
    snackbarHostState: SnackbarHostState,
    context: android.content.Context
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Recent Issues",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(filteredHistory) { index, entry ->
                HistoryItem(
                    entry = entry,
                    index = index,
                    navController = navController,
                    onShare = { parsedEntry, imagePaths ->
                        shareIssue(context, parsedEntry, imagePaths)
                    },
                    onDelete = { idx ->
                        coroutineScope.launch {
                            context.dataStore.edit { preferences ->
                                val currentHistory = preferences[UserPreferencesRepository.PreferencesKeys.ISSUE_HISTORY] ?: emptySet()
                                val updatedHistory = currentHistory.toMutableSet()
                                val entryToDelete = filteredHistory[idx]
                                updatedHistory.remove(entryToDelete)
                                preferences[UserPreferencesRepository.PreferencesKeys.ISSUE_HISTORY] = updatedHistory.toSet()
                            }
                            snackbarHostState.showSnackbar(
                                message = "Entry deleted successfully",
                                withDismissAction = true
                            )
                        }
                    },
                    context = context
                )
            }
        }
    }
}

@Composable
fun HistoryItem(
    entry: String,
    index: Int,
    navController: NavController,
    onShare: (Map<String, String>, List<String>) -> Unit,
    onDelete: (Int) -> Unit,
    context: android.content.Context
) {
    val parsedEntry = parseHistoryEntry(entry)
    val imagePaths = parsedEntry["Images"]?.split('|') ?: emptyList()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // navController.navigate("issue_detail/${Uri.encodeQueryToString(parsedEntry.entries.associate { it.key to it.value }.toString())}/${Uri.encodeQueryToString(imagePaths.joinToString(","))}")
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(colors = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Issue #${index + 1}", // Flutter was _issueHistory.length - index, adjust as needed
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins
                    )
                }
                Row {
                    IconButton(onClick = { onShare(parsedEntry, imagePaths) }) {
                        Icon(Icons.Rounded.Share, contentDescription = "Share", tint = Color.Blue)
                    }
                    IconButton(onClick = { onDelete(index) }) {
                        Icon(Icons.Rounded.DeleteOutline, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F9FF))
                    .border(1.dp, Color(0xFF3B82F6).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF3B82F6).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.ReportProblem, contentDescription = "Issue Details", tint = Color(0xFF3B82F6), modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Issue Details",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3B82F6),
                            fontFamily = Poppins
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                IssueDetailRow(label = "Issue Type", value = parsedEntry["Issue Explanation"] ?: "N/A")
                Spacer(modifier = Modifier.height(8.dp))
                IssueDetailRow(label = "Reason", value = parsedEntry["Reason"] ?: "N/A")
                parsedEntry["Issue Remarks"]?.let {
                    if (it.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        IssueDetailRow(label = "Remarks", value = it)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1E3A8A).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.AccessTime, contentDescription = "Time Information", tint = Color(0xFF1E3A8A), modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Time Information",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A),
                            fontFamily = Poppins
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimeInfo(
                        icon = Icons.Rounded.PlayCircleOutline,
                        label = "Start Time",
                        timeIso = parsedEntry["Start Time"] ?: "",
                        color = Color(0xFF059669)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFFE2E8F0))
                    )
                    TimeInfo(
                        icon = Icons.Rounded.StopCircle,
                        label = "End Time",
                        timeIso = parsedEntry["End Time"] ?: "",
                        color = Color(0xFFEF4444)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1E3A8A).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Timer, contentDescription = "Duration", tint = Color(0xFF3B82F6), modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Duration: ",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.W500,
                            fontFamily = Poppins
                        )
                    )
                    Text(
                        text = formatDuration(parsedEntry["Start Time"] ?: "", parsedEntry["End Time"] ?: ""),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            color = Color(0xFF1E3A8A),
                            fontFamily = Poppins
                        )
                    )
                }
            }
            if (imagePaths.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height((imagePaths.size * 100).dp) // Adjust height based on image count
                ) {
                    items(imagePaths) { imagePath ->
                        var showImageDialog by remember { mutableStateOf(false) }
                        Image(
                            painter = rememberAsyncImagePainter(File(imagePath)),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showImageDialog = true },
                            contentScale = ContentScale.Crop
                        )
                        if (showImageDialog) {
                            Dialog(onDismissRequest = { showImageDialog = false }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.7f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(File(imagePath)),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                    IconButton(
                                        onClick = { shareImage(context, imagePath) },
                                        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                                    ) {
                                        Icon(Icons.Rounded.Download, contentDescription = "Download", tint = Color.White, modifier = Modifier.size(36.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IssueDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${label}: ",
            style = TextStyle(
                fontSize = 13.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                fontFamily = Poppins
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFF3B82F6),
                fontFamily = Poppins
            )
        )
    }
}

@Composable
fun TimeInfo(icon: ImageVector, label: String, timeIso: String, color: Color) {
    val formattedTime = formatTime(timeIso)
    val formattedDate = formatOnlyDate(timeIso)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                fontFamily = Poppins
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = formattedDate,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                color = color,
                fontFamily = Poppins
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = formattedTime,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                fontFamily = Poppins
            )
        )
    }
}

@Composable
fun ClearHistoryDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Yellow.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Warning, contentDescription = "Warning", tint = Color.Yellow, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Clear History",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A),
                        fontFamily = Poppins
                    )
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to clear all issue history? This action cannot be undone.",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Poppins
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Clear", fontFamily = Poppins, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontFamily = Poppins, color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun DeleteEntryDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.DeleteForever, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Delete Entry",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A),
                        fontFamily = Poppins
                    )
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to delete this history entry? This action cannot be undone.",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Poppins
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Delete", fontFamily = Poppins, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontFamily = Poppins, color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}