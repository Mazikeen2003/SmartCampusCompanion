package com.example.smartcampuscompanion.ui.screens.Tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.data.entity.Task
import com.example.smartcampuscompanion.ui.viewmodel.TaskViewModel
import java.util.*

@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    taskId: Int? = null,
    onSaveDone: () -> Unit
) {

    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var dueTime by remember { mutableStateOf("") }

    LaunchedEffect(taskId) {
        taskId?.let {
            val task = viewModel.getTaskById(it)
            task?.let {
                title = it.title
                description = it.description
                dueDate = it.dueDate
                dueTime = it.dueTime
            }
        }
    }

    val calendar = Calendar.getInstance()

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        dueDate = "$day/${month + 1}/$year"
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Text("Select Date")
            }

            Text(text = dueDate)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        dueTime = "$hour:$minute"
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }) {
                Text("Select Time")
            }

            Text(text = dueTime)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val task = Task(
                        id = taskId ?: 0,
                        title = title,
                        description = description,
                        dueDate = dueDate,
                        dueTime = dueTime
                    )

                    if (taskId == null) {
                        viewModel.insertTask(task)
                    } else {
                        viewModel.updateTask(task)
                    }

                    onSaveDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }
    }
}