package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SignalCellularOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.state.AppState

@Composable
fun TopBar(connectionStatus: String = "OFFLINE") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: signal + status
        Row(verticalAlignment = Alignment.CenterVertically) {
            val isOffline = connectionStatus == "OFFLINE"
            val statusColor = if (isOffline) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            Icon(
                imageVector = if (isOffline) Icons.Default.SignalCellularOff else Icons.Default.SignalCellularAlt,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            if (isOffline) {
                Text(
                    "OFFLINE",
                    fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background,
                    letterSpacing = 1.sp,
                    modifier = Modifier.background(MaterialTheme.colorScheme.error).padding(horizontal = 6.dp, vertical = 3.dp)
                )
            } else {
                Text("CONNECTED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
            }
        }

        // Center: Logo
        Text("LIFELINE", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, letterSpacing = 6.sp)

        // Right: theme toggle
        IconButton(onClick = { AppState.toggleDarkMode() }) {
            Icon(
                imageVector = if (AppState.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = "Toggle theme",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
