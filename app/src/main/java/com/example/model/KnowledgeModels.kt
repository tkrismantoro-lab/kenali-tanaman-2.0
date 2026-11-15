package com.example.model

enum class EvidenceLevel(val code: String, val labelId: String) {
    A("A", "Panduan teknis resmi"),
    B("B", "Riset terpublikasi (peer-reviewed)"),
    C("C", "Rekomendasi ditinjau ahli"),
    D("D", "Teruji lapangan"),
    E("E", "Rekomendasi eksperimental"),
    F("F", "Saran umum AI")
}

data class CultivationStageGuide(
    val stageId: String,
    val stageNumber: Int,
    val title: String,
    val durationLabel: String,
    val summary: String,
    val checklist: List<String>,
    val organicTips: String,
    val nonOrganicTips: String,
    val combinedTips: String,
    val commonMistakes: List<String>,
    val troubleshootingHint: String
)

data class OrganicSolution(
    val materialName: String,
    val purpose: String,
    val howToApply: String,
    val suitableStage: String,
    val frequency: String,
    val precautions: String,
    val expectedResponse: String,
    val referenceTitle: String,
    val validationStatus: String,
    val evidenceLevel: EvidenceLevel
)

data class NonOrganicSolution(
    val inputType: String,
    val nutrientComposition: String,
    val applicationMethod: String,
    val timing: String,
    val dosageGuidance: String,
    val precautionsAndPpe: String,
    val withholdingAndReentry: String,
    val compatibilityNotes: String,
    val referenceTitle: String,
    val evidenceLevel: EvidenceLevel
)

data class KnowledgeReference(
    val title: String,
    val institution: String,
    val identifier: String,
    val year: String,
    val evidenceLevel: EvidenceLevel,
    val reviewer: String,
    val lastReviewDate: String
)

data class PlantKnowledge(
    val id: String,
    val commonName: String,
    val scientificName: String,
    val localNames: List<String>,
    val category: String,
    val imageResKey: String,
    val summary: String,
    val characteristics: String,
    val environment: String,
    val sunlight: String,
    val growingMedia: String,
    val wateringPrinciple: String,
    val harvestEstimateDays: String,
    val toxicityNote: String?,
    val stages: List<CultivationStageGuide>,
    val organicSolutions: List<OrganicSolution>,
    val nonOrganicSolutions: List<NonOrganicSolution>,
    val monitoringTips: List<String>,
    val references: List<KnowledgeReference>
)

data class PlantProblemRecord(
    val problemId: String,
    val cropId: String,
    val cropName: String,
    val title: String,
    val symptomDescription: String,
    val visualIndicators: List<String>,
    val possibleCauses: List<String>,
    val diagnosticQuestions: List<String>,
    val distinguishingFeatures: String,
    val organicTreatment: String,
    val nonOrganicTreatment: String,
    val preventiveActions: List<String>,
    val followUpDays: Int,
    val expectedResponse: String,
    val referenceSource: String,
    val evidenceLevel: EvidenceLevel,
    val validationStatus: String
)

data class AiIdentificationResult(
    val commonName: String,
    val scientificName: String,
    val localNames: List<String>,
    val category: String,
    val confidence: String,
    val visibleCharacteristics: List<String>,
    val description: String,
    val benefitsOrUses: String,
    val toxicityWarning: String?,
    val cultivationSuitability: String,
    val possibleMatches: List<String>,
    val photoRetakeAdvice: String?
)

data class AiPossibleCause(
    val cause: String,
    val confidence: String,
    val supportingEvidence: String,
    val missingEvidence: String,
    val additionalCheck: String
)

data class AiDiagnosisResult(
    val plantName: String,
    val scientificName: String,
    val conditionSummary: String,
    val healthStatus: HealthStatus,
    val visualObservations: List<String>,
    val possibleCauses: List<AiPossibleCause>,
    val confidence: String,
    val riskLevel: String,
    val recommendedActions: List<String>,
    val followUpDays: Int,
    val expectedImprovement: String,
    val warningSigns: String,
    val limitations: List<String>,
    val clarifyingQuestions: List<String>
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestampLabel: String,
    val isStreaming: Boolean = false
)
