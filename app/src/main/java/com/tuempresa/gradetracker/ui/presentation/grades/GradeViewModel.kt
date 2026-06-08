package com.tuempresa.gradetracker.ui.presentation.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuempresa.gradetracker.ui.presentation.grades.GradeScreen
import com.tuempresa.gradetracker.ui.presentation.grades.GradeUiState
import com.tuempresa.gradetracker.domain.usecase.AddGradeUseCase
import com.tuempresa.gradetracker.domain.usecase.GetGradesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GradeViewModel(
    private val getGradesUseCase: GetGradesUseCase,
    private val addGradeUseCase: AddGradeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GradeUiState>(GradeUiState.Loading)
            val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()

    private val _currentScreen = MutableStateFlow(GradeScreen.LIST)
    val currentScreen: StateFlow<GradeScreen> = _currentScreen.asStateFlow()

    private var lastSuccessState: GradeUiState.Success? = null

    init {
        viewModelScope.launch {
            getGradesUseCase().collect { grades ->
                    val average = grades.map { it.grade }.average().takeIf { !it.isNaN() } ?: 0.0
                val successState = GradeUiState.Success(grades, average)
                lastSuccessState = successState
                _uiState.value = successState
            }
        }
    }

    fun navigateTo(screen: GradeScreen) {
        _currentScreen.value = screen
    }

    fun saveGrade(activityName: String, subject: String, gradeText: String) {
        val gradeValue = gradeText.toDoubleOrNull() ?: run {
            _uiState.value = GradeUiState.Error("La nota debe ser un valor numérico válido.")
            return
        }
        viewModelScope.launch {
            try {
                addGradeUseCase(activityName, subject, gradeValue)
                _currentScreen.value = GradeScreen.LIST
            } catch (e: IllegalArgumentException) {
                _uiState.value = GradeUiState.Error(e.message ?: "Error desconocido.")
            }
        }
    }

    fun dismissError() {
        lastSuccessState?.let { _uiState.value = it }
    }
}