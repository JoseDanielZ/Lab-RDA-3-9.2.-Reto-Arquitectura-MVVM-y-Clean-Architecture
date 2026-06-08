package com.tuempresa.gradetracker.domain.usecase

import com.tuempresa.gradetracker.domain.model.AcademicGrade
import com.tuempresa.gradetracker.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow

class GetGradesUseCase(private val repository: GradeRepository) {
    operator fun invoke(): Flow<List<AcademicGrade>> = repository.getGrades()
}