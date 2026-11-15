package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.knowledge.PlantKnowledgeRepository
import com.example.model.CultivationMethod
import com.example.model.GrowthStage
import com.example.model.HealthStatus
import com.example.model.UserPlant
import com.example.ui.components.EmptyGardenCard
import com.example.ui.components.HealthStatusBadge
import com.example.ui.components.MethodBadge
import com.example.ui.components.PillBadge
import com.example.ui.components.PlantImage
import com.example.ui.theme.WarningContainer

@Composable
fun MyGardenScreen(
    plants: List<UserPlant>,
    preferredMethod: CultivationMethod,
    onPlantClick: (String) -> Unit,
    onAddPlant: (
        knowledgeId: String,
        plantName: String,
        nickname: String,
        scientificName: String,
        category: String,
        method: CultivationMethod,
        location: String,
        plantingDate: String,
        growthStage: GrowthStage,
        imageResKey: String?
    ) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMethodFilter by remember { mutableStateOf<CultivationMethod?>(null) }
    var selectedHealthFilter by remember { mutableStateOf<HealthStatus?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredPlants = remember(plants, searchQuery, selectedMethodFilter, selectedHealthFilter) {
        plants.filter { plant ->
            val q = searchQuery.trim().lowercase()
            val matchesQuery = q.isEmpty() ||
                plant.plantName.lowercase().contains(q) ||
                plant.nickname.lowercase().contains(q) ||
                plant.plantingLocation.lowercase().contains(q)
            val matchesMethod = selectedMethodFilter == null ||
                plant.cultivationMethod == selectedMethodFilter!!.key
            val matchesHealth = selectedHealthFilter == null ||
                plant.healthStatus == selectedHealthFilter!!.key
            matchesQuery && matchesMethod && matchesHealth
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Kebun Saya",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${plants.size} tanaman terdaftar",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("add_plant_button")
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Tambah")
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari nama tanaman atau lokasi...") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Rounded.Clear, contentDescription = "Hapus pencarian")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("garden_search_input")
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedMethodFilter == null && selectedHealthFilter == null,
                    onClick = {
                        selectedMethodFilter = null
                        selectedHealthFilter = null
                    },
                    label = { Text("Semua") },
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                )
                CultivationMethod.entries.forEach { method ->
                    FilterChip(
                        selected = selectedMethodFilter == method,
                        onClick = {
                            selectedMethodFilter = if (selectedMethodFilter == method) null else method
                        },
                        label = { Text(method.labelId) },
                        modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                    )
                }
                FilterChip(
                    selected = selectedHealthFilter == HealthStatus.NEEDS_ATTENTION,
                    onClick = {
                        selectedHealthFilter = if (selectedHealthFilter == HealthStatus.NEEDS_ATTENTION) null else HealthStatus.NEEDS_ATTENTION
                    },
                    label = { Text("Perlu Perhatian") },
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                )
            }
        }

        if (filteredPlants.isEmpty()) {
            item {
                EmptyGardenCard(
                    title = if (plants.isEmpty()) "Kebun Masih Kosong" else "Tidak Ada Tanaman Cocok",
                    description = if (plants.isEmpty()) {
                        "Tambahkan tanaman pertamamu untuk mulai memantau pertumbuhan dan jadwal perawatan."
                    } else {
                        "Coba ubah kata kunci pencarian atau hapus filter yang aktif."
                    },
                    ctaText = if (plants.isEmpty()) "Tambah Tanaman" else "Reset Filter",
                    onCtaClick = {
                        if (plants.isEmpty()) {
                            showAddDialog = true
                        } else {
                            searchQuery = ""
                            selectedMethodFilter = null
                            selectedHealthFilter = null
                        }
                    }
                )
            }
        } else {
            items(filteredPlants, key = { it.id }) { plant ->
                GardenPlantCard(plant = plant, onClick = { onPlantClick(plant.id) })
            }
        }
    }

    if (showAddDialog) {
        AddEditPlantDialog(
            existing = null,
            defaultMethod = preferredMethod,
            onDismiss = { showAddDialog = false },
            onConfirm = { knowledgeId, plantName, nickname, sci, cat, method, location, date, stage, imgKey ->
                showAddDialog = false
                onAddPlant(knowledgeId, plantName, nickname, sci, cat, method, location, date, stage, imgKey)
            }
        )
    }
}

@Composable
private fun GardenPlantCard(
    plant: UserPlant,
    onClick: () -> Unit
) {
    val method = CultivationMethod.fromKey(plant.cultivationMethod)
    val health = HealthStatus.fromKey(plant.healthStatus)
    val stage = GrowthStage.fromKey(plant.growthStage)

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("plant_card_${plant.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlantImage(
                imageResKey = plant.imageResKey,
                imageBase64 = plant.imageBase64,
                contentDescription = plant.plantName,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = plant.nickname.ifBlank { plant.plantName },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (plant.isDemoData) {
                        PillBadge("Simulasi", WarningContainer, Color(0xFF92400E))
                    }
                }
                Text(
                    text = "${plant.plantName} • ${plant.plantingLocation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Fase: ${stage.labelId} • Tanam: ${plant.plantingDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    HealthStatusBadge(health)
                    MethodBadge(method)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditPlantDialog(
    existing: UserPlant?,
    defaultMethod: CultivationMethod,
    onDismiss: () -> Unit,
    onConfirm: (
        knowledgeId: String,
        plantName: String,
        nickname: String,
        scientificName: String,
        category: String,
        method: CultivationMethod,
        location: String,
        plantingDate: String,
        growthStage: GrowthStage,
        imageResKey: String?
    ) -> Unit
) {
    val allCrops = remember { PlantKnowledgeRepository.allPlants }
    var selectedCrop by remember {
        mutableStateOf(
            if (existing != null) {
                allCrops.firstOrNull { it.id == existing.plantKnowledgeId } ?: allCrops.first()
            } else {
                allCrops.first()
            }
        )
    }
    var cropExpanded by remember { mutableStateOf(false) }
    var customPlantName by remember { mutableStateOf(existing?.plantName ?: selectedCrop.commonName) }
    var nickname by remember { mutableStateOf(existing?.nickname ?: "") }
    var location by remember { mutableStateOf(existing?.plantingLocation ?: "Polybag 35 cm") }
    var plantingDate by remember { mutableStateOf(existing?.plantingDate ?: "2026-11-15") }
    var method by remember {
        mutableStateOf(
            if (existing != null) CultivationMethod.fromKey(existing.cultivationMethod) else defaultMethod
        )
    }
    var stage by remember {
        mutableStateOf(
            if (existing != null) GrowthStage.fromKey(existing.growthStage) else GrowthStage.SEEDLING
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "Tambah Tanaman Baru" else "Edit Data Tanaman") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = cropExpanded,
                    onExpandedChange = { cropExpanded = !cropExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCrop.commonName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Pilih jenis tanaman (20 panduan)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = cropExpanded,
                        onDismissRequest = { cropExpanded = false }
                    ) {
                        allCrops.forEach { crop ->
                            DropdownMenuItem(
                                text = { Text("${crop.commonName} (${crop.category})") },
                                onClick = {
                                    selectedCrop = crop
                                    customPlantName = crop.commonName
                                    cropExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = customPlantName,
                    onValueChange = { customPlantName = it },
                    label = { Text("Nama tanaman") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nama panggilan (opsional)") },
                    placeholder = { Text("Mis. Cabai teras depan") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Lokasi tanam") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = plantingDate,
                    onValueChange = { plantingDate = it },
                    label = { Text("Tanggal tanam (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Metode budidaya:", style = MaterialTheme.typography.labelLarge)
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

                Text("Fase pertumbuhan saat ini:", style = MaterialTheme.typography.labelLarge)
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
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        selectedCrop.id,
                        customPlantName.ifBlank { selectedCrop.commonName },
                        nickname,
                        selectedCrop.scientificName,
                        selectedCrop.category,
                        method,
                        location,
                        plantingDate,
                        stage,
                        existing?.imageResKey ?: selectedCrop.imageResKey
                    )
                },
                modifier = Modifier
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("confirm_save_plant_button")
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
