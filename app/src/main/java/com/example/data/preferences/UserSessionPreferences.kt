package com.example.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kenali_tanaman_prefs")

class UserSessionPreferences(private val context: Context) {
    private object Keys {
        val DEMO_MODE = booleanPreferencesKey("demo_mode_enabled")
        val LOCAL_ONBOARDING_DONE = booleanPreferencesKey("local_onboarding_done")
    }

    val isDemoMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.DEMO_MODE] ?: false
    }

    val isLocalOnboardingDone: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.LOCAL_ONBOARDING_DONE] ?: false
    }

    suspend fun setDemoMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DEMO_MODE] = enabled
        }
    }

    suspend fun setLocalOnboardingDone(done: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LOCAL_ONBOARDING_DONE] = done
        }
    }
}
