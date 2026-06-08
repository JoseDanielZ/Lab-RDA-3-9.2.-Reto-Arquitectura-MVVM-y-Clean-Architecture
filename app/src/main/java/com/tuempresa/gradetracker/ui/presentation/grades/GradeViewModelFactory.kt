package com.tuempresa.gradetracker.ui.presentation.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tuempresa.gradetracker.domain.usecase.AddGradeUseCase
import com.tuempresa.gradetracker.domain.usecase.GetGradesUseCase

class GradeViewModelFactory(
    private val getGradesUseCase: GetGradesUseCase,
    private val addGradeUseCase: AddGradeUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GradeViewModel(getGradesUseCase, addGradeUseCase) as T
    }
}