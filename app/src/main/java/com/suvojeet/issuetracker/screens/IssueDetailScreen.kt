package com.suvojeet.issuetracker.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.suvojeet.issuetracker.ui.theme.Poppins
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailScreen(navController: NavController, issueDetails: Map<String, String>, imagePaths: List<String>) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Issue Details",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(
                        onClick = { shareIssue(context, issueDetails, imagePaths) }
                    ) {
                        Icon(Icons.Rounded.Share, contentDescription = "Share", tint = Color.White)
                    }
                }
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
            IssueInfoCard(issueDetails)
            Spacer(modifier = Modifier.height(20.dp))
            TimeInfoCard(issueDetails)
            Spacer(modifier = Modifier.height(20.dp))
            if (imagePaths.isNotEmpty()) {
                AttachmentsCard(context, imagePaths)
            }
        }
    }
}

@Composable
fun IssueInfoCard(issueDetails: Map<String, String>) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SectionTitle(title = "Issue Information", icon = Icons.Outlined.Info)
            Spacer(modifier = Modifier.height(12.dp))
            DetailRow(label = "Advisor Name", value = issueDetails["Advisor Name"] ?: "N/A")
            DetailRow(label = "CRM ID", value = issueDetails["CRM ID"] ?: "N/A")
            DetailRow(label = "Organization", value = issueDetails["Organization"] ?: "N/A")
            DetailRow(label = "Issue Type", value = issueDetails["Issue Explanation"] ?: "N/A")
            DetailRow(label = "Reason", value = issueDetails["Reason"] ?: "N/A")
            issueDetails["Issue Remarks"]?.let {
                if (it.isNotEmpty()) {
                    DetailRow(label = "Remarks", value = it)
                }
            }
        }
    }
}

@Composable
fun TimeInfoCard(issueDetails: Map<String, String>) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SectionTitle(title = "Time Information", icon = Icons.Rounded.AccessTime)
            Spacer(modifier = Modifier.height(12.dp))
            val fillTime = issueDetails["Fill Time"] ?: ""
            val startTime = issueDetails["Start Time"] ?: ""
            val endTime = issueDetails["End Time"] ?: ""

            DetailRow(label = "Fill Time", value = "${formatOnlyDate(fillTime)} at ${formatTime(fillTime)}")
            DetailRow(label = "Start Time", value = "${formatOnlyDate(startTime)} at ${formatTime(startTime)}")
            DetailRow(label = "End Time", value = "${formatOnlyDate(endTime)} at ${formatTime(endTime)}")
            DetailRow(label = "Duration", value = formatDuration(startTime, endTime))
        }
    }
}

@Composable
fun AttachmentsCard(context: android.content.Context, imagePaths: List<String>) {
    val showImageDialog = remember { mutableStateOf<String?>(null) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SectionTitle(title = "Attachments", icon = Icons.Rounded.AttachFile)
            Spacer(modifier = Modifier.height(12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height((imagePaths.size * 100).dp) // Adjust height based on image count
            ) {
                items(imagePaths) { imagePath ->
                    Image(
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showImageDialog.value = imagePath },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    if (showImageDialog.value != null) {
        Dialog(onDismissRequest = { showImageDialog.value = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(showImageDialog.value),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                IconButton(
                    onClick = { shareImage(context, showImageDialog.value!!) },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                ) {
                    Icon(Icons.Rounded.Download, contentDescription = "Download", tint = Color.White, modifier = Modifier.size(36.dp))
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label: ",
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.W500,
            fontFamily = Poppins
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black,
            fontFamily = Poppins
        )
    }
}

fun formatTime(isoString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoString)
        dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
    } catch (e: DateTimeParseException) {
        "N/A"
    }
}

fun formatOnlyDate(isoString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoString)
        dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: DateTimeParseException) {
        "N/A"
    }
}

fun formatDuration(startTimeIso: String, endTimeIso: String): String {
    return try {
        val start = LocalDateTime.parse(startTimeIso)
        val end = LocalDateTime.parse(endTimeIso)
        val duration = Duration.between(start, end)

        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        if (hours > 0) {
            "${hours}h ${minutes}m"
        } else if (minutes > 0) {
            "${minutes}m"
        } else {
            "${seconds}s"
        }
    } catch (e: DateTimeParseException) {
        "N/A"
    }
}

fun shareIssue(context: android.content.Context, issueDetails: Map<String, String>, imagePaths: List<String>) {
    val message = buildString {
        append("*Issue Report*\n\n")
        append("*Advisor Name:* ${issueDetails["Advisor Name"] ?: "N/A"}\n")
        append("*CRM ID:* ${issueDetails["CRM ID"] ?: "N/A"}\n")
        append("*Organization:* ${issueDetails["Organization"] ?: "N/A"}\n\n")
        append("*Issue:* ${issueDetails["Issue Explanation"] ?: "N/A"}\n")
        append("*Reason:* ${issueDetails["Reason"] ?: "N/A"}\n\n")

        val fillTime = issueDetails["Fill Time"] ?: ""
        val startTime = issueDetails["Start Time"] ?: ""
        val endTime = issueDetails["End Time"] ?: ""

        append("*Start Time:* ${formatTime(startTime)} on ${formatOnlyDate(startTime)}\n")
        append("*End Time:* ${formatTime(endTime)} on ${formatOnlyDate(endTime)}\n")
        append("*Duration:* ${formatDuration(startTime, endTime)}\n")
        append("*Fill Time:* ${formatTime(fillTime)} on ${formatOnlyDate(fillTime)}\n")

        issueDetails["Issue Remarks"]?.let {
            if (it.isNotEmpty()) {
                append("*Remarks:* $it\n")
            }
        }
        append("\nThis report was generated from the Issue Tracker App.")
    }

    val imageUris = imagePaths.map {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(it)
        )
    }

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND_MULTIPLE
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(imageUris))
        putExtra(Intent.EXTRA_TEXT, message)
        type = "image/*" // Or "*/*" if you want to share other file types
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Issue"))
}

fun shareImage(context: android.content.Context, imagePath: String) {
    val imageUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        File(imagePath)
    )

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        type = "image/*"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
}
