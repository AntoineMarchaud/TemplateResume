package com.amarchaud.templateresume.composables

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.rememberSplineBasedDecay
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amarchaud.templateresume.NavigationBarHeight
import com.amarchaud.templateresume.models.ExperienceModel
import com.amarchaud.templateresume.models.experienceModelMock
import com.amarchaud.templateresume.ui.theme.TemplateResumeTheme

const val playStoreUrl = "https://play.google.com/store/apps/details?id="
private const val MAX_ELEMENTS_TO_ANIMATE = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceSection(experiences: List<ExperienceModel>, scrollBehavior: TopAppBarScrollBehavior) {
    var canDisplayEnterAnimation by remember { mutableStateOf(false) }

    var translationY: List<State<Dp>> = remember { mutableStateListOf() }
    if (translationY.isEmpty() && LocalInspectionMode.current.not()) {
        translationY = buildList {
            for (i in 0 until MAX_ELEMENTS_TO_ANIMATE) {
                add(
                    i,
                    animateDpAsState(
                        targetValue = if (canDisplayEnterAnimation) {
                            0.dp
                        } else {
                            (200 + i * 300).dp // start from bottom
                        },
                        animationSpec = tween(
                            durationMillis = 800,
                        ),
                        label = "animate item $i",
                    ),
                )
            }
        }
    }

    LaunchedEffect(Unit){
        canDisplayEnterAnimation = true
    }
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        itemsIndexed(items = experiences) { index, oneExperience ->

            ExperienceCard(
                modifier = Modifier.offset(
                    y = if (index < MAX_ELEMENTS_TO_ANIMATE && translationY.isNotEmpty()) {
                        translationY[index].value
                    } else {
                        0.dp
                    },
                ),
                experience = oneExperience
            )

        }
        item {
            Spacer(modifier = Modifier.height(NavigationBarHeight))
        }
    }
}

@Composable
fun ExperienceCard(
    modifier: Modifier = Modifier,
    experience: ExperienceModel
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
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
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = experience.period,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
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
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
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
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.DarkGray
                    ),
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
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tech,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.primary
                                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ExperienceSectionPreview() {

    TemplateResumeTheme {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
            snapAnimationSpec = spring(),
            flingAnimationSpec = rememberSplineBasedDecay()
        )

        ExperienceSection(List(10) { experienceModelMock }, scrollBehavior = scrollBehavior)
    }
}