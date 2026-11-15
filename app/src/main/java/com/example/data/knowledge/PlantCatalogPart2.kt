package com.example.data.knowledge

import com.example.model.EvidenceLevel
import com.example.model.KnowledgeReference
import com.example.model.NonOrganicSolution
import com.example.model.OrganicSolution
import com.example.model.PlantKnowledge

object PlantCatalogPart2 {
    val plants: List<PlantKnowledge> = listOf(
        PlantKnowledge(
            id = "timun",
            commonName = "Timun (Mentimun)",
            scientificName = "Cucumis sativus",
            localNames = listOf("Mentimun", "Ketimun", "Bonteng"),
            category = "Sayuran Buah Merambat",
            imageResKey = "img_hero_garden",
            summary = "Tanaman merambat cepat berbuah (35–45 hari) yang cocok dibudidayakan secara vertikal memakai rambatan jaring di halaman rumah.",
            characteristics = "Batang menjalar berbulu dengan sulur pembelit, daun lebar berlekuk, bunga kuning terpisah jantan dan betina.",
            environment = "Adaptif di dataran rendah hingga menengah (22–32°C).",
            sunlight = "Sinar matahari penuh 6–8 jam per hari.",
            growingMedia = "Tanah gembur + kompos matang + sekam bakar (2:2:1).",
            wateringPrinciple = "Butuh air cukup dan konsisten saat berbunga/berbuah; penyiraman tidak teratur membuat buah terasa pahit.",
            harvestEstimateDays = "35–45 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Timun",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah gembur, kompos matang, dan sekam bakar (2:2:1)",
                seedPrepTip = "Tanam langsung di wadah akhir atau pot semai biodegradable karena akar timun sensitif jika terganggu saat pindah tanam.",
                seedSoakRecommended = true,
                germinationDays = "3–5 hari",
                spacing = "Polybag 35–40 cm (1 tanaman) atau jarak 40x50 cm dengan lanjaran/para-para",
                vegetativeWaterTip = "Pasang ajir atau jaring rambat sebelum sulur panjang.",
                vegetativeNutrientOrganic = "Tambahkan kompos matang tiap 2 minggu di sekeliling pangkal.",
                vegetativeNutrientNonOrganic = "Pupuk NPK berimbang dosis ringan di minggu ke-2 dan ke-4.",
                floweringOrLeafTip = "Bunga jantan muncul lebih dulu; bunga betina memiliki bakal buah mini di pangkalnya.",
                fruitingOrMaturationTip = "Siram teratur di pagi hari agar buah renyah, lurus, dan tidak pahit.",
                harvestIndicator = "Buah berukuran panjang ideal varietas (15–20 cm), kulit hijau segar dengan duri tumpul, sebelum menguning.",
                harvestWindow = "35–45 HST (panen 2–3 hari sekali)",
                postHarvestTip = "Setelah selesai panen, bersihkan rambatan dan rotasikan dengan tanaman bukan famili Cucurbitaceae."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Rambatan Vertikal + Pemangkasan Daun Bawah",
                    purpose = "Menghindari buah kotor menyentuh tanah dan mencegah embun tepung/embun bulu.",
                    howToApply = "Arahkan sulur naik ke jaring rambat dan buang 2–3 daun terbawah yang menua.",
                    suitableStage = "Vegetatif hingga Panen",
                    frequency = "Arahkan sulur 2–3 hari sekali.",
                    precautions = "Jangan memutar paksa sulur kaku agar tidak patah.",
                    expectedResponse = "Buah tumbuh lurus bersih dan daun lebih kering sehat.",
                    referenceTitle = "Budidaya Mentimun Sistem Rambatan — Kementan",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK Seimbang",
                    nutrientComposition = "Nitrogen, Fosfor, Kalium berimbang.",
                    applicationMethod = "Kocor encer di pangkal media.",
                    timing = "Minggu ke-2 dan awal berbunga (minggu ke-4).",
                    dosageGuidance = "Ikuti takaran kemasan pupuk sayuran buah.",
                    precautionsAndPpe = "Jangan memupuk saat media sangat kering.",
                    withholdingAndReentry = "Berhenti 7 hari sebelum panen.",
                    compatibilityNotes = "Pastikan penyerbukan serangga tidak terganggu penyemprotan di pagi hari.",
                    referenceTitle = "Pedoman Mentimun Produktif — Balitsa",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Periksa bagian atas daun dari bercak tepung putih (powdery mildew) dan bagian bawah dari embun bulu kuning."),
            references = listOf(
                KnowledgeReference(
                    title = "Teknologi Budidaya Mentimun",
                    institution = "Balitsa Kementan",
                    identifier = "Balitsa-Timun-2022",
                    year = "2022",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "pare",
            commonName = "Pare (Peria)",
            scientificName = "Momordica charantia",
            localNames = listOf("Peria", "Paria", "Pepare"),
            category = "Sayuran Buah Merambat",
            imageResKey = "img_hero_garden",
            summary = "Tanaman merambat tangguh berbuah unik berkerut yang membutuhkan para-para kokoh dan pembungkusan buah muda.",
            characteristics = "Sulur panjang bercabang banyak, daun berjari lima, bunga kuning harum lembut, buah hijau berjerawat memanjang.",
            environment = "Sangat adaptif di iklim tropis dataran rendah maupun menengah (24–34°C).",
            sunlight = "Sinar matahari penuh 6–8 jam per hari.",
            growingMedia = "Tanah gembur + kompos matang + sekam (2:2:1).",
            wateringPrinciple = "Siram teratur di pangkal terutama saat fase pembesaran buah; toleransi kekeringan sedang namun tidak suka genangan.",
            harvestEstimateDays = "45–55 hari setelah tanam",
            toxicityNote = "Biji pare yang sudah tua/merah sebaiknya dibuang dan tidak dikonsumsi mentah.",
            stages = buildStandardStages(
                cropName = "Pare",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah subur, kompos matang, dan sekam (2:2:1)",
                seedPrepTip = "Kulit biji pare cukup tebal; kikir tipis ujung tumpul biji atau rendam air hangat kuku 6–12 jam sebelum disemai.",
                seedSoakRecommended = true,
                germinationDays = "5–10 hari",
                spacing = "Jarak 60x80 cm dengan para-para tinggi minimal 180 cm",
                vegetativeWaterTip = "Pangkas cabang samping di bawah ketinggian 60 cm agar tajuk atas cepat rindang.",
                vegetativeNutrientOrganic = "Tambahkan kompos matang dan pupuk organik cair rutin tiap 2 minggu.",
                vegetativeNutrientNonOrganic = "Berikan NPK berimbang dosis bertahap sesuai fase vegetatif dan pembungaan.",
                floweringOrLeafTip = "Serangga penyerbuk sangat penting; penyerbukan manual di pagi hari dapat dilakukan jika bunga betina kurang jadi buah.",
                fruitingOrMaturationTip = "Bungkus buah muda (panjang ~5–7 cm) dengan kertas atau plastik berlubang untuk mencegah sengatan lalat buah.",
                harvestIndicator = "Buah masih hijau segar, permukaan alur tampak penuh mengilap sebelum mulai menguning oranye.",
                harvestWindow = "45–55 HST",
                postHarvestTip = "Bersihkan rambatan dan komposkan sisa daun yang sehat."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Pembungkusan Buah Muda + Perangkap Atraktan Lalat Buah",
                    purpose = "Melindungi buah pare muda dari sengatan lalat buah (Bactrocera spp.) tanpa pestisida semprot.",
                    howToApply = "Bungkus buah berumur 3–5 hari setelah mekar menggunakan pembungkus kertas/plastik berlubang ventilasi di bawah.",
                    suitableStage = "Pembentukan buah",
                    frequency = "Setiap kali bakal buah memanjang 5–7 cm.",
                    precautions = "Jangan membungkus rapat tanpa lubang bawah karena dapat memicu pengembunan dan busuk buah.",
                    expectedResponse = "Tingkat buah cacat/berulat menurun drastis.",
                    referenceTitle = "Pengendalian Lalat Buah Secara Mekanis — Ditjen Hortikultura",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK Majemuk & Kalium Tambahan",
                    nutrientComposition = "Makro lengkap untuk menopang masa panen panjang.",
                    applicationMethod = "Benamkan di alur melingkar sekitar pangkal.",
                    timing = "Umur 14 HST, 30 HST, dan saat panen aktif.",
                    dosageGuidance = "Ikuti takaran pada kemasan produk.",
                    precautionsAndPpe = "Jangan mengaplikasikan dekat batang langsung.",
                    withholdingAndReentry = "Berhenti 7 hari sebelum petik.",
                    compatibilityNotes = "Optimal bila disertai cukup bahan organik penahan air.",
                    referenceTitle = "Panduan Budidaya Pare — Kementan",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Amati titik hitam bekas tusukan lalat buah pada buah muda yang belum dibungkus."),
            references = listOf(
                KnowledgeReference(
                    title = "Pedoman Teknis Tanaman Pare",
                    institution = "Kementerian Pertanian RI",
                    identifier = "Kementan-Pare-2022",
                    year = "2022",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Hortikultura",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "kacang_panjang",
            commonName = "Kacang Panjang",
            scientificName = "Vigna unguiculata subsp. sesquipedalis",
            localNames = listOf("Kacang lanjaran", "Kacang tolo panjang"),
            category = "Kacang-kacangan Merambat",
            imageResKey = "img_hero_garden",
            summary = "Tanaman polong merambat yang mampu mengikat nitrogen dari udara dan sangat baik untuk kesuburan tanah serta rotasi kebun.",
            characteristics = "Daun majemuk beranak tiga, sulur membelit lanjaran, bunga ungu-putih, polong panjang menggantung.",
            environment = "Sangat mudah tumbuh di dataran rendah maupun menengah dengan cuaca hangat.",
            sunlight = "Sinar matahari penuh 6–8 jam per hari.",
            growingMedia = "Tanah gembur + kompos matang (2:1) dengan drainase baik.",
            wateringPrinciple = "Siram teratur saat berkecambah dan saat pengisian polong; hindari genangan air.",
            harvestEstimateDays = "45–55 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Kacang Panjang",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah gembur dan kompos matang (2:1)",
                seedPrepTip = "Tanam biji langsung ke lubang tanam tanpa direndam lama agar tidak membusuk.",
                seedSoakRecommended = false,
                germinationDays = "3–5 hari",
                spacing = "Jarak 30x50 cm dengan ajir/lanjaran bambu setinggi 2 meter",
                vegetativeWaterTip = "Bantu lilitkan pucuk awal ke ajir searah belitan alaminya.",
                vegetativeNutrientOrganic = "Kacang panjang bersimbiosis dengan bakteri Rhizobium di akar sehingga tidak butuh banyak pupuk nitrogen tambahan; cukup kompos matang.",
                vegetativeNutrientNonOrganic = "Utamakan pupuk Fosfor dan Kalium dosis ringan saat awal tanam/berbunga; hindari Nitrogen berlebih.",
                floweringOrLeafTip = "Waspadai kutu daun hitam (Aphis) yang sering berkumpul di pucuk dan tangkai bunga.",
                fruitingOrMaturationTip = "Panen polong tepat waktu mendorong tanaman terus membentuk bunga baru.",
                harvestIndicator = "Polong panjang penuh, masih renyah mudah dipatahkan, biji belum menonjol keras.",
                harvestWindow = "45–55 HST (panen berulang 2–3 hari sekali)",
                postHarvestTip = "Setelah selesai, akar kacang panjang yang sehat kaya bintil nitrogen—bagus sebagai tanaman rotasi sebelum cabai atau pakcoy."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Semprotan Air Bertekanan Ringan / Pemangkasan Pucuk Kutu",
                    purpose = "Mengurangi koloni kutu daun hitam di pucuk dan tangkai polong secara mekanis.",
                    howToApply = "Semprot koloni kutu dengan aliran air terarah di pagi hari atau petik pucuk yang terserang berat.",
                    suitableStage = "Vegetatif & Berbunga",
                    frequency = "Saat ditemukan koloni di pucuk.",
                    precautions = "Lakukan di pagi hari agar tajuk cepat kering.",
                    expectedResponse = "Populasi kutu berkurang tanpa membahayakan serangga predator alami (kepik).",
                    referenceTitle = "Pengelolaan Hama Kacang Panjang — Balitsa",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk Fosfor (SP-36) & Kalium (KCl) Dosis Ringan",
                    nutrientComposition = "Fosfor dan Kalium tanpa kelebihan Nitrogen.",
                    applicationMethod = "Aplikasikan di alur tanam saat awal atau minggu ke-3.",
                    timing = "Awal tanam dan menjelang berbunga.",
                    dosageGuidance = "Ikuti takaran rendah sesuai petunjuk kemasan kacang-kacangan.",
                    precautionsAndPpe = "Kelebihan nitrogen membuat daun terlalu rimbun namun polong sedikit.",
                    withholdingAndReentry = "Cuci polong sebelum dimasak.",
                    compatibilityNotes = "Sangat cocok ditanam setelah tanaman berkebutuhan N tinggi.",
                    referenceTitle = "Pedoman Budidaya Kacang Panjang — Kementan",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Periksa pucuk dan bunga dari koloni kutu hitam kecil dan semut pendamping."),
            references = listOf(
                KnowledgeReference(
                    title = "Budidaya Kacang Panjang di Lahan dan Pekarangan",
                    institution = "Balitsa Kementan",
                    identifier = "Balitsa-KacangPanjang-2022",
                    year = "2022",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "kacang_tanah",
            commonName = "Kacang Tanah",
            scientificName = "Arachis hypogaea",
            localNames = listOf("Kacang suuk", "Kacang brendel"),
            category = "Kacang-kacangan Umbi/Polong Tanah",
            imageResKey = "img_hero_garden",
            summary = "Tanaman polong unik yang membentuk buahnya di dalam tanah setelah bunga dibuahi (ginofor masuk ke media gembur).",
            characteristics = "Tanaman pendek berumpun, daun majemuk empat helai, bunga kuning di dekat pangkal yang memanjang menembus tanah.",
            environment = "Dataran rendah hingga 500 mdpl dengan cuaca hangat dan tanah gembur berpasir.",
            sunlight = "Sinar matahari penuh 6–8 jam per hari.",
            growingMedia = "Tanah berpasir sangat gembur + kompos matang (2:1); tanah keras menghambat bakal polong masuk tanah.",
            wateringPrinciple = "Siram teratur pada fase berbunga dan masuknya ginofor ke tanah; kurangi saat polong siap panen.",
            harvestEstimateDays = "90–100 hari setelah tanam",
            toxicityNote = "Keringkan polong dengan cepat pascapanen untuk mencegah jamur Aspergillus penghasil aflatoksin saat disimpan.",
            stages = buildStandardStages(
                cropName = "Kacang Tanah",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah berpasir gembur dan kompos matang (2:1)",
                seedPrepTip = "Kupas polong benih sesaat sebelum tanam dan pilih biji bernas utuh dengan kulit ari tidak lecet.",
                seedSoakRecommended = false,
                germinationDays = "4–6 hari",
                spacing = "Jarak 20x30 cm, kedalaman 3–4 cm",
                vegetativeWaterTip = "Lakukan pembumbunan ringan tanah gembur di pangkal setelah bunga mulai turun.",
                vegetativeNutrientOrganic = "Gunakan kompos matang dan kapur pertanian bila tanah agak asam untuk membantu pengisian polong.",
                vegetativeNutrientNonOrganic = "Fokus kalsium (dolomit/kalsium) dan fosfor di zona pembentukan polong (5 cm permukaan tanah).",
                floweringOrLeafTip = "Bunga kuning mekar di pangkal lalu tangkai ginofor memanjang menembus permukaan tanah.",
                fruitingOrMaturationTip = "Jangan mencangkul atau mengaduk tanah dekat pangkal setelah ginofor masuk ke media.",
                harvestIndicator = "Sebagian daun menguning dengan bercak tua dan bagian dalam kulit polong tampak kecokelatan gelap berbintik.",
                harvestWindow = "90–100 HST",
                postHarvestTip = "Jemur polong di bawah matahari hingga benar-benar kering sebelum disimpan."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Pembumbunan Tanah Gembur & Penambahan Kapur Pertanian",
                    purpose = "Memudahkan ginofor menembus media dan menyediakan kalsium di zona polong agar polong tidak hampa.",
                    howToApply = "Gemburkan dan naikkan tanah halus di sekitar pangkal saat umur 25–30 HST.",
                    suitableStage = "Pembungaan awal",
                    frequency = "Satu kali sebelum ginofor masuk tanah.",
                    precautions = "Jangan membumbun setelah ginofor tertanam dalam karena dapat mematahkannya.",
                    expectedResponse = "Polong terisi penuh dan jumlah polong hampa berkurang.",
                    referenceTitle = "Pedoman Kacang Tanah — Balitkabi Kementan",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Kalsium / Dolomit + SP-36",
                    nutrientComposition = "Kalsium (Ca) dan Fosfor (P) untuk pengisian biji.",
                    applicationMethod = "Tabur merata di zona perakaran permukaan saat pembungaan.",
                    timing = "Saat tanam dan umur 25 HST.",
                    dosageGuidance = "Ikuti rekomendasi dosis kacang tanah daerah setempat.",
                    precautionsAndPpe = "Polong hampa sering disebabkan kurang kalsium, bukan kurang nitrogen.",
                    withholdingAndReentry = "Tidak diperlukan menjelang panen.",
                    compatibilityNotes = "Jaga media bebas genangan.",
                    referenceTitle = "Teknologi Produksi Kacang Tanah — Balitkabi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Periksa bercak cokelat bulat di daun tua yang umum muncul di fase akhir."),
            references = listOf(
                KnowledgeReference(
                    title = "Budidaya Kacang Tanah",
                    institution = "Balitkabi Kementan",
                    identifier = "Balitkabi-KacangTanah-2022",
                    year = "2022",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Kacang-kacangan",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "jagung",
            commonName = "Jagung Manis",
            scientificName = "Zea mays",
            localNames = listOf("Jagung manis", "Sweet corn"),
            category = "Pangan & Serealia",
            imageResKey = "img_hero_garden",
            summary = "Tanaman batang tinggi penghasil tongkol manis yang membutuhkan penanaman berkelompok agar penyerbukan angin sempurna.",
            characteristics = "Batang beruas tunggal tegak, daun panjang berurat sejajar, bunga jantan di pucuk (malai) dan bunga betina berambut di ketiak daun.",
            environment = "Sangat menyukai lahan terbuka dengan sinar matahari penuh.",
            sunlight = "Sinar matahari penuh minimal 7–8 jam per hari.",
            growingMedia = "Tanah gembur + kompos matang (2:1), kedalaman olah atau wadah besar minimal 35–40 cm.",
            wateringPrinciple = "Kebutuhan air paling kritis saat pembungaan (keluar malai dan rambut tongkol) serta pengisian biji.",
            harvestEstimateDays = "65–75 hari setelah tanam (jagung manis)",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Jagung Manis",
                sunlight = "sinar matahari penuh 7–8 jam/hari",
                media = "tanah gembur kaya kompos (2:1)",
                seedPrepTip = "Tanam benih langsung dalam kelompok minimal 3x3 atau 4x4 baris (bukan 1 baris panjang) agar serbuk sari mudah jatuh ke rambut tongkol.",
                seedSoakRecommended = false,
                germinationDays = "4–6 hari",
                spacing = "Jarak 25x70 cm, kedalaman 3–4 cm",
                vegetativeWaterTip = "Bumbun pangkal batang saat tinggi 40 cm agar tahan angin.",
                vegetativeNutrientOrganic = "Berikan kompos matang cukup banyak saat tanam dan susulan umur 25 HST.",
                vegetativeNutrientNonOrganic = "Jagung tergolong rakus hara N dan K; berikan pemupukan bertahap pada umur 10–14 HST dan 30–35 HST sesuai anjuran.",
                floweringOrLeafTip = "Saat malai jantan dan rambut tongkol keluar, goyangkan batang perlahan di pagi hari jika angin tenang.",
                fruitingOrMaturationTip = "Amati pucuk tongkol dari serangan ulat tongkol dan ulat grayak pada corong daun muda.",
                harvestIndicator = "Rambut tongkol berwarna cokelat mengering dan biji terasa penuh berisi cairan putih susu saat ditekan kuku.",
                harvestWindow = "65–75 HST",
                postHarvestTip = "Batang jagung sehat dapat dicacah menjadi mulsa atau bahan kompos kaya serat karbon."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Tanam Berkelompok (Blok) + Pengamatan Corong Daun",
                    purpose = "Menjamin tongkol penuh biji dan mendeteksi dini larva ulat grayak di corong daun.",
                    howToApply = "Tanam minimal 9–12 tanaman dalam petak persegi dan periksa corong daun muda di pagi hari.",
                    suitableStage = "Vegetatif & Pembungaan",
                    frequency = "Inspeksi corong daun 2 kali seminggu.",
                    precautions = "Penanaman satu baris tunggal sering menghasilkan tongkol ompong.",
                    expectedResponse = "Pengisian biji penuh sampai ujung tongkol.",
                    referenceTitle = "Panduan Budidaya Jagung — Balitsereal Kementan",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk Nitrogen & NPK Susulan",
                    nutrientComposition = "Nitrogen, Fosfor, dan Kalium.",
                    applicationMethod = "Tugal 7 cm di samping batang lalu tutup tanah.",
                    timing = "Umur 10–14 HST dan 30–35 HST.",
                    dosageGuidance = "Ikuti rekomendasi dosis spesifik jagung manis sesuai label produk.",
                    precautionsAndPpe = "Jangan jatuhkan butiran pupuk ke dalam corong pucuk daun karena akan membakar daun muda.",
                    withholdingAndReentry = "Berhenti memupuk 25 hari sebelum panen.",
                    compatibilityNotes = "Pastikan kelembapan tanah cukup saat pemupukan.",
                    referenceTitle = "Pemupukan Berimbang Tanaman Jagung — Balitsereal",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Periksa lubang berbaris dan kotoran seperti serbuk gergaji di corong daun (tanda ulat grayak)."),
            references = listOf(
                KnowledgeReference(
                    title = "Budidaya Jagung Manis dan PHT Ulat Grayak",
                    institution = "Balitsereal Kementan",
                    identifier = "Balitsereal-Jagung-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Serealia",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "melon",
            commonName = "Melon",
            scientificName = "Cucumis melo",
            localNames = listOf("Melon jaring", "Golden melon"),
            category = "Buah Semusim",
            imageResKey = "img_hero_garden",
            summary = "Tanaman buah bernilai tinggi yang membutuhkan sinar matahari maksimal, penyerbukan tepat, dan pengendalian kelembapan ketat agar buah manis.",
            characteristics = "Sulur merambat kuat, daun lebar berlekuk tumpul, buah bulat berjaring (net) atau berkulit halus kuning/hijau.",
            environment = "Menyukai cuaca hangat kering 25–32°C dan sirkulasi udara baik.",
            sunlight = "Sinar matahari penuh 7–8 jam per hari.",
            growingMedia = "Tanah gembur + kompos matang + sekam bakar (2:2:1), drainase sangat baik.",
            wateringPrinciple = "Siram cukup saat pembesaran buah, namun kurangi bertahap 7–10 hari sebelum panen agar kadar gula buah meningkat dan tidak pecah.",
            harvestEstimateDays = "65–80 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Melon",
                sunlight = "sinar matahari penuh 7–8 jam/hari",
                media = "tanah gembur, kompos matang, dan sekam bakar (2:2:1)",
                seedPrepTip = "Rendam benih 2–3 jam dalam air hangat kuku lalu semai di media lembap steril.",
                seedSoakRecommended = true,
                germinationDays = "3–5 hari",
                spacing = "Polybag besar 45–50 cm dengan rambatan tali/ajir dan penopang buah",
                vegetativeWaterTip = "Sisakan 1 batang utama dan pangkas tunas di ruas 1–7 agar energi optimal.",
                vegetativeNutrientOrganic = "Gunakan kompos matang dan ekstrak kompos kalium saat fase pembesaran buah.",
                vegetativeNutrientNonOrganic = "Fokus NPK berimbang saat vegetatif, lalu beralih ke rasio Kalium lebih tinggi saat pembentukan net dan pematangan.",
                floweringOrLeafTip = "Seleksi buah di ruas ke-8 s/d ke-12; polinasi bunga betina dilakukan pagi hari pukul 06.30–09.30.",
                fruitingOrMaturationTip = "Gantung buah menggunakan tali jaring ke ajir agar batang tidak robek; pilih 1–2 buah terbaik per tanaman.",
                harvestIndicator = "Jaring (net) terbentuk penuh rapat, tangkai buah mulai retak melingkar, dan aroma manis tercium lembut.",
                harvestWindow = "65–80 HST",
                postHarvestTip = "Istirahatkan media dan rotasikan dengan tanaman non-Cucurbitaceae."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Penyerbukan Pagi + Seleksi 1–2 Buah & Penopang Tali",
                    purpose = "Menghasilkan ukuran buah besar, bentuk simetris, dan kadar kemanisan tinggi.",
                    howToApply = "Tempelkan benang sari bunga jantan ke putik bunga betina di ruas 8–12 pada pagi hari, lalu ikat tangkai buah saat seukuran telur.",
                    suitableStage = "Berbunga & Berbuah",
                    frequency = "Saat bunga betina mekar sempurna pagi hari.",
                    precautions = "Sisakan minimal 20–25 daun sehat di atas buah sebagai dapur fotosintesis.",
                    expectedResponse = "Buah berhasil terbentuk dengan ukuran dan jaring lebih seragam.",
                    referenceTitle = "Panduan Budidaya Melon Berkualitas — Kementan",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk Kalium & Kalsium Fase Generatif",
                    nutrientComposition = "Kalium tinggi dan Kalsium untuk kekerasan daging serta rasa manis.",
                    applicationMethod = "Kocor bertahap di zona akar.",
                    timing = "14–35 hari setelah polinasi (berhenti 10 hari sebelum panen).",
                    dosageGuidance = "Sesuai anjuran label pupuk hortikultura.",
                    precautionsAndPpe = "Pengairan berlebih mendekati panen mengencerkan rasa manis dan memicu pecah buah.",
                    withholdingAndReentry = "Hentikan pemupukan dan kurangi air 7–10 hari sebelum petik.",
                    compatibilityNotes = "Jaga daun tetap sehat bebas embun tepung.",
                    referenceTitle = "Manajemen Mutu Buah Melon — Hortikultura Indonesia",
                    evidenceLevel = EvidenceLevel.B
                )
            ),
            monitoringTips = listOf("Periksa bagian bawah daun dari gejala embun bulu dan bercak tepung putih."),
            references = listOf(
                KnowledgeReference(
                    title = "Teknologi Budidaya Melon",
                    institution = "Ditjen Hortikultura Kementan",
                    identifier = "Kementan-Melon-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Buah Semusim",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "semangka",
            commonName = "Semangka",
            scientificName = "Citrullus lanatus",
            localNames = listOf("Semangka merah", "Semangka kuning"),
            category = "Buah Semusim",
            imageResKey = "img_hero_garden",
            summary = "Tanaman buah menjalar penyuka sinar matahari penuh dengan perakaran dalam dan buah berair menyegarkan.",
            characteristics = "Daun berlekuk dalam berwarna hijau keabu-abuan, sulur menjalar luas, buah bulat atau lonjong berbobot besar.",
            environment = "Sangat menyukai cuaca cerah hangat (25–34°C) dan media berpasir yang tidak becek.",
            sunlight = "Sinar matahari penuh 8 jam per hari.",
            growingMedia = "Tanah gembur berpasir + kompos matang (2:1) dengan bedengan tinggi atau wadah sangat besar (>50 cm).",
            wateringPrinciple = "Siram dalam dan jarang agar akar tumbuh kuat; kurangi penyiraman menjelang panen untuk menjaga kemanisan.",
            harvestEstimateDays = "70–85 hari setelah tanam",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Semangka",
                sunlight = "sinar matahari penuh 8 jam/hari",
                media = "tanah berpasir gembur dan kompos matang (2:1)",
                seedPrepTip = "Benih semangka non-biji perlu sedikit diretakkan ujungnya secara hati-hati; semangka berbiji cukup direndam air hangat 3 jam.",
                seedSoakRecommended = true,
                germinationDays = "4–7 hari",
                spacing = "Jarak 100x200 cm di lahan atau 1 tanaman di bak planter sangat besar dengan alas buah",
                vegetativeWaterTip = "Beri alas jerami atau papan kering di bawah buah yang terbaring di tanah.",
                vegetativeNutrientOrganic = "Gunakan kompos matang sebagai pupuk dasar di lubang tanam dan mulsa jerami luas.",
                vegetativeNutrientNonOrganic = "Pupuk NPK berimbang saat vegetatif, lalu unsur Kalium saat buah sebesar bola tenis hingga pematangan awal.",
                floweringOrLeafTip = "Sisakan 2–3 sulur utama terbaik dan lakukan seleksi 1–2 bakal buah terbagus per tanaman.",
                fruitingOrMaturationTip = "Balik buah secara sangat perlahan 1 kali di usia pertengahan bila di atas tanah agar warna kulit merata.",
                harvestIndicator = "Sulur kecil tepat di pangkal tangkai buah telah mengering cokelat dan bercak alas buah berubah putih menjadi kuning krem.",
                harvestWindow = "70–85 HST",
                postHarvestTip = "Rotasikan lahan minimal 2 musim sebelum menanam semangka atau melon kembali."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Alas Jerami / Papan Kering di Bawah Buah",
                    purpose = "Mencegah kulit bawah buah lembap, pucat, atau terserang busuk buah tanah.",
                    howToApply = "Selipkan bantalan jerami kering atau tatakan berventilasi di bawah buah muda.",
                    suitableStage = "Pembuahan",
                    frequency = "Sekali pasang saat buah sebesar kepalan tangan.",
                    precautions = "Jangan memutar tangkai buah terlalu keras.",
                    expectedResponse = "Buah bersih dan terhindar dari busuk Phytophthora.",
                    referenceTitle = "Pedoman Budidaya Semangka — Kementan",
                    validationStatus = "Teruji lapangan",
                    evidenceLevel = EvidenceLevel.D
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK & Kalium (ZK/KCl)",
                    nutrientComposition = "Kalium untuk kemanisan dan warna daging merah cerah.",
                    applicationMethod = "Kocor atau tugal 15 cm dari pangkal akar utama.",
                    timing = "Umur 15 HST, 30 HST, dan 45 HST.",
                    dosageGuidance = "Sesuai anjuran label untuk tanaman semangka.",
                    precautionsAndPpe = "Kurangi air dan hentikan pupuk di 10 hari terakhir agar buah tidak hambar atau pecah.",
                    withholdingAndReentry = "Berhenti 10–14 hari sebelum panen.",
                    compatibilityNotes = "Membutuhkan lahan terang dan tidak tergenang hujan.",
                    referenceTitle = "Teknologi Budidaya Semangka — Ditjen Hortikultura",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Cek kondisi sulur di pangkal buah dan gejala layu mendadak saat siang hari."),
            references = listOf(
                KnowledgeReference(
                    title = "Panduan Teknis Budidaya Semangka",
                    institution = "Kementerian Pertanian RI",
                    identifier = "Kementan-Semangka-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Buah Semusim",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "pepaya",
            commonName = "Pepaya (Calina / California)",
            scientificName = "Carica papaya",
            localNames = listOf("Kates", "Gedang", "Betik"),
            category = "Tanaman Buah Tahunan",
            imageResKey = "img_hero_garden",
            summary = "Tanaman buah cepat berproduksi (7–9 bulan) berbatang lunak berongga yang sangat peka terhadap genangan air.",
            characteristics = "Batang tunggal tegak tanpa cabang utama, daun menjari bertangkai panjang berongga, buah menggantung di batang.",
            environment = "Dataran rendah hingga menengah, suhu 24–33°C, bebas genangan air mutlak.",
            sunlight = "Sinar matahari penuh minimal 6–8 jam per hari.",
            growingMedia = "Tanah gembur dalam + kompos matang melimpah, wajib membuat gundukan (guludan) agar pangkal batang tidak pernah tergenang.",
            wateringPrinciple = "Siram saat kemarau dengan menjaga kelembapan, namun pastikan tidak pernah ada air menggenang di leher akar.",
            harvestEstimateDays = "7–9 bulan setelah tanam",
            toxicityNote = "Getah buah muda dan daun mengandung papain yang dapat mengiritasi kulit sensitif.",
            stages = buildStandardStages(
                cropName = "Pepaya",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah gembur dalam dengan gundukan berdrainase cepat dan kompos matang",
                seedPrepTip = "Bersihkan selaput bening biji segar, keringanginkan, lalu rendam air hangat kuku 12 jam sebelum disemai di polybag.",
                seedSoakRecommended = true,
                germinationDays = "10–14 hari",
                spacing = "Jarak tanam 2,5 x 2,5 meter di tanah terbuka",
                vegetativeWaterTip = "Buat guludan/gundukan di pangkal batang agar air hujan langsung mengalir menjauh.",
                vegetativeNutrientOrganic = "Berikan kompos matang 5–10 kg per pohon tiap 3 bulan di sekeliling tajuk.",
                vegetativeNutrientNonOrganic = "Berikan pupuk berimbang NPK bertahap setiap 1–2 bulan menyesuaikan umur pohon.",
                floweringOrLeafTip = "Seleksi tanaman saat berbunga: pilih pohon berbunga sempurna (hermafrodit) untuk buah lonjong padat.",
                fruitingOrMaturationTip = "Pangkas tangkai daun tua menguning yang menggantung kering untuk kebersihan batang; jangan tarik paksa.",
                harvestIndicator = "Muncul 1–2 garis semburat kuning jingga di ujung buah untuk pengiriman, atau kuning 50% untuk konsumsi langsung.",
                harvestWindow = "7–9 bulan (panen berkelanjutan tiap minggu)",
                postHarvestTip = "Saat pohon sudah terlalu tinggi (>2 tahun) atau menurun produktivitasnya, siapkan peremajaan bibit baru di titik tanam berbeda."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Penanaman Sistem Guludan + Pembersihan Kutu Putih Mekanis",
                    purpose = "Mencegah busuk pangkal batang (Phytophthora) dan mengendalikan kutu putih pepaya sejak dini.",
                    howToApply = "Tinggikan gundukan tanah 20–30 cm di sekitar pohon; bersihkan koloni awal kutu putih dengan sikat lembut/semprot air.",
                    suitableStage = "Penanaman & Vegetatif",
                    frequency = "Periksa batang dan balik daun tiap minggu.",
                    precautions = "Jangan memotong pangkal batang dengan alat tumpul saat menyiangi gulma karena luka memicu busuk.",
                    expectedResponse = "Akar tetap sehat dan kutu putih tidak menyebar ke seluruh tajuk.",
                    referenceTitle = "Budidaya Pepaya Calina — PKHT IPB / Kementan",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK Majemuk & Boron Dosis Mikro",
                    nutrientComposition = "NPK seimbang serta boron bila ada gejala benjolan getah pada buah.",
                    applicationMethod = "Tabur melingkar di proyeksi ujung tajuk daun dan tutup tanah tipis.",
                    timing = "Setiap 1–2 bulan sekali.",
                    dosageGuidance = "Ikuti takaran umur tanaman pepaya sesuai panduan agronomis.",
                    precautionsAndPpe = "Hindari menabur pupuk tepat menempel di leher batang lunak pepaya.",
                    withholdingAndReentry = "Aman dipanen selama aplikasi hanya di zona akar dan tidak mengenai buah.",
                    compatibilityNotes = "Boron hanya diberikan dosis sangat kecil bila gejala defisiensi terkonfirmasi.",
                    referenceTitle = "Teknologi Produksi Pepaya Unggul — Balitbu Tropika",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Waspadai lapisan putih seperti kapas (kutu putih) di balik daun dan tangkai buah."),
            references = listOf(
                KnowledgeReference(
                    title = "Panduan Budidaya Pepaya Calina",
                    institution = "Balai Penelitian Tanaman Buah Tropika (Balitbu)",
                    identifier = "Balitbu-Pepaya-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Buah Tropika",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "pisang",
            commonName = "Pisang",
            scientificName = "Musa paradisiaca / Musa acuminata",
            localNames = listOf("Gedang", "Cau", "Punti"),
            category = "Tanaman Buah Tahunan",
            imageResKey = "img_hero_garden",
            summary = "Tanaman buah tropis berumpun yang membutuhkan cukup air, kalium tinggi, dan pengaturan jumlah anakan agar tandan besar.",
            characteristics = "Batang semu dari pelepah daun, daun sangat lebar mudah sobek angin, menghasilkan satu tandan per batang lalu diganti anakan.",
            environment = "Sangat adaptif di seluruh Indonesia (0–1.000 mdpl) dengan curah hujan atau ketersediaan air cukup.",
            sunlight = "Sinar matahari penuh 6–8 jam per hari.",
            growingMedia = "Tanah dalam gembur kaya bahan organik dengan drainase baik.",
            wateringPrinciple = "Pisang berdaun sangat luas dan membutuhkan kelembapan stabil, tetapi lubang tanam tidak boleh tergenang lama.",
            harvestEstimateDays = "9–12 bulan setelah tanam anakan",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Pisang",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah dalam kaya kompos matang",
                seedPrepTip = "Gunakan bibit anakan pedang (daun runcing sempit) yang sehat atau bibit kultur jaringan bebas penyakit layu.",
                seedSoakRecommended = false,
                germinationDays = "14–21 hari (adaptasi tunas baru)",
                spacing = "Jarak tanam 2,5 x 3 meter",
                vegetativeWaterTip = "Berikan mulsa pelepah/daun kering di sekeliling rumpun untuk menjaga kelembapan.",
                vegetativeNutrientOrganic = "Tambahkan kompos matang dan cacahan batang/pelepah sehat di sekeliling rumpun.",
                vegetativeNutrientNonOrganic = "Fokus pemupukan NPK tinggi Kalium setiap 2–3 bulan untuk mendukung tandan besar.",
                floweringOrLeafTip = "Potong jantung pisang setelah sisir buah terakhir terbentuk sempurna agar pengisian buah maksimal.",
                fruitingOrMaturationTip = "Pasang tiang penyangga pada batang yang bertandan berat dan bungkus tandan dengan plastik biru berlubang agar kulit buah mulus.",
                harvestIndicator = "Siku buah mulai membulat penuh dan 1–2 buah di sisir teratas mulai semburat kuning.",
                harvestWindow = "9–12 bulan (sekitar 75–90 hari setelah jantung keluar)",
                postHarvestTip = "Setelah panen, tebang batang induknya dan sisakan 1–2 anakan penerus terbaik per rumpun."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Pengaturan Anakan (1 Induk + 1 Anakan) & Sanitasi Layu",
                    purpose = "Mengurangi persaingan nutrisi dalam rumpun dan mencegah penyebaran penyakit layu.",
                    howToApply = "Sisakan hanya 1 batang induk, 1 anakan remaja, dan 1 anakan kecil per rumpun; bersihkan daun tua mengering.",
                    suitableStage = "Vegetatif & Pasca Panen",
                    frequency = "Evaluasi tiap 1–2 bulan.",
                    precautions = "Sterilkan parang dengan api/alkohol bila baru memotong rumpun yang dicurigai berpenyakit layu.",
                    expectedResponse = "Tandan pisang lebih berat dan rumpun lebih rapi sehat.",
                    referenceTitle = "Panduan Budidaya Pisang Sehat — Balitbu Tropika",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK + Kalium (KCl) Berkala",
                    nutrientComposition = "Kalium tinggi sangat penting untuk bobot dan rasa manis pisang.",
                    applicationMethod = "Sebar di alur melingkar berjarak 50 cm dari batang induk.",
                    timing = "Setiap 2–3 bulan sekali.",
                    dosageGuidance = "Sesuaikan dengan umur rumpun dan panduan agronomis tanaman pisang.",
                    precautionsAndPpe = "Jangan menumpuk pupuk tepat di pangkal anakan muda.",
                    withholdingAndReentry = "Aman selama diaplikasikan di zona tanah.",
                    compatibilityNotes = "Kombinasikan dengan mulsa organik agar penyerapan optimal.",
                    referenceTitle = "Pemupukan Tanaman Pisang — Kementan",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Waspadai penguningan berurutan dari daun tua disertai pecah pangkal batang (indikasi awal layu Fusarium/darah)."),
            references = listOf(
                KnowledgeReference(
                    title = "Pedoman Teknis Budidaya Pisang",
                    institution = "Balitbu Tropika Kementan",
                    identifier = "Balitbu-Pisang-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Buah Tropika",
                    lastReviewDate = "2026-10-01"
                )
            )
        ),
        PlantKnowledge(
            id = "jeruk",
            commonName = "Jeruk Nipis / Jeruk Santang",
            scientificName = "Citrus aurantifolia / Citrus reticulata",
            localNames = listOf("Jeruk nipis", "Jeruk peras", "Tabulampot jeruk"),
            category = "Tanaman Buah Tahunan",
            imageResKey = "img_hero_garden",
            summary = "Tanaman buah tahunan berkayu yang sangat populer sebagai tabulampot (tanaman buah dalam pot) maupun di kebun pekarangan.",
            characteristics = "Pohon perdu rimbun dengan daun beraroma jeruk khas, bunga putih harum, buah berkulit kaya minyak atsiri.",
            environment = "Sangat menyukai panas terik dataran rendah hingga menengah (24–33°C).",
            sunlight = "Sinar matahari penuh minimal 6–8 jam per hari untuk memicu pembungaan lebat.",
            growingMedia = "Tanah gembur + kompos matang + sekam bakar (1:1:1) di pot besar min. 45–50 cm atau tanah terbuka.",
            wateringPrinciple = "Siram teratur; pada tabulampot, stres air ringan terkontrol sebelum musim bunga membantu merangsang pembungaan.",
            harvestEstimateDays = "6–8 bulan dari bunga hingga buah matang (bibit okulasi berbuah mulai umur 1 tahun)",
            toxicityNote = null,
            stages = buildStandardStages(
                cropName = "Jeruk",
                sunlight = "sinar matahari penuh 6–8 jam/hari",
                media = "tanah gembur, kompos matang, dan sekam bakar (1:1:1)",
                seedPrepTip = "Untuk cepat berbuah dan sifat identik induk, gunakan bibit hasil okulasi/cangkok bersertifikat bebas penyakit CVPD.",
                seedSoakRecommended = false,
                germinationDays = "14–30 hari (adaptasi bibit okulasi)",
                spacing = "Pot min. diameter 45–50 cm atau jarak 3x3 meter di kebun",
                vegetativeWaterTip = "Buang tunas yang tumbuh dari batang bawah (di bawah sambungan okulasi).",
                vegetativeNutrientOrganic = "Tambahkan kompos matang setiap 2–3 bulan di tepi pot dan gemburkan permukaan tipis tanpa memutus akar serabut.",
                vegetativeNutrientNonOrganic = "Gunakan pupuk NPK berimbang plus unsur mikro (terutama Seng/Zn dan Magnesium) bila gejala defisiensi muncul.",
                floweringOrLeafTip = "Pangkas ranting yang tumbuh ke dalam tajuk agar cahaya menembus pusat pohon.",
                fruitingOrMaturationTip = "Amati tunas muda dari serangan ulat penggerek/penggulung daun (leafminer) dan kutu loncat jeruk.",
                harvestIndicator = "Kulit buah mengilap, sedikit melunak saat ditekan, dan warna berubah cerah sesuai varietas.",
                harvestWindow = "6–8 bulan sejak pembungaan",
                postHarvestTip = "Setelah panen raya, pangkas ringan cabang kering/bekas buah dan tambahkan kompos segar."
            ),
            organicSolutions = listOf(
                OrganicSolution(
                    materialName = "Pemangkasan Bentuk 1-3-9 & Sanitasi Tunas Batang Bawah",
                    purpose = "Membentuk tajuk terbuka bercahaya baik sehingga mengurangi serangan kutu sisik dan embun jelaga.",
                    howToApply = "Sisakan 3 cabang primer kokoh dan pangkas ranting bersilangan di bagian dalam tajuk setelah panen.",
                    suitableStage = "Vegetatif & Pasca Panen",
                    frequency = "Setelah setiap periode panen.",
                    precautions = "Gunakan gunting pangkas tajam dan bersih.",
                    expectedResponse = "Tajuk lebih sehat, aliran udara lancar, dan pembungaan berikutnya lebih serempak.",
                    referenceTitle = "Panduan Tabulampot Jeruk Sehat — Balitjestro Kementan",
                    validationStatus = "Panduan teknis resmi",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            nonOrganicSolutions = listOf(
                NonOrganicSolution(
                    inputType = "Pupuk NPK + Unsur Mikro (Zn/Mg/Fe)",
                    nutrientComposition = "NPK lengkap dan unsur mikro penting bagi tanaman jeruk.",
                    applicationMethod = "Tabur di pinggiran tajuk/pot dan siram.",
                    timing = "Setiap 1–2 bulan sekali sesuai fase pertumbuhan.",
                    dosageGuidance = "Ikuti takaran kemasan untuk tanaman buah tahunan/tabulampot.",
                    precautionsAndPpe = "Jangan menumpuk pupuk di pangkal batang utama.",
                    withholdingAndReentry = "Aman selama diaplikasikan di media akar.",
                    compatibilityNotes = "Unsur mikro sering lebih efektif bila pH media dijaga di kisaran 6,0–6,5.",
                    referenceTitle = "Pengelolaan Nutrisi Tanaman Jeruk — Balitjestro",
                    evidenceLevel = EvidenceLevel.A
                )
            ),
            monitoringTips = listOf("Amati alur keperakan berkelok di daun muda (ulat peliang daun / leafminer) dan bercak belang kuning asimetris."),
            references = listOf(
                KnowledgeReference(
                    title = "Teknologi Budidaya Jeruk Sehat",
                    institution = "Balai Penelitian Tanaman Jeruk dan Buah Subtropika (Balitjestro)",
                    identifier = "Balitjestro-Jeruk-2023",
                    year = "2023",
                    evidenceLevel = EvidenceLevel.A,
                    reviewer = "Tim Agronomi Jeruk",
                    lastReviewDate = "2026-10-01"
                )
            )
        )
    )
}
