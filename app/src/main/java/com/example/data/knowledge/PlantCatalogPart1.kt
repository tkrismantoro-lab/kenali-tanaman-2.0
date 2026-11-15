package com.example.data.knowledge

import com.example.model.CultivationStageGuide
import com.example.model.EvidenceLevel
import com.example.model.KnowledgeReference
import com.example.model.NonOrganicSolution
import com.example.model.OrganicSolution
import com.example.model.PlantKnowledge

internal fun buildStandardStages(
    cropName: String,
    sunlight: String,
    media: String,
    seedPrepTip: String,
    seedSoakRecommended: Boolean,
    germinationDays: String,
    spacing: String,
    vegetativeWaterTip: String,
    vegetativeNutrientOrganic: String,
    vegetativeNutrientNonOrganic: String,
    floweringOrLeafTip: String,
    fruitingOrMaturationTip: String,
    harvestIndicator: String,
    harvestWindow: String,
    postHarvestTip: String
): List<CultivationStageGuide> = listOf(
    CultivationStageGuide(
        stageId = "stage_1",
        stageNumber = 1,
        title = "Persiapan Lahan & Media Tanam",
        durationLabel = "1–3 hari",
        summary = "Siapkan lokasi dengan $sunlight dan media $media yang gembur serta berdrainase baik.",
        checklist = listOf(
            "Pilih lokasi dengan pencahayaan: $sunlight",
            "Siapkan wadah/bedengan berlubang drainase bersih",
            "Campur media: $media secara merata",
            "Pilih benih/bibit $cropName dari sumber terpercaya dan bebas bercak"
        ),
        organicTips = "Gunakan kompos yang benar-benar matang (tidak panas dan tidak berbau menyengat). Hindari pupuk kandang mentah langsung di sekitar akar muda.",
        nonOrganicTips = "Pastikan pH media netral (sekitar 6,0–6,8). Jika media agak masam, berikan kapur pertanian/dolomit sesuai anjuran kemasan sebelum tanam.",
        combinedTips = "Gunakan media kaya kompos matang sebagai dasar untuk menjaga struktur tanah, lalu siapkan pupuk mineral hanya sebagai suplemen terukur bila dibutuhkan.",
        commonMistakes = listOf(
            "Memakai tanah liat padat tanpa campuran bahan organik atau sekam sehingga air menggenang.",
            "Menggunakan pupuk kandang segar yang masih panas dan berpotensi membakar akar."
        ),
        troubleshootingHint = "Jika air siraman tidak turun dalam 1 menit, tambahkan sekam bakar/pasir kasar dan perbesar lubang drainase."
    ),
    CultivationStageGuide(
        stageId = "stage_2",
        stageNumber = 2,
        title = "Persiapan Benih & Penyemaian",
        durationLabel = germinationDays,
        summary = seedPrepTip,
        checklist = listOf(
            if (seedSoakRecommended) "Rendam benih sehat dalam air hangat kuku 3–6 jam (buang benih mengapung)" else "Gunakan benih kering bersih langsung tanpa perendaman lama agar tidak membusuk",
            "Isi tray/pot semai dengan media halus dan lembap (bukan becek)",
            "Tutup tipis benih dengan media halus atau abu sekam",
            "Letakkan di tempat teduh terang hingga berkecambah ($germinationDays)"
        ),
        organicTips = "Media semai cukup memakai cocopeat tercuci dan kompos saring halus (1:1). Semprot kabut halus agar benih tidak bergeser.",
        nonOrganicTips = "Pastikan benih bersertifikat dan bersih; hindari pemupukan kimia selama fase kecambah karena akar muda sangat sensitif garam.",
        combinedTips = "Fokus pada kelembapan stabil dan sirkulasi udara; pupuk belum diperlukan hingga daun sejati muncul.",
        commonMistakes = listOf(
            "Menanam benih terlalu dalam sehingga kecambah kehabisan energi sebelum menembus permukaan.",
            "Menyiram terlalu deras sehingga benih hanyut atau membusuk (rebah semai)."
        ),
        troubleshootingHint = "Bila batang semai memanjang kurus (etiolasi), pindahkan bertahap ke tempat yang mendapat cahaya pagi."
    ),
    CultivationStageGuide(
        stageId = "stage_3",
        stageNumber = 3,
        title = "Penanaman & Pindah Tanam",
        durationLabel = "Hari 1–7 setelah tanam",
        summary = "Pindahkan bibit atau tanam dengan jarak $spacing agar sirkulasi udara optimal.",
        checklist = listOf(
            "Lakukan pindah tanam pada sore hari untuk menekan stres panas",
            "Jaga jarak tanam ideal: $spacing",
            "Padatkan media di pangkal batang dengan lembut agar akar menempel",
            "Siram secukupnya hingga media lembap merata dan beri naungan sementara 2–3 hari bila terik"
        ),
        organicTips = "Beri mulsa tipis jerami atau daun kering di permukaan media (jangan menempel langsung ke batang) untuk menahan penguapan.",
        nonOrganicTips = "Tunggu 5–7 hari hingga akar beradaptasi sebelum memberikan pupuk mineral dosis ringan pertama.",
        combinedTips = "Gabungkan mulsa organik tipis untuk stabilitas suhu akar sambil memantau adaptasi daun selama minggu pertama.",
        commonMistakes = listOf(
            "Pindah tanam saat siang terik yang menyebabkan layu berat.",
            "Menanam terlalu rapat sehingga kelembapan terperangkap dan memicu jamur."
        ),
        troubleshootingHint = "Jika bibit layu ringan pada siang hari ke-1 s/d ke-2 namun segar kembali di sore hari, itu adaptasi wajar; jangan siram berlebihan."
    ),
    CultivationStageGuide(
        stageId = "stage_4",
        stageNumber = 4,
        title = "Pertumbuhan Vegetatif",
        durationLabel = "Minggu 2–5",
        summary = "Fokus membentuk akar kuat, batang kokoh, dan daun sehat. $vegetativeWaterTip",
        checklist = listOf(
            "Periksa kelembapan media dengan jari sedalam 3 cm sebelum menyiram",
            "Bersihkan gulma pesaing di sekitar pangkal tanaman",
            "Amati bagian bawah daun 2–3 hari sekali untuk deteksi dini kutu atau tungau",
            "Pasang ajir atau tali penopang bila tanaman mulai meninggi atau merambat"
        ),
        organicTips = vegetativeNutrientOrganic,
        nonOrganicTips = vegetativeNutrientNonOrganic,
        combinedTips = "Berikan kompos matang sebagai nutrisi dasar setiap 2 minggu, dan lengkapi pupuk mineral dosis ringan terukur hanya bila pertumbuhan melambat.",
        commonMistakes = listOf(
            "Menyiram rutin setiap hari tanpa mengecek kondisi basah/keringnya media.",
            "Memupuk nitrogen berlebihan sehingga daun terlalu lunak dan mudah diserang kutu."
        ),
        troubleshootingHint = "Jika daun bawah menguning, cek drainase dan kelembapan media terlebih dahulu sebelum menambah pupuk."
    ),
    CultivationStageGuide(
        stageId = "stage_5",
        stageNumber = 5,
        title = "Fase Pembungaan / Pembentukan Tajuk",
        durationLabel = "Minggu 5–8",
        summary = floweringOrLeafTip,
        checklist = listOf(
            "Pastikan sinar matahari cukup agar bunga/tajuk berkembang optimal",
            "Jaga penyiraman stabil—hindari media kering total lalu diguyur berlebih",
            "Pangkas tunas air atau daun tua yang menyentuh tanah agar sirkulasi baik",
            "Perhatikan serangga penyerbuk alami di pagi hari"
        ),
        organicTips = "Tambahkan kompos kaya unsur kalium alami seperti kompos kulit pisang terfermentasi matang atau kompos matang yang diperkaya abu sekam secukupnya.",
        nonOrganicTips = "Sesuaikan keseimbangan hara (kurangi dominasi nitrogen, perhatikan fosfor dan kalium sesuai label produk) untuk mencegah rontok bunga.",
        combinedTips = "Pertahankan mulsa organik dan berikan suplemen unsur P/K secara terukur apabila bunga mulai bermunculan.",
        commonMistakes = listOf(
            "Membiarkan media kekeringan mendadak yang memicu kerontokan kuncup bunga.",
            "Menyemprot cairan apa pun saat bunga mekar di pagi hari karena dapat mengganggu penyerbukan."
        ),
        troubleshootingHint = "Bunga rontok bisa dipicu panas ekstrem, fluktuasi air, atau hama thrips—periksa bagian dalam kelopak bunga."
    ),
    CultivationStageGuide(
        stageId = "stage_6",
        stageNumber = 6,
        title = "Fase Pembuahan / Pematangan",
        durationLabel = "Minggu 8–12",
        summary = fruitingOrMaturationTip,
        checklist = listOf(
            "Topang cabang yang mulai berat agar tidak patah",
            "Pantau gejala busuk ujung buah, lalat buah, atau ulat",
            "Jaga konsistensi kelembapan media untuk mencegah buah pecah",
            "Singkirkan buah atau daun yang membusuk dari area tanam"
        ),
        organicTips = "Gunakan perangkap mekanis (perangkap kuning atau botol atraktan untuk lalat buah bila tanaman berbuah) dan sanitasi rutin.",
        nonOrganicTips = "Perhatikan kecukupan kalsium dan kalium. Jika memakai sarana pengendalian terdaftar, wajib patuhi masa henti sebelum panen (PHI) pada label.",
        combinedTips = "Prioritaskan sanitasi mekanis dan perangkap hama terlebih dahulu sebelum mempertimbangkan intervensi tambahan.",
        commonMistakes = listOf(
            "Penyiraman tidak merata (kering lalu banjir) yang membuat kulit buah pecah.",
            "Membiarkan buah busuk jatuh di atas media sehingga menularkan spora jamur."
        ),
        troubleshootingHint = "Ujung buah menghitam cekung sering berkaitan dengan hambatan serapan kalsium akibat fluktuasi air, bukan sekadar kurang pupuk."
    ),
    CultivationStageGuide(
        stageId = "stage_7",
        stageNumber = 7,
        title = "Panen & Penanganan Awal",
        durationLabel = harvestWindow,
        summary = "Panen saat indikator kematangan tercapai: $harvestIndicator",
        checklist = listOf(
            "Pastikan tanda panen terpenuhi: $harvestIndicator",
            "Panen pada pagi hari saat suhu sejuk dan kadar air tanaman optimal",
            "Gunakan gunting panen bersih agar batang utama tidak rusak",
            "Simpan hasil panen di tempat teduh dan berventilasi baik"
        ),
        organicTips = "Hasil panen organik dicuci bersih dengan air mengalir sebelum dikonsumsi atau disimpan.",
        nonOrganicTips = "Pastikan batas waktu aman setelah aplikasi produk (withholding period) telah terpenuhi sebelum memanen.",
        combinedTips = "Catat tanggal dan perkiraan hasil panen di profil tanaman untuk bahan evaluasi siklus tanam berikutnya.",
        commonMistakes = listOf(
            "Menarik buah/batang dengan paksa hingga melukai cabang produktif.",
            "Menumpuk hasil panen di wadah tertutup panas sehingga cepat membusuk."
        ),
        troubleshootingHint = "Jika ukuran panen mengecil di akhir masa produktif, evaluasi kecukupan cahaya dan kondisi kesuburan media."
    ),
    CultivationStageGuide(
        stageId = "stage_8",
        stageNumber = 8,
        title = "Pasca Panen & Siklus Berikutnya",
        durationLabel = "Setelah panen selesai",
        summary = postHarvestTip,
        checklist = listOf(
            "Bersihkan sisa tanaman dan gulma dari polybag atau bedengan",
            "Komposkan hanya sisa tanaman yang sehat (buang bagian berpenyakit di luar kebun)",
            "Jemur dan gemburkan kembali media sebelum dipakai ulang",
            "Terapkan rotasi tanaman dengan famili berbeda untuk memutus siklus hama"
        ),
        organicTips = "Segarkan media bekas dengan 30–40% kompos matang baru dan istirahatkan media 1–2 minggu sebelum tanam ulang.",
        nonOrganicTips = "Bilas kelebihan residu garam mineral pada pot/polybag bekas sebelum penanaman siklus berikutnya.",
        combinedTips = "Rotasi famili tanaman (misalnya setelah sayuran buah tanam sayuran daun atau kacang-kacangan) sangat efektif menekan penyakit tular tanah.",
        commonMistakes = listOf(
            "Mengomposkan daun yang terserang layu bakteri atau bercak berat ke dalam kompos rumah tangga.",
            "Menanam jenis tanaman yang sama terus-menerus di media bekas tanpa peremajaan."
        ),
        troubleshootingHint = "Bila siklus sebelumnya mengalami busuk akar berat, ganti total media tanam dan bersihkan wadah dengan air sabun."
    )
)

object PlantCatalogPart1 {
    val plants: List<PlantKnowledge> = listOf(
        PlantKnowledge(
            id = "cabai",
            commonName = "Cabai Rawit",
            scientificName = "Capsicum annuum / Capsicum frutescens",
            localNames = listOf("Lombok", "Cabe rawit", "Cengek"),
            category = "Sayuran Buah",
            imageResKey = "img_plant_cabai",
            summary = "Tanaman buah semusim yang sangat populer untuk budidaya polybag maupun lahan terbuka di iklim tropis Indonesia.",
            characteristics = "Berbatang tegak bercabang, daun lonjong meruncing, bunga putih kecil, buah tumbuh tegak atau merunduk berubah dari hijau ke merah.",
            environment = "Dataran rendah hingga tinggi (0–1.200 mdpl), suhu ideal 22–30°C, sirkulasi udara baik.",
            sunlight = "Sinar matahari penuh minimal 6–8 jam per hari.",
            growingMedia = "Tanah gembur + kompos matang + sekam bakar (perbandingan 2:1:1), pH 6,0–6,8.",
            wateringPrinciple = "Siram saat lapisan atas media (2–3 cm) terasa mulai mengering; hindari genangan karena akar cabai peka busuk.",
            harvestEstimateDays = "75–90 hari setelah pindah tanam",
            toxicityNote = "Getah buah mengandung kapsaisin yang dapat mengiritasi mata dan kulit sensitif; cuci tangan setelah memanen.",
            stages = buildStandardStages(
                cropName = "Cabai Rawit",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah gembur, kompos matang, dan sekam bakar (2:1:1)",
                seedPrepTip = "Pilih benih dari buah tua sehat atau benih bersertifikat; rendam singkat membantu seleksi benih bernas.",
                seedSoakRecommended = true,
                germinationDays = "7–12 hari",
                spacing = "Polybag minimal diameter 30–35 cm (1 tanaman) atau jarak 50x60 cm di bedengan",
                vegetativeWaterTip = "Jangan biarkan media becek terus-menerus.",
                vegetativeNutrientOrganic = "Berikan kompos matang 1–2 genggam di sekeliling tajuk tiap 14 hari atau pupuk organik cair encer.",
                vegetativeNutrientNonOrganic = "Gunakan pupuk berimbang (misal NPK seimbang dosis rendah sesuai anjuran kemasan) hanya saat tanaman aktif tumbuh.",
                floweringOrLeafTip = "Memasuki minggu ke-5 s/d ke-7, bunga pertama muncul di ketiak percabangan Y.",
                fruitingOrMaturationTip = "Buah membutuhkan air stabil dan nutrisi kalium-kalsium cukup agar tidak rontok atau busuk ujung.",
                harvestIndicator = "Buah berwarna merah cerah merata atau hijau tua bernas sesuai selera pasar.",
                harvestWindow = "75–90 HST (panen bertahap tiap 4–7 hari)",
                postHarvestTip = "Setelah fase panen puncak selesai, pangkas cabang kering atau lakukan rotasi dengan tanaman non-Solanaceae."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Kompos Matang + Agen Hayati Trichoderma",
                    purpose = "Meningkatkan kesehatan media dan menekan jamur tular tanah di perakaran.",
                    howToApply = "Campurkan 1 bagian kompos matang dengan 2 bagian tanah dan tabur tipis sesuai petunjuk produk hayati di zona akar.",
                    suitableStage = "Persiapan media & Vegetatif",
                    frequency = "Saat olah media dan ulang tipis tiap 3–4 minggu.",
                    precautions = "Jangan gunakan kompos yang masih terasa hangat/panas atau berbau amonia.",
                    expectedResponse = "Perakaran tumbuh lebih putih sehat dan media tetap gembur.",
                    referenceTitle = "Pedoman Budidaya Cabai Ramah Lingkungan — Balitsa / Kementan",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                ),
                OrganicSolution(
                    materialName = "Perangkap Likat Kuning (Yellow Sticky Trap)",
                    purpose = "Memantau dan menekan populasi kutu kebul, thrips, dan kutu daun bersayap secara mekanis.",
                    howToApply = "Gantung lembaran kuning berperekat setinggi pucuk tanaman (10–20 cm di atas tajuk).",
                    suitableStage = "Vegetatif hingga Berbuah",
                    frequency = "Ganti lembaran setiap 2–3 minggu atau saat penuh serangga.",
                    precautions = "Tempatkan secukupnya agar tidak terlalu banyak menjebak serangga penyerbuk.",
                    expectedResponse = "Membantu deteksi dini serangan hama penghisap pembawa virus keriting.",
                    referenceTitle = "Pengendalian Hama Terpadu (PHT) Tanaman Sayuran — FAO / Kementan",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk Mineral Berimbang (NPK 16-16-16 / KNO3)",
                    nutrientComposition = "Nitrogen, Fosfor, Kalium sesuai kebutuhan fase tumbuh.",
                    applicationMethod = "Kocor encer di sekeliling tajuk atau tugal berjarak 5–7 cm dari batang.",
                    timing = "Fase vegetatif (NPK berimbang) dan awal pembungaan (lebih tinggi K).",
                    dosageGuidance = "Ikuti takaran label kemasan pupuk untuk tanaman hortikultura wadah/lahan; jangan melebihi dosis anjuran.",
                    precautionsAndPpe = "Hindari menabur butiran tepat di pangkal batang dan jangan memupuk saat media kering kerontang.",
                    withholdingAndReentry = "Tidak ada masa henti khusus untuk pupuk akar, namun cuci tangan setelah aplikasi.",
                    compatibilityNotes = "Jangan dicampur langsung dengan larutan bersifat sangat basa atau kalsium pekat dalam satu tangki.",
                    referenceTitle = "Rekomendasi Pemupukan Berimbang Tanaman Cabai — Kementan RI",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf(
                "Periksa balik daun muda seminggu 2 kali untuk mendeteksi kutu daun atau tungau.",
                "Catat kondisi kelembapan media setiap sebelum menyiram."
            ),
            references = listOf(
                KnowledgeReference(
                    title = "Panduan Teknis Budidaya Cabai Sehat & PHT",
                    institution = "Balai Penelitian Tanaman Sayuran (Balitsa)",
                    identifier = "Kementan-Balitsa-Cabai",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi Hortikultura",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "tomat",
            commonName = "Tomat",
            scientificName = "Solanum lycopersicum",
            localNames = listOf("Tomat sayur", "Ranti"),
            category = "Sayuran Buah",
            imageResKey = "img_plant_tomat",
            summary = "Tanaman buah kaya likopen yang membutuhkan ajir penopang, sirkulasi udara baik, dan penyiraman teratur di pangkal.",
            characteristics = "Batang berbulu halus, daun majemuk menyirip, bunga kuning berkelompok, buah bulat berdaging berair.",
            environment = "Tumbuh optimal di dataran menengah–tinggi, namun varietas hibrida dataran rendah sangat adaptif di pekarangan.",
            sunlight = "Sinar matahari penuh 6–8 jam per hari.",
            growingMedia = "Tanah gembur + kompos matang + sekam bakar (2:1:1) dengan drainase cepat.",
            wateringPrinciple = "Siram langsung ke media di pangkal batang (hindari membasahi daun di sore hari untuk mencegah hawar daun).",
            harvestEstimateDays = "65–85 hari setelah tanam",
            toxicityNote = "Daun dan batang mengandung solanin/tomatin ringan; bagian yang dikonsumsi hanya buah matang.",
            stages = buildStandardStages(
                cropName = "Tomat",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah subur, kompos matang, dan sekam bakar (2:1:1)",
                seedPrepTip = "Semai benih tomat di media lembap tanpa perendaman lama agar benih tidak berjamur.",
                seedSoakRecommended = false,
                germinationDays = "5–8 hari",
                spacing = "Pot/polybag 35–40 cm atau jarak 50x60 cm dengan ajir tinggi 120–150 cm",
                vegetativeWaterTip = "Pasang ajir sejak dini agar tidak melukai akar saat tanaman membesar.",
                vegetativeNutrientOrganic = "Tambahkan kompos matang dan mulsa jerami tipis; pangkas tunas air di ketiak daun bawah.",
                vegetativeNutrientNonOrganic = "Berikan pupuk berimbang dosis ringan berkala dan perhatikan kecukupan kalsium saat menjelang berbunga.",
                floweringOrLeafTip = "Bunga kuning muncul berkelompok; getarkan ringan tangkai bunga di pagi hari untuk membantu penyerbukan di teras.",
                fruitingOrMaturationTip = "Jaga kelembapan media tetap konsisten guna mencegah busuk ujung buah (blossom-end rot) dan buah pecah.",
                harvestIndicator = "Buah berwarna merah-jingga merata dan terasa padat namun sedikit lentur saat disentuh.",
                harvestWindow = "65–85 HST",
                postHarvestTip = "Buang sisa batang dan daun tomat keluar area tanam dan rotasikan dengan tanaman kacang-kacangan atau bayam."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Mulsa Jerami / Daun Kering + Pemangkasan Daun Bawah",
                    purpose = "Mencegah percikan spora jamur tanah ke daun bawah saat hujan atau penyiraman.",
                    howToApply = "Tutup permukaan media setebal 2–3 cm dan pangkas daun tua 15 cm dari permukaan tanah.",
                    suitableStage = "Vegetatif hingga Berbuah",
                    frequency = "Sekali pasang dan rapikan tiap 2 minggu.",
                    precautions = "Pastikan mulsa tidak menempel rapat pada pangkal batang yang basah.",
                    expectedResponse = "Daun bagian bawah lebih kering dan serangan bercak daun berkurang signifikan.",
                    referenceTitle = "Manajemen Penyakit Daun Tomat Berbasis Sanitasi — Balitsa",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Suplemen Kalsium (misal Kalsium Nitrat / Kapur Pertanian)",
                    nutrientComposition = "Kalsium (Ca) untuk kekuatan dinding sel ujung buah.",
                    applicationMethod = "Aplikasikan pada media tanam sambil menjaga penyiraman teratur.",
                    timing = "Menjelang pembungaan hingga pembesaran buah awal.",
                    dosageGuidance = "Ikuti dosis anjuran pada kemasan produk resmi.",
                    precautionsAndPpe = "Kalsium tidak terserap bila media terlalu kering atau terlalu asam; perbaiki jadwal air terlebih dahulu.",
                    withholdingAndReentry = "Cuci buah bersih sebelum dikonsumsi.",
                    compatibilityNotes = "Jangan dicampur pekat dengan pupuk fosfat atau sulfat dalam satu wadah.",
                    referenceTitle = "Pencegahan Blossom-End Rot pada Tomat — Hortikultura Indonesia",
                    evidenceLevel = EvidenceLevel.B
                )
            ),
            monitoringTips = listOf(
                "Selalu siram di pangkal, bukan dari atas daun.",
                "Periksa apakah ujung buah mulai menghitam (tanda ketidakseimbangan air-kalsium)."
            ),
            references = listOf(
                KnowledgeReference(
                    title = "Budidaya Tomat Dataran Rendah & Penanganan Hama Penyakit",
                    institution = "Kementerian Pertanian RI",
                    identifier = "Kementan-Tomat-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Penyuluh Hortikultura",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "terong",
            commonName = "Terong Ungu",
            scientificName = "Solanum melongena",
            localNames = listOf("Terung", "Terong lalap"),
            category = "Sayuran Buah",
            imageResKey = "img_plant_cabai",
            summary = "Tanaman sayuran buah yang tangguh di cuaca hangat, berproduksi lama, dan cocok untuk pot besar maupun bedengan.",
            characteristics = "Daun lebar berbulu halus, bunga ungu berbentuk bintang, buah silindris mengilap ungu atau hijau.",
            environment = "Sangat menyukai iklim hangat 25–32°C di dataran rendah hingga menengah.",
            sunlight = "Sinar matahari penuh 6–8 jam per hari.",
            growingMedia = "Tanah subur kaya bahan organik + kompos matang + sekam (2:2:1).",
            wateringPrinciple = "Terong berdaun lebar sehingga penguapan lebih cepat; cek kelembapan media pagi/sore dan siram merata tanpa menggenang.",
            harvestEstimateDays = "65–80 hari setelah pindah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Terong",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah subur, kompos matang, dan sekam (2:2:1)",
                seedPrepTip = "Rendam benih terong 4 jam dalam air hangat kuku untuk mempercepat perkecambahan.",
                seedSoakRecommended = true,
                germinationDays = "7–10 hari",
                spacing = "Polybag 40 cm atau jarak 60x70 cm di lahan",
                vegetativeWaterTip = "Daun lebar mudah kehilangan air di siang terik; gunakan mulsa organik.",
                vegetativeNutrientOrganic = "Tambahkan kompos matang dan pupuk organik cair tiap 10–14 hari.",
                vegetativeNutrientNonOrganic = "Berikan pupuk berimbang dosis bertahap sesuai fase vegetatif.",
                floweringOrLeafTip = "Pangkas tunas samping di bawah cabang utama pertama (cabang Y) agar energi fokus ke buah.",
                fruitingOrMaturationTip = "Amati pangkal buah dan pucuk dari serangan penggerek buah serta kutu kebul di balik daun.",
                harvestIndicator = "Kulit buah mengilap cerah, daging terasa empuk-padat saat ditekan ringan, belum terlalu tua berbiji keras.",
                harvestWindow = "65–80 HST (panen rutin tiap 4–5 hari)",
                postHarvestTip = "Jangan tanam terong di media bekas cabai atau tomat tanpa istirahat media dan kompos baru."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Sanitasi Buah Terserang Penggerek & Pemangkasan Daun Tua",
                    purpose = "Memutus siklus larva penggerek pucuk/buah dan menekan kelembapan berlebih.",
                    howToApply = "Petik buah berlubang atau pucuk layu dan musnahkan di luar kebun.",
                    suitableStage = "Vegetatif hingga Panen",
                    frequency = "Setiap kali inspeksi 2–3 hari sekali.",
                    precautions = "Jangan membuang buah berulat ke tumpukan kompos terbuka.",
                    expectedResponse = "Penurunan serangan buah busuk dan berlubang pada panen berikutnya.",
                    referenceTitle = "Panduan PHT Tanaman Terong — Balitsa",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK Majemuk Berimbang",
                    nutrientComposition = "Nitrogen, Fosfor, dan Kalium seimbang.",
                    applicationMethod = "Tabur melingkar berjarak 8 cm dari batang lalu tutup tanah tipis dan siram.",
                    timing = "Setiap 14 hari pada fase aktif tumbuh hingga awal panen.",
                    dosageGuidance = "Sesuaikan dengan takaran kemasan pupuk pertanian resmi.",
                    precautionsAndPpe = "Hindari butiran mengenai daun basah agar tidak terbakar.",
                    withholdingAndReentry = "Cuci tangan setelah pemupukan.",
                    compatibilityNotes = "Kombinasikan dengan bahan organik agar kapasitas tukar kation tanah terjaga.",
                    referenceTitle = "Budidaya Terong Produktif — Kementan RI",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Periksa balik daun lebar dari kutu kebul dan kumbang pemakan daun."),
            references = listOf(
                KnowledgeReference(
                    title = "Teknologi Budidaya Terong",
                    institution = "Balitsa Kementan",
                    identifier = "Balitsa-Terong-2022",
                    year = "2022",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "kangkung",
            commonName = "Kangkung Darat",
            scientificName = "Ipomoea reptans",
            localNames = listOf("Kangkung cabut", "Kangkung darat"),
            category = "Sayuran Daun",
            imageResKey = "img_plant_pakcoy",
            summary = "Sayuran daun super cepat panen (21–30 hari) yang sangat cocok bagi pemula di pot, planter box, maupun bedengan.",
            characteristics = "Batang berongga, daun memanjang runcing hijau segar, tumbuh sangat cepat pada kondisi cukup air dan cahaya.",
            environment = "Adaptif di hampir seluruh wilayah Indonesia (20–34°C).",
            sunlight = "Sinar matahari penuh 5–7 jam per hari.",
            growingMedia = "Tanah gembur + kompos matang (1:1) agar batang renyah dan cepat besar.",
            wateringPrinciple = "Menyukai media lembap merata namun tidak tergenang busuk; cek kelembapan tiap hari terutama pada pot dangkal.",
            harvestEstimateDays = "21–30 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Kangkung Darat",
                sunlight = "sinar matahari 5–7 jam/hari",
                media = "tanah gembur dan kompos matang (1:1)",
                seedPrepTip = "Benih kangkung dapat langsung ditanam di media lembap atau direndam singkat 2–3 jam.",
                seedSoakRecommended = true,
                germinationDays = "3–5 hari",
                spacing = "Tebar larikan jarak 10–15 cm, 2–3 benih per lubang",
                vegetativeWaterTip = "Air cukup membantu batang tetap renyah dan tidak cepat berserat.",
                vegetativeNutrientOrganic = "Cukup mengandalkan kompos matang dasar; bila perlu tambahkan pupuk organik cair encer di hari ke-10.",
                vegetativeNutrientNonOrganic = "Jika media miskin hara, dosis sangat ringan pupuk nitrogen hanya di hari ke-10, hindari menjelang panen.",
                floweringOrLeafTip = "Pada kangkung cabut, tanaman dipanen pada fase vegetatif sebelum berbunga.",
                fruitingOrMaturationTip = "Pastikan daun tetap mulus dan bebas ulat pemakan daun menjelang panen.",
                harvestIndicator = "Tinggi tanaman 20–30 cm dengan batang muda renyah sebelum muncul bunga.",
                harvestWindow = "21–30 HST",
                postHarvestTip = "Dapat dipanen potong sisakan 3–5 cm pangkal untuk panen kedua, atau cabut lalu gemburkan media dengan kompos baru."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Kompos Halus Matang sebagai Media Utama",
                    purpose = "Menyediakan nutrisi nitrogen organik pelepas lambat selama siklus pendek 25 hari.",
                    howToApply = "Campur merata 50% tanah gembur dan 50% kompos matang sebelum tanam.",
                    suitableStage = "Persiapan media",
                    frequency = "Sekali setiap siklus tanam.",
                    precautions = "Pastikan kompos terbebas dari biji gulma.",
                    expectedResponse = "Pertumbuhan seragam, daun hijau segar dan renyah.",
                    referenceTitle = "Budidaya Sayuran Daun Pekarangan — Kementan",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk Urea / Nitrogen Dosis Rendah (jika media kurang subur)",
                    nutrientComposition = "Nitrogen (N) untuk pembentukan daun.",
                    applicationMethod = "Larutkan sangat encer atau tabur tipis di sela barisan pada umur 10 hari.",
                    timing = "Hari ke-10 setelah tanam saja.",
                    dosageGuidance = "Gunakan dosis minimal sesuai label sayuran daun; jangan memupuk 10 hari menjelang panen.",
                    precautionsAndPpe = "Pemupukan N berlebihan membuat daun cepat layu pascapanen dan rentan ulat.",
                    withholdingAndReentry = "Berhenti memupuk minimal 7–10 hari sebelum panen.",
                    compatibilityNotes = "Pastikan media disiram setelah aplikasi butiran.",
                    referenceTitle = "Pedoman Pemupukan Sayuran Daun Cepat Panen",
                    evidenceLevel = EvidenceLevel.C
                )
            ),
            monitoringTips = listOf("Cek lubang daun akibat ulat atau belalang di pagi/sore hari."),
            references = listOf(
                KnowledgeReference(
                    title = "Panduan Sayuran Daun Cepat Panen di Pekarangan",
                    institution = "BPTP / Kementan RI",
                    identifier = "Kementan-Kangkung-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Hortikultura",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "bayam",
            commonName = "Bayam Hijau / Merah",
            scientificName = "Amaranthus tricolor",
            localNames = listOf("Bayam cabut", "Bayam petik"),
            category = "Sayuran Daun",
            imageResKey = "img_plant_pakcoy",
            summary = "Sayuran daun bergizi tinggi yang mudah tumbuh dari biji dalam 25–30 hari tanpa perawatan rumit.",
            characteristics = "Batang lunak hijau atau kemerahan, daun oval lembut, sangat responsif terhadap sinar matahari.",
            environment = "Cocok di dataran rendah maupun menengah dengan suhu hangat.",
            sunlight = "Sinar matahari penuh 5–7 jam per hari.",
            growingMedia = "Tanah gembur + kompos halus matang (2:1) berdrainase baik.",
            wateringPrinciple = "Siram dengan semprotan halus agar bibit kecil tidak roboh; hindari media tergenang di fase kecambah.",
            harvestEstimateDays = "25–30 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Bayam",
                sunlight = "sinar matahari 5–7 jam/hari",
                media = "tanah gembur dan kompos halus (2:1)",
                seedPrepTip = "Campur biji bayam yang sangat kecil dengan sedikit pasir kering/kompos halus saat menebar agar tidak menumpuk.",
                seedSoakRecommended = false,
                germinationDays = "3–5 hari",
                spacing = "Tebar tipis merata lalu jarangkan agar berjarak 5–8 cm antar tanaman",
                vegetativeWaterTip = "Gunakan gembor lubang halus saat menyiram.",
                vegetativeNutrientOrganic = "Cukup dengan media awal kaya kompos matang dan penjarangan agar tanaman leluasa tumbuh.",
                vegetativeNutrientNonOrganic = "Aplikasi nutrisi ringan hanya bila pertumbuhan kerdil dan daun pucat merata.",
                floweringOrLeafTip = "Panen sebelum tanaman mulai membentuk malai bunga karena daun akan menjadi keras.",
                fruitingOrMaturationTip = "Jaga kebersihan gulma karena gulma mudah menutupi bibit bayam muda.",
                harvestIndicator = "Tinggi 20–25 cm dengan 6–8 daun lebar segar.",
                harvestWindow = "25–30 HST",
                postHarvestTip = "Setelah panen cabut, jemur media 3–5 hari dan tambah kompos baru sebelum menanam sawi atau kacang."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Penjarangan Bibit & Kompos Halus",
                    purpose = "Mencegah persaingan cahaya dan mencegah rebah semai akibat kelembapan terlalu rapat.",
                    howToApply = "Cabut bibit yang terlalu rapat saat umur 10 hari dan sisakan jarak 5–8 cm.",
                    suitableStage = "Vegetatif awal",
                    frequency = "Satu kali saat tinggi bibit 5 cm.",
                    precautions = "Siram media sedikit sebelum penjarangan agar akar tetangga tidak ikut terangkat.",
                    expectedResponse = "Batang bayam lebih kokoh dan daun lebih lebar.",
                    referenceTitle = "Teknik Budidaya Bayam Pekarangan — BPTP",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK Berimbang Dosis Ringan",
                    nutrientComposition = "Nitrogen, Fosfor, Kalium seimbang.",
                    applicationMethod = "Kocor encer di media pada umur 10–12 hari bila tanah miskin hara.",
                    timing = "Sekali di awal vegetatif.",
                    dosageGuidance = "Ikuti dosis anjuran minimum label untuk sayuran daun.",
                    precautionsAndPpe = "Bilas dengan air bersih jika larutan mengenai daun muda.",
                    withholdingAndReentry = "Minimal 10 hari sebelum panen.",
                    compatibilityNotes = "Tidak diperlukan bila kompos dasar sudah cukup subur.",
                    referenceTitle = "Pedoman Sayuran Daun — Kementan",
                    evidenceLevel = EvidenceLevel.C
                )
            ),
            monitoringTips = listOf("Amati bercak putih (karat putih) pada permukaan bawah daun bila cuaca sangat lembap."),
            references = listOf(
                KnowledgeReference(
                    title = "Budidaya Bayam Cabut Sehat",
                    institution = "Kementerian Pertanian RI",
                    identifier = "Kementan-Bayam-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Hortikultura",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "sawi",
            commonName = "Sawi Hijau (Caisim)",
            scientificName = "Brassica juncea",
            localNames = listOf("Caisim", "Sawi bakso", "Tosakan"),
            category = "Sayuran Daun",
            imageResKey = "img_plant_pakcoy",
            summary = "Sayuran daun populer untuk masakan sehari-hari yang cepat tumbuh dalam 30–40 hari di polybag maupun bedengan.",
            characteristics = "Daun lebar tipis berwarna hijau cerah dengan tangkai ramping tegak.",
            environment = "Tumbuh baik di dataran rendah maupun tinggi dengan sirkulasi udara baik.",
            sunlight = "Sinar matahari 5–6 jam per hari (toleran naungan ringan di siang sangat terik).",
            growingMedia = "Tanah gembur + kompos matang + sekam bakar (2:2:1).",
            wateringPrinciple = "Cek kelembapan tiap hari; daun sawi cepat layu jika media kering, tetapi akarnya cepat busuk bila tergenang.",
            harvestEstimateDays = "30–40 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Sawi Hijau",
                sunlight = "sinar matahari 5–6 jam/hari",
                media = "tanah gembur, kompos matang, dan sekam bakar (2:2:1)",
                seedPrepTip = "Semai benih sawi di tray semai atau pot kecil sebelum dipindah tanam saat berdaun 4 helai.",
                seedSoakRecommended = false,
                germinationDays = "3–5 hari",
                spacing = "Jarak 15–20 cm antar tanaman",
                vegetativeWaterTip = "Jaga kelembapan seragam agar tangkai daun tidak berserat.",
                vegetativeNutrientOrganic = "Tambahkan kompos matang di sela tanaman 10 hari setelah pindah tanam.",
                vegetativeNutrientNonOrganic = "Pupuk berimbang dosis ringan di minggu ke-2 bila daun tumbuh lambat.",
                floweringOrLeafTip = "Panen saat tajuk maksimal sebelum keluar tangkai bunga kuning.",
                fruitingOrMaturationTip = "Waspadai ulat daun kubis (Plutella) dan siput terutama saat musim hujan.",
                harvestIndicator = "Daun bawah mulai penuh dan tangkai daun tampak renyah berisi (30–40 HST).",
                harvestWindow = "30–40 HST",
                postHarvestTip = "Rotasikan dengan tanaman non-Brassicaceae (misalnya kangkung atau selada) untuk mencegah hama akar gada."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Pengumpulan Mekanis Ulat & Jaring Halus (Insect Net)",
                    purpose = "Melindungi daun muda sawi dari kupu-kupu putih bertelur dan ulat pemakan daun.",
                    howToApply = "Pasang sungkup jaring halus atau periksa bawah daun secara manual di pagi hari.",
                    suitableStage = "Vegetatif",
                    frequency = "Inspeksi tiap 2 hari.",
                    precautions = "Pastikan jaring tetap memungkinkan cahaya matahari masuk optimal.",
                    expectedResponse = "Daun tetap mulus tanpa lubang besar dan bebas residu kimia.",
                    referenceTitle = "Budidaya Sayuran Daun Bebas Pestisida — Balitsa",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK Seimbang Dosis Ringan",
                    nutrientComposition = "Nitrogen, Fosfor, Kalium seimbang.",
                    applicationMethod = "Kocor encer di pangkal akar saat umur 10–14 hari.",
                    timing = "Vegetatif awal.",
                    dosageGuidance = "Ikuti takaran kemasan untuk sayuran daun.",
                    precautionsAndPpe = "Jangan menabur pupuk di pucuk daun karena dapat membakar titik tumbuh.",
                    withholdingAndReentry = "Hentikan pemupukan minimal 10 hari sebelum panen.",
                    compatibilityNotes = "Pastikan pH media >= 6,0 untuk menekan risiko penyakit akar gada.",
                    referenceTitle = "Panduan Pemupukan Sawi/Caisim — Kementan",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Cek bawah daun dari telur kupu-kupu dan ulat hijau kecil."),
            references = listOf(
                KnowledgeReference(
                    title = "Budidaya Caisim Sehat",
                    institution = "Balitsa Kementan",
                    identifier = "Balitsa-Sawi-2022",
                    year = "2022",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "pakcoy",
            commonName = "Pakcoy",
            scientificName = "Brassica rapa subsp. chinensis",
            localNames = listOf("Bok choy", "Sawi sendok"),
            category = "Sayuran Daun",
            imageResKey = "img_plant_pakcoy",
            summary = "Sayuran daun bertangkai tebal renyah berbentuk sendok, sangat ideal ditanam di planter box atau polybag teras.",
            characteristics = "Membentuk roset daun hijau tua mengilap dengan pangkal tangkai putih kehijauan yang lebar dan tebal.",
            environment = "Suhu sejuk–hangat 20–28°C; naungan paranet ringan membantu bila ditanam di dataran rendah yang sangat panas.",
            sunlight = "Sinar matahari 4–6 jam per hari (cahaya pagi paling baik).",
            growingMedia = "Kompos matang + cocopeat/sekam bakar + tanah gembur (2:1:1).",
            wateringPrinciple = "Siram secukupnya di sekitar media tanpa menggenangi pangkal roset daun agar tidak memicu busuk lunak.",
            harvestEstimateDays = "28–35 hari setelah pindah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Pakcoy",
                sunlight = "sinar matahari pagi 4–6 jam/hari",
                media = "kompos matang, sekam bakar, dan tanah gembur (2:1:1)",
                seedPrepTip = "Semai di tray agar akar utuh saat pindah tanam pada umur 10–14 hari (4 daun sejati).",
                seedSoakRecommended = false,
                germinationDays = "3–5 hari",
                spacing = "Jarak 15–20 cm dalam planter box atau 1 tanaman per polybag 20 cm",
                vegetativeWaterTip = "Jangan siram menggenang di tengah mahkota roset daun.",
                vegetativeNutrientOrganic = "Beri top-dress kompos saring halus 1 minggu setelah pindah tanam.",
                vegetativeNutrientNonOrganic = "Berikan nutrisi mineral ringan 10 hari setelah tanam bila media miskin hara.",
                floweringOrLeafTip = "Pakcoy dipanen sebelum batang memanjang membentuk bunga.",
                fruitingOrMaturationTip = "Jaga pangkal roset tetap bersih dari tanah cipratan untuk mencegah busuk pangkal.",
                harvestIndicator = "Pangkal tangkai putih-hijau melebar padat dan tinggi tanaman 18–22 cm.",
                harvestWindow = "28–35 HST",
                postHarvestTip = "Setelah panen, gemburkan media, jemur 5 hari, dan tambahkan kompos matang baru."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Top-Dress Kompos Matang + Mulsa Sekam Bakar",
                    purpose = "Menjaga kelembapan stabil dan mencegah cipratan tanah ke pangkal roset daun.",
                    howToApply = "Tabur kompos matang di sela tanaman lalu lapisi tipis sekam bakar di permukaan.",
                    suitableStage = "Vegetatif",
                    frequency = "Sekali di minggu pertama setelah pindah tanam.",
                    precautions = "Jangan menimbun titik tumbuh di pusat roset.",
                    expectedResponse = "Pangkal batang bersih, renyah, dan terhindar dari busuk lunak pangkal.",
                    referenceTitle = "Panduan Budidaya Pakcoy Urban Farming — Kementan",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Larutan Nutrisi Berimbang / NPK Encer",
                    nutrientComposition = "Unsur hara makro berimbang untuk pembentukan daun tebal.",
                    applicationMethod = "Kocor di media tepi wadah, bukan di pusat mahkota daun.",
                    timing = "Minggu ke-2 setelah pindah tanam.",
                    dosageGuidance = "Ikuti takaran kemasan pupuk hortikultura dosis ringan.",
                    precautionsAndPpe = "Hindari pemupukan berlebih yang menyebabkan ujung daun terbakar (tipburn).",
                    withholdingAndReentry = "Berhenti memupuk minimal 10 hari sebelum panen.",
                    compatibilityNotes = "Perhatikan suhu udara agar serapan kalsium lancar.",
                    referenceTitle = "Manajemen Nutrisi Pakcoy — Hortikultura",
                    evidenceLevel = EvidenceLevel.C
                )
            ),
            monitoringTips = listOf("Periksa pangkal tangkai daun dari tanda busuk basah atau lendir berbau."),
            references = listOf(
                KnowledgeReference(
                    title = "Budidaya Pakcoy Pekarangan",
                    institution = "BPTP Jakarta / Kementan",
                    identifier = "BPTP-Pakcoy-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Urban Farming",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "selada",
            commonName = "Selada Hijau / Keriting",
            scientificName = "Lactuca sativa",
            localNames = listOf("Selada keriting", "Lettuce"),
            category = "Sayuran Daun",
            imageResKey = "img_plant_pakcoy",
            summary = "Sayuran lalapan dan salad yang menyukai suhu agak sejuk, cahaya pagi, dan media sangat gembur.",
            characteristics = "Daun bergelombang renyah tanpa batang keras, rasa segar sedikit manis bila tidak kepanasan.",
            environment = "Suhu optimal 18–26°C; di dataran rendah disarankan mendapat naungan siang.",
            sunlight = "Sinar matahari pagi 4–5 jam per hari; hindari terik siang ekstrem agar daun tidak pahit.",
            growingMedia = "Sekam bakar + kompos matang + tanah gembur (1:1:1) yang sangat porus.",
            wateringPrinciple = "Siram media di pagi hari secukupnya agar kelembapan terjaga tanpa membuat daun lembek.",
            harvestEstimateDays = "35–45 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Selada",
                sunlight = "cahaya matahari pagi 4–5 jam (teduh parsial siang)",
                media = "kompos matang, sekam bakar, dan tanah gembur (1:1:1)",
                seedPrepTip = "Benih selada butuh sedikit cahaya untuk berkecambah; tekan lembut ke permukaan media tanpa menimbun terlalu dalam.",
                seedSoakRecommended = false,
                germinationDays = "4–7 hari",
                spacing = "Jarak 20x20 cm agar roset daun terbuka bebas",
                vegetativeWaterTip = "Panas siang membuat daun cepat pahit dan berbunga dini (bolting).",
                vegetativeNutrientOrganic = "Gunakan kompos matang saring atau vermikompos (kascing) agar nutrisi lembut dan merata.",
                vegetativeNutrientNonOrganic = "Gunakan pupuk berimbang dosis sangat encer hanya jika daun tampak pucat.",
                floweringOrLeafTip = "Panen segera bila batang tengah mulai memanjang ke atas.",
                fruitingOrMaturationTip = "Jaga ujung daun dari tipburn dengan penyiraman konsisten di pagi hari.",
                harvestIndicator = "Roset daun padat selebar 18–25 cm, renyah, sebelum muncul batang bunga.",
                harvestWindow = "35–45 HST",
                postHarvestTip = "Cuci media pot dan tambahkan kompos baru sebelum penanaman ulang."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Vermikompos (Kascing) + Naungan Paranet 40–50%",
                    purpose = "Menjaga suhu media tetap sejuk dan menekan rasa pahit serta ujung daun terbakar.",
                    howToApply = "Campurkan vermikompos 20% dalam media dan pasang peneduh parsial untuk terik siang.",
                    suitableStage = "Pembibitan hingga Panen",
                    frequency = "Aplikasi awal media dan penyiraman pagi teratur.",
                    precautions = "Jangan memindahkan selada ke tempat gelap total karena daun akan lemas.",
                    expectedResponse = "Daun selada lebih manis, renyah, dan tepi daun tidak mengering.",
                    referenceTitle = "Budidaya Selada Ramah Lingkungan — Balitsa",
                    validationStatus = "Expert-reviewed",
                    evidenceLevel = EvidenceLevel.C
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk Kalsium & NPK Dosis Sangat Encer",
                    nutrientComposition = "Makro seimbang plus kalsium.",
                    applicationMethod = "Kocor media di pagi hari.",
                    timing = "Minggu ke-2 setelah pindah tanam.",
                    dosageGuidance = "Gunakan setengah dari dosis umum sayuran buah sesuai panduan kemasan.",
                    precautionsAndPpe = "Larutan terlalu pekat membakar akar halus selada.",
                    withholdingAndReentry = "Berhenti minimal 10 hari sebelum panen.",
                    compatibilityNotes = "Utamakan kelancaran aliran air dan suhu sejuk.",
                    referenceTitle = "Manajemen Tipburn pada Selada",
                    evidenceLevel = EvidenceLevel.B
                )
            ),
            monitoringTips = listOf("Cek tepi daun muda dari gejala mengering kecokelatan (tipburn)."),
            references = listOf(
                KnowledgeReference(
                    title = "Panduan Budidaya Selada Segar",
                    institution = "Balitsa Kementan",
                    identifier = "Balitsa-Selada-2022",
                    year = "2022",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "bawang_merah",
            commonName = "Bawang Merah",
            scientificName = "Allium cepa var. aggregatum",
            localNames = listOf("Brambang", "Bawang abang"),
            category = "Umbi & Bumbu",
            imageResKey = "img_plant_cabai",
            summary = "Tanaman umbi lapis bumbu utama yang menyukai sinar matahari melimpah, media sangat porus, dan tidak tahan genangan.",
            characteristics = "Daun silindris berongga hijau kebiruan, membentuk rumpun umbi lapis kemerahan di permukaan tanah.",
            environment = "Optimal pada musim kemarau atau teras kering yang mendapat sinar matahari penuh.",
            sunlight = "Sinar matahari penuh minimal 7–8 jam per hari.",
            growingMedia = "Tanah berpasir/gembur + kompos matang + sekam bakar (2:1:1) dengan drainase cepat.",
            wateringPrinciple = "Siram terukur di pagi hari; kurangi penyiraman 7–10 hari menjelang panen agar umbi kering dan tahan simpan.",
            harvestEstimateDays = "60–70 hari setelah tanam umbi",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Bawang Merah",
                sunlight = "sinar matahari penuh 7–8 jam/hari",
                media = "tanah gembur berpasir, kompos matang, dan sekam bakar (2:1:1)",
                seedPrepTip = "Pilih umbi bibit sehat padat yang sudah disimpan 2–3 bulan; potong tipis 1/4 bagian ujung atas umbi agar tunas cepat seragam.",
                seedSoakRecommended = false,
                germinationDays = "4–7 hari (tumbuh tunas daun)",
                spacing = "Jarak 15x15 cm; benamkan umbi hanya 3/4 bagian sehingga ujungnya rata permukaan media",
                vegetativeWaterTip = "Jangan biarkan media tergenang agar umbi tidak membusuk.",
                vegetativeNutrientOrganic = "Berikan kompos sangat matang sebelum tanam dan abu sekam kaya kalium di minggu ke-3.",
                vegetativeNutrientNonOrganic = "Fokus unsur fosfor dan kalium di minggu ke-3 s/d ke-5 untuk pembesaran umbi.",
                floweringOrLeafTip = "Pada budidaya umbi, jaga daun tetap tegak hijau dan bebas bercak ungu (Alternaria).",
                fruitingOrMaturationTip = "Pembesaran umbi terjadi pada hari ke-35 hingga ke-55; hindari nitrogen berlebih pada fase ini.",
                harvestIndicator = "60–70% daun mulai rebah menguning secara alami dan leher umbi melunak.",
                harvestWindow = "60–70 HST",
                postHarvestTip = "Jemur umbi hasil panen beserta daunnya 5–7 hari di tempat berventilasi hingga kulit mengering."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Abu Sekam + Sanitasi Ujung Daun Terinfeksi",
                    purpose = "Menambah kalium alami dan mencegah penyebaran spora bercak ungu.",
                    howToApply = "Taburkan abu sekam di antara barisan pada umur 25 hari dan gunting ujung daun yang kering berbintik ungu.",
                    suitableStage = "Vegetatif & Pembesaran umbi",
                    frequency = "Tabur 1–2 kali selama siklus.",
                    precautions = "Gunakan gunting bersih dan jangan menyiram daun di sore hari.",
                    expectedResponse = "Umbi lebih padat dan penyebaran bercak daun melambat.",
                    referenceTitle = "Teknologi Budidaya Bawang Merah — Balitsa",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk KCl / ZK (Kalium) & SP-36",
                    nutrientComposition = "Fosfor dan Kalium untuk pembentukan serta kepadatan umbi.",
                    applicationMethod = "Tabur di larikan sela tanaman dan segera siram secukupnya.",
                    timing = "Pupuk dasar dan susulan umur 25–30 HST.",
                    dosageGuidance = "Ikuti anjuran dosis spesifik bawang merah pada label resmi.",
                    precautionsAndPpe = "Hindari menaburkan pupuk di atas umbi langsung agar tidak membakar leher umbi.",
                    withholdingAndReentry = "Tidak memupuk 2 minggu sebelum panen.",
                    compatibilityNotes = "Drainase buruk membuat pupuk mineral tidak terserap efektif.",
                    referenceTitle = "Rekomendasi Pemupukan Bawang Merah — Kementan",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Periksa bagian dalam daun berongga apakah ada ulat bawang atau bercak ungu konsentris."),
            references = listOf(
                KnowledgeReference(
                    title = "Panduan Teknis Budidaya Bawang Merah",
                    institution = "Balitsa Kementan",
                    identifier = "Balitsa-BawangMerah-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi Umbi",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "bawang_putih",
            commonName = "Bawang Putih",
            scientificName = "Allium sativum",
            localNames = listOf("Bawang bodas"),
            category = "Umbi & Bumbu",
            imageResKey = "img_plant_cabai",
            summary = "Tanaman umbi aromatik dataran tinggi/sejuk yang membutuhkan tanah sangat gembur dan siklus tanam lebih panjang.",
            characteristics = "Daun pipih memanjang seperti pita, umbi terdiri dari beberapa siung dalam selaput putih.",
            environment = "Optimal di daerah sejuk dataran menengah–tinggi (15–24°C).",
            sunlight = "Sinar matahari penuh 6–7 jam per hari.",
            growingMedia = "Tanah sangat gembur + kompos matang + pasir/sekam (2:2:1), drainase sempurna.",
            wateringPrinciple = "Pertahankan media lembap ringan; hentikan penyiraman saat daun mulai mengering menjelang panen.",
            harvestEstimateDays = "90–115 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Bawang Putih",
                sunlight = "sinar matahari penuh 6–7 jam/hari di lokasi sejuk",
                media = "tanah sangat gembur, kompos matang, dan sekam bakar (2:2:1)",
                seedPrepTip = "Pisahkan siung sehat berukuran besar dari umbi utuh; tanam bagian pangkal datar menghadap ke bawah tanpa memotong ujungnya.",
                seedSoakRecommended = false,
                germinationDays = "7–12 hari",
                spacing = "Jarak 15x15 cm, kedalaman 3–4 cm",
                vegetativeWaterTip = "Media berat dan becek membuat siung cepat membusuk.",
                vegetativeNutrientOrganic = "Gunakan kompos matang kaya bahan organik di awal serta mulsa jerami tipis untuk menjaga suhu tanah sejuk.",
                vegetativeNutrientNonOrganic = "Berikan pupuk berimbang dosis ringan di minggu ke-3 dan unsur kalium di minggu ke-7.",
                floweringOrLeafTip = "Jika varietas memunculkan tangkai bunga (scape), pangkas tangkai agar energi penuh masuk ke pembentukan siung.",
                fruitingOrMaturationTip = "Pembentukan siung berlangsung minggu ke-8 hingga panen; jaga bebas gulma.",
                harvestIndicator = "Setengah hingga dua pertiga daun bagian bawah menguning dan mengering alami.",
                harvestWindow = "90–115 HST",
                postHarvestTip = "Gantung ikatan bawang di tempat teduh kering berangin selama 2 minggu untuk proses curing."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Mulsa Jerami Tipis + Rotasi Non-Allium",
                    purpose = "Menjaga kelembapan stabil, menekan gulma, dan menjaga suhu perakaran lebih sejuk.",
                    howToApply = "Tutup sela tanaman dengan jerami kering tipis setebal 2 cm setelah tunas setinggi 10 cm.",
                    suitableStage = "Vegetatif",
                    frequency = "Sekali pasang per musim tanam.",
                    precautions = "Jangan menumpuk mulsa terlalu tebal saat curah hujan tinggi.",
                    expectedResponse = "Siung berkembang lebih besar dengan kulit bersih.",
                    referenceTitle = "Pedoman Pengembangan Bawang Putih — Ditjen Hortikultura",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK & Kalium Susulan",
                    nutrientComposition = "Nitrogen awal, lalu Kalium untuk pengisian siung.",
                    applicationMethod = "Tugal atau larikan di sela barisan.",
                    timing = "Umur 25 HST dan 55 HST.",
                    dosageGuidance = "Ikuti standar budidaya bawang putih sesuai kemasan.",
                    precautionsAndPpe = "Hindari pemupukan N terlambat di atas 60 HST karena memicu pecahnya kulit siung.",
                    withholdingAndReentry = "Berhenti memupuk 30 hari sebelum panen.",
                    compatibilityNotes = "Membutuhkan kesesuaian suhu sejuk untuk membentuk siung penuh.",
                    referenceTitle = "Teknologi Produksi Bawang Putih — Balitsa",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Pantau pangkal batang dari gejala busuk putih atau karat daun oranye."),
            references = listOf(
                KnowledgeReference(
                    title = "Panduan Budidaya Bawang Putih",
                    institution = "Ditjen Hortikultura Kementan",
                    identifier = "Kementan-BawangPutih-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi",
                    lastReviewDate = "2026-10-01"
                )
            )
        )
    )
}
