package com.example.data.knowledge

import com.example.model.EvidenceLevel
import com.example.model.PlantProblemRecord

private data class ProblemSeed(
    val suffix: String,
    val title: String,
    val symptom: String,
    val indicators: List<String>,
    val causes: List<String>,
    val questions: List<String>,
    val distinguishing: String,
    val organic: String,
    val nonOrganic: String,
    val preventive: List<String>,
    val followUpDays: Int,
    val expectedResponse: String,
    val reference: String,
    val level: EvidenceLevel,
    val validation: String
)

private fun buildProblemsForCrop(
    cropId: String,
    cropName: String,
    seeds: List<ProblemSeed>
): List<PlantProblemRecord> = seeds.mapIndexed { idx, s ->
    PlantProblemRecord(
        problemId = "${cropId}_prob_${idx + 1}",
        cropId = cropId,
        cropName = cropName,
        title = s.title,
        symptomDescription = s.symptom,
        visualIndicators = s.indicators,
        possibleCauses = s.causes,
        diagnosticQuestions = s.questions,
        distinguishingFeatures = s.distinguishing,
        organicTreatment = s.organic,
        nonOrganicTreatment = s.nonOrganic,
        preventiveActions = s.preventive,
        followUpDays = s.followUpDays,
        expectedResponse = s.expectedResponse,
        referenceSource = s.reference,
        evidenceLevel = s.level,
        validationStatus = s.validation
    )
}

object PlantProblemsPart1 {
    val problems: List<PlantProblemRecord> = buildList {
        addAll(
            buildProblemsForCrop(
                "cabai", "Cabai Rawit", listOf(
                    ProblemSeed(
                        "kuning_bawah",
                        "Daun Bawah Menguning",
                        "Helai daun bagian bawah berubah kuning pucat lalu gugur sementara pucuk atas masih hijau.",
                        listOf("Daun tua dekat pangkal menguning merata", "Media sering terasa basah/berat"),
                        listOf("Drainase media terhambat", "Kelebihan air siraman", "Kekurangan unsur hara bergerak akibat akar terganggu"),
                        listOf("Apakah media terasa basah saat dicek sedalam 3 cm?", "Sudah berapa lama hujan atau penyiraman berlangsung?"),
                        "Jika media becek dan akar kecokelatan, masalah utama adalah kelembapan; bukan semata kekurangan nitrogen.",
                        "Perbaiki lubang drainase polybag, ganjal alas wadah, dan tunda pemupukan kompos cair selama 3 hari.",
                        "Hentikan penyiraman hingga 2 cm lapisan atas mengering; evaluasi pupuk berimbang hanya setelah akar pulih.",
                        listOf("Cek media sebelum menyiram", "Campur sekam bakar dalam media"),
                        3, "Daun baru tetap hijau dan daun kuning berhenti meluas dalam 3–5 hari.",
                        "Pedoman Budidaya Cabai Sehat — Balitsa Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "keriting_pucuk",
                        "Daun Pucuk Keriting & Menggulung",
                        "Daun muda di ujung cabang mengecil, mengerut ke atas atau ke bawah, dan pertumbuhannya melambat.",
                        listOf("Tepi daun muda melengkung", "Kadang disertai bercak keperakan atau titik kecil di balik daun"),
                        listOf("Serangan thrips atau tungau kuning", "Penularan virus kuning dari kutu kebul", "Stres panas dan kekeringan"),
                        listOf("Apakah terlihat serangga kecil bergerak di balik daun muda?", "Apakah tulang daun juga menguning mozaik?"),
                        "Keriting ke atas seperti sendok umumnya mengarah ke thrips/tungau; mosaik belang kuning terang mengarah ke virus.",
                        "Pasang perangkap likat kuning, pangkas pucuk rusak berat, dan tingkatkan kebersihan gulma sekitar kebun.",
                        "Lakukan pengendalian vektor sesuai prinsip PHT; untuk produk terdaftar, verifikasi label sasaran thrips/tungau dan pakai APD.",
                        listOf("Pasang jaring pelindung saat pembibitan", "Pantau balik daun 2 kali seminggu"),
                        5, "Tunas baru yang keluar setelah pengendalian tumbuh rata dan tidak berkerut.",
                        "Pengendalian Hama Terpadu Cabai — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "antraknosa",
                        "Bercak Cekung pada Buah (Antraknosa / Patek)",
                        "Buah cabai memiliki bercak melingkar cekung berwarna cokelat kehitaman yang meluas dan membusuk.",
                        listOf("Bercak berbatas agak jelas dan cekung di permukaan buah", "Muncul cincin konsentris spora oranye/hitam saat lembap"),
                        listOf("Infeksi jamur Colletotrichum pada buah", "Kelembapan udara tinggi dan percikan air hujan"),
                        listOf("Apakah bercak muncul lebih banyak saat musim hujan?", "Apakah buah terinfeksi dibiarkan menempel di pohon?"),
                        "Berbeda dari sengatan matahari yang kering memutih rata, antraknosa bersifat cekung basah dengan cincin spora.",
                        "Petik segera buah terinfeksi dan buang di luar area kebun, kurangi kelembapan dengan pemangkasan daun bawah.",
                        "Utamakan sanitasi buah terinfeksi dan perbaikan jarak tanam; bila memakai fungisida terdaftar, patuhi masa henti panen pada label.",
                        listOf("Jaga jarak tanam tidak terlalu rapat", "Gunakan mulsa agar tanah tidak memercik"),
                        4, "Buah baru yang terbentuk tetap mulus tanpa bercak cekung.",
                        "Manajemen Antraknosa Cabai — Balitsa Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "layu_mendadak",
                        "Tanaman Layu Mendadak Meski Media Basah",
                        "Seluruh daun mendadak layu di siang hari dan tidak segar kembali pada malam atau pagi hari.",
                        listOf("Daun layu masih berwarna hijau", "Pangkal batang atau jaringan pembuluh tampak kecokelatan bila dicek"),
                        listOf("Layu bakteri (Ralstonia) atau layu Fusarium", "Busuk akar berat akibat genangan air"),
                        listOf("Apakah tanaman layu permanen saat pagi hari?", "Apakah pangkal batang terasa lunak atau berbau?"),
                        "Kekurangan air membuat daun segar kembali setelah disiram; layu pembuluh tetap terkulai meski tanah basah.",
                        "Pisahkan tanaman terinfeksi agar tidak menular ke pot lain, jangan gunakan kembali media bekasnya, dan bersihkan alat kebun.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyembuhkan tanaman yang pembuluhnya sudah tersumbat berat; fokus pada isolasi dan sanitasi media.",
                        listOf("Gunakan bibit sehat", "Hindari melukai akar saat penyiangan"),
                        2, "Tanaman sekitar tetap tegar dan penularan ke pot tetangga terhenti.",
                        "Identifikasi Layu Bakteri & Fusarium — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "rontok_bunga",
                        "Bunga Cabai Rontok Sebelum Jadi Buah",
                        "Kuncup atau bunga mekar mudah jatuh beserta tangkainya sebelum membentuk bakal buah.",
                        listOf("Tangkai bunga menguning lalu patah bersih", "Suhu siang sangat terik atau air tidak teratur"),
                        listOf("Fluktuasi air (kekeringan lalu siram deras)", "Suhu panas ekstrem >34°C", "Hama thrips di dalam kelopak bunga"),
                        listOf("Apakah penyiraman sempat terlambat lalu disiram banyak?", "Apakah bunga rontok terjadi saat cuaca sangat panas?"),
                        "Jika tangkai kuning bersih tanpa bekas gigitan, sering terkait stres air/suhu; jika ada kelopak kecokelatan, cek thrips.",
                        "Stabilkan jadwal penyiraman pagi hari, beri mulsa tipis penahan penguapan, dan evaluasi naungan ringan saat siang ekstrem.",
                        "Jaga kestabilan air dan nutrisi kalium-boron secara terukur; hindari menyemprot saat bunga mekar pagi.",
                        listOf("Pasang mulsa organik tipis", "Siram konsisten saat media mulai mengering"),
                        5, "Bunga baru mampu bertahan hingga membentuk bakal buah hijau kecil.",
                        "Fisiologi Kerontokan Bunga Cabai — Hortikultura", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "tomat", "Tomat", listOf(
                    ProblemSeed(
                        "blossom_end_rot",
                        "Ujung Bawah Buah Menghitam Cekung (Blossom-End Rot)",
                        "Bagian bawah buah tomat muda hingga setengah besar tampak datar kehitaman dan mengeras atau membusuk sekunder.",
                        listOf("Bercak gelap tepat di ujung bawah buah (bekas kelopak)", "Daun umumnya masih tampak normal"),
                        listOf("Hambatan serapan kalsium akibat fluktuasi air ekstrem", "Pertumbuhan terlalu cepat akibat nitrogen berlebih"),
                        listOf("Apakah penyiraman sempat kering total lalu diguyur?", "Apakah buah bagian ujung saja yang menghitam?"),
                        "Ini adalah gangguan fisiologis air-kalsium, bukan infeksi bakteri menular dari buah ke buah.",
                        "Stabilkan penyiraman terukur di pangkal, pasang mulsa organik, dan tambahkan sumber kalsium alami seperti kapur pertanian terukur.",
                        "Perbaiki konsistensi pengairan terlebih dahulu; suplemen kalsium hanya efektif bila akar mendapat aliran air stabil.",
                        listOf("Jaga kelembapan tanah merata", "Hindari nitrogen berlebih saat berbuah"),
                        7, "Buah-buah baru pada dompol berikutnya berkembang mulus tanpa ujung hitam.",
                        "Pencegahan Blossom-End Rot Tomat — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "hawar_daun",
                        "Bercak Basah Abu-abu Kecokelatan pada Daun (Hawar Daun)",
                        "Daun tampak bercak lebar kehijauan pucat yang cepat berubah cokelat gelap hingga mengering seperti tersiram air panas.",
                        listOf("Bercak lebar tidak beraturan di tepi daun", "Lapisan putih tipis di balik daun saat pagi lembap"),
                        listOf("Infeksi Phytophthora infestans atau Alternaria", "Daun sering basah di sore/malam hari"),
                        listOf("Apakah Anda menyiram dari atas daun?", "Apakah gejala menyebar cepat saat cuaca mendung lembap?"),
                        "Hawar daun menyebar sangat cepat saat lembap dingin; bercak awal sering tampak basah di tepi daun.",
                        "Pangkas daun terinfeksi, siram hanya di pangkal media pada pagi hari, dan beri jarak antar tanaman.",
                        "Prioritaskan sanitasi daun dan penghentian siraman di daun; bila diperlukan fungisida terdaftar, konsultasikan produk yang sesuai label tomat.",
                        listOf("Siram pagi hari di zona akar", "Pangkas tunas air dan daun terbawah"),
                        3, "Daun tengah dan atas tetap hijau bersih tanpa bercak baru.",
                        "Pengendalian Hawar Daun Tomat — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "buah_pecah",
                        "Kulit Buah Tomat Pecah Melingkar / Membujur",
                        "Buah tomat yang hampir matang mengalami retakan kulit di sekitar tangkai atau sisi buah.",
                        listOf("Retakan konsentris di bahu buah atau garis vertikal", "Sering muncul setelah hujan deras"),
                        listOf("Lonjakan serapan air mendadak setelah periode kering", "Paparan panas langsung pada buah tanpa naungan daun"),
                        listOf("Apakah baru saja turun hujan deras setelah cuaca kering?", "Apakah retakan terjadi pada buah yang hampir merah?"),
                        "Retakan tanpa spora jamur atau lubang ulat menandakan tekanan turgor air internal.",
                        "Petik buah yang sudah mulai semburat merah sebelum hujan deras, pasang mulsa untuk menahan fluktuasi air media.",
                        "Atur irigasi teratur dan hindari pemupukan nitrogen tinggi saat buah hampir matang.",
                        listOf("Gunakan mulsa", "Panen buah saat fase semburat merah bila musim hujan"),
                        5, "Buah pada dompol berikutnya matang mulus tanpa retak.",
                        "Penanganan Retak Buah Tomat — Hortikultura", EvidenceLevel.C, "Rekomendasi ditinjau ahli"
                    ),
                    ProblemSeed(
                        "ulat_buah",
                        "Lubang Bulat pada Buah & Kotoran Ulat",
                        "Buah tomat muda berlubang masuk rapi dan terkadang ditemukan kotoran butiran di dekat tangkai.",
                        listOf("Lubang masuk bulat pada buah hijau/merah", "Kotoran gelap menempel di permukaan buah"),
                        listOf("Larva ulat penggerek buah tomat (Helicoverpa)", "Ngengat bertelur di daun muda sekitar bunga"),
                        listOf("Apakah ada ulat hijau atau cokelat di dalam buah?", "Apakah bunga juga terlihat berlubang?"),
                        "Lubang masuk yang cukup besar dengan sisa kotoran kasar membedakannya dari tusukan lalat buah yang sangat halus.",
                        "Periksa buah muda dan kumpulkan ulat secara manual di pagi hari, buang buah busuk berlubang di luar kebun.",
                        "Gunakan pengendalian terpadu berbasis pemantauan; jika memakai insektisida hayati Bt atau produk terdaftar, ikuti aturan label dan masa henti.",
                        listOf("Pantau balik daun dekat dompol bunga", "Jaga kebersihan gulma"),
                        4, "Tidak ditemukan lubang baru pada buah-buah muda.",
                        "PHT Ulat Buah Tomat — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "daun_menggulung_fisiologis",
                        "Daun Tua Menggulung ke Atas Seperti Pipa",
                        "Daun tua bagian bawah menggulung ke atas namun tetap hijau, kaku, dan tidak menguning atau layu.",
                        listOf("Tepi daun tua melengkung ke atas", "Tekstur daun terasa tebal renyah"),
                        listOf("Respons stres panas atau pemangkasan tunas yang terlalu agresif", "Fluktuasi kelembapan media"),
                        listOf("Apakah tanaman baru saja dipangkas berat?", "Apakah cuaca siang sangat panas terik?"),
                        "Berbeda dari virus, daun tetap hijau normal dan tidak disertai kekerdilan pucuk.",
                        "Berikan naungan sementara saat terik ekstrem, jaga kelembapan media stabil, dan hindari pemangkasan sekaligus terlalu banyak.",
                        "Pertahankan jadwal siram stabil; kondisi ini umumnya tidak memerlukan tambahan pupuk atau pestisida.",
                        listOf("Pangkas tunas air bertahap", "Gunakan mulsa penahan panas media"),
                        7, "Daun baru di pucuk tumbuh terbuka normal.",
                        "Gangguan Fisiologis Daun Menggulung Tomat — Riset Agronomi", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "terong", "Terong Ungu", listOf(
                    ProblemSeed(
                        "penggerek_pucuk_buah",
                        "Pucuk Layu Menggantung & Buah Berlubang",
                        "Pucuk batang muda tiba-tiba layu terkulai atau buah memiliki lubang kecil tertutup kotoran.",
                        listOf("Ujung tunas tunggal terkulai layu", "Bekas lubang kecil di pangkal kelopak buah"),
                        listOf("Larva penggerek pucuk dan buah terong (Leucinodes orbonalis)"),
                        listOf("Apakah hanya pucuk tertentu yang layu?", "Apakah ada saluran gerekan di dalam batang pucuk?"),
                        "Layu hanya terjadi pada pucuk yang digerek, sementara cabang lain tetap segar.",
                        "Potong pucuk yang layu 3 cm di bawah bagian layu dan musnahkan larva di dalamnya; buang buah berlubang.",
                        "Lakukan sanitasi mekanis rutin tiap 2 hari; konsultasikan produk terdaftar bila serangan meluas ke lebih dari 20% buah.",
                        listOf("Sanitasi pucuk layu segera", "Pasang perangkap feromon bila tersedia"),
                        4, "Pucuk-pucuk baru tumbuh tegak tanpa layu lokal.",
                        "PHT Penggerek Buah Terong — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "kutu_kebul",
                        "Kumpulan Kutu Putih Kecil Terbang di Balik Daun",
                        "Saat daun tersentuh, serangga kecil putih beterbangan dan permukaan daun tampak lengket atau hitam jelaga.",
                        listOf("Serangga kecil putih di bawah daun", "Permukaan atas daun lengket atau berjelaga hitam"),
                        listOf("Infestasi kutu kebul (Bemisia tabaci)", "Jamur embun jelaga tumbuh di cairan manis kutu"),
                        listOf("Apakah banyak serangga putih terbang saat daun digoyang?", "Apakah ada semut di sekitar batang?"),
                        "Lapisan hitam embun jelaga hanyalah jamur sekunder yang hidup di embun madu kutu, bukan penyakit daun langsung.",
                        "Semprot sisi bawah daun dengan air bertekanan ringan di pagi hari, pasang perangkap likat kuning, dan pangkas daun tua penuh kutu.",
                        "Prioritaskan perangkap kuning dan pembersihan daun tua; hindari insektisida spektrum luas yang membunuh musuh alami.",
                        listOf("Kendalikan gulma inang sekitar kebun", "Jaga sirkulasi udara antar tanaman"),
                        4, "Populasi serangga terbang di balik daun berkurang nyata.",
                        "Pengendalian Kutu Kebul Terong — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "kumbang_daun",
                        "Daun Berlubang Seperti Jendela atau Renda",
                        "Permukaan daun terong dimakan hingga menyisakan tulang daun tipis atau lubang tidak beraturan.",
                        listOf("Jaringan hijau daun terkikis transparan", "Terlihat kumbang kecil bulat berbintik kuning-hitam (Epilachna)"),
                        listOf("Kumbang daun (Epilachna) atau larvanya yang berduri kecil"),
                        listOf("Apakah terlihat kumbang kubah kecil atau larva kuning berduri di balik daun?"),
                        "Kumbang Epilachna mengikis lapisan daging daun sehingga tampak seperti renda transparan, berbeda dari ulat yang memotong tepi daun.",
                        "Kumpulkan kumbang dan kelompok telur kuning di balik daun secara manual pada pagi hari.",
                        "Sanitasi daun berlubang berat dan pengendalian mekanis umumnya sudah memadai untuk skala pekarangan.",
                        listOf("Cek kelompok telur di balik daun tiap 3 hari"),
                        3, "Daun muda baru tumbuh utuh tanpa kikisan baru.",
                        "Hama Kumbang Daun Solanaceae — Balitsa", EvidenceLevel.D, "Teruji lapangan"
                    ),
                    ProblemSeed(
                        "bercak_daun_terong",
                        "Bercak Cokelat Bertepi Kuning di Daun Bawah",
                        "Daun tua terong memunculkan bercak bundar cokelat keabuan yang melebar dan membuat daun rontok.",
                        listOf("Bercak bulat di daun bawah", "Daun tua mudah gugur saat lembap"),
                        listOf("Infeksi jamur bercak daun (Cercospora)", "Percikan air siraman dan kelembapan berlebih"),
                        listOf("Apakah bercak dimulai dari daun paling dekat tanah?"),
                        "Dimulai dari daun tertua yang lembap, tidak menyebabkan layu mendadak di pucuk.",
                        "Petik daun bawah yang berbercak, siram hanya di media akar, dan lapisi permukaan media dengan mulsa.",
                        "Perbaiki sanitasi dan sirkulasi udara; konsultasikan penyuluh bila bercak naik hingga daun produktif.",
                        listOf("Pangkas daun tua menyentuh tanah", "Siram pagi hari"),
                        5, "Daun produktif di bagian tengah-atas tetap hijau bersih.",
                        "Penyakit Daun Terong — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "buah_kecil_bengkok",
                        "Buah Terong Tumbuh Kecil, Kusam, atau Bengkok",
                        "Buah berhenti membesar lebih cepat dari biasanya, kulit kurang mengilap, atau melengkung.",
                        listOf("Ukuran buah kerdil", "Kulit tampak kusam kecokelatan"),
                        listOf("Kekurangan air saat pembesaran buah", "Wadah polybag terlalu kecil atau padat akar", "Serangan tungau pada bakal buah"),
                        listOf("Berapa ukuran polybag/pot yang digunakan?", "Apakah penyiraman sering terlambat saat siang panas?"),
                        "Jika kulit kusam kasar seperti berkarat, curigai tungau; jika hanya kecil mulus, curigai keterbatasan ruang akar atau air.",
                        "Pindah ke wadah lebih besar (minimal 35–40 cm) bila akar penuh, tambah kompos matang, dan jaga air rutin.",
                        "Pastikan ukuran wadah cukup dan seimbangkan nutrisi kalium serta pengairan saat fase buah.",
                        listOf("Gunakan polybag min. 35–40 cm", "Jangan biarkan media mengering total"),
                        7, "Buah dari bunga baru tumbuh lurus dan mengilap.",
                        "Manajemen Mutu Terong — Hortikultura", EvidenceLevel.C, "Rekomendasi ditinjau ahli"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "kangkung", "Kangkung Darat", listOf(
                    ProblemSeed(
                        "karat_putih",
                        "Bintik Putih Seperti Tepung di Bawah Daun (Karat Putih)",
                        "Permukaan bawah daun kangkung dipenuhi lepuhan putih menonjol dan bagian atasnya tampak menguning.",
                        listOf("Bintik putih kapur menonjol di sisi bawah daun", "Bercak kuning pucat di sisi atas daun"),
                        listOf("Infeksi Albugo ipomoeae-panduratae (karat putih)", "Kelembapan tinggi dan jarak tanam terlalu rapat"),
                        listOf("Apakah bintik putih terasa seperti serbuk menonjol di bawah daun?"),
                        "Karat putih membentuk bintik putih padat di sisi bawah daun, bukan sekadar debu atau sisa air.",
                        "Cabut atau petik daun/batang yang terserang parah agar tidak menular, jarangkan tanaman, dan kurangi siram sore hari.",
                        "Karena siklus kangkung sangat pendek (25 hari), sanitasi daun dan perbaikan jarak tanam jauh lebih aman daripada fungisida.",
                        listOf("Jaga jarak tanam cukup longgar", "Siram di pagi hari"),
                        4, "Tanaman baru atau daun muda tumbuh bersih tanpa lepuhan putih.",
                        "Penyakit Karat Putih Kangkung — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "daun_berlubang_ulat",
                        "Daun Berlubang Dimakan Ulat atau Belalang",
                        "Helaian daun tampak berlubang tidak beraturan atau sobek di bagian tepi.",
                        listOf("Lubang menganga di tengah atau tepi daun", "Kadang terlihat kotoran hijau kecil di daun"),
                        listOf("Ulat grayak / ulat pemakan daun", "Belalang atau siput malam hari"),
                        listOf("Apakah lubang muncul dalam 1 malam?"),
                        "Siput meninggalkan jejak lendir mengilap; ulat meninggalkan kotoran butiran hijau/hitam.",
                        "Periksa dan ambil ulat secara manual pada pagi dan malam hari; gunakan sungkup jaring halus bila sering berulang.",
                        "Hindari penyemprotan insektisida kimia pada sayuran daun umur pendek; prioritaskan penghalang fisik (insect net).",
                        listOf("Pasang jaring pelindung sederhana", "Bersihkan gulma di sekitar wadah"),
                        3, "Daun baru muncul utuh tanpa lubang baru.",
                        "Budidaya Sayuran Daun Bebas Residu — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "rebah_semai",
                        "Kecambah Kangkung Roboh dan Pangkal Membusuk",
                        "Bibit muda umur 4–10 hari mendadak roboh di permukaan tanah dengan pangkal batang mengecil basah.",
                        listOf("Batang di garis tanah mengerut hitam/cokelat", "Media sangat basah dan padat"),
                        listOf("Rebah semai (damping-off) oleh jamur Pythium/Rhizoctonia", "Penyiraman berlebihan dan penaburan benih terlalu rapat"),
                        listOf("Apakah media selalu basah kuyup dan teduh?"),
                        "Roboh terjadi karena pangkal batang membusuk di batas tanah, bukan patah karena angin.",
                        "Kurangi frekuensi penyiraman, jemur media agar lebih kering dan gembur, dan tabur benih lebih jarang.",
                        "Gunakan media tanam baru yang porus dan kurangi penyiraman; fungisida tidak disarankan untuk semai sayuran daun rumah tangga.",
                        listOf("Pastikan media gembur porus", "Berikan sinar matahari pagi cukup"),
                        4, "Bibit tersisa tetap berdiri tegak dengan pangkal batang kokoh.",
                        "Pengendalian Rebah Semai — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "daun_pucat_kurus",
                        "Batang Kangkung Kurus Pucat & Tumbuh Lambat",
                        "Tanaman tumbuh memanjang kurus dengan daun kecil berwarna hijau kekuningan.",
                        listOf("Batang kecil panjang", "Warna hijau pudar merata di semua daun"),
                        listOf("Kurang sinar matahari (etiolasi)", "Media miskin bahan organik / nitrogen"),
                        listOf("Berapa jam tanaman mendapat sinar matahari langsung setiap hari?"),
                        "Jika batang memanjang mencari cahaya dan rebah, penyebab utamanya kurang sinar matahari, bukan hanya kurang pupuk.",
                        "Pindahkan wadah ke area lebih terang (5–7 jam matahari) dan tambahkan lapisan tipis kompos matang halus.",
                        "Pastikan cahaya cukup terlebih dahulu baru berikan pupuk nitrogen dosis sangat ringan bila umur <12 hari.",
                        listOf("Tempatkan di area terbuka terang", "Gunakan 50% kompos matang di media"),
                        5, "Daun baru tumbuh lebih lebar dan hijau segar.",
                        "Panduan Kangkung Pekarangan — BPTP", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "daun_kecil_berbunga_dini",
                        "Kangkung Cepat Mengeluarkan Kuncup Bunga & Batang Alot",
                        "Tanaman belum tinggi maksimal namun batang sudah keras berserat dan mulai membentuk kuncup bunga.",
                        listOf("Batang terasa liat", "Muncul kuncup bunga putih/ungu muda di ketiak daun"),
                        listOf("Kekurangan air berkepanjangan", "Umur panen sudah terlewat (>30 hari)"),
                        listOf("Sudah berapa hari sejak tanam dan apakah media sering kekeringan?"),
                        "Stres air memicu tanaman cepat masuk fase reproduktif untuk bertahan hidup.",
                        "Segera panen bagian pucuk yang masih muda, perbaiki keteraturan air untuk siklus tanam berikutnya.",
                        "Panen tepat waktu di umur 21–28 hari dan jaga kelembapan tanah merata.",
                        listOf("Panen sebelum umur 30 hari", "Jaga kelembapan media stabil"),
                        7, "Siklus tanam berikutnya menghasilkan batang renyah.",
                        "Budidaya Kangkung Darat — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "bayam", "Bayam Hijau / Merah", listOf(
                    ProblemSeed(
                        "kerdil_kepadatan",
                        "Bayam Tumbuh Kerdil Karena Terlalu Rapat",
                        "Bibit tumbuh berdesakan, batang sangat halus, dan daun tetap kecil tidak melebar.",
                        listOf("Puluhan bibit tumbuh berhimpitan dalam satu titik", "Daun bawah cepat menguning karena ternaungi"),
                        listOf("Penaburan biji tidak merata tanpa penjarangan"),
                        listOf("Apakah biji bayam ditabur langsung tanpa dicampur pasir/kompos halus?"),
                        "Daun bawah menguning karena berebut cahaya dan ruang akar, bukan penyakit.",
                        "Lakukan penjarangan segera dengan mencabut sebagian bibit hingga tersisa jarak 5–8 cm antar batang.",
                        "Penjarangan mekanis adalah solusi utama; pupuk tambahan tidak dapat menggantikan ruang tumbuh.",
                        listOf("Campur biji dengan kompos kering saat menebar", "Jarangkan di umur 10 hari"),
                        5, "Tanaman yang tersisa cepat membesar dan berdaun lebar.",
                        "Teknik Tanam Bayam Cabut — BPTP", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "kutu_daun_bayam",
                        "Daun Muda Melengkung & Terdapat Kutu Hijau/Hitam",
                        "Daun muda di pucuk menggulung ke dalam dan di cekungannya terdapat koloni kutu kecil.",
                        listOf("Pucuk daun mengerut tidak beraturan", "Terlihat kutu lunak kecil dan semut"),
                        listOf("Serangan kutu daun (Aphis)"),
                        listOf("Apakah terlihat kutu lunak menempel di balik lekukan daun muda?"),
                        "Kutu daun mudah terlihat dengan mata telanjang jika balik daun diperiksa.",
                        "Petik pucuk yang penuh kutu atau bilas dengan semprotan air terarah di pagi hari.",
                        "Prioritaskan pembersihan mekanis dan jaring pelindung karena masa panen bayam sangat singkat.",
                        listOf("Kendalikan semut di sekitar pot", "Gunakan jaring serangga"),
                        3, "Pucuk baru tumbuh membuka normal bebas koloni kutu.",
                        "PHT Sayuran Daun — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "busuk_pangkal_bayam",
                        "Pangkal Batang Kecambah Membusuk Basah",
                        "Bibit bayam umur 1–2 minggu tumbang dengan pangkal batang benyek kehitaman.",
                        listOf("Pangkal batang basah mengempis", "Permukaan media berlumut hijau"),
                        listOf("Penyiraman terlalu deras atau media tergenang", "Kurang sinar matahari"),
                        listOf("Apakah permukaan media tampak berlumut dan selalu becek?"),
                        "Lumut hijau di permukaan media adalah tanda jelas kelembapan berlebih dan kurang sirkulasi.",
                        "Hentikan penyiraman sementara, gemburkan tipis permukaan antar barisan, dan pindahkan ke area lebih banyak cahaya pagi.",
                        "Perbaikan drainase dan pengurangan air adalah langkah wajib.",
                        listOf("Gunakan media porus", "Siram dengan gembor halus secukupnya"),
                        4, "Lumut mengering dan bibit tersisa tumbuh kokoh.",
                        "Pengelolaan Semai Bayam — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "bercak_daun_bayam",
                        "Bercak Putih / Cokelat Kecil di Daun",
                        "Muncul titik-titik putih kekuningan atau cokelat bertepi merah pada daun bayam.",
                        listOf("Bintik bulat kecil tersebar di daun", "Lebih parah pada daun bawah saat hujan"),
                        listOf("Penyakit karat putih atau bercak daun Cercospora"),
                        listOf("Apakah tanaman tersiram hujan langsung dan terpercik tanah?"),
                        "Bercak bertepi kemerahan sering terkait Cercospora yang terbawa percikan tanah.",
                        "Petik daun yang berbercak berat dan siram hanya di pangkal media pada pagi hari.",
                        "Panen lebih awal bila sudah layak konsumsi dan perbarui media sebelum tanam ulang.",
                        listOf("Jaga jarak tanam tidak terlalu padat", "Siram pagi di media"),
                        4, "Daun muda bebas bercak baru.",
                        "Penyakit Daun Sayuran Bayam — Balitsa", EvidenceLevel.C, "Rekomendasi ditinjau ahli"
                    ),
                    ProblemSeed(
                        "penggorok_daun",
                        "Garis Alur Putih Berkelok di Dalam Daun (Leafminer)",
                        "Di permukaan daun tampak alur garis putih berliku-liku seperti peta akibat jaringan dalam daun dimakan.",
                        listOf("Corak garis putih berkelok di dalam helai daun"),
                        listOf("Larva lalat pengorok daun (Liriomyza)"),
                        listOf("Apakah garis putih tersebut berada di dalam lapisan daun (bukan kotoran di luar)?"),
                        "Alur berkelok di dalam jaringan daun adalah ciri khas leafminer, berbeda dari jamur atau debu.",
                        "Petik dan buang helai daun yang terdapat alur agar pupa tidak menetas lagi; pasang perangkap likat kuning.",
                        "Sanitasi daun beralur sangat efektif memutus siklus lalat pengorok di pekarangan.",
                        listOf("Pasang perangkap kuning", "Jaga kebersihan daun sisa panen"),
                        5, "Daun baru tumbuh hijau polos tanpa alur putih.",
                        "Pengendalian Liriomyza — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "sawi", "Sawi Hijau (Caisim)", listOf(
                    ProblemSeed(
                        "ulat_daun_kubis",
                        "Daun Berlubang Kecil Banyak Seperti Jendela (Plutella)",
                        "Daun muda hingga dewasa berlubang-lubang kecil hingga tinggal lapisan tipis seperti jendela.",
                        listOf("Banyak lubang kecil seragam", "Ulat hijau sangat kecil lincah menggantung benang saat disentuh"),
                        listOf("Ulat tritip / ulat daun kubis (Plutella xylostella)"),
                        listOf("Apakah ulat hijau kecil menjatuhkan diri dengan benang halus bila daun disentuh?"),
                        "Ulat Plutella berukuran sangat kecil (<1 cm) dan menggantung dengan benang sutra bila terganggu.",
                        "Periksa bawah daun dan kumpulkan secara manual, pasang sungkup jaring halus agar kupu-kupu tidak bertelur.",
                        "Untuk skala pekarangan, jaring pelindung dan pemungutan manual sangat dianjurkan; produk hayati Bt terdaftar menjadi opsi PHT bila tersedia.",
                        listOf("Gunakan insect net pada bedengan/planter", "Pantau bawah daun 2 hari sekali"),
                        4, "Daun tengah baru tumbuh mulus tanpa lubang-lubang kecil.",
                        "PHT Ulat Daun Kubis — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "akar_gada",
                        "Tanaman Layu di Siang Terik & Akar Membengkak (Akar Gada)",
                        "Sawi tampak layu saat siang panas meski media lembap, dan saat dicabut akarnya membengkak tidak normal.",
                        listOf("Akar membengkak berbenjol-benjol seperti gada", "Daun layu siang dan pulih sementara di malam hari"),
                        listOf("Plasmodiophora brassicae pada tanah masam dan basah"),
                        listOf("Apakah keluarga sawi/pakcoy/kubis ditanam berulang di media yang sama?", "Apakah media cenderung masam dan lembap?"),
                        "Diagnosis pasti membutuhkan pemeriksaan akar yang membengkak; penyakit ini tidak bisa disembuhkan setelah menginfeksi.",
                        "Cabut dan musnahkan tanaman terinfeksi di luar kebun, ganti media atau rotasikan dengan tanaman bukan famili kubis selama minimal 2–3 siklus.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyembuhkan tanaman yang sudah terinfeksi akar gada; fokus pada pengapuran (pH >= 6,5) dan rotasi.",
                        listOf("Rotasi tanaman non-Brassicaceae", "Jaga pH media di kisaran 6,2–6,8"),
                        7, "Tanaman rotasi tumbuh sehat dan media baru bebas pembengkakan akar.",
                        "Penyakit Akar Gada Brassicaceae — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "busuk_lunak_sawi",
                        "Pangkal Batang Lembek Berbau Busuk (Busuk Lunak)",
                        "Tangkai daun bagian bawah berubah cokelat basah, lunak berlendir, dan mengeluarkan bau menyengat.",
                        listOf("Jaringan pangkal basah berlendir", "Bau busuk khas bakteri pembusuk"),
                        listOf("Bakteri Pectobacterium carotovorum", "Luka akibat ulat atau cipratan air berlebih di pusat batang"),
                        listOf("Apakah pangkal daun berbau busuk dan lembek saat disentuh?"),
                        "Bau menyengat dan lendir basah membedakan busuk bakteri dari layu kekeringan biasa.",
                        "Singkirkan segera tanaman yang sudah berlendir agar tidak menular lewat air siraman; siram hati-hati di tepi wadah.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyelamatkan batang yang sudah busuk lunak berat; utamakan sanitasi dan perbaikan drainase.",
                        listOf("Hindari menyiram menggenang di tengah tajuk", "Cegah luka mekanis pada pangkal"),
                        3, "Tanaman tetangga tetap segar dan kering di pangkal.",
                        "Busuk Lunak Sayuran Kubis — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "siput_daun",
                        "Daun Robek Kasar & Ada Jejak Lendir Mengilap",
                        "Daun muda hilang dimakan dalam semalam disertai garis berkilau kering di permukaan daun atau pot.",
                        listOf("Jejak lendir kering mengilap", "Lubang besar tidak beraturan terutama di daun tepi"),
                        listOf("Siput telanjang atau bekicot kecil aktif malam hari"),
                        listOf("Apakah kerusakan muncul saat pagi hari dengan jejak keperakan mengilap?"),
                        "Jejak lendir kering adalah petunjuk paling jelas aktivitas siput malam hari.",
                        "Ambil siput secara manual saat senja/pagi hari, bersihkan tempat persembunyian lembap di bawah pot, dan beri pembatas cangkang telur/abu kering di tepi wadah.",
                        "Gunakan pengendalian mekanis dan sanitasi kolong pot sebelum mempertimbangkan umpan siput resmi.",
                        listOf("Angkat pot dari permukaan tanah lembap", "Bersihkan dedaunan busuk di sekitar kebun"),
                        3, "Tidak ditemukan kerusakan baru atau jejak lendir di pagi hari.",
                        "Pengendalian Hama Siput Pekarangan — BPTP", EvidenceLevel.D, "Teruji lapangan"
                    ),
                    ProblemSeed(
                        "daun_ungu_dingin_hara",
                        "Tangkai dan Tepi Daun Berwarna Keunguan Kerdil",
                        "Daun sawi berwarna hijau gelap keunguan dengan pertumbuhan sangat lambat.",
                        listOf("Sisi bawah daun atau tepi daun semburat ungu", "Akar terlihat pendek saat media terlalu padat/masam"),
                        listOf("Hambatan serapan Fosfor (P) akibat media terlalu padat, dingin, atau pH rendah"),
                        listOf("Apakah media tanam terasa sangat padat atau pH terlalu asam?"),
                        "Warna ungu pada varietas sawi hijau umumnya berkaitan dengan gangguan serapan fosfor.",
                        "Gemburkan media sekitar perakaran, tambahkan kompos matang, dan cek pH media (bila asam, beri kapur pertanian tipis).",
                        "Evaluasi pH dan kepadatan media sebelum menambah pupuk fosfat agar unsur bisa diserap akar.",
                        listOf("Gunakan media gembur kaya kompos", "Pastikan pH netral 6,0–6,8"),
                        7, "Daun baru muncul berwarna hijau cerah normal.",
                        "Diagnosis Hara Tanaman Sayuran — Ilmu Tanah", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "pakcoy", "Pakcoy", listOf(
                    ProblemSeed(
                        "tipburn_pakcoy",
                        "Tepi Daun Muda Mengering Cokelat (Tipburn)",
                        "Ujung-ujung daun muda di bagian dalam roset tampak layu tipis lalu mengering cokelat atau kehitaman.",
                        listOf("Terbatas pada ujung/tepi daun muda bagian dalam", "Daun tua umumnya utuh"),
                        listOf("Gangguan aliran kalsium ke daun muda akibat panas berlebih atau penyiraman tidak konsisten", "Media terlalu pekat garam pupuk"),
                        listOf("Apakah cuaca siang sangat panas dan pengap?", "Apakah baru saja diberi pupuk dosis tinggi?"),
                        "Tipburn tidak menyebar seperti bercak jamur dan tidak berlendir seperti busuk lunak.",
                        "Pindahkan ke tempat dengan naungan ringan siang hari, bilas media dengan air bersih secukupnya bila baru dipupuk pekat, dan jaga siram pagi rutin.",
                        "Kurangi kepekatan nutrisi pupuk dan stabilkan pengairan serta sirkulasi udara pagi hari.",
                        listOf("Berikan cahaya pagi + teduh parsial siang terik", "Hindari pemupukan berlebih"),
                        5, "Daun-daun baru di pusat roset tumbuh dengan tepi hijau mulus.",
                        "Fisiologi Tipburn pada Pakcoy — Riset Hortikultura", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed(
                        "busuk_mahkota_pakcoy",
                        "Pusat Roset Daun Lembek & Berbau",
                        "Daun-daun tengah pakcoy mudah lepas saat ditarik ringan karena pangkalnya membusuk basah.",
                        listOf("Pangkal tangkai putih berubah cokelat lembek", "Tercium bau busuk"),
                        listOf("Air terperangkap di tengah roset daun terlalu lama", "Infeksi bakteri busuk lunak"),
                        listOf("Apakah penyiraman dilakukan mengguyur tepat ke tengah daun di sore hari?"),
                        "Jika pangkal sudah berbau busuk menyengat dan berlendir, bakteri pembusuk telah aktif.",
                        "Pisahkan tanaman busuk segera; untuk tanaman lain, siram hanya langsung ke media di tepi pot pada pagi hari.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyelamatkan tanaman yang pusat rosetnya sudah hancur membusuk.",
                        listOf("Selalu siram ke media, bukan ke tengah daun", "Gunakan mulsa sekam bakar di permukaan media"),
                        3, "Tanaman pakcoy lain tetap kokoh dengan tangkai putih bersih.",
                        "Manajemen Busuk Lunak Pakcoy — BPTP", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "kutu_daun_pakcoy",
                        "Daun Mengkerut & Lengket Akibat Kutu Daun",
                        "Bagian bawah daun pakcoy dikerumuni kutu kecil hijau/abu-abu sehingga daun melengkung.",
                        listOf("Koloni kutu di lipatan bawah daun", "Permukaan daun mengilap lengket"),
                        listOf("Kutu daun (Myzus persicae / Lipaphis)"),
                        listOf("Apakah terlihat koloni kutu berkelompok di balik tangkai daun?"),
                        "Kerutan disertai koloni serangga kecil dan embun madu lengket.",
                        "Bilas balik daun dengan semprotan air terarah di pagi hari dan petik daun luar yang terinfeksi berat.",
                        "Gunakan penghalang fisik (jaring halus) dan hindari insektisida kimia mendekati panen.",
                        listOf("Inspeksi balik daun 2–3 hari sekali", "Jaga kebersihan gulma"),
                        3, "Koloni kutu hilang dan daun muda membuka rata.",
                        "Pengendalian Kutu Daun Brassicaceae — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "kumbang_kutu_flea_beetle",
                        "Lubang-Lubang Kecil Bundar Seperti Bekas Tembakan (Flea Beetle)",
                        "Daun pakcoy dipenuhi banyak lubang bulat kecil berdiameter 1–2 mm.",
                        listOf("Puluhan lubang bundar sangat kecil (shot-hole)", "Terlihat kumbang hitam sangat kecil yang melompat saat didekati"),
                        listOf("Kumbang pelompat (Phyllotreta spp.)"),
                        listOf("Apakah ada serangga hitam mengilap sangat kecil yang melompat cepat?"),
                        "Lubang bulat kecil terpisah-pisah tanpa jejak lendir atau ulat merupakan ciri khas flea beetle.",
                        "Pasang sungkup jaring halus segera saat pindah tanam dan gemburkan permukaan media di pagi hari.",
                        "Jaring pelindung halus (insect net) adalah metode paling efektif dan aman untuk sayuran daun ini.",
                        listOf("Pasang insect net sejak hari pertama pindah tanam"),
                        5, "Daun baru tumbuh utuh tanpa lubang tembakan.",
                        "Hama Kumbang Pelompat Pakcoy — Literatur PHT", EvidenceLevel.D, "Teruji lapangan"
                    ),
                    ProblemSeed(
                        "daun_tua_menguning_pakcoy",
                        "Daun Terluar Menguning Sebelum Waktunya",
                        "Satu atau dua daun paling luar berubah kuning sementara bagian dalam roset tetap hijau.",
                        listOf("Hanya daun paling bawah/luar yang menguning", "Wadah planter cukup dangkal"),
                        listOf("Kekurangan ruang akar atau hara ringan", "Penuaan alami daun terbawah atau media terlalu basah"),
                        listOf("Apakah akar sudah memenuhi pot?", "Apakah media terasa sangat basah?"),
                        "Jika hanya 1–2 daun terluar saat umur >25 hari, itu sering wajar; jika terjadi di umur muda, cek drainase dan kecukupan kompos.",
                        "Petik daun luar yang kuning, periksa lubang drainase, dan beri top-dress kompos saring halus.",
                        "Cek kelembapan media terlebih dahulu sebelum memberi larutan nutrisi ringan.",
                        listOf("Gunakan planter kedalaman min. 15–20 cm", "Jarak tanam 15–20 cm"),
                        4, "Roset bagian dalam tumbuh subur hijau tua.",
                        "Panduan Pakcoy Urban Farming — BPTP", EvidenceLevel.A, "Panduan teknis resmi"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "selada", "Selada Hijau / Keriting", listOf(
                    ProblemSeed(
                        "bolting_selada",
                        "Batang Selada Memanjang Tinggi & Daun Terasa Pahit (Bolting)",
                        "Pusat tanaman memanjang ke atas membentuk tangkai tinggi, daun mengecil, dan getah terasa pahit.",
                        listOf("Batang tengah memanjang cepat", "Daun baru mengecil dan berasa pahit"),
                        listOf("Suhu udara terlalu panas dan paparan terik siang berlebih", "Umur tanaman sudah tua"),
                        listOf("Apakah selada diletakkan di tempat panas terik seharian?"),
                        "Bolting adalah respons fisiologis suhu panas, bukan penyakit infeksi.",
                        "Segera panen daun luar yang masih enak sebelum makin pahit; siklus berikutnya beri naungan paranet siang dan pilih varietas tahan panas.",
                        "Tidak ada pupuk atau obat yang mengembalikan tanaman bolting menjadi roset lagi.",
                        listOf("Beri paranet 40–50% saat siang panas", "Gunakan varietas tahan dataran rendah"),
                        7, "Penanaman berikutnya di lokasi lebih sejuk membentuk roset padat.",
                        "Adaptasi Selada Dataran Rendah — Riset Hortikultura", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed(
                        "tipburn_selada",
                        "Tepi Daun Muda Mengering Gosong",
                        "Ujung tepi daun muda mengerut kering cokelat kehitaman.",
                        listOf("Tepi daun dalam mengering", "Pertumbuhan sedikit terhambat"),
                        listOf("Sirkulasi udara terhambat di cuaca hangat", "Gangguan pasokan air & kalsium"),
                        listOf("Apakah selada diletakkan di sudut tertutup tanpa aliran udara?"),
                        "Terjadi simetris di tepi daun termuda, tidak berlendir.",
                        "Perbaiki aliran udara, berikan siraman pagi teratur, dan jaga suhu media dengan mulsa terang/sekam.",
                        "Kurangi kepekatan larutan pupuk dan pastikan pengairan pagi rutin.",
                        listOf("Letakkan di area berangin lembut dengan cahaya pagi", "Siram teratur pagi hari"),
                        5, "Daun muda berikutnya tumbuh dengan tepi segar.",
                        "Manajemen Tipburn Selada — Hortikultura", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed(
                        "busuk_pangkal_selada",
                        "Daun Bawah Lembek Cokelat Menyentuh Media",
                        "Daun-daun paling bawah yang menempel di media basah membusuk cokelat berair.",
                        listOf("Daun bawah benyek cokelat", "Media permukaan selalu basah"),
                        listOf("Busuk pangkal / bottom rot (Rhizoctonia) akibat daun menempel tanah basah"),
                        listOf("Apakah daun terbawah terendam air atau menempel media basah?"),
                        "Pembusukan berawal tepat dari area kontak antara helai daun bawah dan media basah.",
                        "Buang daun bawah yang membusuk, lapisi permukaan media dengan sekam bakar kering sebagai pembatas bersih, dan siram di pagi hari saja.",
                        "Sanitasi daun kontak tanah dan menjaga permukaan media lebih kering sangat efektif.",
                        listOf("Gunakan lapisan atas sekam bakar/kerikil bersih", "Hindari menyiram malam hari"),
                        4, "Daun atas tetap segar renyah tanpa pembusukan berlanjut.",
                        "Penyakit Bottom Rot Selada — Literatur Patologi", EvidenceLevel.C, "Rekomendasi ditinjau ahli"
                    ),
                    ProblemSeed(
                        "etiolasi_selada",
                        "Daun Lemas Menjuntai & Pucat",
                        "Tangkai daun memanjang lemah ke samping dengan warna hijau sangat muda.",
                        listOf("Roset terbuka melebar lemas, tidak kompak", "Warna daun pucat"),
                        listOf("Kekurangan intensitas cahaya pagi"),
                        listOf("Berapa jam cahaya matahari langsung diterima selada?"),
                        "Selada butuh cahaya pagi 4–5 jam; tempat terlalu teduh di dalam teras beratap membuat daun lemas.",
                        "Pindahkan wadah ke lokasi yang menerima cahaya matahari pagi langsung minimal 4 jam.",
                        "Cahaya cukup lebih menentukan kekompakan daun daripada penambahan pupuk.",
                        listOf("Tempatkan di sisi timur pekarangan/balkon"),
                        5, "Daun baru tumbuh lebih tegak dan hijau lebih pekat.",
                        "Kebutuhan Cahaya Selada — BPTP", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "kutu_daun_selada",
                        "Kutu Hijau di Celah Dalam Daun Selada",
                        "Kutu lunak kecil bersembunyi di lipatan terdalam roset daun selada.",
                        listOf("Daun keriting tidak simetris", "Ada kutu kecil dan kulit ganti kutu berwarna putih"),
                        listOf("Infestasi kutu daun (Nasonovia / Myzus)"),
                        listOf("Apakah terdapat kulit kutu putih kecil menempel di lipatan daun?"),
                        "Kutu selada sering bersembunyi di celah terdalam sehingga perlu membuka lembut kelopak daun saat memeriksa.",
                        "Bilas lembut celah daun dengan semprotan air pagi dan potong daun terinfeksi berat.",
                        "Gunakan jaring serangga halus sejak awal tanam karena selada dimakan segar.",
                        listOf("Gunakan insect net", "Inspeksi celah daun tiap 3 hari"),
                        4, "Lipatan daun dalam bersih dari kutu dan kulit putih.",
                        "PHT Selada Segar — Balitsa", EvidenceLevel.D, "Teruji lapangan"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "bawang_merah", "Bawang Merah", listOf(
                    ProblemSeed(
                        "bercak_ungu",
                        "Bercak Putih Cekung Berubah Ungu di Daun (Trotol)",
                        "Daun bawang memiliki bercak cekung pucat yang melebar menjadi bercak ungu kecokelatan dengan cincin konsentris.",
                        listOf("Bercak cekung oval berwarna ungu/cokelat", "Ujung daun di atas bercak mengering dan patah"),
                        listOf("Jamur Alternaria porri (bercak ungu/trotol)", "Kelembapan tinggi dan embun malam berkepanjangan"),
                        listOf("Apakah bercak muncul saat musim hujan atau setelah penyiraman sore?"),
                        "Cincin keunguan cekung pada daun berongga adalah tanda khas trotol.",
                        "Gunting bagian ujung daun yang terinfeksi agar tidak patah menulari umbi, siram hanya di pagi hari langsung ke tanah.",
                        "Perbaiki sirkulasi dan hindari pemupukan N berlebih yang membuat daun lunak; konsultasikan fungisida terdaftar bila meluas di lahan.",
                        listOf("Hindari menyiram dari atas pada sore hari", "Gunakan drainase bedengan/pot sangat baik"),
                        5, "Daun-daun baru tumbuh tegak tanpa perluasan bercak ungu.",
                        "Pengendalian Penyakit Trotol Bawang Merah — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "ulat_bawang",
                        "Ujung Daun Menguning Berlubang & Ada Ulat di Dalam Rongga",
                        "Daun tampak putih transparan terkikis dari dalam atau berlubang memanjang dan patah.",
                        listOf("Bercak putih memanjang transparan pada daun berongga", "Terkadang ada ulat hijau/cokelat di dalam pipa daun"),
                        listOf("Ulat bawang (Spodoptera exigua)"),
                        listOf("Apakah terlihat ulat di dalam rongga daun saat daun dibelah perlahan?"),
                        "Berbeda dari bercak ungu, kerusakan ulat bawang menampakkan kulit luar daun yang masih tipis bening sebelum berlubang.",
                        "Pencet perlahan atau gunting daun yang berisi ulat dan telur di ujung daun secara rutin pagi/sore.",
                        "Lakukan pemantauan kelompok telur putih berserabut di ujung daun; pasang perangkap lampu/likat bila skala luas.",
                        listOf("Buang daun berlubang berisi ulat", "Pantau ujung daun tiap 2 hari"),
                        4, "Daun baru tumbuh utuh tidak terputus atau berlubang putih.",
                        "PHT Ulat Bawang — Balitsa Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "busuk_umbi_moler",
                        "Daun Melintir Kuning Pucat & Umbi Membusuk (Moler)",
                        "Daun tumbuh memanjang melengkung ke bawah, pucat kekuningan, dan pangkal umbi mudah tercabut membusuk.",
                        listOf("Daun memanjang melintir tidak wajar", "Pangkal umbi basah membusuk dengan benang jamur putih"),
                        listOf("Infeksi jamur Fusarium oxysporum (penyakit moler)", "Bibit umbi terinfeksi atau media terlalu basah"),
                        listOf("Apakah daun terlihat melintir panjang pucat sejak awal?", "Apakah dasar umbi busuk putih?"),
                        "Pemanjangan daun melintir (bukan layu lurus) merupakan ciri khas moler akibat gangguan hormon jamur di pangkal umbi.",
                        "Cabut segera rumpun yang terkena beserta tanah di sekitarnya agar tidak menyebar lewat air; jangan pakai ulang bibit dari rumpun tersebut.",
                        "Belum tersedia rekomendasi tervalidasi untuk memulihkan umbi yang sudah terinfeksi moler berat; fokus pada seleksi bibit sehat dan rotasi.",
                        listOf("Gunakan umbi bibit sehat dan kering", "Pastikan drainase sangat lancar"),
                        5, "Rumpun bawang lainnya tumbuh tegak normal tanpa gejala melintir.",
                        "Penyakit Moler Bawang Merah — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "thrips_bawang",
                        "Daun Keperakan Kusam di Bagian Pangkal Pelepah",
                        "Helaian daun bagian dalam tampak bergaris-garis putih keperakan dan ujung daun cepat mengering.",
                        listOf("Kilau keperakan kasar di celah pangkal daun", "Serangga sangat kecil memanjang berwarna kuning-cokelat"),
                        listOf("Hama Thrips tabaci terutama di musim kemarau panas"),
                        listOf("Apakah cuaca kering panas dan pangkal celah daun tampak keperakan?"),
                        "Goresan perak berada di ketiak pelepah daun, lebih sering muncul saat cuaca kering panas.",
                        "Siram semprot terukur di pagi hari untuk menjaga kelembapan mikro dan pasang perangkap likat biru/kuning.",
                        "Kendalikan gulma sekitar kebun; konsultasikan rekomendasi PHT resmi bila populasi tinggi di musim kemarau.",
                        listOf("Jaga kelembapan pagi teratur di musim kering", "Bersihkan gulma inang"),
                        5, "Tunas daun baru di tengah rumpun tumbuh hijau segar.",
                        "Pengendalian Thrips Bawang — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "umbi_kecil_tidak_membesar",
                        "Daun Rimbun Tetapi Umbi Tetap Kecil",
                        "Rumpun daun tampak hijau lebat sekali hingga umur 45 hari namun pembesaran umbi di pangkal sangat lambat.",
                        listOf("Daun lemas hijau tua terlalu subur", "Leher batang tetap tipis tanpa pembulatan umbi"),
                        listOf("Kelebihan nitrogen dibanding kalium", "Kurang sinar matahari (<6 jam) atau penanaman terlalu dalam"),
                        listOf("Apakah lokasi mendapat sinar matahari penuh?", "Apakah pemupukan didominasi nitrogen/kompos basah?"),
                        "Umbi bawang merah hanya membengkak baik bila sinar penuh dan umbi awal tidak terkubur terlalu dalam.",
                        "Pastikan lokasi terbuka penuh sinar matahari, singkap sedikit tanah di sekitar leher umbi, dan tambahkan abu sekam kaya kalium.",
                        "Hentikan nitrogen setelah umur 25 HST dan pastikan kecukupan kalium serta sinar matahari penuh.",
                        listOf("Tanam umbi hanya 3/4 bagian terbenam", "Berikan sinar matahari penuh 7–8 jam"),
                        7, "Pangkal umbi mulai membengkak dan kulit kemerahan terbentuk.",
                        "Pembentukan Umbi Bawang Merah — Riset Agronomi", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    )
                )
            )
        )
        addAll(
            buildProblemsForCrop(
                "bawang_putih", "Bawang Putih", listOf(
                    ProblemSeed(
                        "busuk_putih_bawang_putih",
                        "Pangkal Umbi Diselimuti Jamur Putih Kapas & Butiran Hitam",
                        "Daun tua menguning dari ujung dan saat diperiksa, pangkal siung berselimut miselium putih dengan bintik hitam kecil.",
                        listOf("Daun menguning dimulai dari ujung daun tertua", "Lapisan putih kapas dan butiran hitam (sklerotia) di pangkal"),
                        listOf("Jamur busuk putih (Sclerotium cepivorum)"),
                        listOf("Apakah pangkal umbi memiliki lapisan putih seperti kapas?"),
                        "Sklerotia hitam sebesar kepala jarum di atas miselium putih adalah ciri khas busuk putih.",
                        "Cabut segera tanaman sakit beserta tanah menempel, jangan komposkan, dan rotasi panjang dengan tanaman non-Allium.",
                        "Belum tersedia rekomendasi tervalidasi untuk mengobati tanaman yang sudah terinfeksi berat; fokus pada sanitasi total.",
                        listOf("Gunakan siung bibit sehat", "Rotasi lahan minimal 2–3 tahun untuk kasus berat"),
                        7, "Tanaman lain tetap sehat tanpa gejala menguning mendadak.",
                        "Penyakit Busuk Putih Bawang Putih — Literatur Patologi", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed(
                        "karat_oranye_bawang_putih",
                        "Bintik Tepung Oranye Kemerahan pada Daun",
                        "Muncul bintil-bintil kecil berwarna oranye cerah hingga kecokelatan di permukaan daun bawang putih.",
                        listOf("Pustula bubuk oranye di kedua sisi daun", "Daun mengering lebih cepat"),
                        listOf("Jamur karat daun (Puccinia allii)"),
                        listOf("Apakah bintik mengeluarkan serbuk berwarna jingga bila disentuh jari?"),
                        "Serbuk berwarna oranye karat yang menempel di jari saat disentuh sangat khas.",
                        "Pangkas daun tua yang penuh pustula oranye, kurangi kepadatan tanaman, dan hindari membasahi daun.",
                        "Utamakan sanitasi daun terinfeksi dan perbaikan sirkulasi udara.",
                        listOf("Hindari jarak tanam terlalu rapat", "Siram langsung ke media"),
                        5, "Daun baru tumbuh bersih tanpa pustula oranye.",
                        "Penyakit Karat Bawang Putih — Ditjen Hortikultura", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "tidak_membentuk_siung",
                        "Umbi Tunggal Bulat Tanpa Membelah Menjadi Siung",
                        "Saat panen, bawang putih hanya membentuk satu umbi tunggal bulat (seperti bawang lanang) tanpa siung terpisah.",
                        listOf("Umbi tetap utuh tunggal tidak berbelah siung"),
                        listOf("Suhu lingkungan terlalu panas (dataran rendah) atau bibit kurang periode dingin", "Penanaman terlalu lambat di musim panas"),
                        listOf("Berapa suhu rata-rata harian lokasi tanam Anda?"),
                        "Pembelahan siung bawang putih secara fisiologis dipicu oleh suhu sejuk.",
                        "Umbi tunggal tetap aman dikonsumsi; untuk siklus berikutnya pilih varietas adaptif atau tanam di musim paling sejuk.",
                        "Pupuk tambahan tidak dapat memaksa pembelahan siung bila suhu lingkungan terlalu hangat.",
                        listOf("Tanam di dataran sejuk atau saat suhu malam paling rendah"),
                        14, "Pemilihan lokasi/musim sejuk menghasilkan umbi bersiung di siklus berikut.",
                        "Fisiologi Pembentukan Siung Bawang Putih — Riset Agronomi", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed(
                        "ujung_daun_mengering",
                        "Ujung Daun Menguning Kecokelatan Ringan",
                        "Ujung-ujung daun pita bawang putih mengering sekitar 1–2 cm sementara pangkal tetap hijau.",
                        listOf("Ujung daun kering kecokelatan", "Tidak ada spora ungu atau karat"),
                        listOf("Fluktuasi air atau stres angin kering", "Penuaan normal menjelang masa panen (>75 HST)"),
                        listOf("Berapa umur tanaman saat ini dan bagaimana kelembapan tanah?"),
                        "Pada umur >75 hari, mengeringnya ujung daun adalah proses pematangan alami, bukan penyakit.",
                        "Jika masih muda (<50 hari), cek kelembapan tanah dan tambahkan mulsa jerami tipis; jika sudah tua, kurangi siram secara bertahap.",
                        "Sesuaikan penyiraman dengan umur tanaman; jangan memberi pupuk N terlambat.",
                        listOf("Gunakan mulsa tipis", "Pahami fase umur pematangan"),
                        7, "Pertumbuhan umbi tetap stabil hingga panen.",
                        "Panduan Budidaya Bawang Putih — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed(
                        "tungau_umbi_simpan",
                        "Siung Menjadi Kopong Kering Saat Penyimpanan / Awal Tanam",
                        "Siung bawang terasa ringan, permukaannya keriput kering dengan debu halus kecokelatan.",
                        listOf("Siung kempes ringan saat ditekan", "Bintik cokelat lengket atau debu sangat halus di kulit siung"),
                        listOf("Tungau umbi (Aceria tulipae) atau busuk kering penyimpanan"),
                        listOf("Apakah siung bibit disimpan di tempat lembap pengap sebelum tanam?"),
                        "Periksa kepadatan siung sebelum menanam; siung kopong memiliki daya tumbuh sangat buruk.",
                        "Seleksi dan buang siung kopong sebelum tanam; simpan umbi pascapanen di tempat sejuk berventilasi baik.",
                        "Gunakan hanya siung bibit padat bernas bebas keriput untuk penanaman.",
                        listOf("Sortir siung sebelum tanam", "Curing dan simpan di tempat kering berangin"),
                        7, "Bibit siung sehat berkecambah seragam.",
                        "Penanganan Pascapanen Bawang Putih — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    )
                )
            )
        )
    }
}
