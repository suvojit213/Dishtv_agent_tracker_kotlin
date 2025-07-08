package com.suvojeet.issuetracker.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suvojeet.issuetracker.data.UserPreferencesRepository
import com.suvojeet.issuetracker.ui.theme.Poppins
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialSetupScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var crmId by remember { mutableStateOf("") }
    var advisorName by remember { mutableStateOf("") }
    var selectedTlName by remember { mutableStateOf("Manish Kumar") }
    var showOtherTlNameField by remember { mutableStateOf(false) }
    var otherTlName by remember { mutableStateOf("") }
    var selectedOrganization by remember { mutableStateOf("DISH") }

    var startAnimation by remember { mutableStateOf(false) }
    val fadeAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 750)
    )
    val slideAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 750, easing = androidx.compose.animation.core.EaseOutCubic)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        // Load saved data
        userPreferencesRepository.crmId.collect { crmId = it ?: "" }
        // You'll need to add advisorName, tlName, otherTlName, organization to UserPreferencesRepository
        // For now, they will retain their default values or be empty.
    }

    val isFormValid = remember(crmId, advisorName, selectedTlName, otherTlName) {
        crmId.isNotEmpty() &&
                advisorName.isNotEmpty() &&
                (selectedTlName != "Other" || otherTlName.isNotEmpty())
    }

    fun showErrorSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true
            )
        }
    }

    fun saveData() {
        if (advisorName.isEmpty() || (selectedTlName == "Other" && otherTlName.isEmpty())) {
            showErrorSnackbar("Please fill in all required fields")
            return
        }

        if (crmId.isEmpty() || !crmId.matches(Regex("^[0-9]+$"))) {
            showErrorSnackbar("CRM ID must contain only digits")
            return
        }

        if (!advisorName.matches(Regex("^[a-zA-Z ]+$"))) {
            showErrorSnackbar("Advisor Name must contain only alphabets")
            return
        }

        coroutineScope.launch {
            userPreferencesRepository.setCrmId(crmId)
            // Save other fields here once added to UserPreferencesRepository
            Toast.makeText(context, "Setup Complete!", Toast.LENGTH_SHORT).show()
            navController.navigate("main_app") {
                popUpTo("initial_setup") { inclusive = true }
            }
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
                            Color(0xFF3B82F6),
                            Color(0xFFF8FAFC),
                        ),
                        startY = 0.0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .verticalScroll(rememberScrollState())
                .alpha(fadeAnimation)
                .scale(slideAnimation)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Welcome Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        fontFamily = Poppins,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Issue Tracker App",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Let's set up your profile to get started",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        fontFamily = Poppins,
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Setup Form
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
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
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.PersonAdd, contentDescription = "Profile Setup", tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Profile Setup",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A),
                            fontFamily = Poppins,
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // CRM ID Field
                EnhancedTextField(
                    value = crmId,
                    onValueChange = { crmId = it },
                    label = "CRM ID",
                    icon = Icons.Outlined.Badge,
                    hint = "Enter your CRM ID",
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Advisor Name Field
                EnhancedTextField(
                    value = advisorName,
                    onValueChange = { advisorName = it },
                    label = "Advisor Name",
                    icon = Icons.Outlined.Person,
                    hint = "Enter your advisor name",
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Team Leader Dropdown
                TeamLeaderDropdown(
                    selectedTlName = selectedTlName,
                    onTlSelected = { newValue ->
                        selectedTlName = newValue
                        showOtherTlNameField = newValue == "Other"
                        if (!showOtherTlNameField) {
                            otherTlName = ""
                        }
                    }
                )

                // Other TL Name Field (conditional)
                if (showOtherTlNameField) {
                    Spacer(modifier = Modifier.height(16.dp))
                    EnhancedTextField(
                        value = otherTlName,
                        onValueChange = { otherTlName = it },
                        label = "Other Team Leader Name",
                        icon = Icons.Outlined.SupervisorAccount,
                        hint = "Enter team leader name",
                        keyboardType = KeyboardType.Text
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Organization Dropdown
                OrganizationDropdown(
                    selectedOrganization = selectedOrganization,
                    onOrganizationSelected = { selectedOrganization = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Save Button
                Button(
                    onClick = { if (isFormValid) saveData() },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFormValid) Color(0xFF3B82F6) else Color(0xFFE0E0E0),
                        contentColor = if (isFormValid) Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                ) {
                    Icon(Icons.Rounded.CheckCircle, contentDescription = "Complete Setup", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Complete Setup",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    hint: String,
    keyboardType: KeyboardType,
    maxLength: Int? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            label = { Text(hint) },
            leadingIcon = {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF3B82F6),
                unfocusedBorderColor = Color(0xFFE2E8F0),
                cursorColor = Color(0xFF3B82F6),
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamLeaderDropdown(
    selectedTlName: String,
    onTlSelected: (String) -> Unit
) {
    val tlOptions = listOf(
        "Manish Kumar",
        "Aniket",
        "Imran Khan",
        "Ravi",
        "Gajendra",
        "Suyash Upadhyay",
        "Randhir Kumar",
        "Subham Kumar",
        "Karan",
        "Rohit",
        "Shilpa",
        "Vipin",
        "Sonu",
        "Other"
    )
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Team Leader",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedTlName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Team Leader") },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.SupervisorAccount,
                        contentDescription = "Team Leader",
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    cursorColor = Color(0xFF3B82F6),
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFF1E3A8A),
                    fontFamily = Poppins,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tlOptions.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, fontFamily = Poppins) },
                        onClick = {
                            onTlSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizationDropdown(
    selectedOrganization: String,
    onOrganizationSelected: (String) -> Unit
) {
    val orgOptions = listOf("DISH", "D2H")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Organization",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOrganization,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Organization") },
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Business,
                        contentDescription = "Organization",
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    cursorColor = Color(0xFF3B82F6),
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFF1E3A8A),
                    fontFamily = Poppins,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                orgOptions.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, fontFamily = Poppins) },
                        onClick = {
                            onOrganizationSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}