package com.amarchaud.templateresume.models

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ResumeModel(
    val info: InfoModel,
    val experiences: List<ExperienceModel>,
    val skills: List<String>,
    val contact: ContactModel
)

@Serializable
data class InfoModel(
    val name: String,
    val job: String
)

@Serializable
data class ContactModel(
    val email: String,
    val phone: String,
    val githubUrl: String,
    val linkedInUrl: String
)

internal val contactModelMock = ContactModel(
    email = "email@gmail.com",
    phone = "+336 XX XX XX XX",
    githubUrl = "https://github.com/github-username",
    linkedInUrl = "https://www.linkedin.com/in/linkedin-username"
)

@Serializable
data class ExperienceModel(
    val company: String,
    val period: String,
    val role: String,
    val highlights: List<String>,
    val technologies: List<String>,
    val appId: String? = null
)

internal val experienceModelMock = ExperienceModel(
    company = "Company super super lonnnngggggggg",
    period = "2022 - 2023",
    role = "Role",
    highlights = listOf("Highlight 1", "Highlight 2"),
    technologies = listOf("Tech 1", "Tech 2"),
    appId = "com.example.app"
)

fun parseExperiencesFromRaw(context: Context, resourceId: Int): ResumeModel {
    val inputStream = context.resources.openRawResource(resourceId)
    val jsonContent = inputStream.bufferedReader().use { it.readText() }
    return Json.decodeFromString(jsonContent)
}