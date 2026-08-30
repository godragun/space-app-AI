package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NavTab(val label: String, val route: String, val icon: ImageVector)

val NAV_TABS = listOf(
    NavTab("Dash", "dashboard", Icons.Default.Dashboard),
    NavTab("Log", "procedures", Icons.Default.Description),
    NavTab("Crew", "crew", Icons.Default.Group),
    NavTab("Map", "map", Icons.Default.Explore),
    NavTab("AI", "assistant", Icons.Default.SmartToy),
)

@Composable
fun BottomNav(currentRoute: String, onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NAV_TABS.forEach { tab ->
            NavItem(tab = tab, isSelected = currentRoute == tab.route, onNavigate = onNavigate)
        }
    }
}

@Composable
fun NavItem(tab: NavTab, isSelected: Boolean, onNavigate: (String) -> Unit) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Column(
        modifier = Modifier
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onNavigate(tab.route) }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = tab.label,
            color = color,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            letterSpacing = 1.sp
        )
        if (isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(modifier = Modifier.size(4.dp, 2.dp).background(MaterialTheme.colorScheme.primary))
        }
    }
}
