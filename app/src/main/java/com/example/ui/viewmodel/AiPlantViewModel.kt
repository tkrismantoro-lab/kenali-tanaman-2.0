package com.example.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.PlantAiService
import com.example.model.AiDiagnosisResult
import com.example.model.AiIdentificationResult
import com.example.model.ChatMessage
import com.example.model.CultivationMethod
import com.example.model.GrowthStage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class AiPlantViewModel(
    private val aiService: PlantAiService = PlantAiService()
) : ViewModel() {

    private val _identificationState = MutableStateFlow<UiState<AiIdentificationResult>?>(null)
    val identificationState: StateFlow<UiState<AiIdentificationResult>?> = _identificationState.asStateFlow()

    private val _identificationBitmap = MutableStateFlow<Bitmap?>(null)
    val identificationBitmap: StateFlow<Bitmap?> = _identificationBitmap.asStateFlow()

    private val _diagnosisState = MutableStateFlow<UiState<AiDiagnosisResult>?>(null)
    val diagnosisState: StateFlow<UiState<AiDiagnosisResult>?> = _diagnosisState.asStateFlow()

    private val _diagnosisBitmap = MutableStateFlow<Bitmap?>(null)
    val diagnosisBitmap: StateFlow<Bitmap?> = _diagnosisBitmap.asStateFlow()

    private val _compareState = MutableStateFlow<UiState<String>?>(null)
    val compareState: StateFlow<UiState<String>?> = _compareState.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "welcome",
                isUser = false,
                text = "Halo! Saya Asisten Kebun Kenali Tanaman. Pilih tanaman dari kebunmu atau tanyakan masalah penyiraman, pemupukan, dan perawatan harian. Catatan: chat ini panduan umum, bukan kepastian diagnosis penyakit.",
                timestampLabel = "Sekarang"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatStreaming = MutableStateFlow(false)
    val isChatStreaming: StateFlow<Boolean> = _isChatStreaming.asStateFlow()

    private val _chatError = MutableStateFlow<String?>(null)
    val chatError: StateFlow<String?> = _chatError.asStateFlow()

    fun setIdentificationBitmap(bitmap: Bitmap?) {
        _identificationBitmap.value = bitmap
        if (bitmap == null) {
            _identificationState.value = null
        }
    }

    fun analyzePlantPhoto(bitmap: Bitmap) {
        _identificationBitmap.value = bitmap
        _identificationState.value = UiState.Loading
        viewModelScope.launch {
            aiService.identifyPlant(bitmap)
                .onSuccess { result ->
                    _identificationState.value = UiState.Success(result)
                }
                .onFailure { error ->
                    _identificationState.value = UiState.Error(
                        error.localizedMessage ?: "Gagal menganalisis foto tanaman."
                    )
                }
        }
    }

    fun resetIdentification() {
        _identificationBitmap.value = null
        _identificationState.value = null
    }

    fun setDiagnosisBitmap(bitmap: Bitmap?) {
        _diagnosisBitmap.value = bitmap
    }

    fun runPlantDiagnosis(
        bitmap: Bitmap?,
        plantName: String,
        scientificName: String,
        cultivationMethod: CultivationMethod,
        growthStage: GrowthStage,
        photoType: String,
        symptomNotes: String,
        answers: Map<String, String>
    ) {
        _diagnosisBitmap.value = bitmap
        _diagnosisState.value = UiState.Loading
        viewModelScope.launch {
            aiService.diagnosePlant(
                bitmap = bitmap,
                plantName = plantName,
                scientificName = scientificName,
                cultivationMethod = cultivationMethod,
                growthStage = growthStage,
                photoType = photoType,
                symptomNotes = symptomNotes,
                answers = answers
            )
                .onSuccess { result ->
                    _diagnosisState.value = UiState.Success(result)
                }
                .onFailure { error ->
                    _diagnosisState.value = UiState.Error(
                        error.localizedMessage ?: "Gagal menjalankan diagnosis Dokter Tanaman."
                    )
                }
        }
    }

    fun resetDiagnosis() {
        _diagnosisBitmap.value = null
        _diagnosisState.value = null
    }

    fun compareGrowthPhotos(
        beforeBitmap: Bitmap?,
        afterBitmap: Bitmap?,
        plantName: String,
        beforeLabel: String,
        afterLabel: String
    ) {
        _compareState.value = UiState.Loading
        viewModelScope.launch {
            aiService.compareGrowthPhotos(
                beforeBitmap = beforeBitmap,
                afterBitmap = afterBitmap,
                plantName = plantName,
                beforeLabel = beforeLabel,
                afterLabel = afterLabel
            )
                .onSuccess { summary ->
                    _compareState.value = UiState.Success(summary)
                }
                .onFailure { error ->
                    _compareState.value = UiState.Error(
                        error.localizedMessage ?: "Gagal membandingkan foto pertumbuhan."
                    )
                }
        }
    }

    fun resetComparison() {
        _compareState.value = null
    }

    fun sendChatMessage(question: String, plantContextSummary: String) {
        val trimmed = question.trim()
        if (trimmed.isEmpty() || _isChatStreaming.value) return

        val userMsg = ChatMessage(
            id = "u_${UUID.randomUUID().toString().take(8)}",
            isUser = true,
            text = trimmed,
            timestampLabel = "Baru saja"
        )
        val assistantMsgId = "a_${UUID.randomUUID().toString().take(8)}"
        val placeholderAssistant = ChatMessage(
            id = assistantMsgId,
            isUser = false,
            text = "",
            timestampLabel = "Mengetik...",
            isStreaming = true
        )

        val historySnapshot = _chatMessages.value
        _chatMessages.update { it + userMsg + placeholderAssistant }
        _chatError.value = null
        _isChatStreaming.value = true

        viewModelScope.launch {
            try {
                aiService.askPlantAiStream(trimmed, plantContextSummary, historySnapshot)
                    .collect { partialText ->
                        _chatMessages.update { list ->
                            list.map {
                                if (it.id == assistantMsgId) {
                                    it.copy(text = partialText, timestampLabel = "Sekarang", isStreaming = true)
                                } else it
                            }
                        }
                    }
                _chatMessages.update { list ->
                    list.map {
                        if (it.id == assistantMsgId) it.copy(isStreaming = false, timestampLabel = "Sekarang") else it
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val errText = e.localizedMessage ?: "Gagal terhubung ke Ask Plant AI."
                _chatError.value = errText
                _chatMessages.update { list ->
                    list.filterNot { it.id == assistantMsgId && it.text.isBlank() }
                        .map {
                            if (it.id == assistantMsgId) it.copy(isStreaming = false, timestampLabel = "Terputus") else it
                        }
                }
            } finally {
                _isChatStreaming.value = false
            }
        }
    }

    fun clearChatError() {
        _chatError.value = null
    }

    fun resetChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                id = "welcome_${System.currentTimeMillis()}",
                isUser = false,
                text = "Percakapan baru dimulai. Apa yang ingin kamu tanyakan tentang tanamanmu hari ini?",
                timestampLabel = "Sekarang"
            )
        )
        _chatError.value = null
    }
}
