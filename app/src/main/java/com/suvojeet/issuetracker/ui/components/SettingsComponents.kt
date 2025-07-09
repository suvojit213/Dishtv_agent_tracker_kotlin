package com.suvojeet.issuetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suvojeet.issuetracker.ui.theme.Poppins

@Composable
fun SettingsTile(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            }
        },
        headlineContent = {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                )
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = Color.Gray,
                )
            )
        },
        trailingContent = {
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "Go", modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
    )
}
