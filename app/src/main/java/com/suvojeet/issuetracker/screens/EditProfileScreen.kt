package com.suvojeet.issuetracker.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var crmId by remember { mutableStateOf("") }
    var tlName by remember { mutableStateOf("") }
    var advisorName by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        userPreferencesRepository.crmId.collect { crmId = it ?: "" }
        userPreferencesRepository.tlName.collect { tlName = it ?: "" }
        userPreferencesRepository.advisorName.collect { advisorName = it ?: "" }
    }

    fun saveUserData() {
        coroutineScope.launch {
            userPreferencesRepository.setCrmId(crmId)
            userPreferencesRepository.setTlName(tlName)
            userPreferencesRepository.setAdvisorName(advisorName)
            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
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
                        endY = 0.3f * context.resources.displayMetrics.density // Corresponds to 0.3 stop in Flutter
                    )
                )
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            EditProfileTextField(
                value = crmId,
                onValueChange = { crmId = it },
                label = "CRM ID",
                icon = Icons.Rounded.Badge,
                readOnly = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            EditProfileTextField(
                value = tlName,
                onValueChange = { tlName = it },
                label = "Team Leader Name",
                icon = Icons.Rounded.SupervisorAccount,
                readOnly = true // Make TL Name non-editable
            )
            Spacer(modifier = Modifier.height(20.dp))
            EditProfileTextField(
                value = advisorName,
                onValueChange = { advisorName = it },
                label = "Advisor Name",
                icon = Icons.Rounded.PersonOutline,
                readOnly = false
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { saveUserData() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Rounded.Save, contentDescription = "Save Changes", tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Save Changes",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = Color.White,
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    readOnly: Boolean
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(
            color = Color(0xFF1E3A8A),
            fontFamily = Poppins,
            fontWeight = FontWeight.W500,
        ),
        label = {
            Text(
                text = label,
                style = TextStyle(
                    color = Color.DarkGray,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.W500,
                )
            )
        },
        leadingIcon = {
            Icon(icon, contentDescription = label, tint = Color(0xFF3B82F6))
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color(0xFF1E3A8A),
            unfocusedIndicatorColor = Color(0xFFB0BEC5),
            disabledIndicatorColor = Color(0xFFB0BEC5),
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}