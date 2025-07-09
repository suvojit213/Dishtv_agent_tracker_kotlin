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
import androidx.compose.material.icons.outlined.Info
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
import android.content.Context

// Utility function to launch URLs
fun launchUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open link: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun EnhancedSkillTag(skill: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF3B82F6).copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = skill,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins
            )
        )
    }
}

@Composable
fun EnhancedContactItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B82F6).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF1E3A8A),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E3A8A),
                fontFamily = Poppins
            )
        )
    }
}


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
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Developer Info",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = Poppins
                )
            )
        }

        // Profile Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .alpha(fadeAnimation),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.developer_profile),
                contentDescription = "Developer Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Suvojeet Singh",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontFamily = Poppins
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Android & Full-Stack Developer",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = Poppins
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Passionate about crafting innovative solutions and building seamless user experiences.",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontFamily = Poppins
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Skills Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .alpha(fadeAnimation),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Skills",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = Poppins
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EnhancedSkillTag(skill = "Kotlin")
                EnhancedSkillTag(skill = "Jetpack Compose")
                EnhancedSkillTag(skill = "Android Development")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EnhancedSkillTag(skill = "Java")
                EnhancedSkillTag(skill = "Python")
                EnhancedSkillTag(skill = "Full-Stack")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Contact Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .alpha(fadeAnimation)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Contact & Links",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = Poppins
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            EnhancedContactItem(
                icon = Icons.Rounded.Email,
                label = "Email: suvojeet.singh@example.com"
            ) {
                launchUrl(context, "mailto:suvojeet.singh@example.com")
            }

            EnhancedContactItem(
                icon = Icons.Rounded.Code,
                label = "GitHub: github.com/suvojeet"
            ) {
                launchUrl(context, "https://github.com/suvojeet")
            }

            EnhancedContactItem(
                icon = Icons.Rounded.TrackChanges,
                label = "LinkedIn: linkedin.com/in/suvojeet"
            ) {
                launchUrl(context, "https://www.linkedin.com/in/suvojeet")
            }

            EnhancedContactItem(
                icon = Icons.Rounded.DataObject,
                label = "Portfolio: suvojeet.dev"
            ) {
                launchUrl(context, "https://suvojeet.dev")
            }

            EnhancedContactItem(
                icon = Icons.Outlined.Info,
                label = "About App"
            ) {
                navController.navigate("about_app")
            }

            EnhancedContactItem(
                icon = Icons.Rounded.Favorite,
                label = "Credits"
            ) {
                navController.navigate("credits")
            }
        }
    }
}
