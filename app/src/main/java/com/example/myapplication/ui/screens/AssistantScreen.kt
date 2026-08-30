package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean, val timestamp: String = "", val isThinking: Boolean = false)

val OFFLINE_KNOWLEDGE = mapOf(
    "oxygen" to "Standard O2 flow rate is 12.5 L/min. If flow drops below 10.0 L/min, check valve C-4 for obstruction. Emergency O2 canisters are in lockers 7A, 7B, and 14C.",
    "hull" to "Hull breach procedure: 1. Alert all crew via intercom. 2. Evacuate affected sector. 3. Close bulkhead doors manually. 4. Apply sealant foam to breach area. 5. Monitor pressure gauge.",
    "pressure" to "Cabin pressure nominal range: 95–105 kPa. CRIT threshold below 70 kPa. Use emergency pressure suits if below 80 kPa. Repressurization valve is in Sector 2.",
    "radiation" to "Safe radiation levels below 40%. At 60–80% wear protective suits. Above 80% – immediate evacuation mandatory. Lead shielding panels are in Emergency Locker B12.",
    "power" to "Emergency power protocol: Switch to battery pack Alpha (Sector 5). Disable non-essential systems. Reactor restart requires Commander authorization and 3-step manual override.",
    "temperature" to "Nominal temperature range: 18–26°C. Heating system control in Sector 1. Emergency thermal blankets in Medical Bay – quantity 24.",
    "medic" to "Dr. Aris is stationed in Med Bay (Sector 3). Triage priority: 1. Airway, 2. Breathing, 3. Circulation. Emergency medical kit is marked RED and located near every airlock.",
    "fire" to "Fire protocol: 1. Trigger alert, 2. Evacuate sector, 3. Close O2 vents, 4. Use CO2 extinguisher (NOT water). Halon system activates automatically in engine room.",
    "help" to "I can assist with: oxygen, hull breach, pressure, radiation, power, temperature, medical, fire protocols. Ask me about any system status or emergency procedure.",
    "crew" to "Active crew: Cmdr. Vance (Command), Dr. Aris (Medical), Lt. Reyes (Flight Deck), Eng. Torres (Engineering). Use intercom channel 4 for all-hands broadcast."
)

fun getOfflineResponse(input: String): String {
    val lower = input.lowercase()
    val match = OFFLINE_KNOWLEDGE.entries.firstOrNull { (key, _) -> lower.contains(key) }
    return match?.value
        ?: "Query not found in local knowledge base. I'm operating offline with limited knowledge. Try asking about: oxygen, hull, pressure, radiation, power, temperature, crew, or fire."
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AssistantScreen() {
    val messages = remember { mutableStateListOf(
        ChatMessage("OFFLINE ASSISTANT ACTIVE\n\nI am operating with local knowledge base only. Real-time AI features unavailable. I can assist with emergency procedures, system protocols, and crew information.\n\nType 'help' to see what I know.", false, "08:14:22Z")
    ) }
    var input by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    fun sendMessage() {
        val msg = input.trim()
        if (msg.isBlank()) return
        messages.add(ChatMessage(msg, true, "NOW"))
        input = ""
        isThinking = true
        keyboardController?.hide()

        coroutineScope.launch {
            delay(800) // simulate thinking
            isThinking = false
            messages.add(ChatMessage(getOfflineResponse(msg), false, "NOW"))
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(width = 0.dp, color = Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("OFFLINE ASSISTANT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), letterSpacing = 2.sp)
                Text("LOCAL KNOWLEDGE BASE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f), fontSize = 10.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.error, RoundedCornerShape(50)))
                Text("DISCONNECTED", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error, letterSpacing = 1.sp)
            }
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

        // Topic chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("oxygen", "hull", "fire", "power").forEach { topic ->
                SuggestionChip(
                    onClick = { input = topic; sendMessage() },
                    label = { Text(topic.uppercase(), fontSize = 10.sp, letterSpacing = 1.sp) },
                    modifier = Modifier.height(28.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = MaterialTheme.colorScheme.outlineVariant.copy(0.3f))
                )
            }
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

        // Messages
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(msg)
            }
            if (isThinking) {
                item {
                    ThinkingBubble()
                }
            }
        }

        // Input area
        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter query...", color = MaterialTheme.colorScheme.onSurface.copy(0.4f), fontSize = 14.sp) },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { sendMessage() }),
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
                onClick = { sendMessage() },
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
            ) {
                Icon(Icons.Default.Send, null, tint = MaterialTheme.colorScheme.background)
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!msg.isUser) {
            Box(
                modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SmartToy, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        val bubbleBg = if (msg.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        val textColor = if (msg.isUser) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface
        val borderColor = if (msg.isUser) Color.Transparent else MaterialTheme.colorScheme.outlineVariant

        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(if (msg.isUser) 12.dp else 4.dp, 12.dp, 12.dp, if (msg.isUser) 12.dp else 4.dp))
                .background(bubbleBg)
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            if (!msg.isUser) {
                Text("AI-GENERATED", fontSize = 9.sp, letterSpacing = 2.sp, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(msg.text, style = MaterialTheme.typography.bodyLarge, color = textColor, fontSize = 14.sp, lineHeight = 20.sp)
        }

        if (msg.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun ThinkingBubble() {
    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.SmartToy, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.clip(RoundedCornerShape(4.dp, 12.dp, 12.dp, 4.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text("Processing query...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(0.5f), fontSize = 13.sp)
        }
    }
}
