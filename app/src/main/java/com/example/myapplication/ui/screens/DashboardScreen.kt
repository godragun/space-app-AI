package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Critical Failure Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, null, tint = Color(0xFF93000A), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("CRITICAL FAILURE", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF93000A), letterSpacing = 2.sp)
                Text("HULL INTEGRITY COMPROMISED", style = MaterialTheme.typography.labelSmall, color = Color(0xFF93000A).copy(alpha = 0.8f), letterSpacing = 1.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Stats Grid
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatWidget("RADIATION", "84.2", "%", "HIGH", true, 0.84f, Modifier.weight(1f))
                StatWidget("OXYGEN", "12.4", "%", "CRIT", true, 0.12f, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatWidget("TEMP", "22.1", "°C", "NOMINAL", false, 0.55f, Modifier.weight(1f))
                StatWidget("POWER", "34.8", "%", "LOW", false, 0.35f, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatWidget("HULL PRESS", "42.1", "kPa", "FAIL", true, 0.42f, Modifier.weight(1f))
                StatWidget("EARTH SIG", "0.0", "dB", "N/A", false, 0f, Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Recommended Action
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("AI RECOMMENDED ACTION", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), letterSpacing = 2.sp)
                Text(
                    "PRIORITY 1",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    letterSpacing = 1.sp,
                    modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.error).padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Seal Hull Breach", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
            listOf(
                "PRESSURE DROP DETECTED IN SECTOR 4.",
                "OXYGEN DEPLETION RATE: 2.4%/MIN.",
                "EST. TIME TO HYPOXIA: 4 MIN 12 SEC.",
                "MANUAL OVERRIDE REQUIRED FOR BULKHEAD DOORS."
            ).forEach { line ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("> ", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    Text(line, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.background)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("VIEW FULL PROCEDURE", style = MaterialTheme.typography.labelLarge, letterSpacing = 2.sp)
                    Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Status
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                .padding(16.dp)
        ) {
            Text("SYSTEM STATUS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(12.dp))
            listOf(
                Triple("Life Support", "DEGRADED", false),
                Triple("Navigation", "NOMINAL", true),
                Triple("Communication", "OFFLINE", false),
                Triple("Propulsion", "NOMINAL", true),
            ).forEach { (system, status, ok) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(system, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        status,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (ok) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                        letterSpacing = 1.sp
                    )
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.4f), thickness = 0.5.dp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StatWidget(title: String, value: String, unit: String, status: String, isCritical: Boolean, progress: Float, modifier: Modifier = Modifier) {
    val accent = if (isCritical) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val border = if (isCritical) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, border)
            .padding(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), fontSize = 10.sp, letterSpacing = 1.sp)
            Text(status, style = MaterialTheme.typography.labelSmall, color = accent, fontSize = 10.sp, letterSpacing = 1.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, style = MaterialTheme.typography.titleLarge, color = accent, fontSize = 22.sp)
            Text(unit, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), modifier = Modifier.padding(start = 2.dp, bottom = 3.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)),
            color = accent,
            trackColor = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
