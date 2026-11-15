package com.example.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Compare
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.GrowthPhoto
import com.example.model.GrowthStage
import com.example.model.TreatmentPlan
import com.example.model.TreatmentStatus
import com.example.model.UserPlant
import com.example.ui.components.EmptyGardenCard
import com.example.ui.components.PhotoPickerButtons
import com.example.ui.components.PillBadge
import com.example.ui.components.PlantImage
import com.example.ui.components.TreatmentStatusBadge
import com.example.ui.components.encodeBitmapToBase64
import com.example.ui.theme.DangerContainer
import com.example.ui.theme.DangerRed
import com.example.ui.theme.LightGreen
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.WarningContainer
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TreatmentTrackingScreen(
    plans: List<TreatmentPlan>,
    filterPlantId: String?,
    onUpdateOutcome: (planId: String, status: TreatmentStatus, notes: String) -> Unit,
    onReassessWithDoctor: (plantId: String) -> Unit,
    onBack: () -> Unit
) {
    val filtered = remember(plans, filterPlantId) {
        if (filterPlantId.isNullOrBlank()) plans else plans.filter { it.plantId == filterPlantId }
    }
    var evaluatingPlan by remember { mutableStateOf<TreatmentPlan?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pemantauan Perawatan") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filtered.isEmpty()) {
                item {
                    EmptyGardenCard(
                        title = "Belum Ada Rencana Perawatan",
                        description = "Rencana perawatan akan dibuat otomatis saat kamu menyimpan hasil diagnosis Dokter Tanaman AI.",
                        ctaText = "Buka Dokter Tanaman",
                        onCtaClick = { onReassessWithDoctor(filterPlantId ?: "") }
                    )
                }
            } else {
                items(filtered, key = { it.id }) { plan ->
                    val status = TreatmentStatus.fromKey(plan.status)
                    val isFailed = status == TreatmentStatus.UNCHANGED || status == TreatmentStatus.WORSENED
                    val isLocked = status == TreatmentStatus.COMPLETED

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(plan.plantName, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (plan.isDemoData) {
                                        PillBadge("Simulasi", WarningContainer, Color(0xFF92400E))
                                    }
                                    TreatmentStatusBadge(status)
                                }
                            }
                            Text(plan.problem, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (plan.suspectedCause.isNotBlank()) {
                                Text("Dugaan penyebab: ${plan.suspectedCause}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("Tindakan: ${plan.recommendedTreatment}", style = MaterialTheme.typography.bodySmall)
                            Text("Jadwal evaluasi: ${plan.followUpDate} • Harapan: ${plan.expectedResponse}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            if (plan.outcomeNotes.isNotBlank()) {
                                Surface(color = LightGreen, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                                    Text("Catatan hasil: ${plan.outcomeNotes}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(10.dp))
                                }
                            }

                            if (isFailed) {
                                Surface(color = DangerContainer, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Rounded.WarningAmber, contentDescription = null, tint = DangerRed, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Perlu Evaluasi Ulang", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = DangerRed)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "Kondisi belum membaik. Jangan langsung mengulang bahan yang sama — foto ulang kondisi terbaru untuk ditinjau.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = DangerRed
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { onReassessWithDoctor(plan.plantId) },
                                            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                                        ) {
                                            Icon(Icons.Rounded.MedicalServices, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Periksa Ulang dengan Foto")
                                        }
                                    }
                                }
                            }

                            if (!isLocked) {
                                OutlinedButton(
                                    onClick = { evaluatingPlan = plan },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .defaultMinSize(minHeight = 48.dp)
                                        .testTag("evaluate_plan_button_${plan.id}")
                                ) {
                                    Text("Perbarui Progres / Hasil")
                                }
                            } else {
                                Text("Status selesai — catatan perawatan terkunci sebagai arsip.", style = MaterialTheme.typography.bodySmall, color = PrimaryGreen)
                            }
                        }
                    }
                }
            }
        }
    }

    evaluatingPlan?.let { plan ->
        EvaluateTreatmentDialog(
            plan = plan,
            onDismiss = { evaluatingPlan = null },
            onSave = { newStatus, notes ->
                evaluatingPlan = null
                onUpdateOutcome(plan.id, newStatus, notes)
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EvaluateTreatmentDialog(
    plan: TreatmentPlan,
    onDismiss: () -> Unit,
    onSave: (TreatmentStatus, String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(TreatmentStatus.fromKey(plan.status)) }
    var notes by remember { mutableStateOf(plan.outcomeNotes) }

    val selectableStatuses = listOf(
        TreatmentStatus.IN_PROGRESS,
        TreatmentStatus.AWAITING_EVAL,
        TreatmentStatus.IMPROVED,
        TreatmentStatus.UNCHANGED,
        TreatmentStatus.WORSENED,
        TreatmentStatus.COMPLETED
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Evaluasi Hasil Perawatan") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Bagaimana kondisi tanaman sekarang?", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    selectableStatuses.forEach { st ->
                        FilterChip(
                            selected = selectedStatus == st,
                            onClick = { selectedStatus = st },
                            label = { Text(st.labelId) },
                            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                        )
                    }
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan pengamatan") },
                    placeholder = { Text("Mis. Tunas baru muncul hijau dan daun layu berkurang") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(selectedStatus, notes) },
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GrowthMonitoringScreen(
    plants: List<UserPlant>,
    photos: List<GrowthPhoto>,
    initialPlantId: String?,
    currentUserId: String,
    onAddPhoto: (GrowthPhoto) -> Unit,
    onOpenCompare: (String?) -> Unit,
    onBack: () -> Unit
) {
    var selectedPlantId by remember(initialPlantId) { mutableStateOf(initialPlantId) }
    var showAddPhotoDialog by remember { mutableStateOf(false) }

    val filteredPhotos = remember(photos, selectedPlantId) {
        if (selectedPlantId.isNullOrBlank()) photos else photos.filter { it.plantId == selectedPlantId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pemantauan Pertumbuhan") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { showAddPhotoDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("add_growth_photo_button")
                    ) {
                        Icon(Icons.Rounded.AddAPhoto, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tambah Foto")
                    }
                    OutlinedButton(
                        onClick = { onOpenCompare(selectedPlantId) },
                        enabled = filteredPhotos.size >= 2,
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("open_compare_photos_button")
                    ) {
                        Icon(Icons.Rounded.Compare, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Bandingkan")
                    }
                }
            }

            if (plants.isNotEmpty()) {
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = selectedPlantId == null,
                            onClick = { selectedPlantId = null },
                            label = { Text("Semua Tanaman") },
                            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                        )
                        plants.forEach { plant ->
                            FilterChip(
                                selected = selectedPlantId == plant.id,
                                onClick = { selectedPlantId = plant.id },
                                label = { Text(plant.nickname.ifBlank { plant.plantName }) },
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            )
                        }
                    }
                }
            }

            if (filteredPhotos.isEmpty()) {
                item {
                    EmptyGardenCard(
                        title = "Belum Ada Foto Perkembangan",
                        description = "Unggah foto berkala setiap minggu untuk melihat pertumbuhan tanaman dan membandingkan sebelum-sesudah.",
                        ctaText = "Tambah Foto Pertama",
                        onCtaClick = { showAddPhotoDialog = true }
                    )
                }
            } else {
                items(filteredPhotos, key = { it.id }) { photo ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${photo.plantName} • ${photo.dateLabel}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                PillBadge(
                                    GrowthStage.fromKey(photo.growthStage).labelId,
                                    LightGreen,
                                    PrimaryGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            PlantImage(
                                imageResKey = photo.photoResKey,
                                imageBase64 = photo.photoBase64,
                                contentDescription = "${photo.plantName} ${photo.dateLabel}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (photo.notes.isNotBlank()) {
                                Text(photo.notes, style = MaterialTheme.typography.bodySmall)
                            }
                            if (!photo.aiSummary.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(color = LightGreen, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = photo.aiSummary,
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

    if (showAddPhotoDialog) {
        AddGrowthPhotoDialog(
            plants = plants,
            defaultPlantId = selectedPlantId ?: plants.firstOrNull()?.id,
            currentUserId = currentUserId,
            onDismiss = { showAddPhotoDialog = false },
            onSave = { photo ->
                showAddPhotoDialog = false
                onAddPhoto(photo)
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddGrowthPhotoDialog(
    plants: List<UserPlant>,
    defaultPlantId: String?,
    currentUserId: String,
    onDismiss: () -> Unit,
    onSave: (GrowthPhoto) -> Unit
) {
    var selectedPlant by remember {
        mutableStateOf(plants.firstOrNull { it.id == defaultPlantId } ?: plants.firstOrNull())
    }
    var dateLabel by remember { mutableStateOf("Hari ini") }
    var stage by remember {
        mutableStateOf(GrowthStage.fromKey(selectedPlant?.growthStage))
    }
    var notes by remember { mutableStateOf("") }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Catat Foto Perkembangan") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (plants.isNotEmpty()) {
                    Text("Pilih tanaman:", style = MaterialTheme.typography.labelLarge)
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

                if (capturedBitmap != null) {
                    Image(
                        bitmap = capturedBitmap!!.asImageBitmap(),
                        contentDescription = "Pratinjau foto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                PhotoPickerButtons(onBitmapReady = { capturedBitmap = it })

                OutlinedTextField(
                    value = dateLabel,
                    onValueChange = { dateLabel = it },
                    label = { Text("Tanggal / label") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Fase pertumbuhan:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    GrowthStage.entries.forEach { s ->
                        FilterChip(
                            selected = stage == s,
                            onClick = { stage = s },
                            label = { Text(s.labelId) },
                            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                        )
                    }
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan pengamatan") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = selectedPlant
                    onSave(
                        GrowthPhoto(
                            userId = currentUserId,
                            plantId = p?.id ?: "general",
                            plantName = p?.plantName ?: "Tanaman",
                            dateLabel = dateLabel.ifBlank { "Hari ini" },
                            growthStage = stage.key,
                            notes = notes,
                            photoResKey = if (capturedBitmap == null) p?.imageResKey else null,
                            photoBase64 = capturedBitmap?.let { encodeBitmapToBase64(it) },
                            isDemoData = false
                        )
                    )
                },
                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
            ) {
                Text("Simpan Foto")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PhotoComparisonScreen(
    photos: List<GrowthPhoto>,
    compareState: UiState<String>?,
    onCompareWithAi: (beforeBitmap: Bitmap?, afterBitmap: Bitmap?, plantName: String, beforeLabel: String, afterLabel: String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var beforePhoto by remember(photos) { mutableStateOf(photos.lastOrNull()) }
    var afterPhoto by remember(photos) { mutableStateOf(photos.firstOrNull()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perbandingan Foto") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (photos.size < 2) {
                item {
                    EmptyGardenCard(
                        title = "Butuh Minimal 2 Foto",
                        description = "Tambahkan minimal dua catatan foto pertumbuhan untuk membandingkan perkembangan sebelum dan sesudah.",
                        ctaText = "Kembali",
                        onCtaClick = onBack
                    )
                }
            } else {
                item {
                    Text("Pilih Foto Sebelum:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        photos.forEach { p ->
                            FilterChip(
                                selected = beforePhoto?.id == p.id,
                                onClick = { beforePhoto = p },
                                label = { Text("${p.plantName} (${p.dateLabel})") },
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            )
                        }
                    }
                }
                item {
                    Text("Pilih Foto Sesudah:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        photos.forEach { p ->
                            FilterChip(
                                selected = afterPhoto?.id == p.id,
                                onClick = { afterPhoto = p },
                                label = { Text("${p.plantName} (${p.dateLabel})") },
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            )
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        beforePhoto?.let { p ->
                            Card(modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Sebelum", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                                    Text(p.dateLabel, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    PlantImage(
                                        imageResKey = p.photoResKey,
                                        imageBase64 = p.photoBase64,
                                        contentDescription = "Foto sebelum",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                }
                            }
                        }
                        afterPhoto?.let { p ->
                            Card(modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Sesudah", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                                    Text(p.dateLabel, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    PlantImage(
                                        imageResKey = p.photoResKey,
                                        imageBase64 = p.photoBase64,
                                        contentDescription = "Foto sesudah",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Button(
                        onClick = {
                            val b = beforePhoto
                            val a = afterPhoto
                            if (b != null && a != null) {
                                onCompareWithAi(
                                    resolvePhotoToBitmap(context, b),
                                    resolvePhotoToBitmap(context, a),
                                    a.plantName,
                                    b.dateLabel,
                                    a.dateLabel
                                )
                            }
                        },
                        enabled = compareState !is UiState.Loading && beforePhoto != null && afterPhoto != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("compare_photos_ai_button")
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analisis Perubahan dengan AI")
                    }
                }
                when (compareState) {
                    is UiState.Loading -> item {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Membandingkan perkembangan foto...")
                        }
                    }
                    is UiState.Error -> item {
                        Text(compareState.message, color = MaterialTheme.colorScheme.error)
                    }
                    is UiState.Success -> item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = LightGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Hasil Perbandingan Visual", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(compareState.data, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    null -> Unit
                }
            }
        }
    }
}

private fun resolvePhotoToBitmap(context: Context, photo: GrowthPhoto): Bitmap? {
    if (!photo.photoBase64.isNullOrBlank()) {
        runCatching {
            val bytes = Base64.decode(photo.photoBase64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
    val resId = when (photo.photoResKey) {
        "img_plant_cabai" -> R.drawable.img_plant_cabai
        "img_plant_tomat" -> R.drawable.img_plant_tomat
        "img_plant_pakcoy" -> R.drawable.img_plant_pakcoy
        else -> null
    } ?: return null
    return BitmapFactory.decodeResource(context.resources, resId)
}
