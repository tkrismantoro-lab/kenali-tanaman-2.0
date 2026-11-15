package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.firebase.GardenRepository
import com.example.model.CareTask
import com.example.model.CareTaskType
import com.example.model.CultivationMethod
import com.example.model.GrowthPhoto
import com.example.model.GrowthStage
import com.example.model.HealthStatus
import com.example.model.PlantDiagnosis
import com.example.model.TreatmentPlan
import com.example.model.TreatmentStatus
import com.example.model.UserPlant
import com.example.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

data class GardenSnapshot(
    val profile: UserProfile? = null,
    val plants: List<UserPlant> = emptyList(),
    val diagnoses: List<PlantDiagnosis> = emptyList(),
    val treatmentPlans: List<TreatmentPlan> = emptyList(),
    val careTasks: List<CareTask> = emptyList(),
    val growthPhotos: List<GrowthPhoto> = emptyList()
)

class GardenViewModel(
    private val repository: GardenRepository,
    val currentUserId: String,
    val isDemoMode: Boolean,
    private val initialDisplayName: String = "Pekebun"
) : ViewModel() {

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy.asStateFlow()

    val gardenState: StateFlow<UiState<GardenSnapshot>> = combine(
        repository.observeProfile(currentUserId),
        repository.observePlants(currentUserId),
        repository.observeDiagnoses(currentUserId),
        repository.observeTreatmentPlans(currentUserId),
        combine(
            repository.observeCareTasks(currentUserId),
            repository.observeGrowthPhotos(currentUserId)
        ) { tasks, photos -> tasks to photos }
    ) { profile, plants, diagnoses, plans, (tasks, photos) ->
        GardenSnapshot(
            profile = profile,
            plants = plants,
            diagnoses = diagnoses,
            treatmentPlans = plans,
            careTasks = tasks,
            growthPhotos = photos
        )
    }
        .map<GardenSnapshot, UiState<GardenSnapshot>> { UiState.Success(it) }
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            val rootError = firestoreError ?: error
            Log.w(TAG, "Error observing garden state", rootError)
            emit(UiState.Error(rootError.localizedMessage ?: "Gagal memuat data kebun."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading
        )

    init {
        viewModelScope.launch {
            repository.ensureDemoSeeded(currentUserId, initialDisplayName)
                .onFailure { e ->
                    if (e is CancellationException) throw e
                    Log.w(TAG, "Seed check failed: ${e.message}", e)
                }
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showMessage(message: String) {
        _snackbarMessage.value = message
    }

    fun saveProfile(
        displayName: String,
        experienceLevel: String,
        gardeningType: String,
        preferredMethod: CultivationMethod,
        remindersEnabled: Boolean,
        onboardingCompleted: Boolean,
        onDone: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            val existing = (gardenState.value as? UiState.Success)?.data?.profile
            val updated = (existing ?: UserProfile(userId = currentUserId)).copy(
                userId = currentUserId,
                displayName = displayName.ifBlank { initialDisplayName },
                experienceLevel = experienceLevel,
                gardeningType = gardeningType,
                preferredMethod = preferredMethod.key,
                remindersEnabled = remindersEnabled,
                onboardingCompleted = onboardingCompleted,
                demoSeeded = existing?.demoSeeded ?: true
            )
            repository.saveProfile(currentUserId, updated)
                .onSuccess {
                    _snackbarMessage.value = "Profil dan preferensi tersimpan."
                    onDone()
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal menyimpan profil."
                }
            _isBusy.value = false
        }
    }

    fun addPlant(
        plantKnowledgeId: String,
        plantName: String,
        nickname: String,
        scientificName: String,
        category: String,
        cultivationMethod: CultivationMethod,
        plantingLocation: String,
        plantingDate: String,
        growthStage: GrowthStage,
        imageResKey: String?,
        imageBase64: String? = null,
        onAdded: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            val newPlant = UserPlant(
                userId = currentUserId,
                plantKnowledgeId = plantKnowledgeId,
                plantName = plantName,
                nickname = nickname.ifBlank { plantName },
                scientificName = scientificName,
                category = category,
                cultivationMethod = cultivationMethod.key,
                plantingLocation = plantingLocation.ifBlank { "Polybag / Pot" },
                plantingDate = plantingDate.ifBlank { "2026-11-15" },
                growthStage = growthStage.key,
                healthStatus = HealthStatus.HEALTHY.key,
                isDemoData = false,
                completedStages = emptyList(),
                imageResKey = imageResKey,
                imageBase64 = imageBase64
            )
            repository.addPlant(currentUserId, newPlant)
                .onSuccess { newId ->
                    // Also seed initial care task for the newly added plant
                    repository.addCareTask(
                        currentUserId,
                        CareTask(
                            userId = currentUserId,
                            plantId = newId,
                            plantName = plantName,
                            taskType = CareTaskType.WATER.key,
                            title = "Periksa kelembapan media $plantName",
                            reason = "Tanaman baru ditambahkan. Cek kondisi media tanam sebelum menyiram.",
                            scheduledDate = "Hari ini",
                            frequencyDays = 2L,
                            completed = false,
                            isDemoData = false
                        )
                    )
                    _snackbarMessage.value = "$plantName berhasil ditambahkan ke Kebun Saya."
                    onAdded(newId)
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal menambahkan tanaman."
                }
            _isBusy.value = false
        }
    }

    fun updatePlant(plant: UserPlant, onUpdated: () -> Unit = {}) {
        viewModelScope.launch {
            _isBusy.value = true
            repository.updatePlant(currentUserId, plant)
                .onSuccess {
                    _snackbarMessage.value = "Data ${plant.plantName} diperbarui."
                    onUpdated()
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal memperbarui tanaman."
                }
            _isBusy.value = false
        }
    }

    fun updatePlantMethod(plantId: String, method: CultivationMethod) {
        viewModelScope.launch {
            repository.updatePlantMethod(currentUserId, plantId, method.key)
                .onSuccess {
                    _snackbarMessage.value = "Metode budidaya diubah ke ${method.labelId}. Riwayat tetap tersimpan."
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal mengubah metode budidaya."
                }
        }
    }

    fun toggleStageCompleted(plant: UserPlant, stageId: String) {
        val current = plant.completedStages.toMutableList()
        if (current.contains(stageId)) {
            current.remove(stageId)
        } else {
            current.add(stageId)
        }
        viewModelScope.launch {
            repository.updatePlantCompletedStages(currentUserId, plant.id, current)
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal menyimpan progres tahap."
                }
        }
    }

    fun deletePlant(plantId: String, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deletePlant(currentUserId, plantId)
                .onSuccess {
                    _snackbarMessage.value = "Tanaman dihapus dari kebun."
                    onDeleted()
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal menghapus tanaman."
                }
        }
    }

    fun saveDiagnosis(
        diagnosis: PlantDiagnosis,
        createTreatmentPlan: Boolean = true,
        scheduleFollowUp: Boolean = true,
        onSaved: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            repository.addDiagnosis(currentUserId, diagnosis)
                .onSuccess { diagId ->
                    if (createTreatmentPlan && diagnosis.recommendedActions.isNotEmpty()) {
                        val plan = TreatmentPlan(
                            userId = currentUserId,
                            plantId = diagnosis.plantId,
                            plantName = diagnosis.plantName,
                            diagnosisId = diagId,
                            problem = diagnosis.conditionSummary,
                            suspectedCause = diagnosis.possibleCausesSummary.firstOrNull().orEmpty(),
                            recommendedTreatment = diagnosis.recommendedActions.joinToString("\n"),
                            treatmentDate = "Hari ini",
                            followUpDate = "${diagnosis.followUpDays} hari lagi",
                            expectedResponse = "Gejala tidak meluas dan tunas baru tampak segar.",
                            status = TreatmentStatus.NOT_STARTED.key,
                            outcomeNotes = "",
                            isDemoData = false
                        )
                        repository.addTreatmentPlan(currentUserId, plan)
                    }
                    if (scheduleFollowUp) {
                        val task = CareTask(
                            userId = currentUserId,
                            plantId = diagnosis.plantId,
                            plantName = diagnosis.plantName,
                            taskType = CareTaskType.FOLLOWUP.key,
                            title = "Pemeriksaan ulang ${diagnosis.plantName}",
                            reason = "Evaluasi perkembangan kondisi setelah tindakan perawatan (${diagnosis.followUpDays} hari).",
                            scheduledDate = "${diagnosis.followUpDays} hari lagi",
                            frequencyDays = diagnosis.followUpDays,
                            completed = false,
                            isDemoData = false
                        )
                        repository.addCareTask(currentUserId, task)
                    }
                    // Also update plant health status if linked to a plant
                    val currentPlants = (gardenState.value as? UiState.Success)?.data?.plants.orEmpty()
                    val matchedPlant = currentPlants.firstOrNull { it.id == diagnosis.plantId }
                    if (matchedPlant != null) {
                        repository.updatePlant(
                            currentUserId,
                            matchedPlant.copy(healthStatus = diagnosis.healthStatus)
                        )
                    }
                    _snackbarMessage.value = "Hasil diagnosis dan tindak lanjut disimpan."
                    onSaved(diagId)
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal menyimpan diagnosis."
                }
            _isBusy.value = false
        }
    }

    fun addTreatmentPlan(plan: TreatmentPlan, onAdded: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addTreatmentPlan(currentUserId, plan)
                .onSuccess {
                    _snackbarMessage.value = "Rencana perawatan dibuat."
                    onAdded()
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal membuat rencana perawatan."
                }
        }
    }

    fun updateTreatmentOutcome(planId: String, status: TreatmentStatus, notes: String) {
        viewModelScope.launch {
            repository.updateTreatmentStatus(currentUserId, planId, status.key, notes)
                .onSuccess {
                    _snackbarMessage.value = when (status) {
                        TreatmentStatus.WORSENED, TreatmentStatus.UNCHANGED ->
                            "Status tercatat (${status.labelId}). Disarankan melakukan pemeriksaan ulang dengan foto terbaru."
                        else -> "Progres perawatan diperbarui: ${status.labelId}."
                    }
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal memperbarui status perawatan."
                }
        }
    }

    fun addCareTask(task: CareTask, onAdded: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addCareTask(currentUserId, task)
                .onSuccess {
                    _snackbarMessage.value = "Jadwal perawatan ditambahkan."
                    onAdded()
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal menambah jadwal."
                }
        }
    }

    fun toggleCareTaskCompletion(taskId: String, completed: Boolean) {
        viewModelScope.launch {
            repository.updateCareTaskCompletion(currentUserId, taskId, completed)
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal mengubah status tugas."
                }
        }
    }

    fun rescheduleCareTask(taskId: String, newDate: String, frequencyDays: Long) {
        viewModelScope.launch {
            repository.rescheduleCareTask(currentUserId, taskId, newDate, frequencyDays)
                .onSuccess {
                    _snackbarMessage.value = "Jadwal diperbarui ke $newDate."
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal menjadwal ulang."
                }
        }
    }

    fun deleteCareTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteCareTask(currentUserId, taskId)
                .onSuccess { _snackbarMessage.value = "Tugas perawatan dihapus." }
                .onFailure { _snackbarMessage.value = it.localizedMessage ?: "Gagal menghapus tugas." }
        }
    }

    fun addGrowthPhoto(photo: GrowthPhoto, onAdded: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addGrowthPhoto(currentUserId, photo)
                .onSuccess {
                    _snackbarMessage.value = "Foto perkembangan tanaman disimpan."
                    onAdded()
                }
                .onFailure {
                    _snackbarMessage.value = it.localizedMessage ?: "Gagal menyimpan foto perkembangan."
                }
        }
    }

    private companion object {
        const val TAG = "GardenViewModel"
        const val STOP_TIMEOUT_MILLIS = 5000L
    }
}
