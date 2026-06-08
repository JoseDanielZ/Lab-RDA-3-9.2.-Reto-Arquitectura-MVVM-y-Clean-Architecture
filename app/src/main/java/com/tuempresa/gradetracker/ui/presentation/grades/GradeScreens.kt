package com.tuempresa.gradetracker.ui.presentation.grades

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tuempresa.gradetracker.domain.model.AcademicGrade
import com.tuempresa.gradetracker.ui.presentation.grades.GradeScreen
import com.tuempresa.gradetracker.ui.presentation.grades.GradeUiState

@Composable
fun GradeApp(viewModel: GradeViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    when (currentScreen) {
        GradeScreen.LIST -> GradeListScreen(viewModel)
        GradeScreen.FORM -> GradeFormScreen(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeListScreen(viewModel: GradeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("GradeTracker") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.navigateTo(GradeScreen.FORM) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar calificación")
            }
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is GradeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            is GradeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item { AverageCard(average = state.average) }
                    items(state.grades) { grade -> GradeItem(grade) }
                }
            }
            is GradeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Error",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(state.message, color = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = { viewModel.dismissError() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AverageCard(average: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Promedio General Acumulado",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "%.2f / 10.00".format(average),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun GradeItem(grade: AcademicGrade) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(grade.activityName, style = MaterialTheme.typography.titleSmall)
                Text(grade.subject, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                "%.1f".format(grade.grade),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeFormScreen(viewModel: GradeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var activityName by rememberSaveable { mutableStateOf("") }
    var subject by rememberSaveable { mutableStateOf("") }
    var gradeText by rememberSaveable { mutableStateOf("") }

    val isFormValid = activityName.isNotBlank() && subject.isNotBlank() && gradeText.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Calificación") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.dismissError()
                        viewModel.navigateTo(GradeScreen.LIST)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = activityName,
                onValueChange = { activityName = it },
                label = { Text("Nombre de la Actividad") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Asignatura") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = gradeText,
                onValueChange = { gradeText = it },
                label = { Text("Nota (0.0 – 10.0)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (uiState is GradeUiState.Error) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = (uiState as GradeUiState.Error).message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.dismissError() }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cerrar error",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.saveGrade(activityName, subject, gradeText) },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}