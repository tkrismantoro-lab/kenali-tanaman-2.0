package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Snooze
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.data.knowledge.PlantKnowledgeRepository
import com.example.model.CareTask
import com.example.model.CareTaskType
import com.example.model.PlantProblemRecord
import com.example.model.UserPlant
import com.example.ui.components.EmptyGardenCard
import com.example.ui.components.EvidenceLevelBadge
import com.example.ui.components.PillBadge
import com.example.ui.components.PlantImage
import com.example.ui.theme.LightGreen
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.WarningContainer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CareCalendarScreen(
    tasks: List<CareTask>,
    plants: List<UserPlant>,
    currentUserId: String,
    onToggleComplete: (CareTask) -> Unit,
    onReschedule: (taskId: String, newDate: String, frequencyDays: Long) -> Unit,
    onDeleteTask: (String) -> Unit,
    onAddTask: (CareTask) -> Unit,
    onBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var filterMode by remember { mutableIntStateOf(0) } // 0=Semua, 1=Belum, 2=Selesai
    var rescheduleTarget by remember { mutableStateOf<CareTask?>(null) }

    val filteredTasks = remember(tasks, filterMode) {
        when (filterMode) {
            1 -> tasks.filter { !it.completed }
            2 -> tasks.filter { it.completed }
            else -> tasks
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kalender Perawatan") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("add_care_task_button")
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = "Tambah jadwal perawatan")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Semua (${tasks.size})", "Belum Selesai", "Selesai").forEachIndexed { idx, label ->
                        FilterChip(
                            selected = filterMode == idx,
                            onClick = { filterMode = idx },
                            label = { Text(label) },
                            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                        )
                    }
                }
            }

            if (filteredTasks.isEmpty()) {
                item {
                    EmptyGardenCard(
                        title = "Belum Ada Jadwal Perawatan",
                        description = "Tambahkan jadwal siram, cek kelembapan media, atau evaluasi hama untuk tanamanmu.",
                        ctaText = "Tambah Jadwal",
                        onCtaClick = { showAddDialog = true }
                    )
                }
            } else {
                items(filteredTasks, key = { it.id }) { task ->
                    val type = CareTaskType.fromKey(task.taskType)
                    val typeIcon = when (type) {
                        CareTaskType.WATER -> Icons.Rounded.WaterDrop
                        CareTaskType.FERTILIZE -> Icons.Rounded.Eco
                        CareTaskType.PRUNE -> Icons.Rounded.ContentCut
                        CareTaskType.INSPECT -> Icons.Rounded.BugReport
                        CareTaskType.FOLLOWUP -> Icons.Rounded.Schedule
                        CareTaskType.HARVEST -> Icons.Rounded.Spa
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { onToggleComplete(task) }, modifier = Modifier.size(48.dp)) {
                                    Icon(
                                        imageVector = if (task.completed) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                        contentDescription = if (task.completed) "Selesai" else "Belum selesai",
                                        tint = if (task.completed) PrimaryGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(typeIcon, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                                    )
                                    Text(
                                        text = "${task.plantName} • ${type.labelId} • ${task.scheduledDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PrimaryGreen
                                    )
                                }
                                IconButton(onClick = { rescheduleTarget = task }, modifier = Modifier.size(48.dp)) {
                                    Icon(Icons.Rounded.Snooze, contentDescription = "Tunda jadwal")
                                }
                                IconButton(onClick = { onDeleteTask(task.id) }, modifier = Modifier.size(48.dp)) {
                                    Icon(Icons.Rounded.Delete, contentDescription = "Hapus jadwal")
                                }
                            }
                            if (task.reason.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(color = LightGreen, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Alasan: ${task.reason}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    rescheduleTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { rescheduleTarget = null },
            title = { Text("Jadwal Ulang Tugas") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(target.title, fontWeight = FontWeight.SemiBold)
                    listOf("Besok", "2 hari lagi", "3 hari lagi", "7 hari lagi").forEach { option ->
                        OutlinedButton(
                            onClick = {
                                onReschedule(target.id, option, target.frequencyDays)
                                rescheduleTarget = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                        ) {
                            Text("Pindahkan ke: $option")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { rescheduleTarget = null }) { Text("Tutup") }
            }
        )
    }

    if (showAddDialog) {
        AddCareTaskDialog(
            plants = plants,
            currentUserId = currentUserId,
            onDismiss = { showAddDialog = false },
            onSave = { task ->
                showAddDialog = false
                onAddTask(task)
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddCareTaskDialog(
    plants: List<UserPlant>,
    currentUserId: String,
    onDismiss: () -> Unit,
    onSave: (CareTask) -> Unit
) {
    var selectedPlant by remember { mutableStateOf(plants.firstOrNull()) }
    var selectedType by remember { mutableStateOf(CareTaskType.WATER) }
    var title by remember { mutableStateOf("Cek kelembapan media & siram pangkal") }
    var reason by remember { mutableStateOf("Pastikan 2 cm lapisan atas media sudah mengering sebelum menyiram.") }
    var scheduledDate by remember { mutableStateOf("Hari ini") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Jadwal Perawatan") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (plants.isNotEmpty()) {
                    Text("Tanaman:", style = MaterialTheme.typography.labelLarge)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        plants.forEach { plant ->
                            FilterChip(
                                selected = selectedPlant?.id == plant.id,
                                onClick = { selectedPlant = plant },
                                label = { Text(plant.nickname.ifBlank { plant.plantName }) },
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            )
                        }
                    }
                }
                Text("Jenis tugas:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CareTaskType.entries.forEach { t ->
                        FilterChip(
                            selected = selectedType == t,
                            onClick = { selectedType = t },
                            label = { Text(t.labelId) },
                            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                        )
                    }
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul tugas") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Alasan / catatan panduan") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = scheduledDate,
                    onValueChange = { scheduledDate = it },
                    label = { Text("Jadwal (mis. Hari ini / Besok)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = selectedPlant
                    onSave(
                        CareTask(
                            userId = currentUserId,
                            plantId = p?.id ?: "general",
                            plantName = p?.plantName ?: "Kebun Umum",
                            taskType = selectedType.key,
                            title = title.ifBlank { selectedType.labelId },
                            reason = reason,
                            scheduledDate = scheduledDate.ifBlank { "Hari ini" },
                            frequencyDays = 3L,
                            completed = false,
                            isDemoData = false
                        )
                    )
                },
                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun KnowledgeBaseScreen(
    onOpenCropGuide: (String) -> Unit,
    onOpenProblemArticle: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val crops = remember(searchQuery) { PlantKnowledgeRepository.searchPlants(searchQuery) }
    val problems = remember(searchQuery) { PlantKnowledgeRepository.searchProblems(searchQuery) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text("Basis Pengetahuan", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "20 panduan budidaya tanaman Indonesia & 100 catatan masalah berbasis bukti.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari tanaman, hama, atau gejala...") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("knowledge_search_input")
            )
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("20 Tanaman (${crops.size})") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("100 Masalah (${problems.size})") }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (selectedTab == 0) {
                items(crops, key = { it.id }) { crop ->
                    Card(
                        onClick = { onOpenCropGuide(crop.id) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("knowledge_crop_${crop.id}")
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            PlantImage(
                                imageResKey = crop.imageResKey,
                                contentDescription = crop.commonName,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(crop.commonName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(crop.scientificName, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    PillBadge(crop.category, LightGreen, PrimaryGreen)
                                    PillBadge("Panen ${crop.harvestEstimateDays}", WarningContainer, Color(0xFF92400E))
                                }
                            }
                        }
                    }
                }
            } else {
                items(problems, key = { it.problemId }) { problem ->
                    Card(
                        onClick = { onOpenProblemArticle(problem.problemId) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("knowledge_problem_${problem.problemId}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(problem.cropName, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                                EvidenceLevelBadge(problem.evidenceLevel)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(problem.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(problem.symptomDescription, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeArticleScreen(
    problem: PlantProblemRecord?,
    onAskAiAboutProblem: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(problem?.cropName ?: "Artikel Pengetahuan") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (problem == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Artikel pengetahuan tidak ditemukan.")
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = onBack) { Text("Kembali") }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    EvidenceLevelBadge(problem.evidenceLevel)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(problem.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(problem.symptomDescription, style = MaterialTheme.typography.bodyMedium)
                }

                item {
                    ArticleSectionCard("Ciri Visual Teramati", problem.visualIndicators)
                }
                item {
                    ArticleSectionCard("Kemungkinan Penyebab", problem.possibleCauses)
                }
                item {
                    ArticleSectionCard("Pertanyaan Pengecekan Mandiri", problem.diagnosticQuestions)
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Pembeda Utama", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(problem.distinguishingFeatures, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Penanganan Organik", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                            Text(problem.organicTreatment, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Penanganan Nonorganik / Terpadu", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(problem.nonOrganicTreatment, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                item {
                    ArticleSectionCard("Tindakan Pencegahan", problem.preventiveActions)
                }
                item {
                    Surface(color = LightGreen, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Evaluasi: ${problem.followUpDays} hari", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("Respons diharapkan: ${problem.expectedResponse}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Sumber: ${problem.referenceSource}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                            Text("Status validasi: ${problem.validationStatus}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                item {
                    Button(
                        onClick = { onAskAiAboutProblem("Bagaimana penanganan ${problem.title} pada tanaman ${problem.cropName}?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                    ) {
                        Icon(Icons.Rounded.Chat, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tanyakan Lebih Lanjut ke Plant AI")
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleSectionCard(title: String, items: List<String>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            items.forEach { line ->
                Text("• $line", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}
