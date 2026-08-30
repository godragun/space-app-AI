package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChecklistItem(val text: String, var done: Boolean = false)
data class LogEntry(
    val date: String,
    val time: String,
    val status: String,
    val title: String,
    val desc: String,
    val checklist: List<ChecklistItem>
)

val LOG_ENTRIES = listOf(
    LogEntry("2042.11.04", "08:14:22Z", "CRITICAL", "SYSTEM FAILURE – O2 SCRUBBER",
        "O2 Scrubber primary node offline. Auxiliary engaged.",
        listOf(
            ChecklistItem("Confirm O2 auxiliary system is active"),
            ChecklistItem("Check valve C-4 for obstruction"),
            ChecklistItem("Notify Commander of O2 degradation"),
            ChecklistItem("Deploy portable O2 canisters to Sector 4"),
            ChecklistItem("Monitor O2 levels every 60 seconds"),
        )
    ),
    LogEntry("2042.11.04", "07:30:00Z", "NOMINAL", "SENSOR ARRAY DIAGNOSTIC COMPLETE",
        "External sensor array diagnostic completed. All systems functional.",
        listOf(
            ChecklistItem("Log results to mission log", true),
            ChecklistItem("Upload sensor data to buffer", true),
            ChecklistItem("Schedule next diagnostic in 24h"),
        )
    ),
    LogEntry("2042.11.04", "06:00:15Z", "DELAYED", "TELEMETRY SYNC FAILED",
        "Attempted uplink with Relay Station Echo. Connection timed out.",
        listOf(
            ChecklistItem("Retry uplink on frequency 14.235 MHz"),
            ChecklistItem("Check antenna alignment – Sector 1 Roof"),
            ChecklistItem("Queue telemetry data for next sync window"),
            ChecklistItem("Log delay in communications record"),
        )
    ),
    LogEntry("2042.11.04", "05:12:40Z", "CRITICAL", "HULL BREACH – SECTOR 4",
        "Pressure drop detected in Sector 4. Crew evacuation initiated.",
        listOf(
            ChecklistItem("Evacuate all crew from Sector 4", true),
            ChecklistItem("Engage emergency bulkhead doors", true),
            ChecklistItem("Apply hull sealant foam to breach point"),
            ChecklistItem("Monitor pressure gauge in Sector 4"),
            ChecklistItem("Report to Earth command when comms restore"),
        )
    ),
    LogEntry("2042.11.04", "04:15:00Z", "NOMINAL", "CREW REST INITIATED",
        "Crew rest period initiated. Automated environmental monitoring active.",
        listOf(
            ChecklistItem("Environmental monitoring armed", true),
            ChecklistItem("Emergency intercom on standby", true),
        )
    ),
)

@Composable
fun LogScreen() {
    val expandedEntries = remember { mutableStateMapOf<String, Boolean>() }
    val checkedItems = remember { mutableStateMapOf<String, Boolean>() }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Header bar
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("MISSION LOG", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, letterSpacing = 2.sp)
                Text("TAP ENTRY TO VIEW CHECKLIST", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f), fontSize = 10.sp, letterSpacing = 1.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.error, CircleShape))
                Text("QUEUED FOR SYNC", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), letterSpacing = 1.sp, fontSize = 10.sp)
            }
        }

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.outlineVariant.copy(0.15f)).padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            listOf(
                Triple("CRITICAL", LOG_ENTRIES.count { it.status == "CRITICAL" }.toString(), MaterialTheme.colorScheme.error),
                Triple("DELAYED", LOG_ENTRIES.count { it.status == "DELAYED" }.toString(), MaterialTheme.colorScheme.onSurface),
                Triple("NOMINAL", LOG_ENTRIES.count { it.status == "NOMINAL" }.toString(), MaterialTheme.colorScheme.primary),
            ).forEach { (label, count, color) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(count, style = MaterialTheme.typography.titleLarge, color = color, fontSize = 20.sp)
                    Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f), fontSize = 9.sp, letterSpacing = 1.sp)
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(LOG_ENTRIES) { entry ->
                val entryKey = "${entry.date}_${entry.time}"
                val isExpanded = expandedEntries[entryKey] ?: false

                LogEntryCard(
                    entry = entry,
                    isExpanded = isExpanded,
                    onToggle = { expandedEntries[entryKey] = !isExpanded },
                    checkedItems = checkedItems,
                    entryKey = entryKey
                )
            }
        }
    }
}

@Composable
fun LogEntryCard(
    entry: LogEntry,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    checkedItems: MutableMap<String, Boolean>,
    entryKey: String
) {
    val borderColor = when (entry.status) {
        "CRITICAL" -> MaterialTheme.colorScheme.error
        "DELAYED" -> MaterialTheme.colorScheme.outlineVariant
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val statusColor = when (entry.status) {
        "CRITICAL" -> MaterialTheme.colorScheme.error
        "DELAYED" -> Color(0xFFFFC107)
        else -> Color(0xFF4CAF50)
    }

    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, borderColor)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onToggle() }
    ) {
        // Header row
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(modifier = Modifier.weight(1f).padding(end = 8.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Status dot
                Box(modifier = Modifier.padding(top = 3.dp).size(10.dp).background(statusColor, CircleShape))
                Column {
                    Text(entry.title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(entry.desc, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 12.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(entry.status, style = MaterialTheme.typography.labelSmall, color = statusColor, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(entry.time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error, fontSize = 10.sp)
                Text(entry.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.4f), fontSize = 10.sp)
            }
        }

        // Expand indicator
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.outlineVariant.copy(0.15f)).padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.Checklist, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                Text("${entry.checklist.size} CHECKLIST ITEMS", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 10.sp, letterSpacing = 1.sp)
            }
            Icon(
                if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp)
            )
        }

        // Expandable checklist
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background.copy(0.4f)).padding(horizontal = 14.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("RESPONSE CHECKLIST", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f), letterSpacing = 2.sp, fontSize = 10.sp)
                entry.checklist.forEachIndexed { idx, item ->
                    val itemKey = "${entryKey}_$idx"
                    val isChecked = checkedItems[itemKey] ?: item.done
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                            checkedItems[itemKey] = !isChecked
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(20.dp).border(1.5.dp, if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                                .background(if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isChecked) Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.background, modifier = Modifier.size(14.dp))
                        }
                        Text(
                            item.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isChecked) MaterialTheme.colorScheme.onSurface.copy(0.4f) else MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            textDecoration = if (isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                val doneCount = entry.checklist.indices.count { checkedItems["${entryKey}_$it"] ?: entry.checklist[it].done }
                LinearProgressIndicator(
                    progress = if (entry.checklist.isEmpty()) 0f else doneCount.toFloat() / entry.checklist.size,
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )
                Text("$doneCount / ${entry.checklist.size} completed", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f), fontSize = 10.sp)
            }
        }
    }
}
