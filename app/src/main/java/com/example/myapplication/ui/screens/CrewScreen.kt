package com.example.myapplication.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.state.AppState
import kotlinx.coroutines.launch

data class CrewMember(
    val name: String,
    val rank: String,
    val role: String,
    val location: String,
    val hr: Int,
    val stress: String,
    val status: String,
    val avatarAsset: String,
    val isElevated: Boolean = false
)

val CREW_LIST = listOf(
    CrewMember("Cmdr. Vance", "Commander", "COMMAND MODULE", "Sector 1 – Bridge", 72, "LOW", "SUPERVISING", "crew_commander.jpg"),
    CrewMember("Dr. Aris", "Medical Officer", "MED BAY", "Sector 3 – Triage", 135, "ELEVATED", "TRIAGE PREP", "crew_doctor.jpg", isElevated = true),
    CrewMember("Lt. Reyes", "Flight Lieutenant", "FLIGHT DECK", "Sector 1 – Cockpit", 68, "LOW", "STANDBY", "crew_pilot.jpg"),
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CrewScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("ROSTER", "CREW CHAT")

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Tab bar
        Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
            tabs.forEachIndexed { i, label ->
                val sel = selectedTab == i
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { selectedTab = i }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, style = MaterialTheme.typography.labelLarge,
                        color = if (sel) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 2.sp)
                }
            }
        }
        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

        if (selectedTab == 0) CrewRosterTab()
        else CrewChatTab()
    }
}

@Composable
fun CrewRosterTab() {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Your task card
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("YOUR CURRENT TASK", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), letterSpacing = 2.sp)
                    Text("ACTIVE", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary).padding(horizontal = 6.dp, vertical = 3.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Verify Seal Integrity", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Text("Role: Engineer – Aft Airlock Module", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.background)
                    ) { Text("MARK COMPLETE", style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp) }
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.extraSmall
                    ) { Text("SCHEMATICS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp) }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("CREW ROSTER", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), letterSpacing = 2.sp)
                Text("${CREW_LIST.size} ACTIVE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
            }
        }

        items(CREW_LIST) { member ->
            val bitmap = remember(member.avatarAsset) {
                try { context.assets.open(member.avatarAsset).use { BitmapFactory.decodeStream(it) } } catch (e: Exception) { null }
            }
            CrewMemberCard(member = member, bitmap = bitmap)
        }
    }
}

@Composable
fun CrewMemberCard(member: CrewMember, bitmap: android.graphics.Bitmap?) {
    val accentColor = if (member.isElevated) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val borderColor = if (member.isElevated) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, borderColor)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            // Avatar
            Box(
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .border(2.dp, accentColor, RoundedCornerShape(6.dp))
            ) {
                if (bitmap != null) {
                    Image(bitmap = bitmap.asImageBitmap(), contentDescription = member.name,
                        contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                        modifier = Modifier.align(Alignment.Center).size(32.dp))
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                Text(member.rank, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.onSurface.copy(0.5f), modifier = Modifier.size(12.dp))
                    Text(member.location, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f), fontSize = 11.sp)
                }
            }
            Text(
                member.status,
                style = MaterialTheme.typography.labelSmall,
                color = if (member.isElevated) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.background,
                modifier = Modifier.background(if (member.isElevated) MaterialTheme.colorScheme.errorContainer.copy(0.3f) else MaterialTheme.colorScheme.primary)
                    .border(1.dp, if (member.isElevated) MaterialTheme.colorScheme.error else Color.Transparent)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(12.dp))

        // Vitals row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Column {
                Text("HEART RATE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 10.sp, letterSpacing = 1.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(member.hr.toString(), style = MaterialTheme.typography.titleLarge, color = accentColor, fontSize = 20.sp)
                    Text(" bpm", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f), modifier = Modifier.padding(bottom = 3.dp))
                }
            }
            Column {
                Text("STRESS LEVEL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 10.sp, letterSpacing = 1.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(member.stress, style = MaterialTheme.typography.titleLarge, color = accentColor, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(8.dp).background(accentColor, CircleShape))
                }
            }
            Column {
                Text("SECTOR", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), fontSize = 10.sp, letterSpacing = 1.sp)
                Text(member.role, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
            }
        }

        // Alert banner for elevated stress
        if (member.isElevated) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f))
                    .border(1.dp, MaterialTheme.colorScheme.error)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    Text("Reassign Task to\nCommander?", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                }
                Button(
                    onClick = { },
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("REASSIGN", style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CrewChatTab() {
    val messages = AppState.crewMessages
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Status bar
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.error, CircleShape))
                Text("LOCAL MESH NETWORK", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), letterSpacing = 1.sp)
            }
            Text("${CREW_LIST.size} CREW ONLINE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
        }
        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                CrewChatBubble(msg)
            }
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(8.dp).navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message crew...", color = MaterialTheme.colorScheme.onSurface.copy(0.4f), fontSize = 14.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (input.isNotBlank()) {
                        AppState.sendCrewMessage(input)
                        input = ""
                        keyboardController?.hide()
                        scope.launch { listState.animateScrollToItem(messages.size - 1) }
                    }
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                )
            )
            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        AppState.sendCrewMessage(input)
                        input = ""
                        keyboardController?.hide()
                        scope.launch { listState.animateScrollToItem(messages.size - 1) }
                    }
                },
                modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
            ) {
                Icon(Icons.Default.Send, null, tint = MaterialTheme.colorScheme.background)
            }
        }
    }
}

@Composable
fun CrewChatBubble(msg: com.example.myapplication.ui.state.CrewChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!msg.isMe) {
            Box(modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp)).clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center) {
                Text(msg.sender.take(1).uppercase(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column(horizontalAlignment = if (msg.isMe) Alignment.End else Alignment.Start) {
            if (!msg.isMe) Text(msg.sender, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f), letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier.widthIn(max = 260.dp)
                    .clip(RoundedCornerShape(if (msg.isMe) 12.dp else 4.dp, 12.dp, 12.dp, if (msg.isMe) 12.dp else 4.dp))
                    .background(if (msg.isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    .border(1.dp, if (msg.isMe) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(4.dp))
                    .padding(10.dp, 8.dp)
            ) {
                Text(msg.text, style = MaterialTheme.typography.bodyLarge, fontSize = 14.sp,
                    color = if (msg.isMe) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface)
            }
            Text(msg.time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.4f), fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
        }
    }
}
