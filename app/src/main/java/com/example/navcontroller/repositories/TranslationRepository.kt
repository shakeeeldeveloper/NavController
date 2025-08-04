package com.example.navcontroller.repositories


import android.content.Context
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import android.util.Log
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.mlkit.nl.translate.TranslatorOptions


suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    suspendCancellableCoroutine { cont ->
        addOnSuccessListener { result -> cont.resume(result) }
        addOnFailureListener { e -> cont.resumeWithException(e) }
    }

class TranslationRepository @Inject constructor(
    @ApplicationContext private val context: Context

) {

    suspend fun translateText(
        text: String,
        sourceLang: String,
        targetLang: String,
        onProgress: (String) -> Unit

    ): Result<String> {
        return try {




            val sourceLangCode = TranslateLanguage.fromLanguageTag(getLanguageCode(sourceLang))
                ?: TranslateLanguage.ENGLISH
            val targetLangCode = TranslateLanguage.fromLanguageTag(getLanguageCode(targetLang))
                ?: TranslateLanguage.URDU

            val translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLangCode)
                .setTargetLanguage(targetLangCode)
                .build()

            val translator = Translation.getClient(translatorOptions)
            val model = TranslateRemoteModel.Builder(targetLangCode).build()
            val modelManager = RemoteModelManager.getInstance()

            val isModelAlreadyDownloaded = modelManager.isModelDownloaded(model).await()


            val hasInternet = isInternetAvailable(context)
            if (isModelAlreadyDownloaded) {
                onProgress("Translating...")
                val translatedText = translator.translate(text).await()

                translator.close()

                onProgress("Done.")
                Result.success(translatedText)
            }
            else if (hasInternet && !isModelAlreadyDownloaded) {
                onProgress("Downloading language model. Please wait...")
                translator.downloadModelIfNeeded().await()
                onProgress("Translating...")
                val translatedText = translator.translate(text).await()

                translator.close()

                onProgress("Done.")
                Result.success(translatedText)
            } else {
               // onProgress("No internet.\nPlease connect to a network to download the model (one-time only).")
                Result.failure((Exception("No internet.\nPlease connect to a network to download the model (one-time only).")))


            }

                  } catch (e: Exception) {
            onProgress("Error: ${e.message}")

            Log.e("transll", "Model download failed in catch ❌: ${e.message}")

            Result.failure(e)
        }


    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun getLanguageCode(language: String): String {
        return when (language) {
            "Afrikaans" -> "af"
            "Arabic" -> "ar"
            "Belarusian" -> "be"
            "Bengali" -> "bn"
            "Bulgarian" -> "bg"
            "Catalan" -> "ca"
            "Chinese" -> "zh"
            "Croatian" -> "hr"
            "Czech" -> "cs"
            "Danish" -> "da"
            "Dutch" -> "nl"
            "English" -> "en"
            "Esperanto" -> "eo"
            "Estonian" -> "et"
            "Finnish" -> "fi"
            "French" -> "fr"
            "Galician" -> "gl"
            "German" -> "de"
            "Greek" -> "el"
            "Gujarati" -> "gu"
            "Hebrew" -> "he"
            "Hindi" -> "hi"
            "Hungarian" -> "hu"
            "Icelandic" -> "is"
            "Indonesian" -> "id"
            "Irish" -> "ga"
            "Italian" -> "it"
            "Japanese" -> "ja"
            "Kannada" -> "kn"
            "Korean" -> "ko"
            "Latvian" -> "lv"
            "Lithuanian" -> "lt"
            "Macedonian" -> "mk"
            "Malay" -> "ms"
            "Marathi" -> "mr"
            "Norwegian" -> "no"
            "Persian" -> "fa"
            "Polish" -> "pl"
            "Portuguese" -> "pt"
            "Romanian" -> "ro"
            "Russian" -> "ru"
            "Slovak" -> "sk"
            "Slovenian" -> "sl"
            "Spanish" -> "es"
            "Swahili" -> "sw"
            "Swedish" -> "sv"
            "Tamil" -> "ta"
            "Telugu" -> "te"
            "Thai" -> "th"
            "Turkish" -> "tr"
            "Ukrainian" -> "uk"
            "Urdu" -> "ur"
            "Vietnamese" -> "vi"
            "Welsh" -> "cy"
            else -> "en" // default to English
        }
    }
}
