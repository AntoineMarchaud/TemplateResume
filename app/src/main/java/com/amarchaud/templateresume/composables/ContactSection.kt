package com.amarchaud.templateresume.composables

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amarchaud.templateresume.models.ContactModel
import com.amarchaud.templateresume.models.contactModelMock
import com.amarchaud.templateresume.ui.theme.TemplateResumeTheme

@Composable
fun ContactSection(contact: ContactModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ -> }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ContactItem(
                    icon = Icons.Default.Email,
                    contentDescription = "Email",
                    onClick = {
                        val emailIntent =
                            Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${contact.email}"))
                        launcher.launch(emailIntent)
                    }
                )
                
                ContactItem(
                    icon = Icons.Default.Phone,
                    contentDescription = "Phone",
                    onClick = {
                        val phoneIntent =
                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                        launcher.launch(phoneIntent)
                    }
                )
            }

            HorizontalDivider()

            ContactItem(
                icon = Icons.Default.Build,
                text = "Portfolio",
                contentDescription = "GitHub",
                onClick = {
                    val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse(contact.githubUrl))
                    launcher.launch(githubIntent)
                }
            )

            ContactItem(
                icon = Icons.Default.Person,
                text = "LinkedIn",
                contentDescription = "LinkedIn",
                onClick = {
                    val linkedInIntent = Intent(Intent.ACTION_VIEW, Uri.parse(contact.linkedInUrl))
                    launcher.launch(linkedInIntent)
                }
            )
        }
    }
}

@Composable
private fun ContactItem(
    icon: ImageVector,
    text: String? = null,
    contentDescription: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun ContactSectionPreview() {
    TemplateResumeTheme {
        ContactSection(contactModelMock)
    }
}