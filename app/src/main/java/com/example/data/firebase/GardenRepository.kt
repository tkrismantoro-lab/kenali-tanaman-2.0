package com.example.data.firebase

import android.content.Context
import com.example.R
import com.example.data.knowledge.DemoSampleData
import com.example.model.CareTask
import com.example.model.GrowthPhoto
import com.example.model.PlantDiagnosis
import com.example.model.TreatmentPlan
import com.example.model.UserPlant
import com.example.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface GardenRepository {
    fun observeProfile(userId: String): Flow<UserProfile?>
    fun observePlants(userId: String): Flow<List<UserPlant>>
    fun observeDiagnoses(userId: String): Flow<List<PlantDiagnosis>>
    fun observeTreatmentPlans(userId: String): Flow<List<TreatmentPlan>>
    fun observeCareTasks(userId: String): Flow<List<CareTask>>
    fun observeGrowthPhotos(userId: String): Flow<List<GrowthPhoto>>

    suspend fun saveProfile(userId: String, profile: UserProfile): Result<Unit>
    suspend fun ensureDemoSeeded(userId: String, displayName: String): Result<Unit>

    suspend fun addPlant(userId: String, plant: UserPlant): Result<String>
    suspend fun updatePlant(userId: String, plant: UserPlant): Result<Unit>
    suspend fun updatePlantMethod(userId: String, plantId: String, methodKey: String): Result<Unit>
    suspend fun updatePlantCompletedStages(userId: String, plantId: String, completedStages: List<String>): Result<Unit>
    suspend fun deletePlant(userId: String, plantId: String): Result<Unit>

    suspend fun addDiagnosis(userId: String, diagnosis: PlantDiagnosis): Result<String>
    suspend fun addTreatmentPlan(userId: String, plan: TreatmentPlan): Result<String>
    suspend fun updateTreatmentStatus(userId: String, planId: String, statusKey: String, outcomeNotes: String): Result<Unit>

    suspend fun addCareTask(userId: String, task: CareTask): Result<String>
    suspend fun updateCareTaskCompletion(userId: String, taskId: String, completed: Boolean): Result<Unit>
    suspend fun rescheduleCareTask(userId: String, taskId: String, newDate: String, frequencyDays: Long): Result<Unit>
    suspend fun deleteCareTask(userId: String, taskId: String): Result<Unit>

    suspend fun addGrowthPhoto(userId: String, photo: GrowthPhoto): Result<String>
    suspend fun updateGrowthPhotoSummary(userId: String, photoId: String, aiSummary: String): Result<Unit>
}

class FirestoreGardenRepository(private val db: FirebaseFirestore) : GardenRepository {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(expectedUserId: String): String {
        val uid = auth.currentUser?.uid
            ?: throw IllegalStateException("Pengguna harus masuk dengan Google terlebih dahulu.")
        require(uid == expectedUserId) { "User ID mismatch." }
        return uid
    }

    private fun <T> Flow<T>.handleObserveErrors(op: OperationType, path: String, userId: String): Flow<T> =
        catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            val exceptionToLog = firestoreError ?: (error as? Exception)
            if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                handleFirestoreError(exceptionToLog, op, path)
            }
            throw error
        }

    override fun observeProfile(userId: String): Flow<UserProfile?> {
        val docRef = db.collection("users").document(userId)
        return docRef.snapshots()
            .map { snapshot ->
                if (snapshot.exists()) {
                    snapshot.toObject(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                } else {
                    null
                }
            }
            .handleObserveErrors(OperationType.GET, docRef.path, userId)
    }

    override fun observePlants(userId: String): Flow<List<UserPlant>> {
        val colRef = db.collection("users").document(userId).collection("plants")
        return colRef.snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull {
                    it.toObject(UserPlant::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                }.sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
            }
            .handleObserveErrors(OperationType.LIST, colRef.path, userId)
    }

    override fun observeDiagnoses(userId: String): Flow<List<PlantDiagnosis>> {
        val colRef = db.collection("users").document(userId).collection("diagnoses")
        return colRef.snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull {
                    it.toObject(PlantDiagnosis::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                }.sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
            }
            .handleObserveErrors(OperationType.LIST, colRef.path, userId)
    }

    override fun observeTreatmentPlans(userId: String): Flow<List<TreatmentPlan>> {
        val colRef = db.collection("users").document(userId).collection("treatment_plans")
        return colRef.snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull {
                    it.toObject(TreatmentPlan::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                }.sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
            }
            .handleObserveErrors(OperationType.LIST, colRef.path, userId)
    }

    override fun observeCareTasks(userId: String): Flow<List<CareTask>> {
        val colRef = db.collection("users").document(userId).collection("care_tasks")
        return colRef.snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull {
                    it.toObject(CareTask::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                }.sortedBy { it.completed }
            }
            .handleObserveErrors(OperationType.LIST, colRef.path, userId)
    }

    override fun observeGrowthPhotos(userId: String): Flow<List<GrowthPhoto>> {
        val colRef = db.collection("users").document(userId).collection("growth_photos")
        return colRef.snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull {
                    it.toObject(GrowthPhoto::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                }.sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
            }
            .handleObserveErrors(OperationType.LIST, colRef.path, userId)
    }

    override suspend fun saveProfile(userId: String, profile: UserProfile): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid)
        try {
            val snap = docRef.get().await()
            if (snap.exists()) {
                docRef.update(
                    mapOf(
                        "displayName" to profile.displayName.ifBlank { "Pekebun" }.take(80),
                        "experienceLevel" to profile.experienceLevel,
                        "gardeningType" to profile.gardeningType,
                        "preferredMethod" to profile.preferredMethod,
                        "remindersEnabled" to profile.remindersEnabled,
                        "onboardingCompleted" to profile.onboardingCompleted,
                        "demoSeeded" to profile.demoSeeded,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                docRef.set(profile.toCreateMap(uid)).await()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    override suspend fun ensureDemoSeeded(userId: String, displayName: String): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        val userRef = db.collection("users").document(uid)
        try {
            val userSnap = userRef.get().await()
            val alreadySeeded = userSnap.exists() && (userSnap.getBoolean("demoSeeded") == true)
            if (alreadySeeded) return@runCatching

            val existingPlants = userRef.collection("plants").limit(1).get().await()
            if (existingPlants.isEmpty) {
                val batch = db.batch()
                DemoSampleData.samplePlants(uid).forEach { plant ->
                    val ref = userRef.collection("plants").document(plant.id)
                    batch.set(ref, plant.toCreateMap(uid))
                }
                DemoSampleData.sampleDiagnoses(uid).forEach { diag ->
                    val ref = userRef.collection("diagnoses").document(diag.id)
                    batch.set(ref, diag.toCreateMap(uid))
                }
                DemoSampleData.sampleTreatments(uid).forEach { plan ->
                    val ref = userRef.collection("treatment_plans").document(plan.id)
                    batch.set(ref, plan.toCreateMap(uid))
                }
                DemoSampleData.sampleCareTasks(uid).forEach { task ->
                    val ref = userRef.collection("care_tasks").document(task.id)
                    batch.set(ref, task.toCreateMap(uid))
                }
                DemoSampleData.sampleGrowthPhotos(uid).forEach { photo ->
                    val ref = userRef.collection("growth_photos").document(photo.id)
                    batch.set(ref, photo.toCreateMap(uid))
                }
                batch.commit().await()
            }

            if (!userSnap.exists()) {
                val profile = UserProfile(
                    userId = uid,
                    displayName = displayName.ifBlank { "Pekebun" },
                    demoSeeded = true,
                    onboardingCompleted = false
                )
                userRef.set(profile.toCreateMap(uid)).await()
            } else {
                userRef.update(
                    mapOf(
                        "demoSeeded" to true,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, userRef.path)
            throw e
        }
    }

    override suspend fun addPlant(userId: String, plant: UserPlant): Result<String> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("plants").document()
        try {
            docRef.set(plant.toCreateMap(uid)).await()
            docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    override suspend fun updatePlant(userId: String, plant: UserPlant): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        require(plant.id.isNotBlank()) { "Plant ID cannot be blank." }
        val docRef = db.collection("users").document(uid).collection("plants").document(plant.id)
        val updates = buildMap<String, Any> {
            put("plantKnowledgeId", plant.plantKnowledgeId)
            put("plantName", plant.plantName.take(100))
            put("nickname", plant.nickname.take(100))
            put("scientificName", plant.scientificName.take(120))
            put("category", plant.category.take(60))
            put("cultivationMethod", plant.cultivationMethod)
            put("plantingLocation", plant.plantingLocation.take(80))
            put("plantingDate", plant.plantingDate.take(30))
            put("growthStage", plant.growthStage)
            put("healthStatus", plant.healthStatus)
            put("completedStages", plant.completedStages.take(12))
            plant.imageResKey?.takeIf { it.isNotBlank() }?.let { put("imageResKey", it.take(100)) }
            plant.imageBase64?.takeIf { it.isNotBlank() }?.let { put("imageBase64", it.take(195000)) }
            put("updatedAt", FieldValue.serverTimestamp())
        }
        try {
            docRef.update(updates).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    override suspend fun updatePlantMethod(userId: String, plantId: String, methodKey: String): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("plants").document(plantId)
        try {
            docRef.update(
                mapOf(
                    "cultivationMethod" to methodKey,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    override suspend fun updatePlantCompletedStages(
        userId: String,
        plantId: String,
        completedStages: List<String>
    ): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("plants").document(plantId)
        try {
            docRef.update(
                mapOf(
                    "completedStages" to completedStages.take(12),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    override suspend fun deletePlant(userId: String, plantId: String): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("plants").document(plantId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }

    override suspend fun addDiagnosis(userId: String, diagnosis: PlantDiagnosis): Result<String> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("diagnoses").document()
        try {
            docRef.set(diagnosis.toCreateMap(uid)).await()
            docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    override suspend fun addTreatmentPlan(userId: String, plan: TreatmentPlan): Result<String> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("treatment_plans").document()
        try {
            docRef.set(plan.toCreateMap(uid)).await()
            docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    override suspend fun updateTreatmentStatus(
        userId: String,
        planId: String,
        statusKey: String,
        outcomeNotes: String
    ): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("treatment_plans").document(planId)
        try {
            docRef.update(
                mapOf(
                    "status" to statusKey,
                    "outcomeNotes" to outcomeNotes.take(500),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    override suspend fun addCareTask(userId: String, task: CareTask): Result<String> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("care_tasks").document()
        try {
            docRef.set(task.toCreateMap(uid)).await()
            docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    override suspend fun updateCareTaskCompletion(userId: String, taskId: String, completed: Boolean): Result<Unit> =
        runCatching {
            val uid = requireUserId(userId)
            val docRef = db.collection("users").document(uid).collection("care_tasks").document(taskId)
            try {
                docRef.update(
                    mapOf(
                        "completed" to completed,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.UPDATE, docRef.path)
                throw e
            }
        }

    override suspend fun rescheduleCareTask(
        userId: String,
        taskId: String,
        newDate: String,
        frequencyDays: Long
    ): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("care_tasks").document(taskId)
        try {
            docRef.update(
                mapOf(
                    "scheduledDate" to newDate.take(30),
                    "frequencyDays" to frequencyDays.coerceIn(1L, 180L),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    override suspend fun deleteCareTask(userId: String, taskId: String): Result<Unit> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("care_tasks").document(taskId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }

    override suspend fun addGrowthPhoto(userId: String, photo: GrowthPhoto): Result<String> = runCatching {
        val uid = requireUserId(userId)
        val docRef = db.collection("users").document(uid).collection("growth_photos").document()
        try {
            docRef.set(photo.toCreateMap(uid)).await()
            docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    override suspend fun updateGrowthPhotoSummary(userId: String, photoId: String, aiSummary: String): Result<Unit> =
        runCatching {
            val uid = requireUserId(userId)
            val docRef = db.collection("users").document(uid).collection("growth_photos").document(photoId)
            try {
                docRef.update(
                    mapOf(
                        "aiSummary" to aiSummary.take(1000),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.UPDATE, docRef.path)
                throw e
            }
        }
}

class DemoGardenRepository : GardenRepository {
    private val demoUid = "demo_guest"
    private val profileFlow = MutableStateFlow<UserProfile?>(
        UserProfile(
            id = demoUid,
            userId = demoUid,
            displayName = "Tamu Mode Demo",
            onboardingCompleted = false,
            demoSeeded = true
        )
    )
    private val plantsFlow = MutableStateFlow(DemoSampleData.samplePlants(demoUid))
    private val diagnosesFlow = MutableStateFlow(DemoSampleData.sampleDiagnoses(demoUid))
    private val plansFlow = MutableStateFlow(DemoSampleData.sampleTreatments(demoUid))
    private val tasksFlow = MutableStateFlow(DemoSampleData.sampleCareTasks(demoUid))
    private val photosFlow = MutableStateFlow(DemoSampleData.sampleGrowthPhotos(demoUid))

    override fun observeProfile(userId: String): Flow<UserProfile?> = profileFlow
    override fun observePlants(userId: String): Flow<List<UserPlant>> = plantsFlow
    override fun observeDiagnoses(userId: String): Flow<List<PlantDiagnosis>> = diagnosesFlow
    override fun observeTreatmentPlans(userId: String): Flow<List<TreatmentPlan>> = plansFlow
    override fun observeCareTasks(userId: String): Flow<List<CareTask>> = tasksFlow
    override fun observeGrowthPhotos(userId: String): Flow<List<GrowthPhoto>> = photosFlow

    override suspend fun saveProfile(userId: String, profile: UserProfile): Result<Unit> = runCatching {
        profileFlow.value = profile.copy(id = demoUid, userId = demoUid)
    }

    override suspend fun ensureDemoSeeded(userId: String, displayName: String): Result<Unit> = Result.success(Unit)

    override suspend fun addPlant(userId: String, plant: UserPlant): Result<String> = runCatching {
        val newId = "demo_${UUID.randomUUID().toString().take(8)}"
        val item = plant.copy(id = newId, userId = demoUid, createdAt = Timestamp.now(), updatedAt = Timestamp.now())
        plantsFlow.update { listOf(item) + it }
        newId
    }

    override suspend fun updatePlant(userId: String, plant: UserPlant): Result<Unit> = runCatching {
        plantsFlow.update { list -> list.map { if (it.id == plant.id) plant else it } }
    }

    override suspend fun updatePlantMethod(userId: String, plantId: String, methodKey: String): Result<Unit> = runCatching {
        plantsFlow.update { list -> list.map { if (it.id == plantId) it.copy(cultivationMethod = methodKey) else it } }
    }

    override suspend fun updatePlantCompletedStages(
        userId: String,
        plantId: String,
        completedStages: List<String>
    ): Result<Unit> = runCatching {
        plantsFlow.update { list -> list.map { if (it.id == plantId) it.copy(completedStages = completedStages) else it } }
    }

    override suspend fun deletePlant(userId: String, plantId: String): Result<Unit> = runCatching {
        plantsFlow.update { list -> list.filterNot { it.id == plantId } }
    }

    override suspend fun addDiagnosis(userId: String, diagnosis: PlantDiagnosis): Result<String> = runCatching {
        val newId = "diag_${UUID.randomUUID().toString().take(8)}"
        val item = diagnosis.copy(id = newId, userId = demoUid, createdAt = Timestamp.now(), updatedAt = Timestamp.now())
        diagnosesFlow.update { listOf(item) + it }
        newId
    }

    override suspend fun addTreatmentPlan(userId: String, plan: TreatmentPlan): Result<String> = runCatching {
        val newId = "plan_${UUID.randomUUID().toString().take(8)}"
        val item = plan.copy(id = newId, userId = demoUid, createdAt = Timestamp.now(), updatedAt = Timestamp.now())
        plansFlow.update { listOf(item) + it }
        newId
    }

    override suspend fun updateTreatmentStatus(
        userId: String,
        planId: String,
        statusKey: String,
        outcomeNotes: String
    ): Result<Unit> = runCatching {
        plansFlow.update { list ->
            list.map {
                if (it.id == planId && it.status != "completed") {
                    it.copy(status = statusKey, outcomeNotes = outcomeNotes)
                } else it
            }
        }
    }

    override suspend fun addCareTask(userId: String, task: CareTask): Result<String> = runCatching {
        val newId = "task_${UUID.randomUUID().toString().take(8)}"
        val item = task.copy(id = newId, userId = demoUid, createdAt = Timestamp.now(), updatedAt = Timestamp.now())
        tasksFlow.update { listOf(item) + it }
        newId
    }

    override suspend fun updateCareTaskCompletion(userId: String, taskId: String, completed: Boolean): Result<Unit> =
        runCatching {
            tasksFlow.update { list -> list.map { if (it.id == taskId) it.copy(completed = completed) else it } }
        }

    override suspend fun rescheduleCareTask(
        userId: String,
        taskId: String,
        newDate: String,
        frequencyDays: Long
    ): Result<Unit> = runCatching {
        tasksFlow.update { list ->
            list.map {
                if (it.id == taskId) it.copy(scheduledDate = newDate, frequencyDays = frequencyDays) else it
            }
        }
    }

    override suspend fun deleteCareTask(userId: String, taskId: String): Result<Unit> = runCatching {
        tasksFlow.update { list -> list.filterNot { it.id == taskId } }
    }

    override suspend fun addGrowthPhoto(userId: String, photo: GrowthPhoto): Result<String> = runCatching {
        val newId = "photo_${UUID.randomUUID().toString().take(8)}"
        val item = photo.copy(id = newId, userId = demoUid, createdAt = Timestamp.now(), updatedAt = Timestamp.now())
        photosFlow.update { listOf(item) + it }
        newId
    }

    override suspend fun updateGrowthPhotoSummary(userId: String, photoId: String, aiSummary: String): Result<Unit> =
        runCatching {
            photosFlow.update { list -> list.map { if (it.id == photoId) it.copy(aiSummary = aiSummary) else it } }
        }
}
