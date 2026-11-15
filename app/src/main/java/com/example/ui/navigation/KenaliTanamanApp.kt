package com.example.ui.navigation

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.data.firebase.DemoGardenRepository
import com.example.data.firebase.FirestoreGardenRepository
import com.example.data.firebase.GoogleAuthHelper
import com.example.data.knowledge.PlantKnowledgeRepository
import com.example.data.preferences.UserSessionPreferences
import com.example.model.CultivationMethod
import com.example.model.GrowthStage
import com.example.ui.screens.AskPlantAiScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CareCalendarScreen
import com.example.ui.screens.CultivationGuideScreen
import com.example.ui.screens.CultivationMethodSelectionScreen
import com.example.ui.screens.GrowthMonitoringScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.IdentifyPlantScreen
import com.example.ui.screens.KnowledgeArticleScreen
import com.example.ui.screens.KnowledgeBaseScreen
import com.example.ui.screens.MyGardenScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PhotoComparisonScreen
import com.example.ui.screens.PlantDoctorScreen
import com.example.ui.screens.PlantProfileScreen
import com.example.ui.screens.ProfileAndSettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TreatmentTrackingScreen
import com.example.ui.viewmodel.AiPlantViewModel
import com.example.ui.viewmodel.GardenSnapshot
import com.example.ui.viewmodel.GardenViewModel
import com.example.ui.viewmodel.UiState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

private object Routes {
    const val HOME = "home"
    const val GARDEN = "garden"
    const val IDENTIFY = "identify"
    const val KNOWLEDGE = "knowledge"
    const val PROFILE = "profile"

    const val PLANT_DETAIL = "plant_detail/{plantId}"
    const val METHOD_SELECT = "method_select/{cropId}/{plantId}"
    const val GUIDE = "guide/{cropId}/{plantId}/{methodKey}"
    const val DOCTOR = "doctor?plantId={plantId}&name={name}&sci={sci}"
    const val TREATMENTS = "treatments?plantId={plantId}"
    const val MONITORING = "monitoring?plantId={plantId}"
    const val COMPARE_PHOTOS = "compare_photos"
    const val CALENDAR = "calendar"
    const val ARTICLE = "article/{problemId}"
    const val ASK_AI = "ask_ai?prompt={prompt}"
}

private data class NavTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

private val mainTabs = listOf(
    NavTab(Routes.HOME, "Beranda", Icons.Rounded.Home, "nav_home"),
    NavTab(Routes.GARDEN, "Kebun Saya", Icons.Rounded.Eco, "nav_garden"),
    NavTab(Routes.IDENTIFY, "Identifikasi", Icons.Rounded.CameraAlt, "nav_identify"),
    NavTab(Routes.KNOWLEDGE, "Panduan", Icons.Rounded.MenuBook, "nav_knowledge"),
    NavTab(Routes.PROFILE, "Profil", Icons.Rounded.Person, "nav_profile")
)

@Composable
fun KenaliTanamanRoot() {
    val context = LocalContext.current
    val sessionPrefs = remember(context) { UserSessionPreferences(context.applicationContext) }
    val scope = rememberCoroutineScope()

    var showSplash by rememberSaveable { mutableStateOf(true) }
    val currentUser by GoogleAuthHelper.authStateFlow(Firebase.auth)
        .collectAsStateWithLifecycle(initialValue = Firebase.auth.currentUser)
    val isDemoMode by sessionPrefs.isDemoMode.collectAsStateWithLifecycle(initialValue = false)
    val isLocalOnboardingDone by sessionPrefs.isLocalOnboardingDone.collectAsStateWithLifecycle(initialValue = false)

    if (showSplash) {
        SplashScreen(onFinished = { showSplash = false })
        return
    }

    val user = currentUser
    if (user == null && !isDemoMode) {
        // Strict Auth Gate: Feature ViewModels & Firestore listeners are NEVER instantiated while unauthenticated.
        AuthScreen(
            onAuthSuccess = {
                scope.launch { sessionPrefs.setDemoMode(false) }
            },
            onDemoModeSelected = {
                scope.launch { sessionPrefs.setDemoMode(true) }
            }
        )
    } else {
        val activeUid = user?.uid ?: "demo_guest"
        val demoFlag = (user == null)
        val initialName = user?.displayName?.takeIf { it.isNotBlank() } ?: if (demoFlag) "Tamu Demo" else "Pekebun"

        key(activeUid, demoFlag) {
            AuthenticatedOrDemoShell(
                currentUserId = activeUid,
                userEmail = user?.email,
                initialDisplayName = initialName,
                isDemoMode = demoFlag,
                isLocalOnboardingDone = isLocalOnboardingDone,
                onMarkLocalOnboardingDone = {
                    scope.launch { sessionPrefs.setLocalOnboardingDone(true) }
                },
                onSwitchFromDemoToGoogle = {
                    scope.launch { sessionPrefs.setDemoMode(false) }
                },
                onSignOutComplete = {
                    scope.launch {
                        sessionPrefs.setDemoMode(false)
                        sessionPrefs.setLocalOnboardingDone(false)
                    }
                }
            )
        }
    }
}

@Composable
private fun AuthenticatedOrDemoShell(
    currentUserId: String,
    userEmail: String?,
    initialDisplayName: String,
    isDemoMode: Boolean,
    isLocalOnboardingDone: Boolean,
    onMarkLocalOnboardingDone: () -> Unit,
    onSwitchFromDemoToGoogle: () -> Unit,
    onSignOutComplete: () -> Unit
) {
    val gardenViewModel: GardenViewModel = viewModel(
        key = "${currentUserId}_$isDemoMode",
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY]) as Application
                val repository = if (isDemoMode) {
                    DemoGardenRepository()
                } else {
                    val databaseId = app.getString(R.string.firestore_database_id)
                    FirestoreGardenRepository(FirebaseFirestore.getInstance(databaseId))
                }
                GardenViewModel(
                    repository = repository,
                    currentUserId = currentUserId,
                    isDemoMode = isDemoMode,
                    initialDisplayName = initialDisplayName
                )
            }
        }
    )
    val aiViewModel: AiPlantViewModel = viewModel(key = "ai_${currentUserId}_$isDemoMode")

    val gardenState by gardenViewModel.gardenState.collectAsStateWithLifecycle()
    val snackbarMsg by gardenViewModel.snackbarMessage.collectAsStateWithLifecycle()
    var replayOnboarding by rememberSaveable { mutableStateOf(false) }

    when (val state = gardenState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is UiState.Success -> {
            val snapshot = state.data
            val needsOnboarding = replayOnboarding ||
                (!isLocalOnboardingDone && snapshot.profile?.onboardingCompleted != true)

            if (needsOnboarding) {
                OnboardingScreen(
                    initialDisplayName = snapshot.profile?.displayName ?: initialDisplayName,
                    onSavePreferences = { name, exp, gardenType, method, reminders ->
                        replayOnboarding = false
                        onMarkLocalOnboardingDone()
                        gardenViewModel.saveProfile(
                            displayName = name,
                            experienceLevel = exp,
                            gardeningType = gardenType,
                            preferredMethod = method,
                            remindersEnabled = reminders,
                            onboardingCompleted = true
                        )
                    },
                    onSkip = {
                        replayOnboarding = false
                        onMarkLocalOnboardingDone()
                        gardenViewModel.saveProfile(
                            displayName = snapshot.profile?.displayName ?: initialDisplayName,
                            experienceLevel = snapshot.profile?.experienceLevel ?: "beginner",
                            gardeningType = snapshot.profile?.gardeningType ?: "polybag",
                            preferredMethod = CultivationMethod.fromKey(snapshot.profile?.preferredMethod),
                            remindersEnabled = snapshot.profile?.remindersEnabled ?: true,
                            onboardingCompleted = true
                        )
                    }
                )
            } else {
                MainScaffoldWithNav(
                    snapshot = snapshot,
                    currentUserId = currentUserId,
                    userEmail = userEmail,
                    isDemoMode = isDemoMode,
                    snackbarMessage = snackbarMsg,
                    gardenViewModel = gardenViewModel,
                    aiViewModel = aiViewModel,
                    onReplayOnboarding = { replayOnboarding = true },
                    onSwitchFromDemoToGoogle = onSwitchFromDemoToGoogle,
                    onSignOutComplete = onSignOutComplete
                )
            }
        }
    }
}

@Composable
private fun MainScaffoldWithNav(
    snapshot: GardenSnapshot,
    currentUserId: String,
    userEmail: String?,
    isDemoMode: Boolean,
    snackbarMessage: String?,
    gardenViewModel: GardenViewModel,
    aiViewModel: AiPlantViewModel,
    onReplayOnboarding: () -> Unit,
    onSwitchFromDemoToGoogle: () -> Unit,
    onSignOutComplete: () -> Unit
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val identBitmap by aiViewModel.identificationBitmap.collectAsStateWithLifecycle()
    val identState by aiViewModel.identificationState.collectAsStateWithLifecycle()
    val diagBitmap by aiViewModel.diagnosisBitmap.collectAsStateWithLifecycle()
    val diagState by aiViewModel.diagnosisState.collectAsStateWithLifecycle()
    val compareState by aiViewModel.compareState.collectAsStateWithLifecycle()
    val chatMessages by aiViewModel.chatMessages.collectAsStateWithLifecycle()
    val isChatStreaming by aiViewModel.isChatStreaming.collectAsStateWithLifecycle()
    val chatError by aiViewModel.chatError.collectAsStateWithLifecycle()

    val preferredMethod = CultivationMethod.fromKey(snapshot.profile?.preferredMethod)
    val showTopLevelNav = currentRoute in mainTabs.map { it.route }

    LaunchedEffect(snackbarMessage) {
        if (!snackbarMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(snackbarMessage)
            gardenViewModel.clearSnackbar()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= 840.dp

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (showTopLevelNav && !isExpanded) {
                    NavigationBar {
                        mainTabs.forEach { tab ->
                            NavigationBarItem(
                                selected = currentRoute == tab.route,
                                onClick = {
                                    navController.navigate(tab.route) {
                                        popUpTo(Routes.HOME) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showTopLevelNav && isExpanded) {
                    NavigationRail(modifier = Modifier.fillMaxHeight()) {
                        mainTabs.forEach { tab ->
                            NavigationRailItem(
                                selected = currentRoute == tab.route,
                                onClick = {
                                    navController.navigate(tab.route) {
                                        popUpTo(Routes.HOME) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME,
                        modifier = Modifier
                            .widthIn(max = 920.dp)
                            .fillMaxSize()
                    ) {
                        composable(Routes.HOME) {
                            HomeDashboardScreen(
                                snapshot = snapshot,
                                isDemoMode = isDemoMode,
                                onNavigateIdentify = { navController.navigate(Routes.IDENTIFY) },
                                onNavigateStartGrowing = {
                                    navController.navigate("method_select/cabai/none")
                                },
                                onNavigatePlantDoctor = { plantId ->
                                    val q = if (plantId != null) "?plantId=$plantId" else ""
                                    navController.navigate("doctor$q")
                                },
                                onNavigateGarden = { navController.navigate(Routes.GARDEN) },
                                onNavigateKnowledge = { navController.navigate(Routes.KNOWLEDGE) },
                                onNavigateCalendar = { navController.navigate(Routes.CALENDAR) },
                                onNavigateAskAi = { navController.navigate("ask_ai") },
                                onOpenPlantDetail = { plantId -> navController.navigate("plant_detail/$plantId") },
                                onOpenFeaturedGuide = {
                                    navController.navigate("guide/cabai/none/${preferredMethod.key}")
                                },
                                onToggleCareTask = { task ->
                                    gardenViewModel.toggleCareTaskCompletion(task.id, !task.completed)
                                }
                            )
                        }

                        composable(Routes.GARDEN) {
                            MyGardenScreen(
                                plants = snapshot.plants,
                                preferredMethod = preferredMethod,
                                onPlantClick = { id -> navController.navigate("plant_detail/$id") },
                                onAddPlant = { kId, pName, nick, sci, cat, method, loc, date, stage, imgKey ->
                                    gardenViewModel.addPlant(
                                        plantKnowledgeId = kId,
                                        plantName = pName,
                                        nickname = nick,
                                        scientificName = sci,
                                        category = cat,
                                        cultivationMethod = method,
                                        plantingLocation = loc,
                                        plantingDate = date,
                                        growthStage = stage,
                                        imageResKey = imgKey
                                    )
                                }
                            )
                        }

                        composable(Routes.IDENTIFY) {
                            IdentifyPlantScreen(
                                bitmap = identBitmap,
                                identState = identState,
                                preferredMethod = preferredMethod,
                                onBitmapSelected = { aiViewModel.setIdentificationBitmap(it) },
                                onAnalyzeClick = { aiViewModel.analyzePlantPhoto(it) },
                                onResetClick = { aiViewModel.resetIdentification() },
                                onStartGuide = { cropId ->
                                    navController.navigate("method_select/$cropId/none")
                                },
                                onCheckHealthWithPhoto = { name, sci, bmp ->
                                    aiViewModel.setDiagnosisBitmap(bmp)
                                    navController.navigate("doctor?name=${Uri.encode(name)}&sci=${Uri.encode(sci)}")
                                },
                                onSaveToGarden = { kId, pName, nick, sci, cat, method, loc, imgKey, imgB64 ->
                                    gardenViewModel.addPlant(
                                        plantKnowledgeId = kId,
                                        plantName = pName,
                                        nickname = nick,
                                        scientificName = sci,
                                        category = cat,
                                        cultivationMethod = method,
                                        plantingLocation = loc,
                                        plantingDate = "2026-11-15",
                                        growthStage = GrowthStage.VEGETATIVE,
                                        imageResKey = imgKey,
                                        imageBase64 = imgB64,
                                        onAdded = { newId ->
                                            navController.navigate("plant_detail/$newId")
                                        }
                                    )
                                }
                            )
                        }

                        composable(Routes.KNOWLEDGE) {
                            KnowledgeBaseScreen(
                                onOpenCropGuide = { cropId ->
                                    navController.navigate("guide/$cropId/none/${preferredMethod.key}")
                                },
                                onOpenProblemArticle = { problemId ->
                                    navController.navigate("article/$problemId")
                                }
                            )
                        }

                        composable(Routes.PROFILE) {
                            ProfileAndSettingsScreen(
                                profile = snapshot.profile,
                                userEmail = userEmail,
                                isDemoMode = isDemoMode,
                                plantCount = snapshot.plants.size,
                                treatmentCount = snapshot.treatmentPlans.size,
                                photoCount = snapshot.growthPhotos.size,
                                onSaveProfile = { name, exp, gardenType, method, reminders ->
                                    gardenViewModel.saveProfile(name, exp, gardenType, method, reminders, true)
                                },
                                onReplayOnboarding = onReplayOnboarding,
                                onOpenCalendar = { navController.navigate(Routes.CALENDAR) },
                                onOpenTreatments = { navController.navigate("treatments") },
                                onOpenMonitoring = { navController.navigate("monitoring") },
                                onOpenAskAi = { navController.navigate("ask_ai") },
                                onSignedInFromDemo = onSwitchFromDemoToGoogle,
                                onSignedOut = onSignOutComplete
                            )
                        }

                        composable(
                            route = Routes.PLANT_DETAIL,
                            arguments = listOf(navArgument("plantId") { type = NavType.StringType })
                        ) { entry ->
                            val plantId = entry.arguments?.getString("plantId").orEmpty()
                            val plant = snapshot.plants.firstOrNull { it.id == plantId }
                            PlantProfileScreen(
                                plant = plant,
                                diagnoses = snapshot.diagnoses,
                                treatments = snapshot.treatmentPlans,
                                photos = snapshot.growthPhotos,
                                onBack = { navController.popBackStack() },
                                onUpdatePlant = { updated -> gardenViewModel.updatePlant(updated) },
                                onSwitchMethod = { m -> gardenViewModel.updatePlantMethod(plantId, m) },
                                onDeletePlant = {
                                    gardenViewModel.deletePlant(plantId) {
                                        navController.popBackStack()
                                    }
                                },
                                onOpenGuide = { p ->
                                    navController.navigate("guide/${p.plantKnowledgeId}/${p.id}/${p.cultivationMethod}")
                                },
                                onOpenPlantDoctor = { p ->
                                    navController.navigate("doctor?plantId=${p.id}")
                                },
                                onOpenTreatments = { p ->
                                    navController.navigate("treatments?plantId=${p.id}")
                                },
                                onOpenMonitoring = { p ->
                                    navController.navigate("monitoring?plantId=${p.id}")
                                },
                                onOpenCalendar = { navController.navigate(Routes.CALENDAR) }
                            )
                        }

                        composable(
                            route = Routes.METHOD_SELECT,
                            arguments = listOf(
                                navArgument("cropId") { type = NavType.StringType },
                                navArgument("plantId") { type = NavType.StringType }
                            )
                        ) { entry ->
                            val cropId = entry.arguments?.getString("cropId") ?: "cabai"
                            val plantId = entry.arguments?.getString("plantId") ?: "none"
                            var tempMethod by remember { mutableStateOf(preferredMethod) }
                            CultivationMethodSelectionScreen(
                                selectedMethod = tempMethod,
                                onSelectMethod = { tempMethod = it },
                                onContinue = {
                                    if (plantId != "none") {
                                        gardenViewModel.updatePlantMethod(plantId, tempMethod)
                                    }
                                    navController.navigate("guide/$cropId/$plantId/${tempMethod.key}") {
                                        popUpTo("method_select/$cropId/$plantId") { inclusive = true }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Routes.GUIDE,
                            arguments = listOf(
                                navArgument("cropId") { type = NavType.StringType },
                                navArgument("plantId") { type = NavType.StringType },
                                navArgument("methodKey") { type = NavType.StringType }
                            )
                        ) { entry ->
                            val cropId = entry.arguments?.getString("cropId") ?: "cabai"
                            val plantId = entry.arguments?.getString("plantId") ?: "none"
                            val methodKey = entry.arguments?.getString("methodKey") ?: preferredMethod.key
                            val knowledge = PlantKnowledgeRepository.findPlantById(cropId)
                                ?: PlantKnowledgeRepository.allPlants.first()
                            val activePlant = snapshot.plants.firstOrNull { it.id == plantId }
                            var activeMethod by remember(activePlant, methodKey) {
                                mutableStateOf(
                                    CultivationMethod.fromKey(activePlant?.cultivationMethod ?: methodKey)
                                )
                            }
                            CultivationGuideScreen(
                                knowledge = knowledge,
                                activePlant = activePlant,
                                selectedMethod = activeMethod,
                                onSelectMethod = { newMethod ->
                                    activeMethod = newMethod
                                    if (activePlant != null) {
                                        gardenViewModel.updatePlantMethod(activePlant.id, newMethod)
                                    }
                                },
                                onToggleStage = { stageId ->
                                    if (activePlant != null) {
                                        gardenViewModel.toggleStageCompleted(activePlant, stageId)
                                    }
                                },
                                onAddCropToGarden = { method ->
                                    gardenViewModel.addPlant(
                                        plantKnowledgeId = knowledge.id,
                                        plantName = knowledge.commonName,
                                        nickname = knowledge.commonName,
                                        scientificName = knowledge.scientificName,
                                        category = knowledge.category,
                                        cultivationMethod = method,
                                        plantingLocation = "Polybag / Pot",
                                        plantingDate = "2026-11-15",
                                        growthStage = GrowthStage.SEEDLING,
                                        imageResKey = knowledge.imageResKey,
                                        onAdded = { newId ->
                                            navController.navigate("plant_detail/$newId")
                                        }
                                    )
                                },
                                onOpenDoctor = {
                                    val q = if (activePlant != null) "?plantId=${activePlant.id}" else "?name=${Uri.encode(knowledge.commonName)}"
                                    navController.navigate("doctor$q")
                                },
                                onOpenArticle = {
                                    val firstProb = PlantKnowledgeRepository.problemsForCrop(knowledge.id).firstOrNull()
                                    if (firstProb != null) {
                                        navController.navigate("article/${firstProb.problemId}")
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Routes.DOCTOR,
                            arguments = listOf(
                                navArgument("plantId") { type = NavType.StringType; nullable = true; defaultValue = null },
                                navArgument("name") { type = NavType.StringType; nullable = true; defaultValue = null },
                                navArgument("sci") { type = NavType.StringType; nullable = true; defaultValue = null }
                            )
                        ) { entry ->
                            PlantDoctorScreen(
                                plants = snapshot.plants,
                                initialPlantId = entry.arguments?.getString("plantId"),
                                prefilledName = entry.arguments?.getString("name"),
                                prefilledSciName = entry.arguments?.getString("sci"),
                                bitmap = diagBitmap,
                                diagState = diagState,
                                currentUserId = currentUserId,
                                onBitmapSelected = { aiViewModel.setDiagnosisBitmap(it) },
                                onRunDiagnosis = { bmp, pName, sName, method, stage, pType, notes, ans ->
                                    aiViewModel.runPlantDiagnosis(bmp, pName, sName, method, stage, pType, notes, ans)
                                },
                                onSaveDiagnosis = { diag, createPlan, followUp ->
                                    gardenViewModel.saveDiagnosis(diag, createPlan, followUp)
                                },
                                onReset = { aiViewModel.resetDiagnosis() },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Routes.TREATMENTS,
                            arguments = listOf(
                                navArgument("plantId") { type = NavType.StringType; nullable = true; defaultValue = null }
                            )
                        ) { entry ->
                            TreatmentTrackingScreen(
                                plans = snapshot.treatmentPlans,
                                filterPlantId = entry.arguments?.getString("plantId"),
                                onUpdateOutcome = { planId, status, notes ->
                                    gardenViewModel.updateTreatmentOutcome(planId, status, notes)
                                },
                                onReassessWithDoctor = { pid ->
                                    val q = if (pid.isNotBlank()) "?plantId=$pid" else ""
                                    navController.navigate("doctor$q")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Routes.MONITORING,
                            arguments = listOf(
                                navArgument("plantId") { type = NavType.StringType; nullable = true; defaultValue = null }
                            )
                        ) { entry ->
                            GrowthMonitoringScreen(
                                plants = snapshot.plants,
                                photos = snapshot.growthPhotos,
                                initialPlantId = entry.arguments?.getString("plantId"),
                                currentUserId = currentUserId,
                                onAddPhoto = { gardenViewModel.addGrowthPhoto(it) },
                                onOpenCompare = { navController.navigate(Routes.COMPARE_PHOTOS) },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.COMPARE_PHOTOS) {
                            PhotoComparisonScreen(
                                photos = snapshot.growthPhotos,
                                compareState = compareState,
                                onCompareWithAi = { bBmp, aBmp, pName, bLabel, aLabel ->
                                    aiViewModel.compareGrowthPhotos(bBmp, aBmp, pName, bLabel, aLabel)
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.CALENDAR) {
                            CareCalendarScreen(
                                tasks = snapshot.careTasks,
                                plants = snapshot.plants,
                                currentUserId = currentUserId,
                                onToggleComplete = { t -> gardenViewModel.toggleCareTaskCompletion(t.id, !t.completed) },
                                onReschedule = { id, newDate, freq -> gardenViewModel.rescheduleCareTask(id, newDate, freq) },
                                onDeleteTask = { id -> gardenViewModel.deleteCareTask(id) },
                                onAddTask = { task -> gardenViewModel.addCareTask(task) },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Routes.ARTICLE,
                            arguments = listOf(navArgument("problemId") { type = NavType.StringType })
                        ) { entry ->
                            val problemId = entry.arguments?.getString("problemId").orEmpty()
                            val problem = PlantKnowledgeRepository.allProblems.firstOrNull { it.problemId == problemId }
                            KnowledgeArticleScreen(
                                problem = problem,
                                onAskAiAboutProblem = { q ->
                                    navController.navigate("ask_ai?prompt=${Uri.encode(q)}")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Routes.ASK_AI,
                            arguments = listOf(
                                navArgument("prompt") { type = NavType.StringType; nullable = true; defaultValue = null }
                            )
                        ) { entry ->
                            AskPlantAiScreen(
                                plants = snapshot.plants,
                                messages = chatMessages,
                                isStreaming = isChatStreaming,
                                chatError = chatError,
                                initialPrompt = entry.arguments?.getString("prompt"),
                                onSendMessage = { q, ctx -> aiViewModel.sendChatMessage(q, ctx) },
                                onResetChat = { aiViewModel.resetChat() },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
