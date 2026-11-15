package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CareTask
import com.example.model.HealthStatus
import com.example.model.UserPlant
import com.example.ui.components.DemoSimulationBanner
import com.example.ui.components.HealthStatusBadge
import com.example.ui.components.PillBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.LightGreen
import com.example.ui.theme.Mint
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningContainer
import com.example.ui.viewmodel.GardenSnapshot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    snapshot: GardenSnapshot,
    isDemoMode: Boolean,
    onNavigateIdentify: () -> Unit,
    onNavigateStartGrowing: () -> Unit,
    onNavigatePlantDoctor: (String?) -> Unit,
    onNavigateGarden: () -> Unit,
    onNavigateKnowledge: () -> Unit,
    onNavigateCalendar: () -> Unit,
    onNavigateAskAi: () -> Unit,
    onOpenPlantDetail: (String) -> Unit,
    onOpenFeaturedGuide: () -> Unit,
    onToggleCareTask: (CareTask) -> Unit
) {
    val displayName = snapshot.profile?.displayName?.takeIf { it.isNotBlank() } ?: "Pekebun"
    val pendingTasks = remember(snapshot.careTasks) {
        snapshot.careTasks.filter { !it.completed }
    }
    val alertPlant = remember(snapshot.plants) {
        snapshot.plants.firstOrNull {
            val status = HealthStatus.fromKey(it.healthStatus)
            status == HealthStatus.PROBLEM || status == HealthStatus.NEEDS_ATTENTION || status == HealthStatus.FOLLOW_UP
        }
    }
    val healthyCount = remember(snapshot.plants) {
        snapshot.plants.count { HealthStatus.fromKey(it.healthStatus) == HealthStatus.HEALTHY }
    }
    val attentionCount = remember(snapshot.plants) {
        snapshot.plants.count { HealthStatus.fromKey(it.healthStatus) != HealthStatus.HEALTHY }
    }

    var showNotificationSheet by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Halo, $displayName",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isDemoMode) "Mode Demo Lokal — Kebun Siap Dipantau" else "Kebun Digital Tersinkronisasi",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = { showNotificationSheet = true },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("home_notifications_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (pendingTasks.isNotEmpty()) {
                                Badge { Text("${pendingTasks.size}") }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = "Lihat pengingat perawatan"
                        )
                    }
                }
            }
        }

        if (isDemoMode || snapshot.plants.any { it.isDemoData }) {
            item {
                DemoSimulationBanner(
                    message = if (isDemoMode) {
                        "Mode Demo aktif dengan contoh data simulasi (Cabai, Tomat, Pakcoy). Semua perubahan tersimpan sementara di perangkat."
                    } else {
                        "Contoh data simulasi tersedia sebagai referensi awal. Kamu dapat mengedit atau menambah tanamanmu sendiri."
                    }
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_garden),
                        contentDescription = "Kebun rumah hijau",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        DarkGreen.copy(alpha = 0.25f),
                                        DarkGreen.copy(alpha = 0.88f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(18.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = "Kenali tanamanmu, rawat dengan lebih mudah",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Foto daun untuk kenali jenis tanaman atau ikuti panduan budidaya langkah demi langkah.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Mint
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = onNavigateIdentify,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = DarkGreen
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("home_identify_cta")
                            ) {
                                Icon(Icons.Rounded.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Identifikasi", fontWeight = FontWeight.SemiBold, maxLines = 1)
                            }
                            OutlinedButton(
                                onClick = onNavigateStartGrowing,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, Mint),
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("home_start_growing_cta")
                            ) {
                                Icon(Icons.Rounded.Spa, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Mulai Menanam", maxLines = 1)
                            }
                        }
                    }
                }
            }
        }

        item {
            SectionHeader(title = "Aksi Cepat")
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    QuickActionCard(
                        icon = Icons.Rounded.CameraAlt,
                        title = "Identifikasi Tanaman",
                        subtitle = "Kenali via foto",
                        onClick = onNavigateIdentify,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_identify")
                    )
                    QuickActionCard(
                        icon = Icons.Rounded.MedicalServices,
                        title = "Dokter Tanaman",
                        subtitle = "Diagnosis gejala AI",
                        onClick = { onNavigatePlantDoctor(null) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_doctor")
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    QuickActionCard(
                        icon = Icons.Rounded.Eco,
                        title = "Kebun Saya",
                        subtitle = "${snapshot.plants.size} tanaman aktif",
                        onClick = onNavigateGarden,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_garden")
                    )
                    QuickActionCard(
                        icon = Icons.Rounded.Chat,
                        title = "Tanya Plant AI",
                        subtitle = "Konsultasi cepat",
                        onClick = onNavigateAskAi,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_chat")
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatColumn(value = "${snapshot.plants.size}", label = "Total Tanaman", color = PrimaryGreen)
                    StatColumn(value = "$healthyCount", label = "Kondisi Sehat", color = Color(0xFF16A34A))
                    StatColumn(value = "$attentionCount", label = "Perlu Perhatian", color = Color(0xFFD97706))
                }
            }
        }

        if (alertPlant != null) {
            item {
                PlantHealthAlertCard(
                    plant = alertPlant,
                    onInspect = { onNavigatePlantDoctor(alertPlant.id) },
                    onOpenPlant = { onOpenPlantDetail(alertPlant.id) }
                )
            }
        }

        item {
            SectionHeader(
                title = "Perawatan Hari Ini",
                subtitle = "${pendingTasks.size} tugas belum selesai",
                actionLabel = "Kalender",
                onActionClick = onNavigateCalendar
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (snapshot.careTasks.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.CalendarMonth, contentDescription = null, tint = PrimaryGreen)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Belum ada jadwal perawatan hari ini. Buka Kalender untuk menambahkan pengingat.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    snapshot.careTasks.take(4).forEach { task ->
                        HomeCareTaskRow(task = task, onToggle = { onToggleCareTask(task) })
                    }
                }
            }
        }

        item {
            SectionHeader(
                title = "Panduan Pilihan",
                actionLabel = "Lihat 20 Tanaman",
                onActionClick = onNavigateKnowledge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                onClick = onOpenFeaturedGuide,
                colors = CardDefaults.cardColors(containerColor = LightGreen),
                border = BorderStroke(1.dp, Mint),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("featured_guide_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_plant_cabai),
                        contentDescription = "Cabai Rawit",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        PillBadge(
                            text = "Panduan 8 Tahap",
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Panduan Menanam Cabai di Polybag",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                        Text(
                            text = "Cocok untuk pemula — lengkap untuk Organik, Nonorganik, & Kombinasi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkGreen.copy(alpha = 0.8f)
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Buka panduan",
                        tint = DarkGreen
                    )
                }
            }
        }

        item {
            OutlinedButton(
                onClick = onNavigateKnowledge,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("open_knowledge_base_button")
            ) {
                Icon(Icons.Rounded.MenuBook, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Jelajahi Basis Pengetahuan (20 Tanaman & 100 Masalah)")
            }
        }
    }

    if (showNotificationSheet) {
        ModalBottomSheet(onDismissRequest = { showNotificationSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    "Pengingat & Tindak Lanjut",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (pendingTasks.isEmpty()) {
                    Text(
                        "Semua tugas perawatan saat ini sudah selesai!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    pendingTasks.forEach { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(task.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Text("${task.plantName} • ${task.scheduledDate}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        showNotificationSheet = false
                        onNavigateCalendar()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp)
                ) {
                    Text("Buka Kalender Perawatan")
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.defaultMinSize(minHeight = 88.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(LightGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatColumn(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun PlantHealthAlertCard(
    plant: UserPlant,
    onInspect: () -> Unit,
    onOpenPlant: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WarningContainer.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, WarningAmber.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.WarningAmber, contentDescription = null, tint = Color(0xFFB45309))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (plant.isDemoData) "Contoh data simulasi" else "Perhatian Kesehatan Tanaman",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                }
                HealthStatusBadge(HealthStatus.fromKey(plant.healthStatus))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${plant.nickname.ifBlank { plant.plantName }} (${plant.plantName})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Memerlukan pengecekan kondisi dan pemantauan lanjutan di lokasi ${plant.plantingLocation}.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(
                    onClick = onInspect,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                ) {
                    Text("Periksa Kondisi")
                }
                OutlinedButton(
                    onClick = onOpenPlant,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                ) {
                    Text("Detail Tanaman")
                }
            }
        }
    }
}

@Composable
private fun HomeCareTaskRow(task: CareTask, onToggle: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggle,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (task.completed) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                    contentDescription = if (task.completed) "Tandai belum selesai" else "Tandai selesai",
                    tint = if (task.completed) PrimaryGreen else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = "${task.plantName} • ${task.scheduledDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (task.isDemoData) {
                Surface(shape = CircleShape, color = WarningContainer) {
                    Text(
                        "Simulasi",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
