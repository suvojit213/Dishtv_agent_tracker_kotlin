package com.suvojeet.issuetracker.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suvojeet.issuetracker.data.UserPreferencesRepository
import com.suvojeet.issuetracker.ui.theme.Poppins
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var rating by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var feedbackText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        userPreferencesRepository.advisorName.collect { advisorName ->
            name = advisorName ?: ""
        }
    }

    fun sendFeedback() {
        if (name.isBlank()) {
            Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }
        if (feedbackText.isBlank()) {
            Toast.makeText(context, "Please enter your feedback", Toast.LENGTH_SHORT).show()
            return
        }

        val phoneNumber = "9234577086" // Your WhatsApp number

        val message = buildString {
            append("*App Feedback*\n\n")
            append("*Rating:* \$rating stars\n")
            append("*Feedback/Suggestions:*\n\$feedbackText\n\n")
            append("*Name:* \$name")
        }

        val encodedMessage = java.net.URLEncoder.encode(message, "UTF-8")
        val whatsappUri = Uri.parse("https://wa.me/\$phoneNumber?text=\${encodedMessage}")

        val intent = Intent(Intent.ACTION_VIEW, whatsappUri)
        try {
            context.startActivity(intent)
            Toast.makeText(context, "Opening WhatsApp to send feedback.", Toast.LENGTH_SHORT).show()
            // Optionally clear fields after opening WhatsApp
            rating = 0
            name = "" // Will be reloaded from prefs if available
            feedbackText = ""
        } catch (e: Exception) {
            Toast.makeText(context, "Could not launch WhatsApp. Please ensure WhatsApp is installed.", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Feedback",
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Rate Your Experience",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                                contentDescription = "Star \$i",
                                tint = Color(0xFFFFC107), // Amber color
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable { rating = i }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Name", fontFamily = Poppins) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Rounded.PersonOutline, contentDescription = "Your Name") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Gray,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontFamily = Poppins)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        label = { Text("Review & Suggestions", fontFamily = Poppins) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5,
                        leadingIcon = {
                            Column(modifier = Modifier.padding(top = 12.dp)) { // Adjust padding to align icon at top
                                Icon(Icons.Outlined.Feedback, contentDescription = "Feedback")
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Gray,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontFamily = Poppins)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { sendFeedback() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Rounded.Send, contentDescription = "Send Feedback", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Send Feedback",
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White,
                            )
                        )
                    }
                }
            }
        }
    }
}