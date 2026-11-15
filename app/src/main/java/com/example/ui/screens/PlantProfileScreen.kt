package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CultivationMethod
import com.example.model.GrowthPhoto
import com.example.model.GrowthStage
import com.example.model.HealthStatus
import com.example.model.PlantDiagnosis
import com.example.model.TreatmentPlan
import com.example.model.TreatmentStatus
import com.example.model.UserPlant
import com.example.ui.components.DemoSimulationBanner
import com.example.ui.components.HealthStatusBadge
import com.example.ui.components.MethodBadge
import com.example.ui.components.PlantImage
import com.example.ui.components.TreatmentStatusBadge
import com.example.ui.theme.LightGreen
import com.example.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlantProfileScreen(
    plant: UserPlant?,
    diagnoses: List<PlantDiagnosis>,
    treatments: List<TreatmentPlan>,
    photos: List<GrowthPhoto>,
    onBack: () -> Unit,
    onUpdatePlant: (UserPlant) -> Unit,
    onSwitchMethod: (CultivationMethod) -> Unit,
    onDeletePlant: () -> Unit,
    onOpenGuide: (UserPlant) -> Unit,
    onOpenPlantDoctor: (UserPlant) -> Unit,
    onOpenTreatments: (UserPlant) -> Unit,
    onOpenMonitoring: (UserPlant) -> Unit,
    onOpenCalendar: () -> Unit
) {
    if (plant == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detail Tanaman") },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Tanaman tidak ditemukan atau sudah dihapus.")
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onBack) { Text("Kembali ke Kebun Saya") }
            }
        }
        return
    }

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showMethodPicker by remember { mutableStateOf(false) }

    val plantMethod = CultivationMethod.fromKey(plant.cultivationMethod)
    val plantStage = GrowthStage.fromKey(plant.growthStage)
    val plantHealth = HealthStatus.fromKey(plant.healthStatus)

    val relatedDiagnoses = remember(diagnoses, plant.id) { diagnoses.filter { it.plantId == plant.id } }
    val relatedTreatments = remember(treatments, plant.id) { treatments.filter { it.plantId == plant.id } }
    val relatedPhotos = remember(photos, plant.id) { photos.filter { it.plantId == plant.id } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(plant.nickname.ifBlank { plant.plantName }) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("edit_plant_button")
                    ) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Edit tanaman")
                    }
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("delete_plant_button")
                    ) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Hapus tanaman")
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (plant.isDemoData) {
                item {
                    DemoSimulationBanner("Tanaman ini bagian dari contoh data simulasi. Kamu bisa mengubah atau menghapusnya kapan saja.")
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlantImage(
                                imageResKey = plant.imageResKey,
                                imageBase64 = plant.imageBase64,
                                contentDescription = plant.plantName,
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(RoundedCornerShape(18.dp))
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = plant.plantName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                if (plant.scientificName.isNotBlank()) {
                                    Text(
                                        text = plant.scientificName,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    HealthStatusBadge(plantHealth)
                                    MethodBadge(plantMethod)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        InfoPairRow("Lokasi Tanam", plant.plantingLocation)
                        InfoPairRow("Tanggal Tanam", plant.plantingDate)
                        InfoPairRow("Fase Pertumbuhan", plantStage.labelId)
                        InfoPairRow("Progres Panduan", "${plant.completedStages.size} / 8 tahap selesai")
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = LightGreen),
                    border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Metode: ${plantMethod.labelId}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Kamu dapat mengganti metode kapan saja tanpa menghapus riwayat tanaman.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(
                            onClick = { showMethodPicker = true },
                            modifier = Modifier
                                .defaultMinSize(minHeight = 48.dp)
                                .testTag("switch_method_button")
                        ) {
                            Icon(Icons.Rounded.SwapHoriz, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ganti")
                        }
                    }
                }
            }

            item {
                Text("Menu Perawatan Tanaman", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { onOpenGuide(plant) },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp)
                                .testTag("plant_open_guide_button")
                        ) {
                            Icon(Icons.Rounded.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Panduan 8 Tahap", maxLines = 1)
                        }
                        Button(
                            onClick = { onOpenPlantDoctor(plant) },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp)
                                .testTag("plant_open_doctor_button")
                        ) {
                            Icon(Icons.Rounded.MedicalServices, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Dokter AI", maxLines = 1)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { onOpenTreatments(plant) },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp)
                        ) {
                            Icon(Icons.Rounded.TrackChanges, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Perawatan (${relatedTreatments.size})", maxLines = 1)
                        }
                        OutlinedButton(
                            onClick = { onOpenMonitoring(plant) },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp)
                        ) {
                            Icon(Icons.Rounded.PhotoCamera, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Foto (${relatedPhotos.size})", maxLines = 1)
                        }
                    }
                    OutlinedButton(
                        onClick = onOpenCalendar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                    ) {
                        Icon(Icons.Rounded.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Lihat Jadwal Perawatan")
                    }
                }
            }

            if (relatedTreatments.isNotEmpty()) {
                item {
                    Text("Rencana Perawatan Terbaru", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    relatedTreatments.take(3).forEach { plan ->
                        Card(
                            onClick = { onOpenTreatments(plant) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(plan.problem, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    TreatmentStatusBadge(TreatmentStatus.fromKey(plan.status))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Evaluasi: ${plan.followUpDate}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            if (relatedDiagnoses.isNotEmpty()) {
                item {
                    Text("Riwayat Diagnosis (${relatedDiagnoses.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    relatedDiagnoses.take(3).forEach { diag ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(diag.conditionSummary, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Foto: ${diag.photoType} • Evaluasi ${diag.followUpDays} hari",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        AddEditPlantDialog(
            existing = plant,
            defaultMethod = plantMethod,
            onDismiss = { showEditDialog = false },
            onConfirm = { knowledgeId, plantName, nickname, sci, cat, method, location, date, stage, imgKey ->
                showEditDialog = false
                onUpdatePlant(
                    plant.copy(
                        plantKnowledgeId = knowledgeId,
                        plantName = plantName,
                        nickname = nickname,
                        scientificName = sci,
                        category = cat,
                        cultivationMethod = method.key,
                        plantingLocation = location,
                        plantingDate = date,
                        growthStage = stage.key,
                        imageResKey = imgKey
                    )
                )
            }
        )
    }

    if (showMethodPicker) {
        AlertDialog(
            onDismissRequest = { showMethodPicker = false },
            title = { Text("Pilih Metode Budidaya") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Mengganti metode akan menyesuaikan panduan nutrisi dan perawatan ke depan. Riwayat sebelumnya tetap aman.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    CultivationMethod.entries.forEach { m ->
                        Card(
                            onClick = {
                                showMethodPicker = false
                                onSwitchMethod(m)
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (plantMethod == m) LightGreen else MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(
                                width = if (plantMethod == m) 2.dp else 1.dp,
                                color = if (plantMethod == m) PrimaryGreen else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(m.labelId, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text(m.shortDesc, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMethodPicker = false }) { Text("Tutup") }
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Hapus Tanaman?") },
            text = { Text("Yakin ingin menghapus ${plant.nickname.ifBlank { plant.plantName }} dari Kebun Saya?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeletePlant()
                    },
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun InfoPairRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}
