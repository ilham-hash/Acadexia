package com.acadexia.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acadexia.network.ApiClient
import com.acadexia.network.Task
import com.acadexia.network.TaskRequest
import kotlinx.coroutines.launch

@Composable
fun TasksScreen() {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var loading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = ApiClient.apiService.getTasks()
                if (response.isSuccessful) {
                    tasks = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mes Tâches", fontSize = 28.sp)
            Button(onClick = { showAddDialog = true }) {
                Text("+ Ajouter")
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Nouvelle tâche") },
                text = {
                    Column {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Titre") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        TextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("Matière") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        scope.launch {
                            try {
                                ApiClient.apiService.createTask(
                                    TaskRequest(title, description, subject)
                                )
                                val response = ApiClient.apiService.getTasks()
                                if (response.isSuccessful) {
                                    tasks = response.body() ?: emptyList()
                                }
                                title = ""
                                description = ""
                                subject = ""
                                showAddDialog = false
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }) {
                        Text("Ajouter")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tasks) { task ->
                TaskCard(task)
            }
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(task.title, fontSize = 16.sp)
                Chip(
                    onClick = { },
                    label = { Text(task.priority) }
                )
            }
            Text(
                "Statut: ${task.status}",
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
