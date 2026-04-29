package com.acadexia.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acadexia.network.ApiClient
import com.acadexia.network.Course
import com.acadexia.network.CourseRequest
import kotlinx.coroutines.launch

@Composable
fun CoursesScreen() {
    var courses by remember { mutableStateOf(listOf<Course>()) }
    var loading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = ApiClient.apiService.getCourses()
                if (response.isSuccessful) {
                    courses = response.body() ?: emptyList()
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
            Text("Mes Cours", fontSize = 28.sp)
            Button(onClick = { showAddDialog = true }) {
                Text("+ Ajouter")
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Nouveau cours") },
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
                            value = content,
                            onValueChange = { content = it },
                            label = { Text("Contenu") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        scope.launch {
                            try {
                                ApiClient.apiService.createCourse(CourseRequest(title, subject, content))
                                val response = ApiClient.apiService.getCourses()
                                if (response.isSuccessful) {
                                    courses = response.body() ?: emptyList()
                                }
                                title = ""
                                subject = ""
                                content = ""
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
            items(courses) { course ->
                CourseCard(course)
            }
        }
    }
}

@Composable
fun CourseCard(course: Course) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(course.title, fontSize = 18.sp)
            Text(course.subject, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            Text(
                course.content.take(100),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (course.summary != null) {
                Text("✅ Résumé disponible", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
