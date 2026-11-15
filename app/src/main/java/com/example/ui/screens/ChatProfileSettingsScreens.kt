package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.AssistChip
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.data.firebase.GoogleAuthHelper
import com.example.model.ChatMessage
import com.example.model.CultivationMethod
import com.example.model.UserPlant
import com.example.model.UserProfile
import com.example.ui.components.MethodBadge
import com.example.ui.components.PillBadge
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.LightGreen
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.WarningContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskPlantAiScreen(
    plants: List<UserPlant>,
    messages: List<ChatMessage>,
    isStreaming: Boolean,
    chatError: String?,
    initialPrompt: String?,
    onSendMessage: (question: String, plantContext: String) -> Unit,
    onResetChat: () -> Unit,
    onBack: () -> Unit
) {
    var selectedPlant by remember(plants) { mutableStateOf(plants.firstOrNull()) }
    var inputText by remember(initialPrompt) { mutableStateOf(initialPrompt.orEmpty()) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, messages.lastOrNull()?.text) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val contextSummary = remember(selectedPlant) {
        val p = selectedPlant
        if (p == null) {
            "Kebun rumah umum di Indonesia, belum memilih tanaman spesifik."
        } else {
            "Tanaman: ${p.plantName} (${p.scientificName}), panggilan '${p.nickname}', lokasi '${p.plantingLocation}', metode budidaya '${CultivationMethod.fromKey(p.cultivationMethod).labelId}', fase '${p.growthStage}', status kesehatan '${p.healthStatus}'."
        }
    }

    val suggestions = listOf(
        "Kapan waktu terbaik menyiram tanaman?",
        "Bagaimana cara kompos yang aman untuk pemula?",
        "Tips mencegah hama secara alami di polybag",
        "Mengapa daun bawah sering menguning?"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tanya Plant AI") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onResetChat, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Mulai chat baru")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            if (plants.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedPlant == null,
                        onClick = { selectedPlant = null },
                        label = { Text("Kebun Umum") },
                        modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                    )
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

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Surface(
                        color = WarningContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Catatan: Chat ini memberikan panduan perawatan umum. Untuk gejala penyakit, gunakan fitur Dokter Tanaman AI.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF78350F),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                items(messages, key = { it.id }) { msg ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Surface(
                            color = if (msg.isUser) PrimaryGreen else MaterialTheme.colorScheme.surface,
                            contentColor = if (msg.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(16.dp),
                            border = if (msg.isUser) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.widthIn(max = 320.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (msg.text.isBlank() && msg.isStreaming) "Menulis jawaban..." else msg.text,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = msg.timestampLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (msg.isUser) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (chatError != null) {
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = chatError,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                suggestions.forEach { s ->
                    AssistChip(
                        onClick = { onSendMessage(s, contextSummary) },
                        label = { Text(s) },
                        modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Tanya seputar tanaman...") },
                    maxLines = 3,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field")
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val q = inputText
                        inputText = ""
                        onSendMessage(q, contextSummary)
                    },
                    enabled = inputText.isNotBlank() && !isStreaming,
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("send_chat_button"),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    if (isStreaming) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = "Kirim pertanyaan")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileAndSettingsScreen(
    profile: UserProfile?,
    userEmail: String?,
    isDemoMode: Boolean,
    plantCount: Int,
    treatmentCount: Int,
    photoCount: Int,
    onSaveProfile: (
        displayName: String,
        experienceLevel: String,
        gardeningType: String,
        preferredMethod: CultivationMethod,
        remindersEnabled: Boolean
    ) -> Unit,
    onReplayOnboarding: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenTreatments: () -> Unit,
    onOpenMonitoring: () -> Unit,
    onOpenAskAi: () -> Unit,
    onSignedInFromDemo: () -> Unit,
    onSignedOut: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    var displayName by remember(profile) { mutableStateOf(profile?.displayName ?: "Pekebun") }
    var experienceLevel by remember(profile) { mutableStateOf(profile?.experienceLevel ?: "beginner") }
    var gardeningType by remember(profile) { mutableStateOf(profile?.gardeningType ?: "polybag") }
    var preferredMethod by remember(profile) {
        mutableStateOf(CultivationMethod.fromKey(profile?.preferredMethod))
    }
    var remindersEnabled by remember(profile) { mutableStateOf(profile?.remindersEnabled ?: true) }
    var authError by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Profil & Pengaturan", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = LightGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(14.dp), color = PrimaryGreen, modifier = Modifier.size(52.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Rounded.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = profile?.displayName?.ifBlank { "Pekebun" } ?: "Pekebun",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )
                            Text(
                                text = if (isDemoMode) "Mode Demo Lokal (Belum sinkron cloud)" else (userEmail ?: "Akun Google Terhubung"),
                                style = MaterialTheme.typography.bodySmall,
                                color = DarkGreen.copy(alpha = 0.8f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PillBadge("$plantCount Tanaman", Color.White, DarkGreen)
                        PillBadge("$treatmentCount Perawatan", Color.White, DarkGreen)
                        PillBadge("$photoCount Foto", Color.White, DarkGreen)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    MethodBadge(preferredMethod)
                }
            }
        }

        if (isDemoMode) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, PrimaryGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Simpan Kebun ke Cloud?", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Masuk dengan akun Google agar data tanaman, diagnosis, dan foto tersimpan aman antar perangkat.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (authError != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(authError!!, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                authError = null
                                GoogleAuthHelper.onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = credentialManager,
                                    onAuthSuccess = onSignedInFromDemo,
                                    onAuthError = { authError = it },
                                    scope = scope
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .testTag("profile_google_signin_button")
                        ) {
                            Icon(Icons.Rounded.Login, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Masuk dengan Google")
                        }
                    }
                }
            }
        }

        item {
            Text("Fitur Lainnya", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onOpenCalendar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp)
                ) {
                    Icon(Icons.Rounded.CalendarMonth, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kalender Perawatan")
                }
                OutlinedButton(
                    onClick = onOpenTreatments,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp)
                ) {
                    Icon(Icons.Rounded.TrackChanges, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pemantauan & Hasil Perawatan")
                }
                OutlinedButton(
                    onClick = onOpenMonitoring,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp)
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galeri Pertumbuhan & Perbandingan Foto")
                }
                OutlinedButton(
                    onClick = onOpenAskAi,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp)
                ) {
                    Icon(Icons.Rounded.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tanya Plant AI (Chat)")
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
                    Text("Preferensi Berkebun", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = { Text("Nama tampilan") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Pengalaman:", style = MaterialTheme.typography.labelLarge)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("beginner" to "Pemula", "intermediate" to "Menengah", "advanced" to "Mahir").forEach { (k, label) ->
                            FilterChip(
                                selected = experienceLevel == k,
                                onClick = { experienceLevel = k },
                                label = { Text(label) },
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            )
                        }
                    }

                    Text("Lokasi utama:", style = MaterialTheme.typography.labelLarge)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("polybag" to "Polybag / Pot", "home_garden" to "Halaman", "urban_balcony" to "Balkon").forEach { (k, label) ->
                            FilterChip(
                                selected = gardeningType == k,
                                onClick = { gardeningType = k },
                                label = { Text(label) },
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            )
                        }
                    }

                    Text("Metode budidaya bawaan:", style = MaterialTheme.typography.labelLarge)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        CultivationMethod.entries.forEach { m ->
                            FilterChip(
                                selected = preferredMethod == m,
                                onClick = { preferredMethod = m },
                                label = { Text(m.labelId) },
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Pengingat perawatan", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = remindersEnabled, onCheckedChange = { remindersEnabled = it })
                    }

                    Button(
                        onClick = {
                            onSaveProfile(displayName, experienceLevel, gardeningType, preferredMethod, remindersEnabled)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("save_profile_settings_button")
                    ) {
                        Text("Simpan Preferensi")
                    }

                    OutlinedButton(
                        onClick = onReplayOnboarding,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                    ) {
                        Text("Buka Kembali Pengenalan Awal")
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = {
                    if (isDemoMode) {
                        onSignedOut()
                    } else {
                        GoogleAuthHelper.signOut(
                            credentialManager = credentialManager,
                            scope = scope,
                            onSignOutComplete = onSignedOut
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("sign_out_button")
            ) {
                Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isDemoMode) "Keluar dari Mode Demo" else "Keluar Akun Google")
            }
        }
    }
}
