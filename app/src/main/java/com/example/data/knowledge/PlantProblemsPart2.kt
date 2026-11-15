package com.example.data.knowledge

import com.example.model.EvidenceLevel
import com.example.model.PlantProblemRecord

private data class ProblemSeed2(
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

private fun buildProblems2(
    cropId: String,
    cropName: String,
    seeds: List<ProblemSeed2>
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

object PlantProblemsPart2 {
    val problems: List<PlantProblemRecord> = buildList {
        addAll(
            buildProblems2(
                "timun", "Timun (Mentimun)", listOf(
                    ProblemSeed2(
                        "embun_bulu_timun",
                        "Bercak Kuning Kotak Dibatasi Tulang Daun (Embun Bulu)",
                        "Permukaan atas daun memunculkan bercak kuning bersudut mengikuti urat daun dan tampak lapisan keabuan di sisi bawah.",
                        listOf("Bercak kuning berbentuk menyudut (angular)", "Lapisan spora abu-abu keunguan di bawah daun saat pagi lembap"),
                        listOf("Infeksi Pseudoperonospora cubensis (downy mildew)", "Daun basah terlalu lama di malam hari"),
                        listOf("Apakah bercak kuning dibatasi oleh garis urat daun?"),
                        "Berbeda dari embun tepung yang berwarna putih di atas daun, embun bulu berbentuk kotak kuning dengan spora di bawah daun.",
                        "Pangkas daun tua yang terinfeksi berat, siram hanya di pangkal media pada pagi hari, dan pastikan rambatan tidak terlalu rimbun.",
                        "Utamakan sanitasi daun dan pengurangan kelembapan tajuk; bila memakai fungisida terdaftar mentimun, patuhi label dan masa henti panen.",
                        listOf("Gunakan rambatan vertikal terbuka", "Siram pagi di pangkal media"),
                        4, "Daun-daun atas tetap hijau dan bercak baru berhenti bertambah.",
                        "Pengendalian Embun Bulu Mentimun — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "embun_tepung_timun",
                        "Lapisan Bubuk Putih Seperti Bedak di Atas Daun",
                        "Permukaan atas daun dan tangkai tertutup bercak putih tepung yang lama-lama membuat daun mengering.",
                        listOf("Bercak bulat putih bertepung di sisi atas daun", "Daun tua mengering kecokelatan"),
                        listOf("Jamur embun tepung (Podosphaera xanthii)", "Kelembapan tinggi di antara daun rapat saat cuaca hangat"),
                        listOf("Apakah lapisan putih berada di permukaan atas daun seperti taburan tepung?"),
                        "Lapisan putih berada terutama di atas permukaan daun, bukan di dalam jaringan.",
                        "Pangkas daun tua yang sudah penuh tepung putih, perlebar sirkulasi udara rambatan, dan berikan sinar matahari pagi cukup.",
                        "Lakukan sanitasi daun tua dan perbaiki sirkulasi; konsultasikan sarana pengendalian terdaftar bila serangan meluas ke pucuk.",
                        listOf("Pangkas daun terbawah secara rutin", "Hindari jarak tanam terlalu rapat"),
                        4, "Daun muda tetap bersih dan mengilap hijau.",
                        "Embun Tepung Cucurbitaceae — Literatur Patologi", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "buah_timun_bengkok",
                        "Buah Timun Membentuk Tidak Lurus / Mengecil di Ujung",
                        "Buah tampak melengkung atau menggelembung di satu sisi dan menyempit di sisi lainnya.",
                        listOf("Bentuk buah tidak silindris lurus", "Ujung atau pangkal buah mengecil"),
                        listOf("Penyerbukan bunga betina tidak sempurna", "Kekurangan air atau kalium saat buah membesar", "Buah tersangkut sulur/ajir"),
                        listOf("Apakah banyak lebah/penyerbuk di kebun pagi hari?", "Apakah penyiraman sempat terlambat saat buah memanjang?"),
                        "Jika hanya sebagian buah bengkok di dekat tali ajir, cek hambatan fisik; jika meluas, cek penyerbukan dan pengairan.",
                        "Lakukan penyerbukan bantu pagi hari (pukul 07.00–09.00) dan arahkan bakal buah tergantung bebas tanpa terjepit tali.",
                        "Jaga penyiraman stabil pagi hari dan periksa ketersediaan unsur kalium di fase generatif.",
                        listOf("Bantu polinasi bila serangga sedikit", "Siram konsisten selama fase buah"),
                        5, "Bakal buah baru tumbuh lurus menggantung bebas.",
                        "Fisiologi Pembentukan Buah Timun — Hortikultura", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed2(
                        "timun_pahit",
                        "Rasa Buah Timun Terasa Pahit",
                        "Kulit atau pangkal buah timun yang dipanen terasa pahit tajam.",
                        listOf("Penampilan buah tampak normal atau agak kecil", "Rasa pahit terutama dekat pangkal tangkai"),
                        listOf("Stres kekeringan atau suhu panas ekstrem yang memicu penumpukan cucurbitacin", "Fluktuasi air dan nutrisi"),
                        listOf("Apakah tanaman sering mengalami layu siang akibat telat siram?"),
                        "Rasa pahit adalah reaksi stres lingkungan tanaman, bukan hama.",
                        "Pasang mulsa organik di permukaan media, siram teratur setiap pagi atau saat lapisan atas mengering, dan beri naungan ringan saat panas ekstrem.",
                        "Jaga air konsisten dan hindari kelebihan nitrogen mendadak.",
                        listOf("Gunakan mulsa jerami/kompos", "Pilih varietas non-bitter (bebas pahit)"),
                        7, "Buah yang terbentuk pada kondisi air stabil tidak lagi berasa pahit.",
                        "Penyebab Rasa Pahit Mentimun — Riset Hortikultura", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed2(
                        "kumbang_oteng",
                        "Daun Timun Dimakan Membentuk Cincin Melingkar (Aulacophora)",
                        "Terdapat bekas gigitan melengkung hampir melingkar pada daun dan tampak kumbang kecil berwarna oranye-kuning.",
                        listOf("Pola gigitan melengkung khas", "Kumbang oranye kuning aktif siang hari"),
                        listOf("Kumbang daun Cucurbit (Aulacophora spp.)"),
                        listOf("Apakah terlihat kumbang berwarna oranye terang di sekitar daun muda?"),
                        "Kumbang Aulacophora menggigit daun membentuk lengkungan melingkar sebelum memakan bagian dalamnya.",
                        "Tangkap kumbang dengan tangan atau jaring kecil di pagi hari saat gerakannya masih lambat, dan tutup bibit muda dengan jaring halus.",
                        "Perlindungan fisik dengan sungkup jaring di minggu ke-1 s/d ke-3 sangat efektif karena bibit muda paling rentan.",
                        listOf("Gunakan sungkup jaring pada bibit muda", "Bersihkan gulma famili labu liar"),
                        3, "Daun muda tidak lagi memiliki gigitan melingkar baru.",
                        "Hama Kumbang Daun Timun — Balitsa", EvidenceLevel.D, "Teruji lapangan"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "pare", "Pare (Peria)", listOf(
                    ProblemSeed2(
                        "lalat_buah_pare",
                        "Buah Pare Muda Menguning Busuk Berulat",
                        "Buah pare berukuran kecil hingga sedang berubah kuning di satu sisi, lunak, dan terdapat belatung kecil di dalamnya.",
                        listOf("Titik kecil bekas tusukan hitam di kulit buah", "Bagian dalam buah melunak berulat putih tanpa kaki"),
                        listOf("Serangan lalat buah (Bactrocera spp.)"),
                        listOf("Apakah buah pare dibungkus saat masih muda?"),
                        "Adanya belatung putih tanpa kaki di dalam daging buah adalah ciri pasti lalat buah.",
                        "Petik semua buah terserang dan rendam/kubur dalam agar larva tidak jadi kepompong; bungkus buah baru sejak panjang 5 cm dengan kertas berlubang ventilasi.",
                        "Pembungkusan mekanis ditambah perangkap atraktan lalat buah jauh lebih efektif daripada penyemprotan.",
                        listOf("Bungkus bakal buah berumur 3–5 hari", "Pasang perangkap botol atraktan di luar bedengan"),
                        5, "Buah-buah baru yang dibungkus tumbuh hijau mulus sampai panen.",
                        "Pengendalian Lalat Buah Pare — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "bunga_jantan_dominan",
                        "Banyak Bunga Rontok Karena Hampir Semua Bunga Jantan",
                        "Tanaman berbunga sangat lebat tetapi tidak ada yang menjadi buah karena tidak terdapat bakal buah mini di pangkal bunga.",
                        listOf("Tangkai bunga tipis lurus tanpa tonjolan bakal buah pare", "Bunga mekar lalu gugur sore harinya"),
                        listOf("Fase awal pembungaan (bunga jantan memang muncul lebih dulu)", "Naungan terlalu teduh atau nitrogen terlalu tinggi"),
                        listOf("Apakah ada bunga yang memiliki tonjolan buah kecil di belakang kelopaknya?"),
                        "Pada tanaman famili labu, bunga jantan normalnya muncul 1–2 minggu lebih awal sebelum bunga betina.",
                        "Pastikan tanaman mendapat cahaya penuh 6–8 jam, pangkas cabang terbawah yang terlalu rimbun, dan tunggu 1–2 minggu untuk bunga betina muncul.",
                        "Kurangi pupuk nitrogen dan pastikan pasokan fosfor/kalium seimbang serta cahaya penuh.",
                        listOf("Pastikan cahaya matahari penuh", "Pangkas cabang samping di bawah 60 cm"),
                        10, "Bunga betina berbakal buah mini mulai muncul di cabang lateral.",
                        "Fisiologi Pembungaan Pare — Hortikultura", EvidenceLevel.C, "Rekomendasi ditinjau ahli"
                    ),
                    ProblemSeed2(
                        "antraknosa_daun_pare",
                        "Bercak Cokelat Kering Rapuh pada Daun Pare",
                        "Daun pare memiliki bercak cokelat kehitaman yang bagian tengahnya cepat kering dan mudah robek.",
                        listOf("Bercak tidak beraturan cokelat tua", "Tengah bercak mudah sobek saat kering"),
                        listOf("Infeksi jamur Colletotrichum / Cercospora", "Kelembapan para-para terlalu rapat"),
                        listOf("Apakah sulur dan daun bertumpuk terlalu tebal di para-para?"),
                        "Penumpukan daun berlapis di para-para menahan embun dan mempercepat infeksi bercak.",
                        "Jarang-jarangkan sulur dan daun tua di para-para agar cahaya menembus rata, lalu buang daun berbercak berat.",
                        "Sanitasi tajuk dan perbaikan sirkulasi para-para merupakan langkah utama.",
                        listOf("Atur jarak sulur di para-para", "Siram pagi hari di pangkal"),
                        5, "Tajuk pare lebih terbuka dan daun baru bersih.",
                        "Budidaya Pare Sehat — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "layu_fusarium_pare",
                        "Sulur Pare Menguning & Layu dari Pangkal Batang",
                        "Daun bagian pangkal menguning lalu seluruh sulur layu permanen disertai retakan cokelat di batang bawah.",
                        listOf("Batang dekat tanah pecah cokelat atau mengeluarkan getah", "Sulur layu menyeluruh"),
                        listOf("Layu Fusarium atau busuk pangkal batang"),
                        listOf("Apakah pangkal batang dekat tanah tampak retak kecokelatan atau basah?"),
                        "Layu disertai lesi cokelat di pangkal batang menandakan infeksi pembuluh/pangkal.",
                        "Jangan memindahkan media bekas tanaman sakit ke pot lain; cabut tanaman mati dan ganti media atau rotasi jenis tanaman.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyembuhkan layu pembuluh lanjut; utamakan pencegahan drainase.",
                        listOf("Gunakan media berdrainase cepat", "Jangan tanam famili labu berulang di media sama"),
                        5, "Tanaman lain tetap segar tanpa gejala serupa.",
                        "Penyakit Layu Cucurbitaceae — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "kutu_sisik_batang_pare",
                        "Kerak Putih/Cokelat Menempel Erat di Batang Tua",
                        "Batang pare tua tampak memiliki tonjolan kecil pipih seperti sisik yang tidak terbang bila disentuh.",
                        listOf("Sisik pipih menempel di buku batang", "Kadang dikerumuni semut"),
                        listOf("Kutu sisik atau kutu putih batang"),
                        listOf("Apakah tonjolan tersebut menempel erat dan mengeluarkan cairan bila ditekan?"),
                        "Kutu sisik tidak bergerak lincah seperti kutu daun, tetapi menyedot cairan batang secara terus-menerus.",
                        "Sikat perlahan batang yang ditempeli kutu menggunakan sikat halus dan air bersih, lalu pangkas ranting tua yang sangat parah.",
                        "Pembersihan mekanis batang dan pengendalian semut sangat membantu untuk kebun rumah.",
                        listOf("Periksa batang bawah para-para setiap minggu"),
                        5, "Batang tampak bersih kembali dan tidak ada semut berkerumun.",
                        "Hama Pengisap Batang Tanaman Merambat — PHT", EvidenceLevel.D, "Teruji lapangan"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "kacang_panjang", "Kacang Panjang", listOf(
                    ProblemSeed2(
                        "kutu_daun_hitam_kacang",
                        "Pucuk & Tangkai Polong Penuh Kutu Hitam",
                        "Ujung sulur dan bakal polong diselimuti koloni serangga hitam kecil disertai banyak semut.",
                        listOf("Koloni kutu hitam padat di pucuk muda", "Polong muda tumbuh melengkung kerdil"),
                        listOf("Kutu daun kacang (Aphis craccivora)"),
                        listOf("Apakah koloni hitam berkumpul rapat di pucuk dan tangkai bunga?"),
                        "Kutu hitam Aphis craccivora sangat menyukai jaringan muda kacang-kacangan dan dapat menularkan virus mosaik.",
                        "Semprot koloni dengan aliran air cukup kencang di pagi hari atau petik pucuk terpadat dan hancurkan di luar kebun.",
                        "Biarkan predator alami seperti kepik (ladybug) bekerja; hindari insektisida keras yang justru membunuh kepik.",
                        listOf("Pantau pucuk setiap 2–3 hari", "Kendalikan gulma sekitar lanjaran"),
                        3, "Pucuk bebas koloni hitam dan polong baru tumbuh lurus.",
                        "PHT Kutu Daun Kacang Panjang — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "penggerek_polong",
                        "Polong Kacang Berlubang dengan Kotoran Basah (Maruca)",
                        "Bunga rontok atau polong muda memiliki lubang kecil disertai remahan kotoran di luar lubang.",
                        listOf("Lubang pada bunga atau polong", "Kelopak bunga menempel pada polong muda dengan kotoran ulat"),
                        listOf("Penggerek polong (Maruca vitrata)"),
                        listOf("Apakah ditemukan ulat krem berbintik gelap di dalam bunga atau polong?"),
                        "Ulat Maruca sering merekatkan bunga kering ke bakal polong sebagai tempat berlindung.",
                        "Petik dan buang bunga kering yang menempel mencurigakan serta polong berlubang di luar kebun.",
                        "Sanitasi bunga/polong terserang dilakukan bersamaan saat panen rutin tiap 2 hari.",
                        listOf("Bersihkan bunga rontok di sekitar tanaman", "Panen polong tepat waktu"),
                        4, "Polong baru tumbuh mulus hingga siap petik.",
                        "Pengendalian Penggerek Polong — Balitkabi", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "karat_daun_kacang",
                        "Bintik Cokelat Bertepung pada Balik Daun (Karat Daun)",
                        "Permukaan bawah daun tua dipenuhi bintik-bintik cokelat kemerahan bertepung dan daun cepat rontok.",
                        listOf("Bintil cokelat karat di sisi bawah daun", "Halo kuning tipis di sisi atas daun"),
                        listOf("Jamur karat Uromyces appendiculatus"),
                        listOf("Apakah bintik bertepung cokelat terlihat di sisi bawah daun tua?"),
                        "Bintik menonjol bertepung cokelat karat di balik daun membedakannya dari bercak daun biasa.",
                        "Petik daun tua paling bawah yang berkarat berat agar sirkulasi udara di lanjaran lebih baik, dan siram di pangkal saja.",
                        "Perbaiki jarak lanjaran dan hindari membasahi daun.",
                        listOf("Gunakan jarak tanam cukup lebar", "Pangkas daun tua di pangkal"),
                        5, "Daun produktif di bagian tengah-atas tetap hijau segar.",
                        "Penyakit Karat Kacang Panjang — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "mosaik_kuning_kacang",
                        "Daun Belang Kuning Hijau Tidak Rata (Mosaik)",
                        "Daun muda menunjukkan pola belang hijau tua dan kuning terang disertai bentuk helai sedikit mengerut.",
                        listOf("Pola mozaik kuning-hijau tegas", "Pertumbuhan polong berkurang"),
                        listOf("Infeksi virus mosaik (CAMV / BCMV) yang ditularkan kutu daun"),
                        listOf("Apakah belang kuning terjadi merata dengan batas pola tidak beraturan pada daun baru?"),
                        "Berbeda dari kekurangan hara yang simetris dan halus, virus mosaik membuat pola belang kontras dan daun agak bergelembung.",
                        "Jika hanya 1–2 tanaman di awal tanam, cabut tanaman terinfeksi dan kendalikan kutu daun pada tanaman sehat di sekitarnya.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyembuhkan tanaman terinfeksi virus; fokus pada pengendalian vektor kutu.",
                        listOf("Gunakan benih sehat", "Kendalikan kutu daun sejak awal"),
                        5, "Tanaman sehat di sekitar tidak tertular belang mosaik.",
                        "Virus Mosaik Leguminosae — Literatur Patologi", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed2(
                        "terlalu_rimbun_sedikit_polong",
                        "Daun Sangat Rimbun Tetapi Bunga & Polong Sedikit",
                        "Tanaman tumbuh subur berdaun besar gelap namun pembungaan tertunda.",
                        listOf("Tajuk daun sangat lebat", "Tangkai bunga sangat jarang"),
                        listOf("Kelebihan pupuk nitrogen (karena akar kacang sudah mengikat N sendiri)", "Kurang cahaya matahari"),
                        listOf("Apakah tanaman sering diberi pupuk urea atau pupuk daun tinggi N?"),
                        "Kacang-kacangan tidak membutuhkan banyak nitrogen eksternal; kelebihan N justru menekan fase generatif.",
                        "Hentikan pemberian pupuk tinggi N dan pastikan tanaman mendapat sinar matahari penuh; tambahkan abu sekam atau kompos matang seimbang.",
                        "Berhenti memberi N dan fokus pada kecukupan Fosfor dan Kalium sesuai petunjuk.",
                        listOf("Jangan beri pupuk tinggi N pada kacang-kacangan", "Pastikan paparan sinar penuh"),
                        10, "Tangkai bunga mulai bermunculan di ketiak daun.",
                        "Manajemen Hara Leguminosae — Agronomi", EvidenceLevel.C, "Rekomendasi ditinjau ahli"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "kacang_tanah", "Kacang Tanah", listOf(
                    ProblemSeed2(
                        "polong_hampa_kacang_tanah",
                        "Polong Terbentuk Tetapi Banyak yang Kosong / Hampa",
                        "Saat dipanen atau diperiksa, kulit polong tampak utuh namun bijinya tidak terbentuk atau sangat kecil.",
                        listOf("Polong terasa ringan saat ditekan", "Biji di dalam tidak berkembang"),
                        listOf("Kekurangan kalsium tersedia di zona 5 cm permukaan tanah", "Kekeringan saat fase pengisian polong", "Tanah terlalu keras/padat"),
                        listOf("Apakah tanah di sekitar pangkal gembur dan sudah diberi kapur/kalsium?"),
                        "Polong kacang tanah menyerap kalsium langsung melalui kulit polong di tanah, bukan hanya lewat akar dalam.",
                        "Gemburkan tanah sekitar pangkal sebelum ginofor masuk dan tambahkan kapur pertanian/dolomit serta kompos matang.",
                        "Berikan suplemen kalsium di zona pembentukan polong saat awal berbunga dan jaga kelembapan tanah.",
                        listOf("Gunakan media berpasir gembur", "Tambahkan kapur pertanian sebelum pembungaan"),
                        14, "Polong dari ginofor berikutnya terisi biji penuh.",
                        "Peran Kalsium pada Pengisian Polong Kacang Tanah — Balitkabi", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "bercak_daun_cercospora_kt",
                        "Bercak Cokelat Gelap Bulat di Daun Tua",
                        "Daun bawah dan tengah dipenuhi bercak bundar cokelat tua atau hitam, terkadang dikelilingi tepi kuning.",
                        listOf("Bercak bulat gelap di kedua sisi daun", "Daun tua gugur lebih awal"),
                        listOf("Jamur bercak daun awal/lanjut (Cercospora arachidicola / Phaeoisariopsis)"),
                        listOf("Apakah bercak bulat cokelat tua membuat daun bawah cepat rontok?"),
                        "Umum muncul di umur >45 hari terutama saat kelembapan tinggi.",
                        "Bersihkan daun tua yang rontok di sekitar pangkal agar spora tidak menumpuk di tanah, dan hindari penyiraman sore pada daun.",
                        "Jaga jarak tanam cukup lebar agar daun cepat kering oleh angin pagi.",
                        listOf("Rotasi tanaman", "Bersihkan sisa daun panen sebelumnya"),
                        7, "Daun atas tetap sehat menopang pengisian polong hingga panen.",
                        "Penyakit Bercak Daun Kacang Tanah — Balitkabi", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "layu_bakteri_kt",
                        "Rumpun Layu Mendadak Meski Daun Masih Hijau",
                        "Satu rumpun kacang tanah layu mendadak lalu mati mengering dalam beberapa hari.",
                        listOf("Daun layu hijau di seluruh cabang", "Cairan keruh keluar bila potongan batang dicelup ke air bening"),
                        listOf("Layu bakteri (Ralstonia solanacearum)", "Drainase tanah tergenang"),
                        listOf("Apakah lahan sebelumnya ditanami cabai/tomat yang mengalami layu?"),
                        "Uji celup batang di segelas air bening membantu membedakan layu bakteri (keluar benang putih keruh) dari kekeringan.",
                        "Cabut segera rumpun terinfeksi beserta akarnya dan jangan tanam ulang famili Solanaceae/kacang tanah di media tersebut.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyembuhkan layu bakteri; fokus pada drainase dan rotasi tanaman.",
                        listOf("Perbaiki saluran pembuangan air", "Rotasi dengan jagung atau serealia"),
                        5, "Rumpun sehat lainnya bertahan normal sampai panen.",
                        "Layu Bakteri Kacang Tanah — Balitkabi", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "ginofor_gagal_masuk",
                        "Bakal Polong (Ginofor) Mengering di Atas Tanah",
                        "Tangkai bunga yang memanjang ke bawah tidak mampu menembus tanah dan ujungnya mengering.",
                        listOf("Tangkai ginofor berwarna gelap mengering di permukaan"),
                        listOf("Permukaan tanah terlalu keras berkerak", "Jarak cabang ke tanah terlalu tinggi tanpa pembumbunan"),
                        listOf("Apakah permukaan tanah di sekitar pangkal keras memadat?"),
                        "Ginofor hanya membentuk polong setelah berhasil menembus masuk ke media gelap yang lembap dan gembur.",
                        "Gemburkan dan bumbun tanah halus di sekitar pangkal tepat sebelum ginofor turun, lalu jaga permukaan tetap agak lembap.",
                        "Pembumbunan mekanis ringan saat umur 25–30 HST menyelesaikan masalah ini secara langsung.",
                        listOf("Campur pasir dan kompos agar permukaan tidak memadat"),
                        7, "Ginofor baru berhasil menembus media gembur dengan baik.",
                        "Budidaya Kacang Tanah — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "klorosis_daun_muda_kt",
                        "Daun Muda Kuning dengan Tulang Daun Tetap Hijau",
                        "Daun termuda di pucuk berwarna kuning pucat namun urat-urat daunnya masih terlihat hijau.",
                        listOf("Kuning antar tulang daun pada daun muda", "Daun tua masih hijau normal"),
                        listOf("Gangguan serapan besi (Fe) akibat tanah terlalu basa (pH >7,5) atau tergenang"),
                        listOf("Apakah tanah pernah diberi kapur sangat berlebihan atau tergenang air?"),
                        "Klorosis antar tulang daun pada daun muda berbeda dari kekurangan N yang terjadi di daun tua.",
                        "Periksa drainase dan tambahkan kompos matang yang bersifat netral penyangga untuk menormalkan pH media.",
                        "Evaluasi pH dan drainase tanah sebelum menambah unsur mikro besi bila diperlukan.",
                        listOf("Berikan kapur secukupnya saja sesuai kebutuhan pH", "Jaga aerasi tanah"),
                        7, "Daun baru yang mekar berwarna hijau merata.",
                        "Defisiensi Unsur Mikro Kacang-kacangan — Ilmu Tanah", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "jagung", "Jagung Manis", listOf(
                    ProblemSeed2(
                        "ulat_grayak_jagung",
                        "Corong Daun Muda Berlubang Berbaris & Penuh Kotoran Serbuk",
                        "Daun jagung yang baru membuka berlubang simetris berbaris disertai kotoran basah seperti serbuk gergaji di pucuk.",
                        listOf("Lubang berjajar melintang pada helai daun", "Kotoran kekuningan/cokelat di dalam corong pucuk"),
                        listOf("Ulat grayak jagung (Spodoptera frugiperda / FAW)"),
                        listOf("Apakah terlihat ulat dengan pola Y terbalik di kepalanya di dalam corong pucuk?"),
                        "Lubang berbaris terjadi karena ulat memakan daun saat masih tergulung di dalam corong.",
                        "Periksa corong pucuk pagi/sore dan ambil ulat secara manual; jaga kebersihan gulma dan gunakan musuh alami bila ada.",
                        "Bila populasi melampaui ambang kendali di lahan, gunakan produk terdaftar khusus sasaran FAW sesuai label dan APD lengkap.",
                        listOf("Inspeksi corong daun sejak umur 10 HST", "Tanam serentak dalam satu blok"),
                        4, "Daun baru yang keluar dari corong pucuk tampak utuh.",
                        "Pedoman Pengendalian FAW Jagung — Kementan RI", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "bulai_jagung",
                        "Daun Bergaris Putih Memanjang dari Pangkal & Kerdil (Bulai)",
                        "Daun muda jagung menunjukkan garis-garis kuning keputihan sejajar tulang daun disertai serbuk putih di pagi hari.",
                        listOf("Garis klorotik tegas memanjang dari pangkal daun", "Tanaman tumbuh kerdil dan sering tidak bertongkol"),
                        listOf("Penyakit bulai (Peronosclerospora spp.) dari benih atau spora angin pagi"),
                        listOf("Apakah garis kuning putih berawal tepat dari pangkal daun sejak tanaman masih muda?"),
                        "Garis putih dimulai dari pangkal daun dan meluas tegas di sepanjang tulang daun.",
                        "Cabut dan musnahkan tanaman terinfeksi bulai sedini mungkin agar spora paginya tidak menulari tanaman sehat lain.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyembuhkan jagung yang sudah terserang bulai sistemik; fokus pada benih sehat dan pencabutan dini.",
                        listOf("Gunakan benih varietas tahan bulai", "Lakukan pencabutan dini (rouging)"),
                        5, "Tanaman sehat di sekitar tidak tertular gejala garis putih.",
                        "Pengendalian Penyakit Bulai Jagung — Balitsereal", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "tongkol_ompong",
                        "Tongkol Jagung Tidak Penuh Biji (Ompong di Ujung)",
                        "Saat dikupas, bagian ujung atau sebagian sisi tongkol jagung kosong tidak berisi biji.",
                        listOf("Biji hanya terisi di bagian pangkal atau acak", "Banyak ruang kosong di tongkol"),
                        listOf("Penanaman hanya 1 baris sehingga serbuk sari tertiup angin lewat", "Kekeringan saat keluar rambut tongkol"),
                        listOf("Apakah jagung ditanam dalam kelompok blok atau hanya sebaris?", "Apakah sempat kering saat pembungaan?"),
                        "Setiap helai rambut tongkol terhubung ke satu calon biji; rambut yang tidak menerima serbuk sari menghasilkan biji kosong.",
                        "Tanam minimal 3x3 atau 4x4 baris di siklus berikut dan bantu goyangkan malai saat mekar; pastikan air cukup saat rambut keluar.",
                        "Pemupukan tidak bisa mengisi biji yang tidak terbuahi; perbaiki tata letak tanam blok dan air fase pembungaan.",
                        listOf("Selalu tanam berkelompok blok", "Siram cukup saat fase keluar rambut tongkol"),
                        14, "Penanaman blok menghasilkan tongkol terisi penuh di siklus berikutnya.",
                        "Fisiologi Penyerbukan Jagung — Balitsereal", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "kuning_bentuk_v",
                        "Daun Bawah Menguning Membentuk Huruf V dari Ujung",
                        "Daun tua bagian bawah menguning dimulai dari ujung daun lalu merambat ke pangkal mengikuti tulang tengah membentuk huruf V.",
                        listOf("Pola V kuning terbalik dari ujung daun tua", "Daun atas masih hijau"),
                        listOf("Kekurangan unsur Nitrogen saat pertumbuhan cepat", "Akar terganggu genangan sehingga gagal menyerap N"),
                        listOf("Apakah tanah terasa becek atau baru terguyur hujan lebat?"),
                        "Pola V dari ujung ke tulang tengah daun tua merupakan pola khas keterbatasan nitrogen (berbeda dari tepi daun kering yang terkait kalium).",
                        "Periksa drainase dulu, lalu tambahkan kompos matang atau pupuk organik cair di sekitar perakaran.",
                        "Bila drainase baik dan tanaman masih di fase vegetatif (<35 HST), berikan pupuk N susulan sesuai dosis anjuran jagung.",
                        listOf("Berikan pupuk dasar cukup kompos", "Pastikan air tidak menggenang"),
                        7, "Penguningan tidak naik ke daun produktif di sekitar tongkol.",
                        "Identifikasi Gejala Hara Jagung — Balitsereal", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "ulat_tongkol",
                        "Ujung Tongkol Berlubang Dimakan Ulat",
                        "Rambut tongkol terpotong dan biji di ujung tongkol rusak dimakan ulat.",
                        listOf("Rambut tongkol putus dini", "Terdapat ulat di ujung dalam kelobot tongkol"),
                        listOf("Ulat penggerek tongkol (Helicoverpa armigera)"),
                        listOf("Apakah kerusakan hanya di ujung tongkol tanpa merusak batang?"),
                        "Kerusakan umumnya terbatas pada 2–3 cm ujung tongkol yang dapat dipotong bersih saat panen.",
                        "Periksa ujung rambut tongkol saat mulai kecokelatan dan bersihkan ulat secara manual bila ditemukan di ujung kelobot.",
                        "Untuk kebun rumah, memotong bagian ujung saat panen jauh lebih bijak daripada menyemprotkan pestisida pada tongkol.",
                        listOf("Pilih varietas dengan kelobot menutup ujung tongkol rapat"),
                        5, "Sebagian besar biji tongkol tetap utuh dan aman dikonsumsi.",
                        "Hama Tongkol Jagung — Balitsereal", EvidenceLevel.A, "Panduan teknis resmi"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "melon", "Melon", listOf(
                    ProblemSeed2(
                        "embun_tepung_melon",
                        "Bercak Tepung Putih di Daun Melon",
                        "Muncul bercak putih seperti bedak di permukaan atas daun tua yang meluas cepat ke daun tengah.",
                        listOf("Bercak bulat tepung putih", "Daun tua cepat mengering rapuh"),
                        listOf("Jamur embun tepung (Podosphaera)", "Sirkulasi udara kurang dan daun terlalu rimbun"),
                        listOf("Apakah daun di bawah buah saling bertumpuk rapat?"),
                        "Menyerang permukaan daun dan tangkai saat kelembapan hangat.",
                        "Pangkas daun tua yang sakit berat dan rapikan cabang samping agar udara mengalir lancar.",
                        "Utamakan sanitasi dan sirkulasi; bila menyemprot sarana pengendalian terdaftar, patuhi aturan label melon.",
                        listOf("Pangkas tunas samping di ruas bawah", "Beri jarak antar tanaman minimal 50 cm"),
                        4, "Daun-daun penopang buah tetap hijau berfungsi baik.",
                        "Penyakit Daun Melon — Balitsa", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "buah_melon_pecah",
                        "Kulit Buah Melon Retak Mendadak Menjelang Panen",
                        "Buah yang sudah membentuk jaring mengalami retakan panjang dari pangkal atau sisi kulit.",
                        listOf("Retakan terbuka pada kulit berjaring", "Terjadi setelah penyiraman besar atau hujan"),
                        listOf("Peningkatan air mendadak pada fase pematangan buah"),
                        listOf("Apakah penyiraman tetap banyak saat umur buah sudah >30 hari setelah polinasi?"),
                        "Kulit melon yang sudah mengeras membentuk net tidak elastis lagi menahan lonjakan air.",
                        "Kurangi penyiraman secara bertahap 7–10 hari sebelum panen dan lindungi media dari luapan air hujan.",
                        "Hentikan pemupukan dan kurangi air menjelang panen sesuai jadwal budidaya melon.",
                        listOf("Kurangi air bertahap di 10 hari terakhir", "Gunakan atap naungan hujan jika budidaya intensif"),
                        5, "Buah pada tanaman lain matang tanpa retak.",
                        "Manajemen Retak Buah Melon — Hortikultura", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed2(
                        "bakal_buah_menguning_gugur",
                        "Bakal Buah Kecil Menguning dan Tidak Membesar",
                        "Setelah bunga betina mekar, bakal buah tetap kecil, berubah kuning layu, dan gugur.",
                        listOf("Bakal buah tidak membengkak setelah 3 hari mekar", "Warna menguning lalu keriput"),
                        listOf("Gagal penyerbukan", "Jumlah buah terlalu banyak pada satu tanaman", "Serangan lalat buah"),
                        listOf("Apakah dilakukan penyerbukan manual pagi hari atau ada lebah di kebun?", "Apakah sudah lebih dari 2 buah membesar di pohon sama?"),
                        "Tanaman melon secara alami menggugurkan bakal buah baru jika sudah menanggung beban buah lain atau tidak terserbuki.",
                        "Bantu penyerbukan manual di pagi hari pukul 06.30–09.00, sisakan hanya 1–2 buah terbaik per tanaman, dan cek apakah ada belatung di bakal buah.",
                        "Jaga kecukupan nutrisi kalium dan hindari penyemprotan apa pun saat bunga mekar di pagi hari.",
                        listOf("Polinasi manual pagi hari", "Seleksi 1–2 buah saja per tanaman"),
                        5, "Bakal buah yang diserbuki mulai membesar hijau segar dalam 3–4 hari.",
                        "Teknik Polinasi Melon — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "busuk_batang_bergetah",
                        "Batang Pecah Mengeluarkan Getah Cokelat Kemerahan (Gummy Stem Blight)",
                        "Pada ruas batang dekat pangkal atau ketiak daun muncul bercak basah dengan cairan getah lengket cokelat.",
                        listOf("Luka basah pada batang dengan eksudat getah cokelat", "Daun di atas luka melayu"),
                        listOf("Jamur Didymella bryoniae", "Luka pangkasan saat kondisi sangat lembap"),
                        listOf("Apakah pangkal batang mengeluarkan getah lengket berwarna amber/cokelat?"),
                        "Keluarnya getah lengket pada batang berbercak basah sangat khas untuk gummy stem blight.",
                        "Pangkas tunas samping di pagi hari cerah menggunakan pisau bersih agar luka cepat kering; jaga pangkal batang tetap kering bebas genangan.",
                        "Hindari pemangkasan saat hujan/sore hari; konsultasikan penyuluh bila luka melingkari batang utama.",
                        listOf("Pangkas di pagi cerah dengan alat steril", "Jangan timbun pangkal batang terlalu basah"),
                        5, "Luka pangkasan mengering bersih tanpa keluar getah lagi.",
                        "Penyakit Gummy Stem Blight Melon — Literatur Patologi", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed2(
                        "buah_kurang_manis",
                        "Daging Buah Terasa Hambar Saat Dipanen",
                        "Tekstur buah cukup baik tetapi rasa manis sangat kurang.",
                        listOf("Jaring tipis jarang", "Daging terlalu berair tawar"),
                        listOf("Panen terlalu dini sebelum matang fisiologis", "Terlalu banyak air saat pematangan", "Daun rontok sebelum buah matang"),
                        listOf("Berapa hari sejak polinasi buah dipanen?", "Apakah daun penopang di atas buah masih sehat?"),
                        "Daun sehat di atas buah berfungsi menyuplai gula; kehilangan daun akibat penyakit langsung menurunkan kemanisan.",
                        "Jaga kesehatan daun hingga panen, kurangi penyiraman 7–10 hari sebelum petik, dan tunggu tanda panen (retak cincin tangkai).",
                        "Pastikan pasokan kalium cukup selama pembesaran buah dan hentikan N di akhir fase.",
                        listOf("Sisakan minimal 20 daun sehat di atas buah", "Panen tepat umur matang"),
                        10, "Buah berikutnya mencapai aroma harum dan rasa manis lebih baik.",
                        "Faktor Kemanisan Melon — Riset Hortikultura", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "semangka", "Semangka", listOf(
                    ProblemSeed2(
                        "busuk_buah_tanah_semangka",
                        "Sisi Bawah Buah Lembek Cokelat Berselimut Kapas Putih",
                        "Bagian buah yang menempel tanah membusuk lunak cokelat dan berjamur putih saat lembap.",
                        listOf("Pembusukan dimulai dari bagian bawah buah yang menyentuh tanah", "Lapisan putih jamur di area basah"),
                        listOf("Phytophthora capsici / busuk buah tanah", "Buah terbaring langsung di atas tanah becek"),
                        listOf("Apakah buah menyentuh langsung tanah basah tanpa alas?"),
                        "Dimulai tepat dari titik kontak buah dengan tanah lembap.",
                        "Angkat buah terinfeksi keluar kebun dan pasang alas jerami kering atau papan berlubang di bawah buah yang masih sehat.",
                        "Perbaiki saluran drainase di antara bedengan dan berikan alas buah sejak buah seukuran apel.",
                        listOf("Selalu beri alas kering di bawah buah semangka", "Buat bedengan cukup tinggi"),
                        4, "Buah sehat di atas alas tetap kering dan kulit bawah mulus.",
                        "Pengendalian Busuk Buah Semangka — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "layu_mendadak_semangka",
                        "Sulur Semangka Layu di Siang Hari Lalu Mengering",
                        "Satu atau beberapa sulur utama mendadak terkulai layu sementara sulur lain awalnya masih segar.",
                        listOf("Sulur layu di siang hari lalu permanen", "Garis cokelat memanjang di jaringan dalam batang bila dipotong"),
                        listOf("Layu Fusarium (Fusarium oxysporum f.sp. niveum)"),
                        listOf("Apakah lahan pernah ditanami semangka atau melon sebelumnya?"),
                        "Sering terjadi satu sisi cabang layu lebih dulu karena penyumbatan pembuluh sebagian.",
                        "Cabut tanaman sakit dan buang jauh dari kebun; lakukan rotasi tanaman non-Cucurbitaceae minimal 2 musim.",
                        "Belum tersedia rekomendasi tervalidasi untuk mengobati layu Fusarium yang sudah menyumbat pembuluh.",
                        listOf("Rotasi tanaman wajib", "Gunakan varietas toleran dan kompos matang kaya mikroba baik"),
                        7, "Sulur sehat pada tanaman lain tetap segar.",
                        "Layu Fusarium Semangka — Literatur Patologi", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "antraknosa_semangka",
                        "Bercak Hitam Cekung di Daun & Kulit Buah",
                        "Daun berbercak hitam kering dan kulit buah memunculkan lekukan bulat agak gelap.",
                        listOf("Bercak cekung berbatas jelas pada buah", "Bercak cokelat gelap pada tangkai daun"),
                        listOf("Jamur antraknosa (Colletotrichum orbiculare)"),
                        listOf("Apakah bercak muncul menyebar cepat setelah hujan hangat?"),
                        "Bercak pada buah berbentuk cekung bulat tegas.",
                        "Singkirkan buah dan daun terinfeksi berat, siram di pangkal akar saja pagi hari.",
                        "Utamakan sanitasi dan pergiliran tanaman; konsultasikan penyuluh untuk penanganan lahan luas.",
                        listOf("Gunakan benih bersih bersertifikat", "Hindari bekerja di kebun saat daun masih basah embun"),
                        5, "Buah dan tunas sulur baru tumbuh bebas bercak cekung.",
                        "Penyakit Antraknosa Semangka — Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "sunburn_semangka",
                        "Bercak Putih Kering Keras di Bagian Atas Buah",
                        "Permukaan atas kulit buah yang menghadap matahari langsung berubah pucat keputihan lalu mengering cekung tipis.",
                        listOf("Bercak pucat kering hanya di sisi buah menghadap matahari siang"),
                        listOf("Sengatan matahari langsung (sunscald) akibat kurangnya daun pelindung"),
                        listOf("Apakah daun di sekitar buah rontok atau tersingkap sehingga buah terjemur penuh?"),
                        "Terjadi hanya di bagian atas buah yang terpapar sinar langsung, tanpa spora atau belatung.",
                        "Tutup lembut bagian atas buah dengan segenggam jerami kering atau arahkan sulur berdaun sehat menaungi buah.",
                        "Jaga kesehatan daun peneduh buah melalui pengairan dan pengendalian hama daun.",
                        listOf("Lindungi buah dengan jerami kering bila daun jarang"),
                        5, "Kulit bagian atas buah tidak bertambah pucat.",
                        "Gangguan Abiotik Semangka — Hortikultura", EvidenceLevel.C, "Rekomendasi ditinjau ahli"
                    ),
                    ProblemSeed2(
                        "kutu_daun_semangka",
                        "Daun Pucuk Sulur Melengkung Menggulung",
                        "Ujung sulur muda mengecil mengerut dan terasa lengket di bagian bawah.",
                        listOf("Ujung sulur mengumpul keriting", "Ada kutu kecil dan semut di balik daun muda"),
                        listOf("Serangan kutu daun (Aphis gossypii)"),
                        listOf("Apakah ada koloni kutu di ujung sulur yang mengerut?"),
                        "Menyerang jaringan ujung sulur yang lunak.",
                        "Bilas ujung sulur dengan air bersih pagi hari atau pangkas ujung sulur kecil yang sangat padat kutu, serta pasang perangkap kuning.",
                        "Kendalikan semut dan lindungi predator alami seperti kepik.",
                        listOf("Pantau ujung sulur 2 kali seminggu"),
                        3, "Sulur baru memanjang lurus dengan daun terbuka lebar.",
                        "PHT Semangka — Balitsa", EvidenceLevel.D, "Teruji lapangan"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "pepaya", "Pepaya (Calina / California)", listOf(
                    ProblemSeed2(
                        "kutu_putih_pepaya",
                        "Lapisan Putih Kapas Menempel di Balik Daun & Buah Pepaya",
                        "Permukaan bawah daun, tulang daun, dan pangkal buah tertutup gumpalan putih berlilin disertai semut dan embun jelaga hitam.",
                        listOf("Gumpalan putih tepung berlilin tebal", "Daun atas tampak hitam berjelaga dan kerdil"),
                        listOf("Kutu putih pepaya (Paracoccus marginatus)"),
                        listOf("Apakah ada lapisan putih tebal di sepanjang tulang daun bawah dan leher buah?"),
                        "Kutu putih pepaya membentuk koloni lilin tebal yang tidak mudah hilang hanya oleh hujan ringan.",
                        "Semprot koloni dengan air bertekanan cukup kuat berulang di pagi hari atau sikat lembut tangkai buah; petik dan musnahkan daun tua paling parah.",
                        "Kendalikan semut pelindung kutu di batang; untuk pohon tinggi atau kebun luas, konsultasikan pengendalian hayati (Acerophagus papayae) ke dinas pertanian.",
                        listOf("Periksa bibit sebelum tanam", "Kendalikan semut di pangkal pohon"),
                        5, "Koloni putih berkurang dan daun pucuk baru tumbuh hijau normal.",
                        "Pengendalian Kutu Putih Pepaya — Balitbu & Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "busuk_pangkal_batang_pepaya",
                        "Pangkal Batang Dekat Tanah Basah Kehitaman & Pohon Mudah Roboh",
                        "Leher batang pepaya di permukaan tanah melunak basah gelap dan daun atas menguning lalu layu.",
                        listOf("Kulit pangkal batang lunak busuk", "Terjadi setelah hujan lebat atau genangan"),
                        listOf("Jamur Phytophthora palmivora", "Penanaman tanpa guludan di tanah tergenang"),
                        listOf("Apakah air hujan sering menggenang di sekitar batang pepaya?"),
                        "Pepaya sangat tidak toleran genangan walau hanya 24 jam.",
                        "Segera buat parit pembuangan air menjauh dari pangkal batang dan jangan menumpuk kompos basah menempel langsung ke kulit batang.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyelamatkan pohon yang pangkal batangnya sudah busuk melingkar; fokus pada drainase pohon lain.",
                        listOf("Selalu tanam di atas guludan/gundukan 20–30 cm", "Jauhkan genangan air"),
                        5, "Area pangkal batang tetap kering dan pohon lain berdiri kokoh.",
                        "Penyakit Busuk Pangkal Pepaya — Balitbu Tropika", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "virus_prsv_pepaya",
                        "Daun Pucuk Mengerut Kecil Seperti Tali Sepatu & Bercak Cincin di Buah",
                        "Daun muda mengecil terdistorsi parah, tangkai daun bergaris basah gelap, dan kulit buah muda memiliki bercak cincin.",
                        listOf("Bercak cincin hijau tua pada kulit buah", "Garis berminyak pada tangkai daun", "Daun muda mengecil seperti tali"),
                        listOf("Papaya Ringspot Virus (PRSV) yang ditularkan kutu daun"),
                        listOf("Apakah terdapat pola cincin pada buah dan garis hijau tua berminyak di tangkai daun?"),
                        "Bercak cincin pada buah disertai garis berminyak pada tangkai adalah ciri kuat PRSV.",
                        "Jika terkonfirmasi dini pada 1 pohon di kebun berisi banyak pohon muda, cabut untuk mencegah penularan lewat kutu; tidak bisa diobati dengan pupuk.",
                        "Belum tersedia obat antivirus untuk tanaman; kendalikan vektor kutu daun dan jaga nutrisi pohon sehat.",
                        listOf("Gunakan bibit bebas virus", "Jangan tanam cucurbit/labu dekat pepaya karena bisa berbagi vektor"),
                        7, "Pohon pepaya muda lain terlindungi dari penularan cepat.",
                        "Pengenalan PRSV Pepaya — Balitbu Tropika", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "benjolan_getah_buah_pepaya",
                        "Kulit Buah Berbenjol-Benjol Keluar Getah & Biji Kurang Jadi",
                        "Buah pepaya berbentuk bengkok berbenjol tidak mulus dan keluar bintik getah mengeras di kulitnya.",
                        listOf("Permukaan buah berbenjol tidak rata", "Tetesan getah di kulit tanpa luka tusukan"),
                        listOf("Kekurangan unsur mikro Boron di tanah", "Kekeringan panjang saat pembungaan"),
                        listOf("Apakah tidak ada bekas tusukan lalat buah di dalam benjolan tersebut?"),
                        "Jika dibelah tidak ada belatung namun daging di bawah benjolan terasa agak keras, curigai kekurangan boron.",
                        "Perkaya tanah dengan kompos matang beragam bahan organik dan jaga kelembapan tanah saat musim kemarau.",
                        "Konsultasikan uji tanah atau aplikasi boron dosis sangat kecil terukur (kelebihan boron beracun bagi tanaman).",
                        listOf("Gunakan kompos matang lengkap", "Jaga penyiraman saat kemarau"),
                        14, "Buah-buah baru di tingkat atas tumbuh lebih mulus.",
                        "Defisiensi Boron pada Pepaya — Balitbu Tropika", EvidenceLevel.B, "Riset terpublikasi (peer-reviewed)"
                    ),
                    ProblemSeed2(
                        "antraknosa_buah_pepaya",
                        "Bercak Bulat Cekung Lembek Saat Buah Mulai Matang",
                        "Saat buah pepaya mulai menguning, muncul bercak bulat berair cekung yang cepat meluas dan membusukkan daging buah.",
                        listOf("Bercak bulat cekung cokelat dengan cincin merah muda/jingga", "Cepat membesar saat buah matang"),
                        listOf("Jamur Colletotrichum gloeosporioides (antraknosa)"),
                        listOf("Apakah bercak baru terlihat jelas ketika buah mulai menguning?"),
                        "Infeksi sering menempel sejak buah muda lalu muncul saat buah melunak matang.",
                        "Panen buah saat muncul 1–2 semburat kuning (jangan tunggu terlalu matang di pohon), bersihkan daun tua kering dan buah jatuh busuk.",
                        "Sanitasi daun tua dan buah jatuh adalah langkah terpenting menekan sumber spora.",
                        listOf("Bersihkan daun tua kering yang menggantung", "Jaga kebersihan sekitar pohon"),
                        7, "Buah yang dipanen tepat waktu matang bersih dan mulus.",
                        "Antraknosa Buah Pepaya — Balitbu Tropika", EvidenceLevel.A, "Panduan teknis resmi"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "pisang", "Pisang", listOf(
                    ProblemSeed2(
                        "layu_fusarium_pisang",
                        "Daun Tua Menguning dari Tepi & Pangkal Batang Pecah (Panama)",
                        "Daun paling tua menguning dari pinggir menuju tulang tengah, tangkai daun patah menggantung membentuk rok di batang, dan batang dalam cokelat kemerahan.",
                        listOf("Daun tua menguning berurutan ke atas", "Tangkai daun patah di dekat batang", "Garis cokelat-ungu di irisan batang semu"),
                        listOf("Layu Fusarium (Fusarium oxysporum f.sp. cubense)"),
                        listOf("Apakah daun tua patah menggantung dan penampang batang berwarna kecokelatan tanpa bau busuk menyengat?"),
                        "Layu Fusarium umumnya tidak mengeluarkan lendir berbau sangat busuk seperti layu bakteri darah.",
                        "Jangan ambil anakan dari rumpun sakit untuk ditanam! Bongkar rumpun sakit, bersihkan alat dengan api/alkohol, dan ganti jenis tanaman.",
                        "Belum tersedia pengobatan kimia yang efektif menyembuhkan pohon terinfeksi; pencegahan lewat bibit kultur jaringan sehat adalah kunci.",
                        listOf("Gunakan bibit kultur jaringan sehat", "Sterilkan parang antar rumpun"),
                        7, "Rumpun sehat di lokasi lain tidak tertular melalui alat atau bibit.",
                        "Penanganan Layu Fusarium Pisang — Balitbu Tropika", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "penyakit_darah_pisang",
                        "Daun Muda Layu Kuning & Jantung/Buah Menghitam Busuk Berlendir",
                        "Daun muda tengah berubah kuning pucat layu, jantung pisang mengerut hitam, dan saat buah dibelah dagingnya busuk cokelat kemerahan.",
                        listOf("Jantung pisang menghitam membusuk", "Lendir kemerahan/kecokelatan keluar saat batang dipotong", "Daging buah busuk merah-cokelat meski kulit luar tampak utuh"),
                        listOf("Layu bakteri Ralstonia syzygii subsp. celebesensis (penyakit darah)"),
                        listOf("Apakah bagian dalam buah busuk kecokelatan dan jantung mengering hitam?"),
                        "Daging buah membusuk kemerahan dan jantung menghitam adalah ciri kuat penyakit darah.",
                        "Bungkus tandan dengan plastik segera setelah jantung keluar dan potong jantung lebih awal dengan alat steril; musnahkan rumpun terinfeksi.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyembuhkan pohon sakit; fokus pada pembungkusan tandan untuk mencegah penularan serangga.",
                        listOf("Bungkus tandan segera setelah jantung keluar", "Sterilkan parang dengan alkohol/api"),
                        7, "Penularan ke rumpun pisang lain melalui serangga dan alat terhenti.",
                        "Pengendalian Penyakit Darah Pisang — Kementan RI", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "bercak_sigatoka_pisang",
                        "Garis Kuning Berubah Cokelat Kering di Daun (Sigatoka)",
                        "Muncul garis-garis tipis kuning sejajar urat daun yang melebar menjadi bercak cokelat kering dengan tepi kuning.",
                        listOf("Garis kecil sejajar tulang daun", "Bercak kering luas membuat ujung daun mati"),
                        listOf("Jamur bercak daun Sigatoka (Mycosphaerella)", "Rumpun terlalu rimbun dan lembap"),
                        listOf("Apakah rumpun memiliki terlalu banyak anakan berdesakan?"),
                        "Mengurangi luas daun hijau sehingga pengisian tandan kurang maksimal jika >50% daun rusak.",
                        "Pangkas dan buang bagian daun yang mengering berat (deleafing), atur jumlah anakan cukup 1 induk + 1 anakan, dan perbaiki drainase.",
                        "Pengaturan anakan dan pemotongan bagian daun sakit sangat efektif menekan kelembapan di rumpun.",
                        listOf("Sisakan maksimal 2–3 batang dalam 1 rumpun", "Pangkas daun kering tiap 2 minggu"),
                        7, "Daun-daun bendera baru tetap hijau bersih hingga buah matang.",
                        "Manajemen Sigatoka Pisang — Balitbu Tropika", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "kudis_kulit_pisang",
                        "Kulit Buah Berbintik Kasar Kecokelatan (Kudis / Scab)",
                        "Kulit buah pisang muda bertaburan bintik cokelat kasar seperti luka goresan meski daging buah di dalam masih baik.",
                        listOf("Permukaan kulit buah kasar berbintik cokelat", "Muncul sejak buah baru terbuka dari seludang"),
                        listOf("Hama thrips bunga/buah pisang (Chaetanaphothrips)", "Gesekan daun kering saat angin"),
                        listOf("Apakah tandan dibungkus saat masih muda?"),
                        "Hanya merusak penampilan kulit luar; buah masih aman dikonsumsi.",
                        "Bungkus tandan pisang dengan kantong plastik biru berlubang segera setelah semua sisir terbentuk dan jantung dipotong.",
                        "Pembungkusan tandan fisik adalah solusi standar petani pisang untuk menghasilkan kulit buah mulus.",
                        listOf("Bungkus tandan sejak muda", "Pangkas daun kering dekat tandan"),
                        14, "Tandan baru yang dibungkus menghasilkan buah berkulit mulus cerah.",
                        "Pembungkusan Tandan Pisang — Balitbu Tropika", EvidenceLevel.D, "Teruji lapangan"
                    ),
                    ProblemSeed2(
                        "tandan_kecil_pisang",
                        "Tandan Buah Kecil & Siku Buah Lama Membulat",
                        "Jumlah sisir sedikit dan pengisian buah terasa lambat atau tipis.",
                        listOf("Sisir buah sedikit (<5 sisir)", "Batang semu induk relatif kurus"),
                        listOf("Terlalu banyak anakan berebut nutrisi dalam satu rumpun", "Kekurangan kalium dan air saat pembentukan tandan"),
                        listOf("Ada berapa batang/anakan dalam satu rumpun pisang saat ini?"),
                        "Rumpun yang dibiarkan tumbuh 5–8 anakan sekaligus hampir selalu menghasilkan tandan kecil.",
                        "Kurangi anakan hingga menyisakan 1–2 anakan terbaik, beri mulsa dan kompos matang ditambah sumber kalium alami di sekeliling rumpun.",
                        "Atur jumlah anakan terlebih dahulu lalu berikan pemupukan kalium berimbang sesuai umur rumpun.",
                        listOf("Lakukan penjarangan anakan rutin", "Siram saat kemarau panjang"),
                        14, "Anakan penerus tumbuh lebih gemuk berbatang kokoh.",
                        "Pengelolaan Rumpun Pisang Produktif — Balitbu", EvidenceLevel.A, "Panduan teknis resmi"
                    )
                )
            )
        )
        addAll(
            buildProblems2(
                "jeruk", "Jeruk Nipis / Jeruk Santang", listOf(
                    ProblemSeed2(
                        "leafminer_jeruk",
                        "Daun Muda Berkelok Keperakan & Menggulung (Peliang Daun)",
                        "Tunas daun muda yang baru keluar tampak memiliki garis berkelok mengilap dan helainya mengerut melipat.",
                        listOf("Jalur berliku keperakan di dalam jaringan daun muda", "Daun mengerut tidak mau rata"),
                        listOf("Larva ngengat peliang daun jeruk (Phyllocnistis citrella)"),
                        listOf("Apakah serangan terjadi tepat pada tunas daun muda yang baru muncul?"),
                        "Menyerang khusus daun muda lunak; daun tua yang sudah kaku tidak lagi diserang.",
                        "Petik tunas daun muda yang tergulung parah untuk memutus siklus, atur penyiraman/pemupukan agar tunas baru muncul serempak.",
                        "Di tabulampot pekarangan, pemangkasan tunas tergulung di awal sudah sangat membantu menjaga tajuk tetap rapi.",
                        listOf("Jangan terlalu sering merangsang tunas muda terus-menerus dengan N tinggi"),
                        7, "Gelombang tunas muda berikutnya tumbuh rata hijau mengilap.",
                        "PHT Peliang Daun Jeruk — Balitjestro Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "cvpd_jeruk",
                        "Daun Menguning Belang Tidak Simetris & Buah Kerdil Asam (CVPD)",
                        "Sebagian cabang memunculkan daun dengan belang kuning-hijau yang tidak simetris antara sisi kiri dan kanan tulang daun.",
                        listOf("Belang kuning tidak simetris (blotchy mottle)", "Daun mengecil kaku mengarah ke atas", "Buah kerdil terasa pahit/asam dengan biji kempes"),
                        listOf("Dugaan penyakit CVPD / Huanglongbing yang ditularkan kutu loncat jeruk (Diaphorina citri)", "Bisa juga menyerupai defisiensi Zn jika simetris"),
                        listOf("Apakah pola kuning di kiri dan kanan tulang daun berbeda/tidak simetris?"),
                        "Defisiensi hara (Zn/Mg) umumnya menghasilkan pola kuning simetris di kedua belah daun; CVPD menghasilkan bercak kuning acak asimetris.",
                        "Periksa apakah ada kutu loncat di tunas muda; untuk dugaan CVPD, konsultasikan petugas penyuluh/Balitjestro sebelum mengambil tindakan tegas pada pohon.",
                        "Belum tersedia rekomendasi tervalidasi untuk menyembuhkan pohon yang sudah positif terinfeksi CVPD; jangan mengambil mata tunas dari pohon tersebut untuk perbanyakan.",
                        listOf("Selalu beli bibit berlabel bebas penyakit", "Kendalikan vektor kutu loncat jeruk"),
                        7, "Identifikasi jelas apakah defisiensi nutrisi simetris atau dugaan CVPD.",
                        "Pengenalan Gejala CVPD Jeruk — Balitjestro", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "kanker_jeruk",
                        "Bintik Cokelat Menonjol Seperti Gabus Dikelilingi Halo Kuning",
                        "Pada daun, ranting, dan kulit buah tampak bintil cokelat kasar menonjol seperti kawah gabus dengan lingkar kuning di tepinya.",
                        listOf("Lesi gabus menonjol terasa kasar saat diraba", "Cincin kuning mengelilingi bintik pada daun"),
                        listOf("Bakteri Xanthomonas citri (kanker jeruk)", "Luka akibat leafminer dan percikan hujan angin"),
                        listOf("Apakah bintik terasa menonjol kasar di kedua sisi daun serta di kulit buah?"),
                        "Tekstur kasar menonjol bergabus di kedua permukaan daun membedakannya dari bercak jamur biasa.",
                        "Pangkas ranting dan daun yang penuh lesi kanker pada cuaca kering, kendalikan leafminer, dan hindari menyiram tajuk pohon.",
                        "Sanitasi bagian terinfeksi pada cuaca kering dan pencegahan peliang daun adalah langkah utama.",
                        listOf("Pangkas tajuk dalam agar cepat kering", "Jangan perbanyak bibit dari pohon bergejala"),
                        7, "Tunas baru tumbuh bersih tanpa bintik gabus.",
                        "Penyakit Kanker Jeruk — Balitjestro Kementan", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "kutu_sisik_embun_jelaga",
                        "Lapisan Hitam Seperti Jelaga Menutupi Daun Jeruk",
                        "Permukaan atas daun jeruk tampak kotor tertutup kerak tipis hitam seperti jelaga yang bisa dikelupas.",
                        listOf("Lapisan hitam kering di sisi atas daun", "Di balik daun atau ranting ada kutu sisik/kutu putih/kutu loncat"),
                        listOf("Jamur embun jelaga yang tumbuh di atas cairan manis dari hama kutu", "Tajuk terlalu rimbun teduh"),
                        listOf("Apakah lapisan hitam dapat diseka atau dikelupas dari daun hijau di bawahnya?"),
                        "Embun jelaga bukan penyebab kerusakan utama—ia hanya indikator adanya hama pengisap dan semut di tajuk.",
                        "Lap/bersihkan daun dan sikat kutu sisik di ranting dengan air bersih, kendalikan semut di pangkal batang, dan pangkas cabang dalam yang terlalu rimbun.",
                        "Setelah kutu pengisap dan semut dikendalikan, embun jelaga akan mengering dan rontok sendiri.",
                        listOf("Pangkas ranting bersilangan di tengah tajuk", "Hentikan jalur semut naik ke batang"),
                        7, "Kutu sisik berkurang dan daun baru tumbuh hijau bersih mengilap.",
                        "Pengelolaan Embun Jelaga Jeruk — Balitjestro", EvidenceLevel.A, "Panduan teknis resmi"
                    ),
                    ProblemSeed2(
                        "defisiensi_seng_jeruk",
                        "Daun Muda Berpola Kuning Simetris di Antara Tulang Daun",
                        "Daun-daun baru berukuran lebih kecil dengan warna kuning pucat simetris di antara tulang daun hijau.",
                        listOf("Urat daun tetap hijau tegas membentuk jaring, daging daun di antaranya kuning pucat", "Pola simetris kiri dan kanan"),
                        listOf("Kekurangan unsur mikro Seng (Zn) atau Magnesium", "Media pot terlalu padat atau pH terlalu tinggi/rendah"),
                        listOf("Sudah berapa lama media tabulampot belum ditambah kompos atau digemburkan?"),
                        "Pola tulang daun hijau dengan latar kuning simetris pada daun muda berbeda dari CVPD yang acak asimetris.",
                        "Gemburkan lapisan atas media pot dengan hati-hati dan tambahkan kompos matang yang kaya unsur mikro alami.",
                        "Periksa pH media (ideal 6,0–6,5) lalu berikan pupuk mikro mengandung Zn sesuai dosis kemasan bila gejala menetap.",
                        listOf("Peremajaan lapisan atas media tabulampot tiap 6 bulan", "Jaga drainase pot"),
                        14, "Gelombang tunas daun berikutnya tumbuh lebih lebar dengan warna hijau merata.",
                        "Diagnosis Defisiensi Hara Jeruk — Balitjestro", EvidenceLevel.A, "Panduan teknis resmi"
                    )
                )
            )
        )
    }
}
