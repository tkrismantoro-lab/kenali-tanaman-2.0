package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class CultivationMethod(val key: String, val labelId: String, val shortDesc: String) {
    ORGANIC(
        key = "organic",
        labelId = "Organik",
        shortDesc = "Budidaya yang mengutamakan bahan organik, kesehatan tanah, dan pengendalian hama secara alami atau biologis."
    ),
    NON_ORGANIC(
        key = "nonorganic",
        labelId = "Nonorganik",
        shortDesc = "Budidaya menggunakan pupuk mineral dan sarana pengendalian yang sesuai kebutuhan tanaman serta ketentuan penggunaan."
    ),
    COMBINED(
        key = "combined",
        labelId = "Kombinasi / Semi-Organik",
        shortDesc = "Pendekatan kombinasi menggunakan bahan organik dan pupuk mineral secara terukur sesuai kebutuhan tanaman."
    );

    companion object {
        fun fromKey(key: String?): CultivationMethod =
            entries.firstOrNull { it.key == key } ?: ORGANIC
    }
}

enum class GrowthStage(val key: String, val labelId: String) {
    SEEDLING("seedling", "Pembibitan / Semai"),
    VEGETATIVE("vegetative", "Vegetatif"),
    FLOWERING("flowering", "Berbunga"),
    FRUITING("fruiting", "Berbuah"),
    HARVEST("harvest", "Siap Panen");

    companion object {
        fun fromKey(key: String?): GrowthStage =
            entries.firstOrNull { it.key == key } ?: VEGETATIVE
    }
}

enum class HealthStatus(val key: String, val labelId: String) {
    HEALTHY("healthy", "Sehat"),
    NEEDS_ATTENTION("needs_attention", "Perlu perhatian"),
    PROBLEM("problem", "Bermasalah"),
    FOLLOW_UP("follow_up", "Perlu pemeriksaan lanjutan");

    companion object {
        fun fromKey(key: String?): HealthStatus =
            entries.firstOrNull { it.key == key } ?: HEALTHY
    }
}

enum class TreatmentStatus(val key: String, val labelId: String) {
    NOT_STARTED("not_started", "Belum dilakukan"),
    IN_PROGRESS("in_progress", "Sedang dilakukan"),
    AWAITING_EVAL("awaiting_eval", "Menunggu evaluasi"),
    IMPROVED("improved", "Membaik"),
    UNCHANGED("unchanged", "Tidak berubah"),
    WORSENED("worsened", "Memburuk"),
    COMPLETED("completed", "Selesai");

    companion object {
        fun fromKey(key: String?): TreatmentStatus =
            entries.firstOrNull { it.key == key } ?: NOT_STARTED
    }
}

enum class CareTaskType(val key: String, val labelId: String) {
    WATER("water", "Penyiraman"),
    FERTILIZE("fertilize", "Pemupukan"),
    PRUNE("prune", "Pemangkasan"),
    INSPECT("inspect", "Pemeriksaan Hama"),
    FOLLOWUP("followup", "Evaluasi Diagnosis"),
    HARVEST("harvest", "Panen");

    companion object {
        fun fromKey(key: String?): CareTaskType =
            entries.firstOrNull { it.key == key } ?: INSPECT
    }
}

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "Pekebun",
    val experienceLevel: String = "beginner",
    val gardeningType: String = "home_garden",
    val preferredMethod: String = CultivationMethod.ORGANIC.key,
    val remindersEnabled: Boolean = true,
    val onboardingCompleted: Boolean = false,
    val demoSeeded: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "displayName" to displayName.ifBlank { "Pekebun" }.take(80),
        "experienceLevel" to experienceLevel,
        "gardeningType" to gardeningType,
        "preferredMethod" to preferredMethod,
        "remindersEnabled" to remindersEnabled,
        "onboardingCompleted" to onboardingCompleted,
        "demoSeeded" to demoSeeded,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@IgnoreExtraProperties
data class UserPlant(
    @DocumentId val id: String = "",
    val userId: String = "",
    val plantKnowledgeId: String = "cabai",
    val plantName: String = "",
    val nickname: String = "",
    val scientificName: String = "",
    val category: String = "",
    val cultivationMethod: String = CultivationMethod.ORGANIC.key,
    val plantingLocation: String = "Polybag",
    val plantingDate: String = "2026-11-10",
    val growthStage: String = GrowthStage.VEGETATIVE.key,
    val healthStatus: String = HealthStatus.HEALTHY.key,
    val isDemoData: Boolean = false,
    val completedStages: List<String> = emptyList(),
    val imageResKey: String? = null,
    val imageBase64: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(uid: String): Map<String, Any> = buildMap {
        put("userId", uid)
        put("plantKnowledgeId", plantKnowledgeId.ifBlank { "cabai" })
        put("plantName", plantName.ifBlank { "Tanaman" }.take(100))
        put("nickname", nickname.take(100))
        put("scientificName", scientificName.take(120))
        put("category", category.take(60))
        put("cultivationMethod", cultivationMethod)
        put("plantingLocation", plantingLocation.take(80))
        put("plantingDate", plantingDate.take(30))
        put("growthStage", growthStage)
        put("healthStatus", healthStatus)
        put("isDemoData", isDemoData)
        put("completedStages", completedStages.take(12))
        imageResKey?.takeIf { it.isNotBlank() }?.let { put("imageResKey", it.take(100)) }
        imageBase64?.takeIf { it.isNotBlank() }?.let { put("imageBase64", it.take(195000)) }
        put("createdAt", FieldValue.serverTimestamp())
        put("updatedAt", FieldValue.serverTimestamp())
    }
}

@IgnoreExtraProperties
data class PlantDiagnosis(
    @DocumentId val id: String = "",
    val userId: String = "",
    val plantId: String = "",
    val plantName: String = "",
    val photoType: String = "Daun",
    val symptomNotes: String = "",
    val cultivationMethod: String = CultivationMethod.ORGANIC.key,
    val conditionSummary: String = "",
    val healthStatus: String = HealthStatus.NEEDS_ATTENTION.key,
    val observations: List<String> = emptyList(),
    val possibleCausesSummary: List<String> = emptyList(),
    val confidence: String = "moderate",
    val riskLevel: String = "moderate",
    val recommendedActions: List<String> = emptyList(),
    val followUpDays: Long = 3L,
    val limitations: List<String> = emptyList(),
    val isDemoData: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "plantId" to plantId,
        "plantName" to plantName.ifBlank { "Tanaman" }.take(100),
        "photoType" to photoType.take(40),
        "symptomNotes" to symptomNotes.take(1000),
        "cultivationMethod" to cultivationMethod,
        "conditionSummary" to conditionSummary.ifBlank { "Pemeriksaan awal" }.take(500),
        "healthStatus" to healthStatus,
        "observations" to observations.take(15).map { it.take(300) },
        "possibleCausesSummary" to possibleCausesSummary.take(10).map { it.take(500) },
        "confidence" to confidence,
        "riskLevel" to riskLevel,
        "recommendedActions" to recommendedActions.take(15).map { it.take(400) },
        "followUpDays" to followUpDays.coerceIn(1L, 90L),
        "limitations" to limitations.take(10).map { it.take(300) },
        "isDemoData" to isDemoData,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@IgnoreExtraProperties
data class TreatmentPlan(
    @DocumentId val id: String = "",
    val userId: String = "",
    val plantId: String = "",
    val plantName: String = "",
    val diagnosisId: String = "",
    val problem: String = "",
    val suspectedCause: String = "",
    val recommendedTreatment: String = "",
    val treatmentDate: String = "",
    val followUpDate: String = "",
    val expectedResponse: String = "",
    val status: String = TreatmentStatus.NOT_STARTED.key,
    val outcomeNotes: String = "",
    val isDemoData: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "plantId" to plantId,
        "plantName" to plantName.ifBlank { "Tanaman" }.take(100),
        "diagnosisId" to diagnosisId.take(128),
        "problem" to problem.ifBlank { "Perawatan tanaman" }.take(300),
        "suspectedCause" to suspectedCause.take(300),
        "recommendedTreatment" to recommendedTreatment.ifBlank { "Lakukan pemantauan rutin." }.take(1500),
        "treatmentDate" to treatmentDate.take(30),
        "followUpDate" to followUpDate.take(30),
        "expectedResponse" to expectedResponse.take(500),
        "status" to status,
        "outcomeNotes" to outcomeNotes.take(500),
        "isDemoData" to isDemoData,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@IgnoreExtraProperties
data class CareTask(
    @DocumentId val id: String = "",
    val userId: String = "",
    val plantId: String = "",
    val plantName: String = "",
    val taskType: String = CareTaskType.INSPECT.key,
    val title: String = "",
    val reason: String = "",
    val scheduledDate: String = "Hari ini",
    val frequencyDays: Long = 3L,
    val completed: Boolean = false,
    val isDemoData: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "plantId" to plantId,
        "plantName" to plantName.ifBlank { "Tanaman" }.take(100),
        "taskType" to taskType,
        "title" to title.ifBlank { "Periksa kondisi tanaman" }.take(160),
        "reason" to reason.take(400),
        "scheduledDate" to scheduledDate.take(30),
        "frequencyDays" to frequencyDays.coerceIn(1L, 180L),
        "completed" to completed,
        "isDemoData" to isDemoData,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@IgnoreExtraProperties
data class GrowthPhoto(
    @DocumentId val id: String = "",
    val userId: String = "",
    val plantId: String = "",
    val plantName: String = "",
    val dateLabel: String = "",
    val growthStage: String = GrowthStage.VEGETATIVE.key,
    val notes: String = "",
    val photoResKey: String? = null,
    val photoBase64: String? = null,
    val aiSummary: String? = null,
    val isDemoData: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(uid: String): Map<String, Any> = buildMap {
        put("userId", uid)
        put("plantId", plantId)
        put("plantName", plantName.ifBlank { "Tanaman" }.take(100))
        put("dateLabel", dateLabel.ifBlank { "Hari ini" }.take(40))
        put("growthStage", growthStage)
        put("notes", notes.take(500))
        photoResKey?.takeIf { it.isNotBlank() }?.let { put("photoResKey", it.take(100)) }
        photoBase64?.takeIf { it.isNotBlank() }?.let { put("photoBase64", it.take(195000)) }
        aiSummary?.takeIf { it.isNotBlank() }?.let { put("aiSummary", it.take(1000)) }
        put("isDemoData", isDemoData)
        put("createdAt", FieldValue.serverTimestamp())
        put("updatedAt", FieldValue.serverTimestamp())
    }
}
