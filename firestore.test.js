const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");
const ts = () => new Date(Date.now() - 2000);

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function validProfile(uid) {
  return {
    userId: uid,
    displayName: "Budi Petani",
    experienceLevel: "beginner",
    gardeningType: "home_garden",
    preferredMethod: "organic",
    remindersEnabled: true,
    onboardingCompleted: true,
    demoSeeded: false,
    createdAt: ts(),
    updatedAt: ts(),
  };
}

function validPlant(uid) {
  return {
    userId: uid,
    plantKnowledgeId: "cabai",
    plantName: "Cabai Rawit",
    nickname: "Cabai depan rumah",
    scientificName: "Capsicum annuum",
    category: "Sayuran Buah",
    cultivationMethod: "organic",
    plantingLocation: "Polybag",
    plantingDate: "2026-11-10",
    growthStage: "vegetative",
    healthStatus: "healthy",
    isDemoData: false,
    completedStages: ["stage_1"],
    createdAt: ts(),
    updatedAt: ts(),
  };
}

function validTreatment(uid) {
  return {
    userId: uid,
    plantId: "plant_001",
    plantName: "Cabai Rawit",
    diagnosisId: "diag_001",
    problem: "Daun bawah menguning",
    suspectedCause: "Media terlalu lembap",
    recommendedTreatment: "Periksa drainase polybag sebelum menyiram.",
    treatmentDate: "2026-11-15",
    followUpDate: "2026-11-18",
    expectedResponse: "Daun baru hijau segar",
    status: "in_progress",
    outcomeNotes: "",
    isDemoData: false,
    createdAt: ts(),
    updatedAt: ts(),
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read or write user profile or plants", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("plants").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("plants").doc("p1").set(validPlant(ALICE_UID)));
});

test("Authenticated owner: can create, read, and update own profile and plants", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).set(validProfile(ALICE_UID)));
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("plants").doc("plant_001").set(validPlant(ALICE_UID)));
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("plants").get());
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("plants").doc("plant_001").update({
      cultivationMethod: "combined",
      updatedAt: ts(),
    })
  );
});

test("Cross-user isolation: Bob cannot read or write Alice's plants", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("plants").doc("plant_001").set(validPlant(ALICE_UID)));

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("plants").doc("plant_001").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("plants").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("plants").doc("plant_002").set(validPlant(ALICE_UID)));
});

test("Shadow field and spoofing prevention: rejects ghost fields and mismatched userId", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).set({
      ...validProfile(ALICE_UID),
      isAdmin: true,
    })
  );
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("plants").doc("plant_bad").set(validPlant(BOB_UID))
  );
});

test("Terminal state locking: completed treatment plan cannot be mutated", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const planRef = aliceDb.collection("users").doc(ALICE_UID).collection("treatment_plans").doc("plan_001");
  await assertSucceeds(planRef.set(validTreatment(ALICE_UID)));
  await assertSucceeds(planRef.update({ status: "completed", outcomeNotes: "Membaik", updatedAt: ts() }));
  await assertFails(planRef.update({ status: "in_progress", updatedAt: ts() }));
});
