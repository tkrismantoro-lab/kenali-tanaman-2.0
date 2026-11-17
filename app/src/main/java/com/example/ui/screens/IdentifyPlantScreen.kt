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
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.knowledge.PlantKnowledgeRepository
import com.example.model.AiIdentificationResult
import com.example.model.CultivationMethod
import com.example.model.GrowthStage
import com.example.ui.components.ConfidenceBadge
import com.example.ui.components.PhotoPickerButtons
import com.example.ui.components.PillBadge
import com.example.ui.components.decodeDrawableToBitmap
import com.example.ui.components.encodeBitmapToBase64
import com.example.ui.theme.LightGreen
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.WarningContainer
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IdentifyPlantScreen(
    bitmap: Bitmap?,
    identState: UiState<AiIdentificationResult>?,
    preferredMethod: CultivationMethod,
    onBitmapSelected: (Bitmap) -> Unit,
    onAnalyzeClick: (Bitmap) -> Unit,
    onResetClick: () -> Unit,
    onStartGuide: (String) -> Unit,
    onCheckHealthWithPhoto: (String, String, Bitmap?) -> Unit,
    onSaveToGarden: (
        knowledgeId: String,
        plantName: String,
        nickname: String,
        scientificName: String,
        category: String,
        method: CultivationMethod,
        location: String,
        imageResKey: String?,
        imageBase64: String?
    ) -> Unit
) {
    val context = LocalContext.current
    var showSaveDialog by remember { mutableStateOf(false) }
    var showDiagDialog by remember { mutableStateOf(false) }

    if (showDiagDialog) {
        com.example.ui.components.FirebaseAiDiagnosticDialog(
            onDismiss = { showDiagDialog = false }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Identifikasi Tanaman",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { showDiagDialog = true },
                    modifier = Modifier.testTag("open_ai_diag_header_button")
                ) {
                    Icon(
                        Icons.Rounded.VerifiedUser,
                        contentDescription = "Diagnostik & Token AI",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = "Foto daun, bunga, buah, atau keseluruhan tanaman untuk mengenali jenis dan cara merawatnya.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Foto tanaman yang dipilih",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { onAnalyzeClick(bitmap) },
                                enabled = identState !is UiState.Loading,
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("analyze_plant_button")
                            ) {
                                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Analisis Tanaman")
                            }
                            OutlinedButton(
                                onClick = onResetClick,
                                enabled = identState !is UiState.Loading,
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            ) {
                                Icon(Icons.Rounded.Refresh, contentDescription = "Reset foto")
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(LightGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.CameraAlt,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Pilih foto dari kamera atau galeri",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PrimaryGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    PhotoPickerButtons(
                        onBitmapReady = { picked ->
                            onBitmapSelected(picked)
                        },
                        cameraTestTag = "identify_camera_button",
                        galleryTestTag = "identify_gallery_button"
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Atau uji cepat dengan foto contoh:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            Triple("Cabai", R.drawable.img_plant_cabai, "sample_photo_cabai"),
                            Triple("Tomat", R.drawable.img_plant_tomat, "sample_photo_tomat"),
                            Triple("Pakcoy", R.drawable.img_plant_pakcoy, "sample_photo_pakcoy")
                        ).forEach { (label, resId, tag) ->
                            FilterChip(
                                selected = false,
                                onClick = {
                                    decodeDrawableToBitmap(context, resId)?.let { sampleBmp ->
                                        onAnalyzeClick(sampleBmp)
                                    }
                                },
                                label = { Text("Contoh: $label") },
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag(tag)
                            )
                        }
                    }
                }
            }
        }

        when (identState) {
            is UiState.Loading -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = LightGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    "Mengamati ciri tanaman...",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "AI sedang menganalisis bentuk daun, batang, dan karakteristik visual secara objektif.",
                                    style = MaterialTheme.typography.bodySmall
                                )
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
                            Text(
                                "Gagal Menganalisis Foto",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                identState.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (bitmap != null) {
                                    Button(
                                        onClick = { onAnalyzeClick(bitmap) },
                                        modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                                    ) {
                                        Text("Coba Lagi")
                                    }
                                }
                                OutlinedButton(
                                    onClick = { showDiagDialog = true },
                                    modifier = Modifier.defaultMinSize(minHeight = 48.dp).testTag("open_ai_diag_from_error_button")
                                ) {
                                    Icon(Icons.Rounded.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Diagnostik & Token AI")
                                }
                            }
                        }
                    }
                }
            }

            is UiState.Success -> {
                val res = identState.data
                val matchedKnowledge = PlantKnowledgeRepository.findPlantByNameOrMatch(res.commonName)

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PillBadge(
                                    text = res.category,
                                    containerColor = LightGreen,
                                    contentColor = PrimaryGreen
                                )
                                ConfidenceBadge(res.confidence)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = res.commonName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = res.scientificName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (res.localNames.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Nama daerah: ${res.localNames.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(res.description, style = MaterialTheme.typography.bodyMedium)

                            if (res.visibleCharacteristics.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Ciri Visual Teramati", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                res.visibleCharacteristics.forEach { trait ->
                                    Text("• $trait", style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Manfaat & Kegunaan", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(res.benefitsOrUses, style = MaterialTheme.typography.bodySmall)

                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Kecocokan Budidaya", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(res.cultivationSuitability, style = MaterialTheme.typography.bodySmall)

                            if (!res.toxicityWarning.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    color = WarningContainer,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.WarningAmber, contentDescription = null, tint = Color(0xFFB45309))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(res.toxicityWarning, style = MaterialTheme.typography.bodySmall, color = Color(0xFF78350F))
                                    }
                                }
                            }

                            if (res.possibleMatches.isNotEmpty() || !res.photoRetakeAdvice.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        if (res.possibleMatches.isNotEmpty()) {
                                            Text("Kemungkinan Jenis Lain:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                res.possibleMatches.forEach { m ->
                                                    PillBadge(m, LightGreen, PrimaryGreen)
                                                }
                                            }
                                        }
                                        if (!res.photoRetakeAdvice.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text("Saran Foto Ulang: ${res.photoRetakeAdvice}", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showSaveDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("save_identified_plant_button")
                            ) {
                                Icon(Icons.Rounded.AddCircleOutline, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Simpan ke Kebun Saya")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    onClick = { onStartGuide(matchedKnowledge?.id ?: "cabai") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .defaultMinSize(minHeight = 48.dp)
                                ) {
                                    Icon(Icons.Rounded.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Panduan Tanam", maxLines = 1)
                                }
                                OutlinedButton(
                                    onClick = { onCheckHealthWithPhoto(res.commonName, res.scientificName, bitmap) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .defaultMinSize(minHeight = 48.dp)
                                ) {
                                    Icon(Icons.Rounded.MedicalServices, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Cek Kondisi", maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }

            null -> Unit
        }
    }

    if (showSaveDialog && identState is UiState.Success) {
        val res = identState.data
        val matchedKnowledge = PlantKnowledgeRepository.findPlantByNameOrMatch(res.commonName)
        SaveIdentifiedPlantDialog(
            result = res,
            defaultMethod = preferredMethod,
            onDismiss = { showSaveDialog = false },
            onConfirm = { nickname, location, method ->
                showSaveDialog = false
                val encoded = bitmap?.let { encodeBitmapToBase64(it) }
                onSaveToGarden(
                    matchedKnowledge?.id ?: "cabai",
                    res.commonName,
                    nickname,
                    res.scientificName,
                    res.category,
                    method,
                    location,
                    matchedKnowledge?.imageResKey,
                    encoded
                )
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SaveIdentifiedPlantDialog(
    result: AiIdentificationResult,
    defaultMethod: CultivationMethod,
    onDismiss: () -> Unit,
    onConfirm: (nickname: String, location: String, method: CultivationMethod) -> Unit
) {
    var nickname by remember { mutableStateOf(result.commonName) }
    var location by remember { mutableStateOf("Polybag 35 cm") }
    var method by remember { mutableStateOf(defaultMethod) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Simpan ke Kebun Saya") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nama panggilan tanaman") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Lokasi tanam (mis. Polybag teras)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Pilih metode budidaya:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CultivationMethod.entries.forEach { m ->
                        FilterChip(
                            selected = method == m,
                            onClick = { method = m },
                            label = { Text(m.labelId) },
                            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nickname, location, method) },
                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.defaultMinSize(minHeight = 48.dp)) {
                Text("Batal")
            }
        }
    )
}
