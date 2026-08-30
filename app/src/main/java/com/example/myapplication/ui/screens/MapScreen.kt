package com.example.myapplication.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MapScreen() {
    var routeConfirmed by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val bitmap = remember {
        try { context.assets.open("station_map.jpg").use { BitmapFactory.decodeStream(it) } } catch (e: Exception) { null }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Critical Alert Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f))
                .border(1.dp, MaterialTheme.colorScheme.error)
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                Text("CRITICAL ALERT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error, letterSpacing = 2.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("SECTOR 4 PRESSURE BREACH", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error, fontSize = 16.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("EVAC INITIATED", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.background(MaterialTheme.colorScheme.error).padding(horizontal = 6.dp, vertical = 3.dp))
                Text("T-MINUS 04:12", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.error).padding(horizontal = 6.dp, vertical = 3.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Station Map Label
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.Explore, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Text("ORBITAL STATION SIGMA-7 – DECK 14", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), letterSpacing = 1.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.MyLocation, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                Text("SECTOR 4", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error, letterSpacing = 1.sp)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Fixed map image (no pinch-zoom, no pan)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .padding(horizontal = 12.dp)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.error)
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Station Map – Orbital Station Sigma-7",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("[ MAP UNAVAILABLE ]", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface.copy(0.4f))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sector Status Grid
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
            Text("SECTOR STATUS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("S1 CMD" to "NOMINAL", "S2 LIFE" to "TASK", "S3 MED" to "NOMINAL").forEach { (name, stat) ->
                    SectorBadge(name, stat, Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("S4 AFT" to "BREACH", "S5 ENG" to "NOMINAL", "DOCK" to "LOCKED").forEach { (name, stat) ->
                    SectorBadge(name, stat, Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Route Intelligence
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("ROUTE INTELLIGENCE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), letterSpacing = 2.sp)
                Text("CRITICAL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.error).padding(horizontal = 6.dp, vertical = 2.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Destination: Sector 4 – Hull Breach", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Path Status: OBSTRUCTED (Aft Corridor)", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Column {
                    Text("RECOMMENDED PATH", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 10.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("12", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                        Text("m", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), modifier = Modifier.padding(start = 2.dp, bottom = 4.dp))
                    }
                }
                Column {
                    Text("OBSTACLES", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 10.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("2", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                        Text(" Bulkheads", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), modifier = Modifier.padding(bottom = 4.dp))
                    }
                }
                Column {
                    Text("ETA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 10.sp)
                    Text("2m 34s", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (routeConfirmed) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary.copy(0.1f))
                        .border(1.dp, MaterialTheme.colorScheme.primary).padding(12.dp),
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ROUTE CONFIRMED – NAVIGATE TO SECTOR 4", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
                }
            } else {
                Button(
                    onClick = { routeConfirmed = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.background)
                ) {
                    Text("CONFIRM ROUTE", style = MaterialTheme.typography.labelLarge, letterSpacing = 2.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SectorBadge(name: String, status: String, modifier: Modifier = Modifier) {
    val isBreach = status == "BREACH"
    val isDegraded = status == "DEGRADED"
    val isTask = status == "TASK"
    val bgColor = when {
        isBreach -> MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
        isTask -> androidx.compose.ui.graphics.Color(0xFFFFC107).copy(alpha = 0.15f)
        isDegraded -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surface
    }
    val textColor = when {
        isBreach -> MaterialTheme.colorScheme.error
        isTask -> androidx.compose.ui.graphics.Color(0xFFFFC107)
        isDegraded -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.primary
    }
    val borderColor = when {
        isBreach -> MaterialTheme.colorScheme.error
        isTask -> androidx.compose.ui.graphics.Color(0xFFFFC107)
        isDegraded -> MaterialTheme.colorScheme.outlineVariant
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    Column(
        modifier = modifier.background(bgColor).border(1.dp, borderColor).padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 10.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(status, style = MaterialTheme.typography.labelSmall, color = textColor, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
    }
}
