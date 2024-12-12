package com.amarchaud.templateresume

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amarchaud.templateresume.composables.ContactSection
import com.amarchaud.templateresume.composables.ElegantNavigationBar
import com.amarchaud.templateresume.composables.ExperienceSection
import com.amarchaud.templateresume.composables.SkillsSection
import com.amarchaud.templateresume.models.InfoModel
import com.amarchaud.templateresume.models.ResumeModel
import com.amarchaud.templateresume.models.contactModelMock
import com.amarchaud.templateresume.models.experienceModelMock
import com.amarchaud.templateresume.models.parseExperiencesFromRaw
import com.amarchaud.templateresume.ui.theme.StoreDynamicColor
import com.amarchaud.templateresume.ui.theme.TemplateResumeTheme
import kotlin.math.pow

enum class ResumeSection {
    EXPERIENCE, SKILLS, CONTACT
}

val NavigationBarHeight = 128.dp

@Composable
fun ResumeComposable() {
    val context = LocalContext.current
    val resumeModel = remember {
        parseExperiencesFromRaw(context = context, R.raw.resume_template)
    }

    Resume(resumeModel = resumeModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Resume(resumeModel: ResumeModel) {
    val dynamicColor = remember { StoreDynamicColor.getInstance() }

    var selectedSection by remember { mutableStateOf(ResumeSection.EXPERIENCE) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        snapAnimationSpec = spring(),
        flingAnimationSpec = rememberSplineBasedDecay()
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .alpha(
                                        alpha = 1f.times(scrollBehavior.state.collapsedFraction.let { 1 - it })
                                    ),
                                text = resumeModel.info.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp * 1f.times(scrollBehavior.state.collapsedFraction.let { 1 - it }),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                ),
                            )
                            Text(
                                modifier = Modifier
                                    .alpha(
                                        alpha = 1f.times(scrollBehavior.state.collapsedFraction.let { 1 - it })
                                    ),
                                fontSize = 16.sp * 1f.times(scrollBehavior.state.collapsedFraction.let { 1 - it }),
                                text = resumeModel.info.job,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                            )
                        }


                        if (dynamicColor.canBeDisplayed) {
                            Column (
                                modifier = Modifier
                                    .scale(
                                        scale = 1f.times(scrollBehavior.state.collapsedFraction.let { 1 - it })
                                    )
                                    .alpha(
                                        1 - scrollBehavior.state.collapsedFraction.coerceIn(
                                            0f,
                                            1f
                                        )
                                    )
                                    .padding(end = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Material You",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                )

                                val checked = dynamicColor.dynamicColor.collectAsState().value
                                Checkbox(
                                    modifier = Modifier.size(24.dp),
                                        checked = checked,
                                    onCheckedChange = {
                                        dynamicColor.activateDynamicColor(!checked)
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            ElegantNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding(),
                selectedSection = selectedSection,
                onSectionSelected = { selectedSection = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = selectedSection,
                label = ""
            ) { selectedSection ->
                when (selectedSection) {
                    ResumeSection.EXPERIENCE -> ExperienceSection(
                        experiences = resumeModel.experiences,
                        scrollBehavior = scrollBehavior
                    )

                    ResumeSection.SKILLS -> SkillsSection(
                        skills = resumeModel.skills,
                        scrollBehavior = scrollBehavior
                    )

                    ResumeSection.CONTACT -> ContactSection(contact = resumeModel.contact)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ResumeAppPreview() {
    TemplateResumeTheme {
        Resume(
            resumeModel = ResumeModel(
                info = InfoModel(
                    name = "Antoine Marchaud",
                    job = "Senior Android Developer (10 years)"
                ),
                experiences = List(20) { experienceModelMock },
                skills = List(19) { "Skill" },
                contact = contactModelMock
            )
        )
    }
}