package com.amarchaud.templateresume.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amarchaud.templateresume.ResumeSection
import com.amarchaud.templateresume.ui.theme.TemplateResumeTheme

@Composable
fun ElegantNavigationBar(
    modifier: Modifier = Modifier,
    selectedSection: ResumeSection,
    onSectionSelected: (ResumeSection) -> Unit
) {
    val navItems = listOf(
        NavItem(
            section = ResumeSection.EXPERIENCE,
            icon = Icons.Default.Person,
            label = "Experiences"
        ),
        NavItem(
            section = ResumeSection.SKILLS,
            icon = Icons.Default.Build,
            label = "Skills"
        ),
        NavItem(
            section = ResumeSection.CONTACT,
            icon = Icons.Default.Email,
            label = "Contact"
        )
    )

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                        )
                    )
                )
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                ElegantNavItem(
                    modifier = Modifier.weight(1f),
                    item = item,
                    isSelected = selectedSection == item.section,
                    onSelect = { onSectionSelected(item.section) }
                )
            }
        }
    }
}

@Composable
fun ElegantNavItem(
    modifier: Modifier = Modifier,
    item: NavItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable(onClick = onSelect)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = item.label,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

data class NavItem(
    val section: ResumeSection,
    val icon: ImageVector,
    val label: String
)

@Preview
@Composable
private fun ElegantNavigationBarPreview() {
    TemplateResumeTheme {
        ElegantNavigationBar(
            selectedSection = ResumeSection.EXPERIENCE,
            onSectionSelected = {}
        )
    }
}