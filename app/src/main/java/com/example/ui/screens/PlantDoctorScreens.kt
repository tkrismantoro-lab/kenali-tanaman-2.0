package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AiDiagnosisResult
import com.example.model.CultivationMethod
import com.example.model.GrowthStage
import com.example.model.PlantDiagnosis
import com.example.model.UserPlant
import com.example.ui.components.ConfidenceBadge
import com.example.ui.components.HealthStatusBadge
import com.example.ui.components.MethodBadge
import com.example.ui.components.PhotoPickerButtons
import com.example.ui.components.RiskLevelBadge
import com.example.ui.theme.LightGreen
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.WarningContainer
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlantDoctorScreen(
    plants: List<UserPlant>,
    initialPlantId: String?,
    prefilledName: String?,
    prefilledSciName: String?,
    bitmap: Bitmap?,
    diagState: UiState<AiDiagnosisResult>?,
    currentUserId: String,
    onBitmapSelected: (Bitmap) -> Unit,
    onRunDiagnosis: (
        bitmap: Bitmap?,
        plantName: String,
        scientificName: String,
        method: CultivationMethod,
        stage: GrowthStage,
        photoType: String,
        symptomNotes: String,
        answers: Map<String, String>
    ) -> Unit,
    onSaveDiagnosis: (PlantDiagnosis, createPlan: Boolean, scheduleFollowUp: Boolean) -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    val initialPlant = remember(plants, initialPlantId) {
        plants.firstOrNull { it.id == initialPlantId } ?: plants.firstOrNull()
    }
    var selectedPlant by remember(initialPlant) { mutableStateOf(initialPlant) }
    var plantMenuExpanded by remember { mutableStateOf(false) }

    var customPlantName by remember(prefilledName, selectedPlant) {
        mutableStateOf(prefilledName ?: selectedPlant?.plantName ?: "Cabai Rawit")
    }
    var selectedMethod by remember(selectedPlant) {
        mutableStateOf(CultivationMethod.fromKey(selectedPlant?.cultivationMethod))
    }
    var selectedStage by remember(selectedPlant) {
        mutableStateOf(GrowthStage.fromKey(selectedPlant?.growthStage))
    }

    val photoTypes = listOf("Seluruh tanaman", "Daun", "Batang", "Akar", "Bunga", "Buah")
    var selectedPhotoType by remember { mutableStateOf("Daun") }
    var symptomNotes by remember { mutableStateOf("") }

    val clarifyingAnswers = remember { mutableStateMapOf<String, String>() }
    var savedOnce by remember(diagState) { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dokter Tanaman AI") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
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
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = LightGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Diagnosis Awal Berbasis Bukti", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Dokter Tanaman memisahkan pengamatan visual dari kemungkinan penyebab, menyesuaikan saran dengan metode budidayamu, dan tidak pernah menebak dosis pupuk atau pestisida.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (plants.isNotEmpty()) {
                            ExposedDropdownMenuBox(
                                expanded = plantMenuExpanded,
                                onExpandedChange = { plantMenuExpanded = !plantMenuExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedPlant?.let { "${it.nickname.ifBlank { it.plantName }} (${it.plantName})" } ?: "Tanaman Umum",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Pilih tanaman dari Kebun Saya") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = plantMenuExpanded) },
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = plantMenuExpanded,
                                    onDismissRequest = { plantMenuExpanded = false }
                                ) {
                                    plants.forEach { plant ->
                                        DropdownMenuItem(
                                            text = { Text("${plant.nickname.ifBlank { plant.plantName }} (${plant.plantName})") },
                                            onClick = {
                                                selectedPlant = plant
                                                customPlantName = plant.plantName
                                                selectedMethod = CultivationMethod.fromKey(plant.cultivationMethod)
                                                selectedStage = GrowthStage.fromKey(plant.growthStage)
                                                plantMenuExpanded = false
                                            }
                                        )
                                    }
                                    DropdownMenuItem(
                                        text = { Text("Tanaman Lain / Belum Disimpan") },
                                        onClick = {
                                            selectedPlant = null
                                            plantMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        if (selectedPlant == null) {
                            OutlinedTextField(
                                value = customPlantName,
                                onValueChange = { customPlantName = it },
                                label = { Text("Jenis tanaman") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Text("Metode Budidaya:", style = MaterialTheme.typography.labelLarge)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CultivationMethod.entries.forEach { m ->
                                FilterChip(
                                    selected = selectedMethod == m,
                                    onClick = { selectedMethod = m },
                                    label = { Text(m.labelId) },
                                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                                )
                            }
                        }

                        Text("Bagian yang Difoto:", style = MaterialTheme.typography.labelLarge)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            photoTypes.forEach { pt ->
                                FilterChip(
                                    selected = selectedPhotoType == pt,
                                    onClick = { selectedPhotoType = pt },
                                    label = { Text(pt) },
                                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                                )
                            }
                        }

                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Foto gejala tanaman",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                        }

                        PhotoPickerButtons(
                            onBitmapReady = onBitmapSelected,
                            cameraTestTag = "doctor_camera_button",
                            galleryTestTag = "doctor_gallery_button"
                        )

                        OutlinedTextField(
                            value = symptomNotes,
                            onValueChange = { symptomNotes = it },
                            label = { Text("Ceritakan gejala yang terlihat") },
                            placeholder = { Text("Contoh: Daun bawah menguning sejak 3 hari lalu setelah hujan deras...") },
                            minLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("doctor_symptom_input")
                        )

                        Text("Informasi Tambahan (membantu ketepatan):", style = MaterialTheme.typography.labelLarge)
                        ClarifyingQuestionChipRow(
                            question = "Kondisi media tanam saat ini?",
                            options = listOf("Becek / terlalu basah", "Lembap normal", "Kering / retak"),
                            selected = clarifyingAnswers["Kondisi media"],
                            onSelect = { clarifyingAnswers["Kondisi media"] = it }
                        )
                        ClarifyingQuestionChipRow(
                            question = "Sudah berapa lama gejala muncul?",
                            options = listOf("1-2 hari", "3-7 hari", "> 1 minggu"),
                            selected = clarifyingAnswers["Lama gejala"],
                            onSelect = { clarifyingAnswers["Lama gejala"] = it }
                        )
                        ClarifyingQuestionChipRow(
                            question = "Paparan sinar matahari?",
                            options = listOf("Penuh (>6 jam)", "Sedang (3-5 jam)", "Teduh"),
                            selected = clarifyingAnswers["Sinar matahari"],
                            onSelect = { clarifyingAnswers["Sinar matahari"] = it }
                        )

                        Button(
                            onClick = {
                                onRunDiagnosis(
                                    bitmap,
                                    customPlantName.ifBlank { "Tanaman" },
                                    prefilledSciName ?: selectedPlant?.scientificName ?: "",
                                    selectedMethod,
                                    selectedStage,
                                    selectedPhotoType,
                                    symptomNotes,
                                    clarifyingAnswers.toMap()
                                )
                            },
                            enabled = (bitmap != null || symptomNotes.isNotBlank()) && diagState !is UiState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 52.dp)
                                .testTag("run_diagnosis_button")
                        ) {
                            Icon(Icons.Rounded.MedicalServices, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Jalankan Diagnosis AI")
                        }
                    }
                }
            }

            when (diagState) {
                is UiState.Loading -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = LightGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text("Memeriksa foto & gejala...", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Text("Dokter Tanaman sedang mengevaluasi pengamatan visual dan menyusun langkah aman sesuai metode ${selectedMethod.labelId}.", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Diagnosis Belum Berhasil", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                                Text(diagState.message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(onClick = onReset) { Text("Reset") }
                            }
                        }
                    }
                }

                is UiState.Success -> {
                    val res = diagState.data
                    item {
                        DiagnosisResultSection(
                            result = res,
                            method = selectedMethod,
                            savedOnce = savedOnce,
                            onSave = { createPlan, scheduleFollowUp ->
                                savedOnce = true
                                val entity = PlantDiagnosis(
                                    userId = currentUserId,
                                    plantId = selectedPlant?.id ?: "general",
                                    plantName = res.plantName.ifBlank { customPlantName },
                                    photoType = selectedPhotoType,
                                    symptomNotes = symptomNotes,
                                    cultivationMethod = selectedMethod.key,
                                    conditionSummary = res.conditionSummary,
                                    healthStatus = res.healthStatus.key,
                                    observations = res.visualObservations,
                                    possibleCausesSummary = res.possibleCauses.map {
                                        "${it.cause} (Keyakinan: ${it.confidence}) — ${it.supportingEvidence}"
                                    },
                                    confidence = res.confidence,
                                    riskLevel = res.riskLevel,
                                    recommendedActions = res.recommendedActions,
                                    followUpDays = res.followUpDays.toLong(),
                                    limitations = res.limitations,
                                    isDemoData = false
                                )
                                onSaveDiagnosis(entity, createPlan, scheduleFollowUp)
                            }
                        )
                    }
                }

                null -> Unit
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ClarifyingQuestionChipRow(
    question: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column {
        Text(question, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            options.forEach { option ->
                FilterChip(
                    selected = selected == option,
                    onClick = { onSelect(option) },
                    label = { Text(option) },
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DiagnosisResultSection(
    result: AiDiagnosisResult,
    method: CultivationMethod,
    savedOnce: Boolean,
    onSave: (createPlan: Boolean, scheduleFollowUp: Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                HealthStatusBadge(result.healthStatus)
                ConfidenceBadge(result.confidence)
                RiskLevelBadge(result.riskLevel)
                MethodBadge(method)
            }

            Text(result.conditionSummary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            if (result.visualObservations.isNotEmpty()) {
                Column {
                    Text("1. Pengamatan Visual Objektif", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    result.visualObservations.forEach { obs ->
                        Text("• $obs", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            if (result.possibleCauses.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("2. Kemungkinan Penyebab (Bukan Kepastian Tunggal)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                    result.possibleCauses.forEach { cause ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(cause.cause, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    ConfidenceBadge(cause.confidence)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Bukti mendukung: ${cause.supportingEvidence}", style = MaterialTheme.typography.bodySmall)
                                Text("Bukti belum ada: ${cause.missingEvidence}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Cek di rumah: ${cause.additionalCheck}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            if (result.recommendedActions.isNotEmpty()) {
                Column {
                    Text("3. Langkah Penanganan (${method.labelId})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    result.recommendedActions.forEachIndexed { index, step ->
                        val text = if (step.trimStart().firstOrNull()?.isDigit() == true) step else "${index + 1}. $step"
                        Text(text, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }

            Surface(
                color = LightGreen,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Pemantauan Ulang: ${result.followUpDays} hari lagi", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("Tanda membaik: ${result.expectedImprovement}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Surface(
                color = WarningContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Rounded.WarningAmber, contentDescription = null, tint = Color(0xFFB45309))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Kapan perlu bantuan ahli / tindakan cepat:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                        Text(result.warningSigns, style = MaterialTheme.typography.bodySmall, color = Color(0xFF78350F))
                        if (result.limitations.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Keterbatasan analisis:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                            result.limitations.forEach { lim ->
                                Text("• $lim", style = MaterialTheme.typography.bodySmall, color = Color(0xFF78350F))
                            }
                        }
                    }
                }
            }

            if (result.clarifyingQuestions.isNotEmpty()) {
                Column {
                    Text("Pertanyaan Lanjutan untuk Diagnosis Lebih Tepat:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    result.clarifyingQuestions.forEach { q ->
                        Text("• $q", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Button(
                onClick = { onSave(true, true) },
                enabled = !savedOnce,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("save_diagnosis_and_plan_button")
            ) {
                Icon(
                    imageVector = if (savedOnce) Icons.Rounded.CheckCircle else Icons.Rounded.Schedule,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (savedOnce) "Tersimpan ke Riwayat & Rencana" else "Simpan & Buat Rencana + Jadwal Ulang")
            }
        }
    }
}
