package com.example.data.ai

import android.graphics.Bitmap
import android.util.Log
import com.example.model.AiDiagnosisResult
import com.example.model.AiIdentificationResult
import com.example.model.AiPossibleCause
import com.example.model.ChatMessage
import com.example.model.CultivationMethod
import com.example.model.GrowthStage
import com.example.model.HealthStatus
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

class PlantAiService {
    companion object {
        private const val TAG = "PlantAiService"
        private const val MODEL_NAME = "gemini-3.8-flash"

        private const val PLANT_DOCTOR_SYSTEM_PROMPT = """
You are Plant Doctor AI, an agricultural assistant specializing in horticulture, plant cultivation, plant health monitoring, and preliminary plant disease diagnosis.

Your objective is to help users cultivate plants successfully using safe, practical, scientifically supported recommendations.

You must:
1. Analyze plant photographs carefully.
2. Describe visible symptoms objectively.
3. Separate visual observations from diagnostic hypotheses.
4. Consider multiple possible causes.
5. Ask additional diagnostic questions when needed.
6. Consider plant species, growth stage, cultivation method, environmental conditions, watering, and fertilization history.
7. Never claim certainty without sufficient evidence.
8. Never invent scientific references.
9. Never invent fertilizer or pesticide dosages.
10. Prioritize prevention and integrated pest management.
11. Respect the selected cultivation method.
12. Provide organic recommendations for organic users.
13. Provide conventional recommendations for non-organic users.
14. Provide balanced recommendations for combined-method users.
15. Explain the reason behind each recommended action.
16. Include relevant precautions.
17. Provide follow-up and monitoring instructions.
18. Explain when a qualified agricultural professional is needed.
19. Use Bahasa Indonesia that is simple, friendly, and understandable.
20. Do not overwhelm users with unnecessary technical information.

Your responses must be practical, structured, evidence-aware, and transparent about uncertainty.
When the available information is insufficient, explain what additional information is needed instead of guessing.
"""
    }

    private val identificationSchema by lazy {
        Schema.obj(
            properties = mapOf(
                "common_name" to Schema.string("Common Indonesian plant name"),
                "scientific_name" to Schema.string("Botanical scientific name"),
                "local_names" to Schema.array(Schema.string(), "Alternative Indonesian local names"),
                "category" to Schema.string("Plant category e.g. Sayuran Buah, Sayuran Daun, Buah, Umbi"),
                "confidence" to Schema.enumeration(listOf("low", "moderate", "high"), "Identification confidence"),
                "visible_characteristics" to Schema.array(Schema.string(), "Objective visible plant traits"),
                "description" to Schema.string("Friendly simple Indonesian description of the plant"),
                "benefits_or_uses" to Schema.string("Practical culinary, household, or agricultural benefits"),
                "toxicity_warning" to Schema.string("Toxicity note if relevant, or empty string if safe/none", nullable = true),
                "cultivation_suitability" to Schema.string("Suitability for polybag, pot, or open soil in Indonesia"),
                "possible_matches" to Schema.array(Schema.string(), "Top alternative matches when confidence is low/moderate"),
                "photo_retake_advice" to Schema.string("Advice for better photo if needed", nullable = true)
            ),
            optionalProperties = listOf("toxicity_warning", "photo_retake_advice")
        )
    }

    private val diagnosisSchema by lazy {
        Schema.obj(
            properties = mapOf(
                "plant_name" to Schema.string("Plant common Indonesian name"),
                "scientific_name" to Schema.string("Botanical scientific name"),
                "condition_summary" to Schema.string("Short friendly Indonesian summary of current visible condition"),
                "health_status" to Schema.enumeration(
                    listOf("healthy", "needs_attention", "problem", "follow_up"),
                    "Plant health status"
                ),
                "visual_observations" to Schema.array(Schema.string(), "Objective visual observations only"),
                "possible_causes" to Schema.array(
                    Schema.obj(
                        properties = mapOf(
                            "cause" to Schema.string("Possible cause name in simple Indonesian"),
                            "confidence" to Schema.enumeration(listOf("low", "moderate", "high"), "Evidence confidence"),
                            "supporting_evidence" to Schema.string("Visible or reported evidence supporting this cause"),
                            "missing_evidence" to Schema.string("What evidence is still missing"),
                            "additional_check" to Schema.string("Simple check the user can do at home")
                        )
                    ),
                    "Up to 3 possible causes"
                ),
                "confidence" to Schema.enumeration(listOf("low", "moderate", "high"), "Overall evidence quality"),
                "risk_level" to Schema.enumeration(listOf("low", "moderate", "high", "urgent"), "Plant health risk level"),
                "recommended_actions" to Schema.array(Schema.string(), "Numbered actionable steps respecting cultivation method"),
                "follow_up_days" to Schema.integer("Recommended days until follow-up check (1-14)"),
                "expected_improvement" to Schema.string("What signs of improvement to look for"),
                "warning_signs" to Schema.string("Signs requiring urgent attention or expert help"),
                "limitations" to Schema.array(Schema.string(), "Diagnostic limitations and uncertainty notes"),
                "clarifying_questions" to Schema.array(Schema.string(), "1-3 relevant follow-up questions if evidence is incomplete")
            )
        )
    }

    private val identificationModel by lazy {
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = identificationSchema
            }
        )
    }

    private val diagnosisModel by lazy {
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = diagnosisSchema
            },
            systemInstruction = content { text(PLANT_DOCTOR_SYSTEM_PROMPT) }
        )
    }

    private val generalTextModel by lazy {
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            systemInstruction = content { text(PLANT_DOCTOR_SYSTEM_PROMPT) }
        )
    }

    suspend fun identifyPlant(bitmap: Bitmap): Result<AiIdentificationResult> {
        return try {
            val scaled = scaleBitmapForUpload(bitmap)
            val promptText = """
                Analisis foto tanaman ini dengan teliti.
                Berikan identifikasi dalam Bahasa Indonesia yang ramah dan mudah dipahami pemula:
                - Nama umum Indonesia, nama ilmiah, nama lokal alternatif, dan kategori tanaman.
                - Tingkat keyakinan (low/moderate/high) berdasarkan kejelasan bagian tanaman di foto.
                - Ciri-ciri visual yang terlihat secara objektif.
                - Deskripsi singkat, manfaat/kegunaan, peringatan toksisitas jika relevan, dan kecocokan budidaya (polybag/pot/lahan).
                - Jika foto kurang jelas atau keyakinan tidak tinggi, sertakan kemungkinan kecocokan lain dan saran pengambilan foto ulang (misalnya foto daun, bunga, atau buah).
            """.trimIndent()

            val response = withTimeout(60_000L) {
                identificationModel.generateContent(
                    content {
                        image(scaled)
                        text(promptText)
                    }
                )
            }
            val rawJson = response.text ?: throw IllegalStateException("AI mengembalikan respons kosong.")
            val json = JSONObject(rawJson)

            val result = AiIdentificationResult(
                commonName = json.optString("common_name", "Tanaman Belum Dikenali"),
                scientificName = json.optString("scientific_name", "-"),
                localNames = json.optJSONArray("local_names").toStringList(),
                category = json.optString("category", "Umum"),
                confidence = json.optString("confidence", "low"),
                visibleCharacteristics = json.optJSONArray("visible_characteristics").toStringList(),
                description = json.optString("description", "Belum cukup ciri visual untuk deskripsi rinci."),
                benefitsOrUses = json.optString("benefits_or_uses", "-"),
                toxicityWarning = json.optString("toxicity_warning").takeIf { it.isNotBlank() && it != "null" },
                cultivationSuitability = json.optString("cultivation_suitability", "Perlu pemeriksaan lokasi tanam."),
                possibleMatches = json.optJSONArray("possible_matches").toStringList(),
                photoRetakeAdvice = json.optString("photo_retake_advice").takeIf { it.isNotBlank() && it != "null" }
            )
            Result.success(result)
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Plant identification timed out", e)
            Result.failure(Exception("Analisis foto melebihi batas waktu 60 detik. Periksa koneksi lalu coba lagi."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Plant identification failed", e)
            Result.failure(Exception(friendlyAiError(e)))
        }
    }

    suspend fun diagnosePlant(
        bitmap: Bitmap?,
        plantName: String,
        scientificName: String,
        cultivationMethod: CultivationMethod,
        growthStage: GrowthStage,
        photoType: String,
        symptomNotes: String,
        answers: Map<String, String>
    ): Result<AiDiagnosisResult> {
        return try {
            val formattedAnswers = if (answers.isEmpty()) {
                "Belum ada jawaban tambahan."
            } else {
                answers.entries.joinToString("\n") { "- ${it.key}: ${it.value}" }
            }

            val promptText = """
                Lakukan diagnosis awal Dokter Tanaman AI untuk tanaman berikut:
                - Nama tanaman: $plantName ($scientificName)
                - Metode budidaya pengguna: ${cultivationMethod.labelId} (${cultivationMethod.shortDesc})
                - Fase pertumbuhan: ${growthStage.labelId}
                - Bagian yang difoto: $photoType
                - Catatan gejala pengguna: ${symptomNotes.ifBlank { "Tidak ada catatan tambahan" }}
                - Informasi konteks tambahan:
                $formattedAnswers

                Aturan wajib:
                1. Pisahkan pengamatan visual objektif dari dugaan penyebab.
                2. Berikan maksimal 3 kemungkinan penyebab disertai bukti pendukung, bukti yang masih kurang, dan pemeriksaan tambahan di rumah.
                3. Hormati metode budidaya (${cultivationMethod.labelId}): jangan sarankan pestisida kimia untuk pengguna Organik; jangan sarankan dosis kimia/pupuk buatan sendiri tanpa label resmi.
                4. Jangan menganggap setiap daun kuning adalah kekurangan nitrogen, dan jangan menganggap setiap tanaman layu hanya kurang air.
                5. Jika belum cukup bukti atau tidak ada foto, nyatakan ketidakpastian secara transparan.
            """.trimIndent()

            val response = withTimeout(60_000L) {
                diagnosisModel.generateContent(
                    content {
                        if (bitmap != null) {
                            image(scaleBitmapForUpload(bitmap))
                        }
                        text(promptText)
                    }
                )
            }
            val rawJson = response.text ?: throw IllegalStateException("AI mengembalikan respons kosong.")
            val json = JSONObject(rawJson)

            val causesArr = json.optJSONArray("possible_causes")
            val causes = buildList {
                if (causesArr != null) {
                    for (i in 0 until causesArr.length().coerceAtMost(3)) {
                        val obj = causesArr.optJSONObject(i) ?: continue
                        add(
                            AiPossibleCause(
                                cause = obj.optString("cause", "Perlu pemeriksaan lanjut"),
                                confidence = obj.optString("confidence", "low"),
                                supportingEvidence = obj.optString("supporting_evidence", "-"),
                                missingEvidence = obj.optString("missing_evidence", "-"),
                                additionalCheck = obj.optString("additional_check", "-")
                            )
                        )
                    }
                }
            }

            val result = AiDiagnosisResult(
                plantName = json.optString("plant_name", plantName),
                scientificName = json.optString("scientific_name", scientificName),
                conditionSummary = json.optString(
                    "condition_summary",
                    "Belum cukup bukti untuk menentukan penyebab secara pasti. Silakan ambil foto tambahan."
                ),
                healthStatus = HealthStatus.fromKey(json.optString("health_status", "follow_up")),
                visualObservations = json.optJSONArray("visual_observations").toStringList(),
                possibleCauses = causes,
                confidence = json.optString("confidence", "low"),
                riskLevel = json.optString("risk_level", "moderate"),
                recommendedActions = json.optJSONArray("recommended_actions").toStringList(),
                followUpDays = json.optInt("follow_up_days", 3).coerceIn(1, 30),
                expectedImprovement = json.optString("expected_improvement", "Tunas baru terlihat segar dan gejala tidak meluas."),
                warningSigns = json.optString("warning_signs", "Jika daun rontok parah atau batang membusuk, segera konsultasikan ke penyuluh pertanian."),
                limitations = json.optJSONArray("limitations").toStringList(),
                clarifyingQuestions = json.optJSONArray("clarifying_questions").toStringList()
            )
            Result.success(result)
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Plant diagnosis timed out", e)
            Result.failure(Exception("Diagnosis melebihi batas waktu 60 detik. Silakan coba lagi."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Plant diagnosis failed", e)
            Result.failure(Exception(friendlyAiError(e)))
        }
    }

    suspend fun compareGrowthPhotos(
        beforeBitmap: Bitmap?,
        afterBitmap: Bitmap?,
        plantName: String,
        beforeLabel: String,
        afterLabel: String
    ): Result<String> {
        return try {
            val promptText = """
                Bandingkan dua catatan pertumbuhan tanaman $plantName ($beforeLabel vs $afterLabel).
                Jelaskan perubahan visual yang tampak secara objektif dalam Bahasa Indonesia sederhana (misalnya perubahan jumlah daun, kerapatan tajuk, warna daun, munculnya bunga/buah).
                Jangan mengarang ukuran sentimeter yang tidak terlihat. Sertakan saran pemantauan selanjutnya.
            """.trimIndent()
            val response = withTimeout(60_000L) {
                generalTextModel.generateContent(
                    content {
                        if (beforeBitmap != null) image(scaleBitmapForUpload(beforeBitmap))
                        if (afterBitmap != null) image(scaleBitmapForUpload(afterBitmap))
                        text(promptText)
                    }
                )
            }
            val text = response.text
            if (text.isNullOrBlank()) {
                Result.failure(Exception("AI mengembalikan respons kosong."))
            } else {
                Result.success(text)
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Photo comparison timed out", e)
            Result.failure(Exception("Perbandingan foto melebihi batas waktu 60 detik."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Photo comparison failed", e)
            Result.failure(Exception(friendlyAiError(e)))
        }
    }

    fun askPlantAiStream(
        question: String,
        plantContextSummary: String,
        history: List<ChatMessage>
    ): Flow<String> = flow {
        val recentHistory = history.takeLast(6).joinToString("\n") {
            val role = if (it.isUser) "Pengguna" else "Ask Plant AI"
            "$role: ${it.text}"
        }
        val prompt = """
            Konteks tanaman pengguna saat ini:
            $plantContextSummary

            Riwayat percakapan singkat:
            $recentHistory

            Pertanyaan pengguna:
            $question

            Berikan jawaban praktis dalam Bahasa Indonesia sederhana, jelaskan alasan setiap saran, sesuaikan dengan metode budidaya tanaman, dan ingatkan bahwa jawaban chat bersifat panduan umum (bukan kepastian diagnosis penyakit).
        """.trimIndent()

        val accumulated = StringBuilder()
        try {
            withTimeout(60_000L) {
                generalTextModel.generateContentStream(prompt).collect { chunk ->
                    val chunkText = chunk.text.orEmpty()
                    if (chunkText.isNotEmpty()) {
                        accumulated.append(chunkText)
                        emit(accumulated.toString())
                    }
                }
            }
            if (accumulated.isBlank()) {
                throw IllegalStateException("AI tidak mengembalikan jawaban.")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Chat stream timed out", e)
            throw Exception("Permintaan chat melebihi batas waktu 60 detik.")
        } catch (e: Exception) {
            Log.e(TAG, "Chat stream failed", e)
            throw Exception(friendlyAiError(e))
        }
    }

    private fun scaleBitmapForUpload(bitmap: Bitmap): Bitmap {
        val maxDim = 1024
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxDim && height <= maxDim) return bitmap
        val ratio = width.toFloat() / height.toFloat()
        val targetW: Int
        val targetH: Int
        if (ratio >= 1f) {
            targetW = maxDim
            targetH = (maxDim / ratio).toInt().coerceAtLeast(1)
        } else {
            targetH = maxDim
            targetW = (maxDim * ratio).toInt().coerceAtLeast(1)
        }
        return Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
    }

    private fun org.json.JSONArray?.toStringList(): List<String> {
        if (this == null) return emptyList()
        return (0 until length()).mapNotNull { idx ->
            optString(idx).takeIf { it.isNotBlank() }
        }
    }

    private fun friendlyAiError(e: Exception): String {
        val msg = e.localizedMessage.orEmpty()
        return when {
            msg.contains("403") || msg.contains("AppCheck", ignoreCase = true) ->
                "Akses AI ditolak verifikasi App Check ($msg). Pastikan layanan Firebase AI Logic aktif."
            msg.contains("429") || msg.contains("quota", ignoreCase = true) ->
                "Batas permintaan AI sementara tercapai. Silakan coba beberapa saat lagi."
            msg.contains("Unable to resolve host", ignoreCase = true) || msg.contains("network", ignoreCase = true) ->
                "Tidak dapat terhubung ke server AI. Periksa koneksi internet Anda."
            else -> msg.ifBlank { "Gagal memproses permintaan AI." }
        }
    }
}
