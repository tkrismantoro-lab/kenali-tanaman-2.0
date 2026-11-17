package com.example

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.example.data.ai.PlantAiService
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.example.model.HealthStatus
import com.example.ui.components.createTempCameraUri
import com.example.ui.components.decodeDrawableToBitmap
import com.example.ui.components.decodeUriToBitmap
import com.example.ui.components.encodeBitmapToBase64
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.io.FileOutputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PlantPhotoAnalysisTest {

    @Test
    fun `decodeDrawableToBitmap returns software ARGB_8888 bitmap`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val bmp = decodeDrawableToBitmap(context, R.drawable.img_plant_cabai)
        assertNotNull(bmp)
        assertEquals(Bitmap.Config.ARGB_8888, bmp!!.config)
        assertTrue(bmp.width <= 1024 && bmp.height <= 1024)
        assertTrue(encodeBitmapToBase64(bmp).isNotBlank())
    }

    @Test
    fun `decodeUriToBitmap scales large JPEG into software ARGB_8888 bitmap`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val tempFile = File(context.cacheDir, "test_large_plant.jpg")
        val large = Bitmap.createBitmap(2400, 1600, Bitmap.Config.ARGB_8888)
        FileOutputStream(tempFile).use { out ->
            large.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        val decoded = decodeUriToBitmap(context, Uri.fromFile(tempFile))
        assertNotNull(decoded)
        assertEquals(Bitmap.Config.ARGB_8888, decoded!!.config)
        assertTrue(decoded.width <= 1280)
        assertTrue(decoded.height <= 1280)
    }

    @Test
    fun `createTempCameraUri creates fileprovider uri cleanly`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val uri = createTempCameraUri(context)
        assertTrue(uri.toString().contains("${context.packageName}.fileprovider"))
    }

    @Test
    fun `PlantAiService scales bitmap to ARGB_8888 and parses JSON responses`() {
        val service = PlantAiService()
        val wide = Bitmap.createBitmap(2048, 1024, Bitmap.Config.RGB_565)
        val scaled = service.scaleBitmapForUpload(wide)
        assertEquals(Bitmap.Config.ARGB_8888, scaled.config)
        assertEquals(1024, scaled.width)
        assertEquals(512, scaled.height)

        val identJson = """
            ```json
            {
              "common_name": "Cabai Rawit",
              "scientific_name": "Capsicum frutescens",
              "local_names": ["Lombok rawit"],
              "category": "Sayuran Buah",
              "confidence": "high",
              "visible_characteristics": ["Daun hijau lonjong", "Buah kecil tegak"],
              "description": "Tanaman cabai populer untuk pot maupun kebun rumah.",
              "benefits_or_uses": "Bumbu masakan",
              "toxicity_warning": "",
              "cultivation_suitability": "Cocok di polybag 35 cm",
              "possible_matches": [],
              "photo_retake_advice": ""
            }
            ```
        """.trimIndent()
        val ident = service.parseIdentificationJson(identJson)
        assertEquals("Cabai Rawit", ident.commonName)
        assertEquals("high", ident.confidence)
        assertEquals(null, ident.toxicityWarning)

        val diagJson = """
            {
              "plant_name": "Cabai Rawit",
              "scientific_name": "Capsicum frutescens",
              "condition_summary": "Daun bagian bawah menguning ringan.",
              "health_status": "needs_attention",
              "visual_observations": ["Klorosis ringan pada daun tua"],
              "possible_causes": [
                {
                  "cause": "Media tanam terlalu basah",
                  "confidence": "moderate",
                  "supporting_evidence": "Daun bawah menguning merata",
                  "missing_evidence": "Kondisi drainase bawah pot",
                  "additional_check": "Sentuh tanah sedalam 3 cm"
                }
              ],
              "confidence": "moderate",
              "risk_level": "moderate",
              "recommended_actions": ["Kurangi penyiraman sementara"],
              "follow_up_days": 3,
              "expected_improvement": "Tidak ada daun baru menguning",
              "warning_signs": "Batang menghitam",
              "limitations": ["Hanya satu sudut foto"],
              "clarifying_questions": ["Apakah pot berlubang drainase?"]
            }
        """.trimIndent()
        val diag = service.parseDiagnosisJson(diagJson, "Cabai Rawit", "Capsicum frutescens")
        assertEquals(HealthStatus.NEEDS_ATTENTION, diag.healthStatus)
        assertEquals(1, diag.possibleCauses.size)
        assertEquals(3, diag.followUpDays)
    }

    @Test
    fun `verify google services resources are present`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val apiKeyId = context.resources.getIdentifier("google_api_key", "string", context.packageName)
        assertTrue("google_api_key resource must exist", apiKeyId != 0)
        val apiKey = context.getString(apiKeyId)
        assertTrue("google_api_key must start with AIza", apiKey.startsWith("AIza"))
        val appIdId = context.resources.getIdentifier("google_app_id", "string", context.packageName)
        assertTrue("google_app_id resource must exist", appIdId != 0)
        val projectIdId = context.resources.getIdentifier("project_id", "string", context.packageName)
        assertTrue("project_id resource must exist", projectIdId != 0)

        com.example.data.firebase.configureAppCheck(context, null)
        val diag = com.example.data.firebase.getFirebaseDiagnostics(context)
        assertTrue("API key should be detected as valid", diag.isApiKeyValidFormat)
        assertTrue("App Check debug token should be generated automatically", !diag.appCheckDebugToken.isNullOrBlank())

        val customToken = "12345678-1234-1234-1234-123456789abc"
        com.example.data.firebase.updateAppCheckDebugToken(context, customToken)
        val updatedDiag = com.example.data.firebase.getFirebaseDiagnostics(context)
        assertEquals(customToken, updatedDiag.appCheckDebugToken)
    }
}
