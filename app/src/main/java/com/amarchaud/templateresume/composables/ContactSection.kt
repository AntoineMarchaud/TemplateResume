package com.amarchaud.templateresume.composables

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Email: ${contact.email}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0),
                modifier = Modifier.clickable {
                    val emailIntent =
                        Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${contact.email}"))
                    launcher.launch(emailIntent)
                }
            )
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)

            Text(
                text = "Phone: ${contact.phone}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0),
                modifier = Modifier.clickable {
                    val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                    launcher.launch(phoneIntent)
                }
            )
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            Text(
                text = "GitHub: ${contact.githubUrl}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0),
                modifier = Modifier.clickable {
                    val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse(contact.githubUrl))
                    launcher.launch(githubIntent)
                }
            )
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            Text(
                text = "LinkedIn: ${contact.linkedInUrl}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0),
                modifier = Modifier.clickable {
                    val linkedInIntent = Intent(Intent.ACTION_VIEW, Uri.parse(contact.linkedInUrl))
                    launcher.launch(linkedInIntent)
                }
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