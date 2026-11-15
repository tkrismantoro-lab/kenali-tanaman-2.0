package com.example.data.knowledge

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

object DemoSampleData {

    fun samplePlants(uid: String): List<UserPlant> = listOf(
        UserPlant(
            id = "demo_cabai_001",
            userId = uid,
            plantKnowledgeId = "cabai",
            plantName = "Cabai Rawit",
            nickname = "Cabai depan rumah",
            scientificName = "Capsicum annuum",
            category = "Sayuran Buah",
            cultivationMethod = CultivationMethod.ORGANIC.key,
            plantingLocation = "Polybag 35 cm (Teras)",
            plantingDate = "2026-10-20",
            growthStage = GrowthStage.VEGETATIVE.key,
            healthStatus = HealthStatus.NEEDS_ATTENTION.key,
            isDemoData = true,
            completedStages = listOf("stage_1", "stage_2", "stage_3"),
            imageResKey = "img_plant_cabai"
        ),
        UserPlant(
            id = "demo_tomat_002",
            userId = uid,
            plantKnowledgeId = "tomat",
            plantName = "Tomat",
            nickname = "Tomat pagar timur",
            scientificName = "Solanum lycopersicum",
            category = "Sayuran Buah",
            cultivationMethod = CultivationMethod.COMBINED.key,
            plantingLocation = "Pot 40 cm + Ajir Bambu",
            plantingDate = "2026-10-05",
            growthStage = GrowthStage.FRUITING.key,
            healthStatus = HealthStatus.HEALTHY.key,
            isDemoData = true,
            completedStages = listOf("stage_1", "stage_2", "stage_3", "stage_4", "stage_5"),
            imageResKey = "img_plant_tomat"
        ),
        UserPlant(
            id = "demo_pakcoy_003",
            userId = uid,
            plantKnowledgeId = "pakcoy",
            plantName = "Pakcoy",
            nickname = "Pakcoy rak hidro-organik",
            scientificName = "Brassica rapa subsp. chinensis",
            category = "Sayuran Daun",
            cultivationMethod = CultivationMethod.ORGANIC.key,
            plantingLocation = "Planter Box Kompos",
            plantingDate = "2026-11-01",
            growthStage = GrowthStage.VEGETATIVE.key,
            healthStatus = HealthStatus.HEALTHY.key,
            isDemoData = true,
            completedStages = listOf("stage_1", "stage_2", "stage_3"),
            imageResKey = "img_plant_pakcoy"
        )
    )

    fun sampleDiagnoses(uid: String): List<PlantDiagnosis> = listOf(
        PlantDiagnosis(
            id = "demo_diag_001",
            userId = uid,
            plantId = "demo_cabai_001",
            plantName = "Cabai Rawit",
            photoType = "Daun",
            symptomNotes = "Contoh data simulasi: Sebagian daun bagian bawah tampak menguning dalam 3 hari terakhir setelah hujan.",
            cultivationMethod = CultivationMethod.ORGANIC.key,
            conditionSummary = "Daun cabai menunjukkan gejala menguning pada sebagian daun bagian bawah.",
            healthStatus = HealthStatus.NEEDS_ATTENTION.key,
            observations = listOf(
                "Penguningan terjadi terutama pada helai daun bagian bawah dekat pangkal batang",
                "Daun pucuk atas masih tampak hijau dan belum menggulung",
                "Belum terlihat bercak nekrotik berbatas tegas atau serangga jelas pada foto"
            ),
            possibleCausesSummary = listOf(
                "Media terlalu lembap (Keyakinan: Sedang) — Gejala muncul setelah hujan dan media polybag terasa basah.",
                "Kekurangan unsur hara tertentu (Keyakinan: Rendah-Sedang) — Daun tua menguning lebih dulu bisa berkaitan dengan mobilitas hara atau serapan akar terganggu.",
                "Gangguan akar awal (Keyakinan: Rendah) — Belum ada pemeriksaan langsung pada warna dan aroma perakaran."
            ),
            confidence = "moderate",
            riskLevel = "moderate",
            recommendedActions = listOf(
                "1. Periksa kelembapan media tanam sedalam 3–5 cm sebelum menyiram kembali.",
                "2. Pastikan lubang drainase di bawah polybag tidak tersumbat tanah liat.",
                "3. Amati kondisi pangkal batang dan akar serabut bila genangan terjadi berulang.",
                "4. Tunda penambahan pupuk (termasuk kompos cair) hingga kelembapan dan kondisi akar stabil.",
                "5. Foto ulang tanaman setelah 3 hari pemantauan untuk membandingkan perubahan."
            ),
            followUpDays = 3L,
            limitations = listOf(
                "Diagnosis ini merupakan analisis awal dari contoh data simulasi, bukan kepastian penyakit.",
                "Belum tersedia foto sisi bawah daun dan kondisi lubang drainase."
            ),
            isDemoData = true
        )
    )

    fun sampleTreatments(uid: String): List<TreatmentPlan> = listOf(
        TreatmentPlan(
            id = "demo_plan_001",
            userId = uid,
            plantId = "demo_cabai_001",
            plantName = "Cabai Rawit",
            diagnosisId = "demo_diag_001",
            problem = "Daun bawah Cabai Rawit menguning (Contoh data simulasi)",
            suspectedCause = "Media terlalu lembap / drainase terhambat",
            recommendedTreatment = "Periksa kelembapan media, bersihkan lubang drainase polybag, ganjal alas polybag dengan bata agar air mudah keluar, dan tunda pemupukan tambahan selama 3 hari.",
            treatmentDate = "2026-11-15",
            followUpDate = "2026-11-18",
            expectedResponse = "Penguningan tidak merambat ke daun tengah dan pucuk tetap segar dalam 3 hari.",
            status = TreatmentStatus.AWAITING_EVAL.key,
            outcomeNotes = "Drainase polybag sudah diperbaiki, menunggu evaluasi hari ke-3.",
            isDemoData = true
        )
    )

    fun sampleCareTasks(uid: String): List<CareTask> = listOf(
        CareTask(
            id = "demo_task_001",
            userId = uid,
            plantId = "demo_cabai_001",
            plantName = "Cabai Rawit",
            taskType = CareTaskType.INSPECT.key,
            title = "Periksa kelembapan media & daun bawah cabai",
            reason = "Tindak lanjut penguningan daun bawah (jangan siram jika media masih lembap).",
            scheduledDate = "Hari ini",
            frequencyDays = 3L,
            completed = false,
            isDemoData = true
        ),
        CareTask(
            id = "demo_task_002",
            userId = uid,
            plantId = "demo_tomat_002",
            plantName = "Tomat",
            taskType = CareTaskType.WATER.key,
            title = "Cek media & siram pangkal tanaman tomat",
            reason = "Fase berbuah membutuhkan kelembapan stabil di pangkal tanpa membasahi daun.",
            scheduledDate = "Hari ini",
            frequencyDays = 2L,
            completed = false,
            isDemoData = true
        ),
        CareTask(
            id = "demo_task_003",
            userId = uid,
            plantId = "demo_pakcoy_003",
            plantName = "Pakcoy",
            taskType = CareTaskType.FERTILIZE.key,
            title = "Evaluasi pemberian kompos matang pakcoy",
            reason = "Pakcoy berumur 14 hari di fase vegetatif aktif; cek kesegaran daun sebelum top-dress kompos.",
            scheduledDate = "Hari ini",
            frequencyDays = 7L,
            completed = false,
            isDemoData = true
        )
    )

    fun sampleGrowthPhotos(uid: String): List<GrowthPhoto> = listOf(
        GrowthPhoto(
            id = "demo_photo_001",
            userId = uid,
            plantId = "demo_cabai_001",
            plantName = "Cabai Rawit",
            dateLabel = "20 Okt (Contoh simulasi)",
            growthStage = GrowthStage.SEEDLING.key,
            notes = "Bibit dipindah ke polybag 35 cm setelah memiliki 4 daun sejati.",
            photoResKey = "img_plant_cabai",
            aiSummary = "Contoh data simulasi: Bibit muda tampak tegak dengan daun kotiledon dan 4 daun sejati awal.",
            isDemoData = true
        ),
        GrowthPhoto(
            id = "demo_photo_002",
            userId = uid,
            plantId = "demo_cabai_001",
            plantName = "Cabai Rawit",
            dateLabel = "03 Nov (Contoh simulasi)",
            growthStage = GrowthStage.VEGETATIVE.key,
            notes = "Tunas samping mulai tumbuh rapi, penyiraman dilakukan saat lapisan atas media mengering.",
            photoResKey = "img_plant_cabai",
            aiSummary = "Contoh data simulasi: Percabangan vegetatif bertambah rimbun dengan warna hijau merata.",
            isDemoData = true
        ),
        GrowthPhoto(
            id = "demo_photo_003",
            userId = uid,
            plantId = "demo_tomat_002",
            plantName = "Tomat",
            dateLabel = "15 Nov (Contoh simulasi)",
            growthStage = GrowthStage.FRUITING.key,
            notes = "Buah mulai memerah pada dompol pertama, diikat rapi ke ajir bambu.",
            photoResKey = "img_plant_tomat",
            aiSummary = "Contoh data simulasi: Dompol buah terbentuk sehat dan tajuk tertopang ajir dengan baik.",
            isDemoData = true
        )
    )
}
