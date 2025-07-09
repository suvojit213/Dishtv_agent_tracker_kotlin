package com.suvojeet.issuetracker.screens

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.InfoOutlined
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material.icons.rounded.SupervisorAccount
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.InfoOutlined
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.suvojeet.issuetracker.data.UserPreferencesRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueTrackerScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var crmId by remember { mutableStateOf("") }
    var tlName by remember { mutableStateOf("") }
    var advisorName by remember { mutableStateOf("") }
    var organization by remember { mutableStateOf("") }
    var selectedIssueExplanation by remember { mutableStateOf("System Hang / Voice Issue") }
    var selectedReason by remember { mutableStateOf("System issue (Network , Asset & Aspect /WDE issue)") }
    var issueRemarks by remember { mutableStateOf("") }

    var issueStartHour by remember { mutableIntStateOf(-1) }
    var issueStartMinute by remember { mutableIntStateOf(-1) }
    var issueStartPeriod by remember { mutableStateOf("AM") }

    var issueEndHour by remember { mutableIntStateOf(-1) }
    var issueEndMinute by remember { mutableIntStateOf(-1) }
    var issueEndPeriod by remember { mutableStateOf("AM") }

    val images = remember { mutableStateListOf<Uri>() }

    val pickImagesLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        images.addAll(uris)
    }

    LaunchedEffect(key1 = true) {
        userPreferencesRepository.crmId.collect { crmId = it ?: "" }
        userPreferencesRepository.tlName.collect { tlName = it ?: "" }
        userPreferencesRepository.advisorName.collect { advisorName = it ?: "" }
        userPreferencesRepository.organization.collect { organization = it ?: "" }
    }

    var startAnimation by remember { mutableStateOf(false) }
    val slideAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 500)
    )
    val fadeAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
    }

    fun formatTime(hour: Int, minute: Int, period: String): String {
        if (hour == -1 || minute == -1) return "Select Time"
        val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return String.format("%02d:%02d %s", h, minute, period)
    }

    fun isFormValid(): Boolean {
        return issueStartHour != -1 && issueStartMinute != -1 &&
                issueEndHour != -1 && issueEndMinute != -1
    }

    fun submitIssue() {
        if (!isFormValid()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "Please select both start and end times.",
                    withDismissAction = true
                )
            }
            return
        }

        coroutineScope.launch {
            val savedImagePaths = mutableListOf<String>()
            images.forEach { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = "IMG_${System.currentTimeMillis()}.jpg"
                val outputFile = File(context.filesDir, fileName)
                val outputStream = FileOutputStream(outputFile)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                savedImagePaths.add(outputFile.absolutePath)
            }

            val startDateTime = LocalDateTime.now()
                .withHour(if (issueStartPeriod == "PM" && issueStartHour != 12) issueStartHour + 12 else if (issueStartPeriod == "AM" && issueStartHour == 12) 0 else issueStartHour)
                .withMinute(issueStartMinute)
            val endDateTime = LocalDateTime.now()
                .withHour(if (issueEndPeriod == "PM" && issueEndHour != 12) issueEndHour + 12 else if (issueEndPeriod == "AM" && issueEndHour == 12) 0 else issueEndHour)
                .withMinute(issueEndMinute)

            val entry = buildString {
                append("CRM ID: $crmId, ")
                append("TL Name: $tlName, ")
                append("Advisor Name: $advisorName, ")
                append("Organization: $organization, ")
                append("Issue Explanation: $selectedIssueExplanation, ")
                append("Reason: $selectedReason, ")
                append("Start Time: ${startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}, ")
                append("End Time: ${endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}, ")
                append("Fill Time: ${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}, ")
                append("Issue Remarks: $issueRemarks")
                if (savedImagePaths.isNotEmpty()) {
                    append(", Images: ${savedImagePaths.joinToString("|")}")
                }
            }

            userPreferencesRepository.addIssueToHistory(entry)

            openGoogleForm(
                context,
                crmId,
                advisorName,
                tlName,
                organization,
                selectedIssueExplanation,
                selectedReason,
                startDateTime,
                endDateTime,
                navController
            )

            Toast.makeText(context, "Issue Tracker filled successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
        ) {
            // Custom App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Text(
                    text = "Fill Issue Tracker",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                    )
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { navController.navigate("developer_info") }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.InfoOutlined,
                        contentDescription = "Info",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .alpha(fadeAnimation)
                    .scale(slideAnimation)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Advisor Information Card
                EnhancedCard(
                    content = {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFF1E3A8A),
                                                    Color(0xFF3B82F6)
                                                )
                                            )
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.PersonOutline,
                                        contentDescription = "Advisor Information",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Advisor Information",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E3A8A),
                                        fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            EnhancedInfoRow(Icons.Outlined.Badge, "CRM ID", crmId)
                            Spacer(modifier = Modifier.height(12.dp))
                            EnhancedInfoRow(Icons.Rounded.SupervisorAccount, "Team Leader", tlName)
                            Spacer(modifier = Modifier.height(12.dp))
                            EnhancedInfoRow(Icons.Rounded.PersonOutline, "Advisor Name", advisorName)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Time Selection Section
                Text(
                    text = "Issue Timing",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A),
                        fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                EnhancedTimeSelector(
                    icon = Icons.Rounded.PlayCircleOutline,
                    title = "Issue Start Time",
                    hour = issueStartHour,
                    minute = issueStartMinute,
                    period = issueStartPeriod,
                    onTimeSelected = { h, m, p ->
                        issueStartHour = h
                        issueStartMinute = m
                        issueStartPeriod = p
                    },
                    gradient = Brush.linearGradient(
                        colors = listOf(Color(0xFF059669), Color(0xFF10B981))
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                EnhancedTimeSelector(
                    icon = Icons.Rounded.StopCircle,
                    title = "Issue End Time",
                    hour = issueEndHour,
                    minute = issueEndMinute,
                    period = issueEndPeriod,
                    onTimeSelected = { h, m, p ->
                        issueEndHour = h
                        issueEndMinute = m
                        issueEndPeriod = p
                    },
                    gradient = Brush.linearGradient(
                        colors = listOf(Color(0xFFEF4444), Color(0xFFF87171))
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Issue Explanation
                IssueExplanationDropdownField(
                    selectedIssueExplanation = selectedIssueExplanation,
                    onIssueExplanationSelected = { selectedIssueExplanation = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Attachment Section
                AttachmentSection(
                    images = images,
                    onPickImages = { pickImagesLauncher.launch("image/*") },
                    onRemoveImage = { index -> images.removeAt(index) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Reason Section
                ReasonSelection(
                    selectedReason = selectedReason,
                    onReasonSelected = { selectedReason = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Issue Remarks
                IssueRemarksField(
                    issueRemarks = issueRemarks,
                    onValueChange = { issueRemarks = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Submit Button
                Button(
                    onClick = { submitIssue() },
                    enabled = isFormValid(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFormValid()) Color(0xFF3B82F6) else Color(0xFFE0E0E0),
                        contentColor = if (isFormValid()) Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                ) {
                    Icon(Icons.Rounded.Send, contentDescription = "Submit", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Submit Issue and Open Form",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFormValid()) Color.White else Color.Gray,
                            fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                        ),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A).copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(width = 1.5.dp, color = Color(0xFF1E3A8A).copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E3A8A).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.InfoOutlined, contentDescription = "Info", tint = Color(0xFF1E3A8A), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "After submitting, you will be redirected to a Google Form Please Confirm additional details And Submit.",
                            style = TextStyle(
                                color = Color(0xFF1E3A8A),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                            )
                        )
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun EnhancedCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            content()
        }
    }
}

@Composable
fun EnhancedInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
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
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.W600,
                    letterSpacing = 0.5.sp,
                    fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (value.isNotEmpty()) value else "Not set",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF1E3A8A),
                    fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                )
            )
        }
    }
}

@Composable
fun EnhancedTimeSelector(
    icon: ImageVector,
    title: String,
    hour: Int,
    minute: Int,
    period: String,
    onTimeSelected: (Int, Int, String) -> Unit,
    gradient: Brush
) {
    val context = LocalContext.current
    EnhancedCard(
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(gradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A),
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray.copy(alpha = 0.2f))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            .clickable {
                                val currentHour = if (hour != -1) hour else 0
                                val currentMinute = if (minute != -1) minute else 0
                                TimePickerDialog(
                                    context,
                                    { _, selectedHour, selectedMinute ->
                                        val p = if (selectedHour >= 12) "PM" else "AM"
                                        val h = if (selectedHour > 12) selectedHour - 12 else if (selectedHour == 0) 12 else selectedHour
                                        onTimeSelected(h, selectedMinute, p)
                                    },
                                    currentHour,
                                    currentMinute,
                                    false // 24 hour format
                                ).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = formatTime(hour, minute, period),
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = if (hour == -1 || minute == -1) Color.Gray else (gradient as? Brush.LinearGradient)?.colors?.first() ?: Color.Black,
                                fontWeight = FontWeight.W600,
                            )
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueExplanationDropdownField(
    selectedIssueExplanation: String,
    onIssueExplanationSelected: (String) -> Unit
) {
    val issueOptions = listOf(
        "Electricity Issue/Power Failure",
        "Head Phone Issue",
        "Wifi Stopped Working",
        "System Hang / Voice Issue",
        "Voice Issue / Cx Voice Not Audible",
        "Mobile Phone Hang",
        "Auto Call Drop",
        "Aspect / WDE issue",
        "Mobile Network Connectivity",
    )
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Explain Issue",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        EnhancedCard(
            content = {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = selectedIssueExplanation,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.HelpOutline,
                                contentDescription = "Explain Issue",
                                tint = Color(0xFF3B82F6),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF1E3A8A),
                            fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        issueOptions.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item, fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins) },
                                onClick = {
                                    onIssueExplanationSelected(item)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun AttachmentSection(
    images: List<Uri>,
    onPickImages: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Issue Snap",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        EnhancedCard(
            content = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(if (images.isEmpty()) 120.dp else ((images.size / 3) + 1) * 120.dp) // Adjust height dynamically
                ) {
                    itemsIndexed(images) { index, uri ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { onRemoveImage(index) },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(Icons.Rounded.Close, contentDescription = "Remove Image", tint = Color.White)
                            }
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray.copy(alpha = 0.2f))
                                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                                .clickable { onPickImages() },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Rounded.AddAPhoto,
                                    contentDescription = "Add Image",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Add Image",
                                    style = TextStyle(
                                        color = Color.Gray,
                                        fontWeight = FontWeight.W600
                                    )
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ReasonSelection(
    selectedReason: String,
    onReasonSelected: (String) -> Unit
) {
    val reasonOptions = listOf(
        "Voice Issue",
        "System issue (Network , Asset & Aspect /WDE issue)",
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Reason",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        EnhancedCard(
            content = {
                Column {
                    reasonOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selectedReason == option) Color(0xFF1E3A8A).copy(alpha = 0.1f) else Color.White)
                                .border(
                                    width = if (selectedReason == option) 2.dp else 1.dp,
                                    color = if (selectedReason == option) Color(0xFF1E3A8A) else Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onReasonSelected(option) }
                                .padding(horizontal = 8.dp)
                        ) {
                            RadioButton(
                                selected = (selectedReason == option),
                                onClick = { onReasonSelected(option) },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1E3A8A))
                            )
                            Text(
                                text = option,
                                style = TextStyle(
                                    fontWeight = FontWeight.W500,
                                    color = if (selectedReason == option) Color(0xFF1E3A8A) else Color.DarkGray,
                                    fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
                                )
                            )
                        }
                        if (option != reasonOptions.last()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun IssueRemarksField(
    issueRemarks: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Issue Remarks (Optional)",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        EnhancedCard(
            content = {
                TextField(
                    value = issueRemarks,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Enter any additional remarks about the issue...",
                            style = TextStyle(
                                fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins,
                                color = Color.Gray
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Notes,
                            contentDescription = "Remarks",
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(
                        fontFamily = com.suvojeet.issuetracker.ui.theme.Poppins,
                        color = Color(0xFF1E3A8A),
                        fontWeight = FontWeight.W500,
                    ),
                    maxLines = 3
                )
            }
        )
    }
}

fun openGoogleForm(
    context: android.content.Context,
    crmId: String,
    advisorName: String,
    tlName: String,
    organization: String,
    issueExplanation: String,
    reason: String,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    navController: NavController
) {
    val crmIdEntryId = "1005447471"
    val advisorNameEntryId = "44222229"
    val startTimeHourEntryId = "1521239602_hour"
    val startTimeMinuteEntryId = "1521239602_minute"
    val endTimeHourEntryId = "701130970_hour"
    val endTimeMinuteEntryId = "701130970_minute"
    val tlNameEntryId = "115861300"
    val organizationEntryId = "313975949"
    val startDateYearEntryId = "702818104_year"
    val startDateMonthEntryId = "702818104_month"
    val startDateDayEntryId = "702818104_day"
    val endDateYearEntryId = "514450388_year"
    val endDateMonthEntryId = "514450388_month"
    val endDateDayEntryId = "514450388_day"
    val explainIssueEntryId = "1211413190"
    val reasonEntryId = "1231067802"

    val encodedCrmId = Uri.encode(crmId)
    val encodedAdvisorName = Uri.encode(advisorName)
    val encodedTlName = Uri.encode(tlName)
    val encodedOrganization = Uri.encode(organization)
    val encodedIssueExplanation = Uri.encode(issueExplanation)
    val encodedReason = Uri.encode(reason)

    val startTimeHour = startTime.hour.toString().padStart(2, '0')
    val startTimeMinute = startTime.minute.toString().padStart(2, '0')
    val endTimeHour = endTime.hour.toString().padStart(2, '0')
    val endTimeMinute = endTime.minute.toString().padStart(2, '0')

    val currentYear = LocalDateTime.now().year.toString()
    val currentMonth = LocalDateTime.now().monthValue.toString()
    val currentDay = LocalDateTime.now().dayOfMonth.toString()

    var url = "https://docs.google.com/forms/d/e/1FAIpQLSdeWylhfFaHmM3osSGRbxh9S_XvnAEPCIhTemuh-I7-LNds_w/viewform?usp=pp_url"
    url += "&entry." + crmIdEntryId + "=" + encodedCrmId
    url += "&entry." + advisorNameEntryId + "=" + encodedAdvisorName
    url += "&entry." + startTimeHourEntryId + "=" + startTimeHour
    url += "&entry." + startTimeMinuteEntryId + "=" + startTimeMinute
    url += "&entry." + endTimeHourEntryId + "=" + endTimeHour
    url += "&entry." + endTimeMinuteEntryId + "=" + endTimeMinute
    url += "&entry." + tlNameEntryId + "=" + encodedTlName
    url += "&entry." + organizationEntryId + "=" + encodedOrganization
    url += "&entry." + startDateYearEntryId + "=" + currentYear
    url += "&entry." + startDateMonthEntryId + "=" + currentMonth
    url += "&entry." + startDateDayEntryId + "=" + currentDay
    url += "&entry." + endDateYearEntryId + "=" + currentYear
    url += "&entry." + endDateMonthEntryId + "=" + currentMonth
    url += "&entry." + endDateDayEntryId + "=" + currentDay
    url += "&entry." + explainIssueEntryId + "=" + encodedIssueExplanation
    url += "&entry." + reasonEntryId + "=" + encodedReason

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

private fun formatTime(hour: Int, minute: Int, period: String): String {
    if (hour == -1 || minute == -1) return "Select Time"
    val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    return String.format("%02d:%02d %s", h, minute, period)
}