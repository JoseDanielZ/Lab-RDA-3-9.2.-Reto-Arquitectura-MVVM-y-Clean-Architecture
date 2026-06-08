package com.tuempresa.gradetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.tuempresa.gradetracker.data.repository.InMemoryGradeRepository
import com.tuempresa.gradetracker.domain.usecase.AddGradeUseCase
import com.tuempresa.gradetracker.domain.usecase.GetGradesUseCase
import com.tuempresa.gradetracker.ui.presentation.grades.GradeApp
import com.tuempresa.gradetracker.ui.presentation.grades.GradeViewModel
import com.tuempresa.gradetracker.ui.presentation.grades.GradeViewModelFactory
import com.tuempresa.gradetracker.ui.theme.GradeTrackerTheme

class MainActivity : ComponentActivity() {

    private lateinit var gradeViewModel: GradeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciación manual de dependencias (Clean Architecture)
        val repository = InMemoryGradeRepository()
        val getGradesUseCase = GetGradesUseCase(repository)
        val addGradeUseCase = AddGradeUseCase(repository)

        gradeViewModel = ViewModelProvider(
            this,
            GradeViewModelFactory(getGradesUseCase, addGradeUseCase)
        )[GradeViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            GradeTrackerTheme {
                GradeApp(viewModel = gradeViewModel)
            }
        }
    }
}