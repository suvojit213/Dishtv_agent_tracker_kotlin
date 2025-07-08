package com.suvojeet.issuetracker.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ContactMail
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.InfoOutlined
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suvojeet.issuetracker.R
import com.suvojeet.issuetracker.ui.theme.Poppins

@Composable
fun DeveloperInfoScreen(navController: NavController) {
    val context = LocalContext.current

    var startAnimation by remember { mutableStateOf(false) }
    val fadeAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600)
    )
    val slideAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
    }

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
                    endY = 0.4f * context.resources.displayMetrics.density // Corresponds to 0.4 stop in Flutter
                )
            )
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
                text = "Developer Info",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins
                )
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .alpha(fadeAnimation)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.developer_profile),
                            contentDescription = "Developer Profile",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Suvojeet Sengupta",
                        style = TextStyle(
                            color = Color(0xFF1E3A8A),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mobile App Developer",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = Poppins,
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EnhancedSkillTag("Flutter", Color(0xFF1E3A8A))
                        Spacer(modifier = Modifier.width(12.dp))
                        EnhancedSkillTag("Dart", Color(0xFF059669))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EnhancedSkillTag("Mobile Development", Color(0xFFEF4444))
                        Spacer(modifier = Modifier.width(12.dp))
                        EnhancedSkillTag("UI/UX Design", Color(0xFF8B5CF6))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // About Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
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
                                        colors = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.InfoOutlined, contentDescription = "About", tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "About the Developer",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A),
                            fontFamily = Poppins
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Hi, I'm Suvojeet Sengupta – currently working as an Advisor at DishTV (GSC). I've always been interested in technology, and with some basic knowledge of Flutter and the help of AI tools, I started exploring app development.\n\nDuring my work, I noticed how time-consuming and frustrating it was to fill out Google Forms again and again for issue tracking. So, I created this simple app as a solution – to make the process smoother and more efficient.\n\nThis app may not be perfect, but it's built with real-life experience, practical thinking, and a passion to solve problems in smarter ways.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.W500,
                        fontFamily = Poppins
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // App Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A).copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder(true)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                                        )
                                    )
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.app_logo),
                                    contentDescription = "App Logo",
                                    modifier = Modifier.size(50.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Issue Tracker App",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E3A8A),
                                        fontFamily = Poppins
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Version 1.0.2",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.W500,
                                        fontFamily = Poppins
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "A smart issue tracking app built using basic Flutter skills and AI support — designed to replace repetitive Google Form entries with a faster, smoother, and more efficient solution, inspired by real work needs at DishTV (GSC).",
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 22.5.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.W500,
                                fontFamily = Poppins
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
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
                                        colors = listOf(Color(0xFF059669), Color(0xFF10B981))
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.ContactMail, contentDescription = "Contact", tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Get in Touch",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A),
                            fontFamily = Poppins
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                EnhancedContactItem(
                    icon = Icons.Rounded.Email,
                    title = "Email",
                    subtitle = "suvojitsengupta21@gmail.com",
                    gradient = Brush.linearGradient(
                        colors = listOf(Color(0xFFEF4444), Color(0xFFF87171))
                    ),
                    onClick = { launchUrl(context, "mailto:suvojitsengupta21@gmail.com") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                EnhancedContactItem(
                    icon = Icons.Rounded.Code,
                    title = "GitHub",
                    subtitle = "suvojit213",
                    gradient = Brush.linearGradient(
                        colors = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                    ),
                    onClick = { launchUrl(context, "https://github.com/suvojit213") }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Footer
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "© 2025 Suvojeet Sengupta",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.W600,
                        fontFamily = Poppins
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Developed with ",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.W500,
                            fontFamily = Poppins
                        )
                    )
                    Icon(Icons.Rounded.Favorite, contentDescription = "Love", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                    Text(
                        text = " using Compose",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.W500,
                            fontFamily = Poppins
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedSkillTag(skill: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(color, color.copy(alpha = 0.8f))
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = skill,
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                fontFamily = Poppins
            )
        )
    }
}

@Composable
fun EnhancedContactItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    gradient: Brush,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(gradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A),
                        fontFamily = Poppins
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.W500,
                        fontFamily = Poppins
                    )
                )
            }
            Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Go", tint = Color.Gray, modifier = Modifier.size(16.dp).background(Color.LightGray.copy(alpha = 0.2f)).clip(RoundedCornerShape(8.dp)))
        }
    }
}

fun launchUrl(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not launch $url", Toast.LENGTH_SHORT).show()
    }
}