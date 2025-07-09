package com.suvojeet.issuetracker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suvojeet.issuetracker.R
import com.suvojeet.issuetracker.ui.theme.Poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "About App",
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
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Issue Tracker App",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = Color(0xFF1E3A8A),
                    )
                )
                Text(
                    text = "Version 1.0.2",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontFamily = Poppins,
                    )
                )
                Spacer(modifier = Modifier.height(40.dp))
            }

            Text(
                text = "About the Issue Tracker App",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF1E3A8A),
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "The Issue Tracker App is a meticulously crafted, open-source solution designed to revolutionize how advisors manage and report issues within the DishTV/D2H ecosystem. Developed independently by Suvojeet, this application stands as a testament to innovative problem-solving, offering a streamlined and user-friendly alternative to traditional, lengthy form submissions.",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color.DarkGray,
                    lineHeight = 24.sp,
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Bridging the Gap: Google Forms Integration",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF1E3A8A),
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "At its core, the Issue Tracker App seamlessly integrates with the existing Google Form-based issue tracking system previously utilized by DishTV/D2H. This intelligent integration eliminates the cumbersome manual entry process, automatically populating critical data fields within the Google Form. Advisors can now effortlessly record details such as CRM ID, full name, team leader, brand (DISH/D2H), issue type, and precise start and end times directly through the app's intuitive interface. This not only saves valuable time but also significantly reduces the potential for human error, ensuring data accuracy and consistency.",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color.DarkGray,
                    lineHeight = 24.sp,
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Key Features Designed for Efficiency:",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF1E3A8A),
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            MotivePoint("Intuitive User Interface", "A clean, modern, and easy-to-navigate design ensures a smooth user experience for all advisors.")
            MotivePoint("Automated Data Pre-filling", "Say goodbye to repetitive typing. The app intelligently pre-fills relevant information into the Google Form, including user details, organization preference, and current date.")
            MotivePoint("Dynamic Team Leader Selection", "Easily select your team leader from a predefined list or add a new one, ensuring accurate reporting hierarchies.")
            MotivePoint("Precise Issue Timing", "Accurately log the start and end times of issues with a user-friendly time picker, providing comprehensive data for analysis.")
            MotivePoint("Persistent Preferences", "The app remembers your organization (DISH/D2H) and other key details, minimizing setup time for subsequent issue reports.")
            MotivePoint("In-App Webview", "Experience a fully integrated workflow as the Google Form opens directly within the app, eliminating the need to switch between applications.")
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "An Open-Source Initiative for the Community",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF1E3A8A),
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This Issue Tracker App is a proudly open-source project, built with the community in mind. It is not an official product of DishTV or D2H but rather a dedicated effort by Suvojeet to enhance the daily operations of advisors. Its open-source nature encourages transparency, collaboration, and continuous improvement, allowing for future enhancements and adaptations based on user feedback and evolving needs.",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color.DarkGray,
                    lineHeight = 24.sp,
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Developed by Suvojeet",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF1E3A8A),
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This application is the result of Suvojeet's commitment to creating practical and efficient tools that empower users. It reflects a deep understanding of the challenges faced by advisors and a passion for leveraging technology to simplify complex processes.",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color.DarkGray,
                    lineHeight = 24.sp,
                )
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "© 2025 Issue Tracker App. All rights reserved.",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Poppins,
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun MotivePoint(title: String, description: String) {
    Row(
        modifier = Modifier.padding(bottom = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            Icons.Rounded.CheckCircleOutline,
            contentDescription = "Checkmark",
            tint = Color(0xFF3B82F6),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF1E3A8A),
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = Poppins,
                    color = Color.DarkGray,
                    lineHeight = 21.sp,
                )
            )
        }
    }
}