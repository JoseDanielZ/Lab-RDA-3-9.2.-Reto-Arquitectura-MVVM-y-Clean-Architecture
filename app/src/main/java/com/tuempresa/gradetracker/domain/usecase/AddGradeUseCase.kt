package com.tuempresa.gradetracker.domain.usecase

import com.tuempresa.gradetracker.domain.model.AcademicGrade
import com.tuempresa.gradetracker.domain.repository.GradeRepository
import java.util.UUID

class AddGradeUseCase(private val repository: GradeRepository) {
    suspend operator fun invoke(activityName: String, subject: String, gradeValue: Double) {
        if (activityName.isBlank() || subject.isBlank()) {
            throw IllegalArgumentException("Todos los campos son obligatorios.")
        }
        if (gradeValue < 0.0 || gradeValue > 10.0) {
            throw IllegalArgumentException("La nota debe estar en el rango de 0.0 a 10.0.")
        }
        repository.addGrade(
                AcademicGrade(
                        id = UUID.randomUUID().toString(),
                        activityName = activityName,
                        subject = subject,
                        grade = gradeValue
                )
        )
    }
}