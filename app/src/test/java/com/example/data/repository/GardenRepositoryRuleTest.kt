package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.firebase.FirestoreGardenRepository
import com.example.model.CultivationMethod
import com.example.model.GrowthStage
import com.example.model.HealthStatus
import com.example.model.TreatmentPlan
import com.example.model.TreatmentStatus
import com.example.model.UserPlant
import com.example.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GardenRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun addPlant_authenticatedOwner_createsAndObservesPlant() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repo = FirestoreGardenRepository(firestore)

        val plant = UserPlant(
            userId = uid,
            plantKnowledgeId = "cabai",
            plantName = "Cabai Rawit",
            nickname = "Cabai Test",
            scientificName = "Capsicum annuum",
            category = "Sayuran Buah",
            cultivationMethod = CultivationMethod.ORGANIC.key,
            plantingLocation = "Polybag",
            plantingDate = "2026-11-15",
            growthStage = GrowthStage.VEGETATIVE.key,
            healthStatus = HealthStatus.HEALTHY.key,
            completedStages = listOf("stage_1")
        )

        val createResult = withTimeout(DEFAULT_TIMEOUT_MS) { repo.addPlant(uid, plant) }
        assertTrue(createResult.isSuccess)
        val plantId = createResult.getOrThrow()

        val observed = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observePlants(uid).first { list -> list.any { it.id == plantId } }
        }
        assertTrue(observed.any { it.id == plantId && it.plantName == "Cabai Rawit" })
    }

    @Test
    fun saveProfile_authenticatedOwner_succeeds() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repo = FirestoreGardenRepository(firestore)

        val profile = UserProfile(
            userId = uid,
            displayName = "Alice Petani",
            onboardingCompleted = true
        )
        val result = withTimeout(DEFAULT_TIMEOUT_MS) { repo.saveProfile(uid, profile) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun updateTreatmentStatus_completedLock_rejectsUpdate() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repo = FirestoreGardenRepository(firestore)

        val plan = TreatmentPlan(
            userId = uid,
            plantId = "p_1",
            plantName = "Tomat",
            diagnosisId = "d_1",
            problem = "Ujung buah menghitam",
            suspectedCause = "Fluktuasi air",
            recommendedTreatment = "Siram teratur di pangkal.",
            treatmentDate = "2026-11-15",
            followUpDate = "2026-11-20",
            expectedResponse = "Buah baru mulus",
            status = TreatmentStatus.IN_PROGRESS.key
        )
        val planId = withTimeout(DEFAULT_TIMEOUT_MS) { repo.addTreatmentPlan(uid, plan).getOrThrow() }
        val completeRes = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.updateTreatmentStatus(uid, planId, TreatmentStatus.COMPLETED.key, "Sudah pulih")
        }
        assertTrue(completeRes.isSuccess)

        val reopenRes = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.updateTreatmentStatus(uid, planId, TreatmentStatus.IN_PROGRESS.key, "Coba ubah")
        }
        assertTrue(reopenRes.isFailure)
        val firestoreEx = reopenRes.exceptionOrNull()?.unwrapFirestoreException()
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
    }

    @Test
    fun observePlants_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repo = FirestoreGardenRepository(firestore)
        withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.addPlant(
                aliceUid,
                UserPlant(
                    userId = aliceUid,
                    plantKnowledgeId = "pakcoy",
                    plantName = "Pakcoy",
                    scientificName = "Brassica rapa",
                    category = "Sayuran Daun"
                )
            ).getOrThrow()
        }

        signInTestUser(BOB_EMAIL)
        var caught: Throwable? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) { repo.observePlants(aliceUid).first() }
        } catch (e: Throwable) {
            caught = e
        }
        val firestoreEx = checkNotNull(caught?.unwrapFirestoreException()) {
            "Expected FirebaseFirestoreException for cross-user access, got $caught"
        }
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx.code)
    }

    @Test
    fun observePlants_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repo = FirestoreGardenRepository(firestore)

        var caught: Throwable? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) { repo.observePlants("some_uid").first() }
        } catch (e: Throwable) {
            caught = e
        }
        val firestoreEx = checkNotNull(caught?.unwrapFirestoreException()) {
            "Expected FirebaseFirestoreException for unauthenticated access, got $caught"
        }
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx.code)
    }

    private companion object {
        const val ALICE_EMAIL = "alice_garden@test.com"
        const val BOB_EMAIL = "bob_garden@test.com"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
