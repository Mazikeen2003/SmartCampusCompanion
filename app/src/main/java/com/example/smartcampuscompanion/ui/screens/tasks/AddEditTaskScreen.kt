package com.example.smartcampuscompanion.ui.screens.tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.ui.viewmodel.TaskViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    taskId: Int? = null,
    onSaveDone: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    // Use LaunchedEffect to load task data if editing
    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.handleIntent(TaskIntent.LoadTaskForEdit(taskId))
        } else {
            viewModel.handleIntent(TaskIntent.ClearForm)
        }
    }

    // Effect handling for navigation
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is TaskEffect.NavigateBack) {
                onSaveDone()
            }
        }
    }

    val calendar = Calendar.getInstance()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        if (taskId == null) "New Task" else "Edit Task",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.handleIntent(TaskIntent.SaveTask) },
                        enabled = uiState.isFormValid
                    ) {
                        Text("SAVE", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Section Title
            Text(
                "Task Details",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            // Title Field
            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.handleIntent(TaskIntent.UpdateTitle(it)) },
                label = { Text("What is the task?") },
                placeholder = { Text("e.g. Study for Midterms") },
                leadingIcon = { Icon(Icons.Default.EditCalendar, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            // Description Field
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.handleIntent(TaskIntent.UpdateDescription(it)) },
                label = { Text("Notes (Optional)") },
                placeholder = { Text("Add details or instructions...") },
                leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Schedule Section
            Text(
                "Schedule",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Date Picker driven by UI State
                FilledTonalButton(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                val date = String.format("%02d/%02d/%d", day, month + 1, year)
                                viewModel.handleIntent(TaskIntent.UpdateDate(date))
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(uiState.dueDate.ifEmpty { "Select Date" }, style = MaterialTheme.typography.bodyMedium)
                }

                // Time Picker driven by UI State
                FilledTonalButton(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val time = String.format("%02d:%02d", hour, minute)
                                viewModel.handleIntent(TaskIntent.UpdateTime(time))
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(uiState.dueTime.ifEmpty { "Select Time" }, style = MaterialTheme.typography.bodyMedium)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Primary Save Action
            Button(
                onClick = { viewModel.handleIntent(TaskIntent.SaveTask) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.large,
                enabled = uiState.isFormValid,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (taskId == null) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Task", style = MaterialTheme.typography.titleMedium)
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Update Task", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
