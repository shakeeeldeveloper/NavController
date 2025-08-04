package com.example.navcontroller.viewmodels

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.navcontroller.repositories.SpeechRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatSpeechViewModel @Inject constructor(
    private val speechRepository: SpeechRepository
) : ViewModel() {

    var spokenText by mutableStateOf("")
        private set

    fun updateSpokenText(text: String) {
        spokenText = text
    }


    val languageCodeMap = mapOf(
        "Afrikaans" to "af-ZA",
        "Albanian" to "sq-AL",
        "Arabic" to "ar-SA",
        "Bengali" to "bn-IN",
        "Bulgarian" to "bg-BG",
        "Catalan" to "ca-ES",
        "Chinese" to "zh-CN",
        "Croatian" to "hr-HR",
        "Czech" to "cs-CZ",
        "Danish" to "da-DK",
        "Dutch" to "nl-NL",
        "English" to "en-US",
        "Estonian" to "et-EE",
        "Finnish" to "fi-FI",
        "French" to "fr-FR",
        "Galician" to "gl-ES",
        "German" to "de-DE",
        "Greek" to "el-GR",
        "Gujarati" to "gu-IN",
        "Hebrew" to "he-IL",
        "Hindi" to "hi-IN",
        "Hungarian" to "hu-HU",
        "Icelandic" to "is-IS",
        "Indonesian" to "id-ID",
        "Italian" to "it-IT",
        "Japanese" to "ja-JP",
        "Kannada" to "kn-IN",
        "Korean" to "ko-KR",
        "Latvian" to "lv-LV",
        "Lithuanian" to "lt-LT",
        "Macedonian" to "mk-MK",
        "Malay" to "ms-MY",
        "Marathi" to "mr-IN",
        "Norwegian" to "no-NO",
        "Persian" to "fa-IR",
        "Polish" to "pl-PL",
        "Portuguese" to "pt-PT",
        "Romanian" to "ro-RO",
        "Russian" to "ru-RU",
        "Slovak" to "sk-SK",
        "Slovenian" to "sl-SI",
        "Spanish" to "es-ES",
        "Swedish" to "sv-SE",
        "Swahili" to "sw-TZ",
        "Tamil" to "ta-IN",
        "Telugu" to "te-IN",
        "Thai" to "th-TH",
        "Turkish" to "tr-TR",
        "Ukrainian" to "uk-UA",
        "Urdu" to "ur-PK",
        "Vietnamese" to "vi-VN",
        "Welsh" to "cy-GB"
    )

    fun getLanguageCode(language: String): String {
        return languageCodeMap[language] ?: "en-US"
    }

    fun getSpeechIntent(language: String): Intent {
        val code = getLanguageCode(language)
        return speechRepository.getSpeechRecognizerIntent(code)
    }
}
