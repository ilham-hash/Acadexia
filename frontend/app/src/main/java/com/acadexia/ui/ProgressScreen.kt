package com.acadexia.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acadexia.network.ApiClient
import com.acadexia.network.StatsResponse
import kotlinx.coroutines.launch

@Composable
fun ProgressScreen() {
    var stats by remember { mutableStateOf<StatsResponse?>(null) }
    var loading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = ApiClient.apiService.getStats()
                if (response.isSuccessful) {
                    stats = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Ma Progression", fontSize = 28.sp, modifier = Modifier.padding(bottom = 16.dp))

        if (stats != null) {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Statistiques Globales", fontSize = 20.sp, modifier = Modifier.padding(bottom = 12.dp))
                    Text("Niveau: ${stats!!.level}", fontSize = 16.sp)
                    Text("XP: ${stats!!.xp}", fontSize = 16.sp)
                    Text("Streak: 🔥 ${stats!!.streak} jours", fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
                    Text("Temps d'étude: ⏱️ ${stats!!.totalStudyTime} heures", fontSize = 16.sp)
                }
            }

            if (stats!!.badges.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Badges (${stats!!.badges.size})", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                        stats!!.badges.forEach { badge ->
                            Text("✨ $badge", fontSize = 14.sp, modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }

            if (stats!!.subjects.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Temps par matière", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                        stats!!.subjects.forEach { subject ->
                            Text("${subject.subject}: ${subject.studyTime}h", fontSize = 14.sp, modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Quizzes", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Text("Total: ${stats!!.totalQuizzes}", fontSize = 14.sp)
                    Text("Score moyen: ${stats!!.avgQuizScore}%", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        } else if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
