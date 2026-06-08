package com.tuempresa.gradetracker.data.repository

import com.tuempresa.gradetracker.domain.model.AcademicGrade
import com.tuempresa.gradetracker.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryGradeRepository : GradeRepository {

    private val _grades = MutableStateFlow(
            listOf(
                    AcademicGrade("1", "Examen Parcial", "Matemáticas", 8.5),
                    AcademicGrade("2", "Proyecto Final", "Programación Móvil", 9.2)
            )
    )

    override fun getGrades(): Flow<List<AcademicGrade>> = _grades.asStateFlow()

    override suspend fun addGrade(grade: AcademicGrade) {
        _grades.value = _grades.value + grade
    }
}