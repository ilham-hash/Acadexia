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
fun DashboardScreen(onNavigate: (String) -> Unit) {
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
        Text(
            text = "Tableau de bord",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (stats != null) {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Niveau ${stats!!.level}", fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))
                    LinearProgressIndicator(
                        progress = (stats!!.xp % 100) / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    Text("${stats!!.xp} XP", fontSize = 14.sp)
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Card(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔥", fontSize = 24.sp)
                        Text("${stats!!.streak}", fontSize = 18.sp)
                        Text("Streak", fontSize = 12.sp)
                    }
                }
                Card(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⏱️", fontSize = 24.sp)
                        Text("${stats!!.totalStudyTime}h", fontSize = 18.sp)
                        Text("Étude", fontSize = 12.sp)
                    }
                }
            }

            Text("Menu", fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))

            Button(
                onClick = { onNavigate("chat") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("💬 Chat IA")
            }

            Button(
                onClick = { onNavigate("courses") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("📚 Mes Cours")
            }

            Button(
                onClick = { onNavigate("tasks") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("✅ Mes Tâches")
            }

            Button(
                onClick = { onNavigate("progress") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("📊 Progression")
            }
        } else if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
