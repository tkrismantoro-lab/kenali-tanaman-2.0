import java.util.Base64

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.google.services) apply false
}

// On GitHub clones / CI, debug.keystore is gitignored while debug.keystore.base64 is tracked.
// Automatically restore debug.keystore from debug.keystore.base64 only when missing so APK signing
// always matches the SHA-1 registered in app/google-services.json.
val debugKeystoreFile = rootProject.file("debug.keystore")
val debugKeystoreBase64File = rootProject.file("debug.keystore.base64")
if (!debugKeystoreFile.exists() && debugKeystoreBase64File.exists()) {
  runCatching {
    val cleanB64 = debugKeystoreBase64File.readText().replace("\\s+".toRegex(), "")
    debugKeystoreFile.writeBytes(Base64.getDecoder().decode(cleanB64))
  }
}
