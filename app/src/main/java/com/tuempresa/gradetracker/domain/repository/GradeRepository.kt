package com.tuempresa.gradetracker.domain.repository

import com.tuempresa.gradetracker.domain.model.AcademicGrade
import kotlinx.coroutines.flow.Flow

interface GradeRepository {
    fun getGrades(): Flow<List<AcademicGrade>>
    suspend fun addGrade(grade: AcademicGrade)
}