package com.tuempresa.gradetracker.domain.model

data class AcademicGrade(
    val id: String,
    val activityName: String,
    val subject: String,
    val grade: Double
)