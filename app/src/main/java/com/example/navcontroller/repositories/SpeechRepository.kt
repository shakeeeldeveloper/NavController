package com.example.navcontroller.repositories


import android.content.Intent
import android.speech.RecognizerIntent
import javax.inject.Inject

class SpeechRepository @Inject constructor() {

    fun getSpeechRecognizerIntent(languageCode: String): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
        }
    }
}
