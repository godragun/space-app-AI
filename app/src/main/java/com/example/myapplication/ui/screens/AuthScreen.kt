package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.state.AppState

fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = this.clickable(
    indication = null,
    interactionSource = MutableInteractionSource()
) { onClick() }

@Composable
fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    focusedTextColor = MaterialTheme.colorScheme.primary,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedContainerColor = MaterialTheme.colorScheme.background,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
)

@Composable
fun AuthScreen(onAuthenticate: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("SIGN IN", "SIGN UP")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "LIFELINE",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 8.sp
        )
        Text(
            "ORBITAL MISSION CONTROL",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            letterSpacing = 4.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Offline alert banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
            Column {
                Text("SYSTEM ALERT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error, letterSpacing = 2.sp)
                Text(
                    "Mainframe connection lost. Proceeding with local authentication cache. Some features may be degraded.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Auth Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            // Tab row
            Row(modifier = Modifier.fillMaxWidth()) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            .clickableNoRipple { selectedTab = index }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 3.sp
                        )
                    }
                }
            }

            Divider(color = MaterialTheme.colorScheme.outlineVariant)

            if (selectedTab == 0) {
                SignInForm(onAuthenticate = onAuthenticate)
            } else {
                SignUpForm(onAuthenticate = onAuthenticate)
            }
        }
    }
}

@Composable
fun SignInForm(onAuthenticate: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("AUTH.SYS_v2.4", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text("STANDBY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.primary).padding(horizontal = 6.dp, vertical = 2.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("CREDENTIALS REQUIRED", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))

        Text("CREW ID / EMAIL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMsg = "" },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurface) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = outlinedTextFieldColors()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("PASSPHRASE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMsg = "" },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onSurface) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = MaterialTheme.colorScheme.onSurface)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = outlinedTextFieldColors()
        )

        AnimatedVisibility(visible = errorMsg.isNotEmpty()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                val ok = AppState.signIn(email, password)
                if (ok) onAuthenticate() else errorMsg = "Invalid credentials. Minimum 4 characters required."
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.background)
        ) {
            Text("AUTHENTICATE  \u2192", style = MaterialTheme.typography.labelLarge, letterSpacing = 2.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = {
                AppState.signIn("offline@lifeline.space", "1234")
                onAuthenticate()
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Text("CONTINUE OFFLINE  \u229f", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, letterSpacing = 2.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun SignUpForm(onAuthenticate: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("CREATE CREW PROFILE", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))

        Text("FULL NAME", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(value = name, onValueChange = { name = it; errorMsg = "" }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = outlinedTextFieldColors())

        Spacer(modifier = Modifier.height(12.dp))
        Text("EMAIL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it; errorMsg = "" }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), colors = outlinedTextFieldColors()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("CREW ROLE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = role, onValueChange = { role = it }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            placeholder = { Text("e.g. Engineer, Medic, Commander", color = MaterialTheme.colorScheme.onSurface.copy(0.4f), fontSize = 13.sp) },
            colors = outlinedTextFieldColors()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("PASSPHRASE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it; errorMsg = "" }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = MaterialTheme.colorScheme.onSurface)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = outlinedTextFieldColors()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("CONFIRM PASSPHRASE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = confirmPassword, onValueChange = { confirmPassword = it; errorMsg = "" }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = outlinedTextFieldColors()
        )

        AnimatedVisibility(visible = errorMsg.isNotEmpty()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                when {
                    name.isBlank() -> errorMsg = "Name required."
                    email.isBlank() -> errorMsg = "Email required."
                    password.length < 4 -> errorMsg = "Passphrase must be at least 4 characters."
                    password != confirmPassword -> errorMsg = "Passphrases do not match."
                    else -> {
                        AppState.signIn(email, password)
                        onAuthenticate()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.background)
        ) {
            Text("CREATE CREW PROFILE  \u2192", style = MaterialTheme.typography.labelLarge, letterSpacing = 2.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
