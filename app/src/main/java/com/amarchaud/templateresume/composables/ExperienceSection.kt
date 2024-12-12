package com.amarchaud.templateresume.composables

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amarchaud.templateresume.models.ExperienceModel
import com.amarchaud.templateresume.models.experienceModelMock
import com.amarchaud.templateresume.ui.theme.TemplateResumeTheme

const val playStoreUrl = "https://play.google.com/store/apps/details?id="

@Composable
fun ExperienceSection(experiences: List<ExperienceModel>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = experiences) { oneExperience ->
            ExperienceCard(oneExperience)
        }
    }
}

@Composable
fun ExperienceCard(experience: ExperienceModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column {
            OneExperienceMainPart(experience = experience)

            // what can be shrink
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            ) {
                OneExperienceSecondPart(experience = experience)
            }
        }

    }
}

@Composable
private fun OneExperienceMainPart(experience: ExperienceModel) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = experience.company,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF1565C0),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = experience.period,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = experience.role,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF2196F3)
            )

            experience.appId?.let {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { _ -> }

                Icon(modifier = Modifier.clickable {
                    val emailIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("$playStoreUrl${it}"))
                    launcher.launch(emailIntent)
                }, imageVector = Icons.Default.Info, contentDescription = "app")
            }
        }
    }
}

@Composable
private fun OneExperienceSecondPart(
    modifier: Modifier = Modifier,
    experience: ExperienceModel
) {
    Column(modifier = modifier) {
        // highlights
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            experience.highlights.forEach { highlight ->
                Text(
                    text = "• $highlight",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // techs
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 48.dp)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                experience.technologies.forEach { tech ->

                    item {
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFF2196F3).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tech,
                                color = Color(0xFF1565C0),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }

            // Left gradient
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.8f),
                                Color.Transparent
                            )
                        )
                    )
                    .align(Alignment.CenterStart)
            )

            // Right gradient
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.8f)
                            )
                        )
                    )
                    .align(Alignment.CenterEnd)
            )

        }
    }
}

@Preview
@Composable
private fun ExperienceSectionPreview() {
    TemplateResumeTheme {
        ExperienceSection(List(10) { experienceModelMock })
    }
}