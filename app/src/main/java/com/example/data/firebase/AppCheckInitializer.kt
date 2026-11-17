package com.example.data.firebase

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory
import java.util.UUID

private const val TAG = "AppCheckInit"
private const val DEBUG_TOKEN_EXTRA = "FIREBASE_APPCHECK_DEBUG_TOKEN"
private const val DEBUG_STORE_PREFIX = "com.google.firebase.appcheck.debug.store."
private const val DEBUG_SECRET_KEY = "com.google.firebase.appcheck.debug.DEBUG_SECRET"
private const val RECAPTCHA_SITE_KEY_RES = "recaptcha_site_key"

data class FirebaseDiagnosticsInfo(
    val projectId: String,
    val applicationId: String,
    val packageName: String,
    val apiKeyMasked: String,
    val isApiKeyValidFormat: Boolean,
    val isDebugBuild: Boolean,
    val appCheckDebugToken: String?
)

fun configureAppCheck(context: Context, intent: Intent?) {
    try {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }

        if (BuildConfig.DEBUG) {
            val debugToken = intent?.getStringExtra(DEBUG_TOKEN_EXTRA)
            val persistenceKey = FirebaseApp.getInstance().persistenceKey
            val prefs = context.getSharedPreferences(
                "$DEBUG_STORE_PREFIX$persistenceKey",
                Context.MODE_PRIVATE
            )
            if (!debugToken.isNullOrEmpty()) {
                prefs.edit()
                    .putString(DEBUG_SECRET_KEY, debugToken)
                    .commit()
                Log.d(TAG, "App Check debug secret configured successfully.")
            } else {
                val existing = prefs.getString(DEBUG_SECRET_KEY, null)
                if (existing.isNullOrBlank()) {
                    val generated = UUID.randomUUID().toString()
                    prefs.edit()
                        .putString(DEBUG_SECRET_KEY, generated)
                        .commit()
                    Log.w(TAG, "No debug token in Intent; generated persistent debug token: $generated")
                } else {
                    Log.d(TAG, "Using existing stored App Check debug token.")
                }
            }

            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
            Log.d(TAG, "DebugAppCheckProviderFactory installed.")
        } else {
            val resId = context.resources.getIdentifier(
                RECAPTCHA_SITE_KEY_RES,
                "string",
                context.packageName
            )
            if (resId != 0) {
                val siteKey = context.getString(resId)
                if (siteKey.isNotBlank()) {
                    FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                        RecaptchaAppCheckProviderFactory.getInstance(siteKey)
                    )
                    Log.d(TAG, "RecaptchaAppCheckProviderFactory installed.")
                } else {
                    Log.e(TAG, "recaptcha_site_key resource string is blank.")
                }
            } else {
                Log.e(TAG, "recaptcha_site_key string resource not found in package.")
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Failed to initialize Firebase App Check", e)
    }
}

fun getFirebaseDiagnostics(context: Context): FirebaseDiagnosticsInfo {
    return try {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        val app = FirebaseApp.getInstance()
        val options = app.options
        val apiKey = options.apiKey.orEmpty()
        val isValidKey = apiKey.startsWith("AIza") && apiKey.length >= 30
        val masked = if (apiKey.length >= 12) {
            "${apiKey.take(8)}...${apiKey.takeLast(4)}"
        } else if (apiKey.isNotBlank()) {
            "Tidak valid ($apiKey)"
        } else {
            "Kosong (google-services.json tidak terbaca)"
        }
        val prefs = context.getSharedPreferences(
            "$DEBUG_STORE_PREFIX${app.persistenceKey}",
            Context.MODE_PRIVATE
        )
        val debugToken = if (BuildConfig.DEBUG) prefs.getString(DEBUG_SECRET_KEY, null) else null
        FirebaseDiagnosticsInfo(
            projectId = options.projectId.orEmpty().ifBlank { "-" },
            applicationId = options.applicationId.ifBlank { "-" },
            packageName = context.packageName,
            apiKeyMasked = masked,
            isApiKeyValidFormat = isValidKey,
            isDebugBuild = BuildConfig.DEBUG,
            appCheckDebugToken = debugToken
        )
    } catch (e: Exception) {
        FirebaseDiagnosticsInfo(
            projectId = "-",
            applicationId = "-",
            packageName = context.packageName,
            apiKeyMasked = "Error: ${e.localizedMessage}",
            isApiKeyValidFormat = false,
            isDebugBuild = BuildConfig.DEBUG,
            appCheckDebugToken = null
        )
    }
}

fun updateAppCheckDebugToken(context: Context, newToken: String): Boolean {
    val cleaned = newToken.trim()
    if (cleaned.isBlank()) return false
    return try {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        val persistenceKey = FirebaseApp.getInstance().persistenceKey
        val prefs = context.getSharedPreferences(
            "$DEBUG_STORE_PREFIX$persistenceKey",
            Context.MODE_PRIVATE
        )
        val saved = prefs.edit()
            .putString(DEBUG_SECRET_KEY, cleaned)
            .commit()
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
        saved
    } catch (e: Exception) {
        Log.e(TAG, "Failed to update custom App Check debug token", e)
        false
    }
}
