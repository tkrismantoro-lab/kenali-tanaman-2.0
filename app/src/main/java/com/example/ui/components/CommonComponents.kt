package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.R
import com.example.model.CultivationMethod
import com.example.model.EvidenceLevel
import com.example.model.HealthStatus
import com.example.model.TreatmentStatus
import com.example.ui.theme.DangerContainer
import com.example.ui.theme.DangerRed
import com.example.ui.theme.InfoBlue
import com.example.ui.theme.InfoContainer
import com.example.ui.theme.LightGreen
import com.example.ui.theme.Mint
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningContainer
import java.io.ByteArrayOutputStream
import java.io.File

@Composable
fun PlantImage(
    imageResKey: String?,
    imageBase64: String? = null,
    bitmapOverride: Bitmap? = null,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val decodedBitmap = remember(imageBase64) {
        if (imageBase64.isNullOrBlank()) null
        else runCatching {
            val bytes = Base64.decode(imageBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }.getOrNull()
    }

    val activeBitmap = bitmapOverride ?: decodedBitmap
    val resId = remember(imageResKey) {
        when (imageResKey) {
            "img_plant_cabai" -> R.drawable.img_plant_cabai
            "img_plant_tomat" -> R.drawable.img_plant_tomat
            "img_plant_pakcoy" -> R.drawable.img_plant_pakcoy
            "img_hero_garden" -> R.drawable.img_hero_garden
            else -> null
        }
    }

    when {
        activeBitmap != null -> {
            Image(
                bitmap = activeBitmap.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        resId != null -> {
            Image(
                painter = painterResource(id = resId),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        else -> {
            Box(
                modifier = modifier.background(Mint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Spa,
                    contentDescription = contentDescription,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun PillBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun HealthStatusBadge(status: HealthStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        HealthStatus.HEALTHY -> SuccessContainer to SuccessGreen
        HealthStatus.NEEDS_ATTENTION -> WarningContainer to Color(0xFFB45309)
        HealthStatus.PROBLEM -> DangerContainer to DangerRed
        HealthStatus.FOLLOW_UP -> InfoContainer to InfoBlue
    }
    PillBadge(text = status.labelId, containerColor = bg, contentColor = fg, modifier = modifier)
}

@Composable
fun MethodBadge(method: CultivationMethod, modifier: Modifier = Modifier) {
    val (bg, fg) = when (method) {
        CultivationMethod.ORGANIC -> LightGreen to PrimaryGreen
        CultivationMethod.NON_ORGANIC -> InfoContainer to InfoBlue
        CultivationMethod.COMBINED -> Mint to Color(0xFF1B4332)
    }
    PillBadge(text = method.labelId, containerColor = bg, contentColor = fg, modifier = modifier)
}

@Composable
fun ConfidenceBadge(level: String, modifier: Modifier = Modifier) {
    val normalized = level.lowercase()
    val (label, bg, fg) = when (normalized) {
        "high", "tinggi" -> Triple("Keyakinan: Tinggi", SuccessContainer, SuccessGreen)
        "moderate", "sedang" -> Triple("Keyakinan: Sedang", WarningContainer, Color(0xFFB45309))
        else -> Triple("Keyakinan: Rendah", DangerContainer, DangerRed)
    }
    PillBadge(text = label, containerColor = bg, contentColor = fg, modifier = modifier)
}

@Composable
fun RiskLevelBadge(risk: String, modifier: Modifier = Modifier) {
    val normalized = risk.lowercase()
    val (label, bg, fg) = when (normalized) {
        "urgent", "mendesak" -> Triple("Risiko: Mendesak", DangerContainer, DangerRed)
        "high", "tinggi" -> Triple("Risiko: Tinggi", DangerContainer, DangerRed)
        "moderate", "sedang" -> Triple("Risiko: Sedang", WarningContainer, Color(0xFFB45309))
        else -> Triple("Risiko: Rendah", SuccessContainer, SuccessGreen)
    }
    PillBadge(text = label, containerColor = bg, contentColor = fg, modifier = modifier)
}

@Composable
fun TreatmentStatusBadge(status: TreatmentStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        TreatmentStatus.IMPROVED, TreatmentStatus.COMPLETED -> SuccessContainer to SuccessGreen
        TreatmentStatus.WORSENED -> DangerContainer to DangerRed
        TreatmentStatus.UNCHANGED, TreatmentStatus.AWAITING_EVAL -> WarningContainer to Color(0xFFB45309)
        TreatmentStatus.IN_PROGRESS -> InfoContainer to InfoBlue
        TreatmentStatus.NOT_STARTED -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    PillBadge(text = status.labelId, containerColor = bg, contentColor = fg, modifier = modifier)
}

@Composable
fun EvidenceLevelBadge(level: EvidenceLevel, modifier: Modifier = Modifier) {
    val (bg, fg) = when (level) {
        EvidenceLevel.A, EvidenceLevel.B -> SuccessContainer to SuccessGreen
        EvidenceLevel.C, EvidenceLevel.D -> InfoContainer to InfoBlue
        EvidenceLevel.E, EvidenceLevel.F -> WarningContainer to Color(0xFFB45309)
    }
    PillBadge(
        text = "Bukti ${level.code}: ${level.labelId}",
        containerColor = bg,
        contentColor = fg,
        modifier = modifier
    )
}

@Composable
fun DemoSimulationBanner(
    message: String = "Contoh data simulasi — tambahkan tanamanmu sendiri kapan saja",
    modifier: Modifier = Modifier
) {
    Surface(
        color = WarningContainer.copy(alpha = 0.7f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, WarningAmber.copy(alpha = 0.4f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = "Informasi data simulasi",
                tint = Color(0xFFB45309),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF78350F)
            )
        }
    }
}

@Composable
fun PhotoPickerButtons(
    onBitmapReady: (Bitmap) -> Unit,
    cameraTestTag: String = "camera_capture_button",
    galleryTestTag: String = "gallery_picker_button",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var permissionMessage by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = pendingCameraUri
        if (success && uri != null) {
            decodeUriToBitmap(context, uri)?.let(onBitmapReady)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            permissionMessage = null
            val uri = createTempCameraUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            permissionMessage = "Izin kamera ditolak. Kamu tetap bisa memilih foto dari galeri."
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            permissionMessage = null
            decodeUriToBitmap(context, uri)?.let(onBitmapReady)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val hasCam = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if (hasCam) {
                        val uri = createTempCameraUri(context)
                        pendingCameraUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag(cameraTestTag)
            ) {
                Icon(Icons.Rounded.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ambil Foto")
            }

            OutlinedButton(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag(galleryTestTag)
            ) {
                Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Pilih Galeri")
            }
        }
        if (permissionMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = permissionMessage!!,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (actionLabel != null && onActionClick != null) {
            androidx.compose.material3.TextButton(
                onClick = onActionClick,
                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
fun EmptyGardenCard(
    title: String,
    description: String,
    ctaText: String,
    onCtaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LightGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Eco,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCtaClick,
                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
            ) {
                Text(ctaText)
            }
        }
    }
}

fun createTempCameraUri(context: Context): Uri {
    val dir = File(context.cacheDir, "camera").apply { mkdirs() }
    val file = File(dir, "plant_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

fun decodeUriToBitmap(context: Context, uri: Uri): Bitmap? {
    return runCatching {
        context.contentResolver.openInputStream(uri)?.use { stream ->
            val original = BitmapFactory.decodeStream(stream) ?: return@use null
            val maxDim = 1024
            if (original.width <= maxDim && original.height <= maxDim) {
                original
            } else {
                val ratio = original.width.toFloat() / original.height.toFloat()
                val w = if (ratio >= 1f) maxDim else (maxDim * ratio).toInt().coerceAtLeast(1)
                val h = if (ratio >= 1f) (maxDim / ratio).toInt().coerceAtLeast(1) else maxDim
                Bitmap.createScaledBitmap(original, w, h, true)
            }
        }
    }.getOrNull()
}

fun encodeBitmapToBase64(bitmap: Bitmap): String {
    val maxDim = 480
    val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
    val w = if (bitmap.width > maxDim && ratio >= 1f) maxDim else if (bitmap.height > maxDim) (maxDim * ratio).toInt().coerceAtLeast(1) else bitmap.width
    val h = if (bitmap.height > maxDim && ratio < 1f) maxDim else if (bitmap.width > maxDim) (maxDim / ratio).toInt().coerceAtLeast(1) else bitmap.height
    val scaled = if (w != bitmap.width || h != bitmap.height) Bitmap.createScaledBitmap(bitmap, w, h, true) else bitmap
    val baos = ByteArrayOutputStream()
    scaled.compress(Bitmap.CompressFormat.JPEG, 72, baos)
    return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
}
