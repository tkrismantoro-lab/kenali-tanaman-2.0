package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CultivationMethod
import com.example.model.CultivationStageGuide
import com.example.model.PlantKnowledge
import com.example.model.UserPlant
import com.example.ui.components.EvidenceLevelBadge
import com.example.ui.components.MethodBadge
import com.example.ui.components.PlantImage
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.LightGreen
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.WarningContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CultivationMethodSelectionScreen(
    selectedMethod: CultivationMethod,
    onSelectMethod: (CultivationMethod) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pilih Metode Budidaya") },
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
                Text(
                    text = "Sesuaikan cara menanam dengan kebutuhan kebunmu",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pilihan metode memengaruhi rekomendasi pupuk, perawatan media, dan solusi hama. Kamu selalu bisa menggantinya nanti tanpa kehilangan riwayat.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(CultivationMethod.entries) { method ->
                val isSelected = method == selectedMethod
                val details = when (method) {
                    CultivationMethod.ORGANIC -> listOf(
                        "Pupuk: Kompos matang, pupuk kandang terfermentasi, pupuk hijau, biochar",
                        "Hama: Pemangkasan sanitasi, perangkap likat, predator alami, mulsa",
                        "Cocok untuk konsumsi keluarga harian & kesuburan tanah jangka panjang"
                    )
                    CultivationMethod.NON_ORGANIC -> listOf(
                        "Pupuk: NPK berimbang atau pupuk larut sesuai fase pertumbuhan",
                        "Hama: Pengendalian terpadu (PHT) + produk terdaftar sesuai label resmi",
                        "Menekankan aturan dosis aman, APD, dan masa henti panen (PHI)"
                    )
                    CultivationMethod.COMBINED -> listOf(
                        "Kombinasi kompos dasar untuk struktur media + pupuk mineral terukur",
                        "Mengutamakan pencegahan & sanitasi sebelum tindakan korektif",
                        "Catatan: Metode Kombinasi tidak termasuk budidaya organik bersertifikat"
                    )
                }
                Card(
                    onClick = { onSelectMethod(method) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) LightGreen else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) PrimaryGreen else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("method_card_${method.key}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = method.labelId,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) DarkGreen else MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) PrimaryGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(method.shortDesc, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        details.forEach { d ->
                            Text("• $d", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 52.dp)
                        .testTag("confirm_method_continue_button")
                ) {
                    Text("Terapkan Metode ${selectedMethod.labelId}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CultivationGuideScreen(
    knowledge: PlantKnowledge,
    activePlant: UserPlant?,
    selectedMethod: CultivationMethod,
    onSelectMethod: (CultivationMethod) -> Unit,
    onToggleStage: (String) -> Unit,
    onAddCropToGarden: (CultivationMethod) -> Unit,
    onOpenDoctor: () -> Unit,
    onOpenArticle: (String) -> Unit,
    onBack: () -> Unit
) {
    var localCompletedStages by remember(knowledge.id) { mutableStateOf(setOf<String>()) }
    val completedSet = activePlant?.completedStages?.toSet() ?: localCompletedStages
    var expandedStageId by remember(knowledge.id) { mutableStateOf(knowledge.stages.firstOrNull()?.stageId) }
    val progress = if (knowledge.stages.isEmpty()) 0f else completedSet.size.toFloat() / knowledge.stages.size.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panduan ${knowledge.commonName}") },
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
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlantImage(
                                imageResKey = knowledge.imageResKey,
                                contentDescription = knowledge.commonName,
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(knowledge.commonName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text(knowledge.scientificName, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(6.dp))
                                MethodBadge(selectedMethod)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(knowledge.summary, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Sinar matahari: ${knowledge.sunlight}", style = MaterialTheme.typography.bodySmall)
                        Text("Media tanam: ${knowledge.growingMedia}", style = MaterialTheme.typography.bodySmall)
                        Text("Prinsip siram: ${knowledge.wateringPrinciple}", style = MaterialTheme.typography.bodySmall)
                        Text("Perkiraan panen: ${knowledge.harvestEstimateDays}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = PrimaryGreen)

                        if (!knowledge.toxicityNote.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(color = WarningContainer, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.WarningAmber, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(knowledge.toxicityNote, style = MaterialTheme.typography.bodySmall, color = Color(0xFF78350F))
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text("Metode Panduan Aktif", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CultivationMethod.entries.forEach { method ->
                        FilterChip(
                            selected = selectedMethod == method,
                            onClick = { onSelectMethod(method) },
                            label = { Text(method.labelId) },
                            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                        )
                    }
                }
                if (selectedMethod == CultivationMethod.COMBINED) {
                    Text(
                        text = "Catatan: Metode Kombinasi tidak termasuk budidaya organik bersertifikat.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = LightGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Progres 8 Tahap Budidaya", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("${completedSet.size}/${knowledge.stages.size}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(50))
                        )
                    }
                }
            }

            items(knowledge.stages, key = { it.stageId }) { stage ->
                StageTimelineCard(
                    stage = stage,
                    method = selectedMethod,
                    isCompleted = completedSet.contains(stage.stageId),
                    isExpanded = expandedStageId == stage.stageId,
                    onToggleExpand = {
                        expandedStageId = if (expandedStageId == stage.stageId) null else stage.stageId
                    },
                    onToggleComplete = {
                        if (activePlant != null) {
                            onToggleStage(stage.stageId)
                        } else {
                            localCompletedStages = if (localCompletedStages.contains(stage.stageId)) {
                                localCompletedStages - stage.stageId
                            } else {
                                localCompletedStages + stage.stageId
                            }
                        }
                    }
                )
            }

            if (selectedMethod != CultivationMethod.NON_ORGANIC && knowledge.organicSolutions.isNotEmpty()) {
                item {
                    Text("Rekomendasi Sarana Organik", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    knowledge.organicSolutions.forEach { sol ->
                        Card(
                            onClick = { onOpenArticle("org_${knowledge.id}") },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                EvidenceLevelBadge(sol.evidenceLevel)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(sol.materialName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("Tujuan: ${sol.purpose}", style = MaterialTheme.typography.bodySmall)
                                Text("Cara pakai: ${sol.howToApply}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Pencegahan: ${sol.precautions}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFB45309))
                            }
                        }
                    }
                }
            }

            if (selectedMethod != CultivationMethod.ORGANIC && knowledge.nonOrganicSolutions.isNotEmpty()) {
                item {
                    Text("Rekomendasi Sarana Nonorganik / Terukur", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    knowledge.nonOrganicSolutions.forEach { sol ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                EvidenceLevelBadge(sol.evidenceLevel)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(sol.inputType, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("Kandungan: ${sol.nutrientComposition}", style = MaterialTheme.typography.bodySmall)
                                Text("Panduan dosis: ${sol.dosageGuidance}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("APD & Keamanan: ${sol.precautionsAndPpe}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFB45309))
                            }
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (activePlant == null) {
                        Button(
                            onClick = { onAddCropToGarden(selectedMethod) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Tambahkan ${knowledge.commonName} ke Kebun Saya")
                        }
                    }
                    OutlinedButton(
                        onClick = onOpenDoctor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                    ) {
                        Icon(Icons.Rounded.MedicalServices, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ada Gejala Aneh? Buka Dokter Tanaman")
                    }
                }
            }
        }
    }
}

@Composable
private fun StageTimelineCard(
    stage: CultivationStageGuide,
    method: CultivationMethod,
    isCompleted: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onToggleComplete: () -> Unit
) {
    val methodTips = when (method) {
        CultivationMethod.ORGANIC -> stage.organicTips
        CultivationMethod.NON_ORGANIC -> stage.nonOrganicTips
        CultivationMethod.COMBINED -> stage.combinedTips
    }

    Card(
        onClick = onToggleExpand,
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) LightGreen.copy(alpha = 0.55f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, if (isCompleted) PrimaryGreen else MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleComplete, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                        contentDescription = if (isCompleted) "Tahap selesai" else "Tandai tahap selesai",
                        tint = if (isCompleted) PrimaryGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) PrimaryGreen else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${stage.stageNumber}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(stage.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(stage.durationLabel, style = MaterialTheme.typography.bodySmall, color = PrimaryGreen)
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = if (isExpanded) "Tutup detail" else "Buka detail"
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(stage.summary, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Checklist Praktis:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                stage.checklist.forEach { item ->
                    Text("✓ $item", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Surface(color = LightGreen, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Tips Khusus (${method.labelId}):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = DarkGreen)
                        Text(methodTips, style = MaterialTheme.typography.bodySmall, color = DarkGreen)
                    }
                }
                if (stage.commonMistakes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Kesalahan Umum:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    stage.commonMistakes.forEach { m ->
                        Text("• $m", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tips kendala: ${stage.troubleshootingHint}",
                    style = MaterialTheme.typography.bodySmall,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
