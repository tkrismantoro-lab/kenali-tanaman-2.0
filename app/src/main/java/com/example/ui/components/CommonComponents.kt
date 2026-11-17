package com.example.ui.components

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

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
    val scope = rememberCoroutineScope()
    var pendingCameraUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var pickerMessage by remember { mutableStateOf<String?>(null) }
    var isDecoding by remember { mutableStateOf(false) }

    fun handlePickedUri(uri: Uri, sourceLabel: String) {
        scope.launch {
            isDecoding = true
            pickerMessage = null
            val decoded = withContext(Dispatchers.IO) {
                decodeUriToBitmap(context, uri)
            }
            isDecoding = false
            if (decoded != null) {
                onBitmapReady(decoded)
            } else {
                pickerMessage = "Gagal membaca foto dari $sourceLabel. Coba pilih gambar JPG/PNG lain."
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val uriStr = pendingCameraUriString
        pendingCameraUriString = null
        if (success && !uriStr.isNullOrBlank()) {
            handlePickedUri(Uri.parse(uriStr), "kamera")
        } else if (success) {
            pickerMessage = "Lokasi file kamera tidak ditemukan. Silakan ulangi atau pilih dari galeri."
        }
    }

    fun launchCameraSafely() {
        try {
            val uri = createTempCameraUri(context)
            pendingCameraUriString = uri.toString()
            cameraLauncher.launch(uri)
        } catch (e: ActivityNotFoundException) {
            Log.w("PhotoPicker", "No camera app available", e)
            pickerMessage = "Aplikasi kamera tidak tersedia di perangkat ini. Gunakan Pilih Galeri atau Foto Contoh."
        } catch (e: Exception) {
            Log.e("PhotoPicker", "Failed to launch camera", e)
            pickerMessage = "Tidak dapat membuka kamera (${e.localizedMessage ?: "error"})."
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            pickerMessage = null
            launchCameraSafely()
        } else {
            pickerMessage = "Izin kamera ditolak. Kamu tetap bisa memilih foto dari galeri."
        }
    }

    val fallbackGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            handlePickedUri(uri, "galeri")
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            handlePickedUri(uri, "galeri")
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
                        launchCameraSafely()
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                enabled = !isDecoding,
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
                    pickerMessage = null
                    try {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } catch (e: ActivityNotFoundException) {
                        Log.w("PhotoPicker", "PickVisualMedia unavailable, falling back to GetContent", e)
                        try {
                            fallbackGalleryLauncher.launch("image/*")
                        } catch (inner: Exception) {
                            pickerMessage = "Galeri tidak tersedia di perangkat ini. Gunakan Foto Contoh di bawah."
                        }
                    } catch (e: Exception) {
                        Log.e("PhotoPicker", "Failed to open gallery", e)
                        pickerMessage = "Gagal membuka galeri (${e.localizedMessage ?: "error"})."
                    }
                },
                enabled = !isDecoding,
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
        if (isDecoding) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        if (pickerMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pickerMessage!!,
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
    val maxDim = 1280
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val imageDecoderBitmap = runCatching {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                val w = info.size.width
                val h = info.size.height
                if (w > maxDim || h > maxDim) {
                    val ratio = w.toFloat() / h.coerceAtLeast(1).toFloat()
                    val targetW = if (ratio >= 1f) maxDim else (maxDim * ratio).roundToInt().coerceAtLeast(1)
                    val targetH = if (ratio >= 1f) (maxDim / ratio).roundToInt().coerceAtLeast(1) else maxDim
                    decoder.setTargetSize(targetW, targetH)
                }
            }
        }.onFailure { t ->
            Log.w("PhotoPicker", "ImageDecoder failed for $uri, falling back to BitmapFactory", t)
        }.getOrNull()

        if (imageDecoderBitmap != null) {
            return runCatching { ensureSoftwareArgb8888(scaleToMaxDim(imageDecoderBitmap, maxDim)) }.getOrNull()
        }
    }

    return try {
        val boundsOpts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, boundsOpts)
        }
        var sampleSize = 1
        val outW = boundsOpts.outWidth
        val outH = boundsOpts.outHeight
        if (outW > maxDim || outH > maxDim) {
            while ((outW / sampleSize) > maxDim * 2 || (outH / sampleSize) > maxDim * 2) {
                sampleSize *= 2
            }
        }
        val decodeOpts = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val decoded = context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOpts)
        } ?: return null
        ensureSoftwareArgb8888(scaleToMaxDim(decoded, maxDim))
    } catch (t: Throwable) {
        Log.e("PhotoPicker", "Failed to decode URI bitmap via BitmapFactory: $uri", t)
        null
    }
}

fun decodeDrawableToBitmap(context: Context, resId: Int, maxDim: Int = 1024): Bitmap? {
    return try {
        val opts = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val decoded = BitmapFactory.decodeResource(context.resources, resId, opts) ?: return null
        ensureSoftwareArgb8888(scaleToMaxDim(decoded, maxDim))
    } catch (t: Throwable) {
        Log.e("PhotoPicker", "Failed to decode drawable resource: $resId", t)
        null
    }
}

private fun scaleToMaxDim(bitmap: Bitmap, maxDim: Int): Bitmap {
    if (bitmap.width <= maxDim && bitmap.height <= maxDim) return bitmap
    val ratio = bitmap.width.toFloat() / bitmap.height.coerceAtLeast(1).toFloat()
    val w = if (ratio >= 1f) maxDim else (maxDim * ratio).roundToInt().coerceAtLeast(1)
    val h = if (ratio >= 1f) (maxDim / ratio).roundToInt().coerceAtLeast(1) else maxDim
    return Bitmap.createScaledBitmap(bitmap, w, h, true)
}

private fun ensureSoftwareArgb8888(bitmap: Bitmap): Bitmap {
    return if (bitmap.config == Bitmap.Config.HARDWARE || bitmap.config != Bitmap.Config.ARGB_8888) {
        bitmap.copy(Bitmap.Config.ARGB_8888, false) ?: bitmap
    } else {
        bitmap
    }
}

fun encodeBitmapToBase64(bitmap: Bitmap): String {
    val safeBitmap = ensureSoftwareArgb8888(bitmap)
    val maxDim = 480
    val scaled = scaleToMaxDim(safeBitmap, maxDim)
    val baos = ByteArrayOutputStream()
    scaled.compress(Bitmap.CompressFormat.JPEG, 72, baos)
    return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
}

@Composable
fun FirebaseAiDiagnosticDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
    var info by remember { mutableStateOf(com.example.data.firebase.getFirebaseDiagnostics(context)) }
    var customTokenInput by remember { mutableStateOf(info.appCheckDebugToken.orEmpty()) }
    var statusNotice by remember { mutableStateOf<String?>(null) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Diagnostik Firebase & AI") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Project ID: ${info.projectId}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                Text("Package: ${info.packageName}", style = MaterialTheme.typography.bodySmall)
                Text(
                    "API Key: ${info.apiKeyMasked} (${if (info.isApiKeyValidFormat) "Terbaca" else "Bermasalah"})",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (info.isApiKeyValidFormat) PrimaryGreen else MaterialTheme.colorScheme.error
                )
                if (info.isDebugBuild) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Jika menggunakan APK Debug di HP fisik (di luar emulator AI Studio), daftarkan Debug Token berikut di Firebase Console → App Check → Apps → Manage debug tokens:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = customTokenInput,
                        onValueChange = { customTokenInput = it },
                        label = { Text("App Check Debug Token") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                clipboard.setText(androidx.compose.ui.text.AnnotatedString(customTokenInput))
                                statusNotice = "Token disalin ke clipboard."
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Salin Token")
                        }
                        Button(
                            onClick = {
                                if (com.example.data.firebase.updateAppCheckDebugToken(context, customTokenInput)) {
                                    info = com.example.data.firebase.getFirebaseDiagnostics(context)
                                    statusNotice = "Token berhasil disimpan."
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Simpan Token")
                        }
                    }
                }
                if (statusNotice != null) {
                    Text(statusNotice!!, style = MaterialTheme.typography.bodySmall, color = PrimaryGreen)
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}
