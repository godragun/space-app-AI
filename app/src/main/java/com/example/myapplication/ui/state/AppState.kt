package com.example.myapplication.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class User(val id: String, val email: String, val name: String, val role: String)
data class CrewChatMessage(val sender: String, val text: String, val time: String, val isMe: Boolean)

object AppState {
    var currentUser by mutableStateOf<User?>(null)
    var isOnline by mutableStateOf(false)
    var isDarkMode by mutableStateOf(true)

    val crewMessages = mutableStateListOf(
        CrewChatMessage("Cmdr. Vance", "All crew, report status immediately. Hull breach in Sector 4 confirmed.", "08:10:01Z", false),
        CrewChatMessage("Dr. Aris", "Medical bay secured. Three crew members being treated for minor lacerations.", "08:11:45Z", false),
        CrewChatMessage("Lt. Reyes", "Flight deck nominal. Emergency thrusters on standby for stabilization.", "08:12:30Z", false),
    )

    fun signIn(email: String, password: String): Boolean {
        if (email.isNotBlank() && password.length >= 4) {
            currentUser = User(
                id = "CREW-${email.take(3).uppercase()}-001",
                email = email,
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                role = "Engineer - Aft Airlock Module"
            )
            return true
        }
        return false
    }

    fun signOut() {
        currentUser = null
    }

    fun sendCrewMessage(text: String) {
        val user = currentUser ?: return
        crewMessages.add(CrewChatMessage(user.name, text, "NOW", true))
    }

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }
}
