package com.example.navcontroller.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navcontroller.model.HistoryEntity
import com.example.navcontroller.repositories.TranslationRepository
import com.example.navcontroller.viewmodels.HistoryManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val translationRepository: TranslationRepository,
    private val historyManager: HistoryManager,
    private val prefManager: PreferenceManager



) : ViewModel(

) {

    var firstLang by mutableStateOf("")
        private set

    var secondLang by mutableStateOf("")
    private set
    init {
        loadLanguagesFromPrefs()
    }

    private fun loadLanguagesFromPrefs() {
        firstLang = prefManager.getChatFirstLang()
        secondLang = prefManager.getChatSecondLang()
    }
    /*var firstLang by mutableStateOf("English")
        private set

    var secondLang by mutableStateOf("Urdu")
        private set*/

    var currentLangType by mutableStateOf("")
    private val _translated = MutableStateFlow("")
    val translated: StateFlow<String> = _translated

    val _progressStatus = MutableStateFlow("Idle")
    val progressStatus: StateFlow<String> = _progressStatus


    var isTranslating by mutableStateOf(false)
    var errorMessage by mutableStateOf("")


    var firsttext = mutableStateOf("Speak Text")
    var secondtext = mutableStateOf("Speak Text")

     var currentSpeaker by mutableStateOf("second")


    var inputChatText by mutableStateOf("")
    var translatedText by mutableStateOf("")



    fun swapLanguages() {

        Log.d("cur", "swipe in viewmodel")

        val temp = firstLang
        firstLang = secondLang
        secondLang = temp
    }

    fun updateSelectedLanguage(language: String) {
        if (currentLangType == "first") {
            firstLang = language
            prefManager.saveChatFirstLang(language)

        } else if (currentLangType == "second") {
            Log.d("cur",language+" true "+currentLangType)

            secondLang = language
            prefManager.saveChatSecondLang(language)

        }
    }

    val historyList = mutableStateOf<List<HistoryEntity>>(emptyList())

    fun addHistory(item: HistoryEntity) {
        historyManager.saveHistoryItem(item)
        historyList.value = historyManager.getHistoryList()
    }
    fun loadHistory() {
        historyList.value = historyManager.getHistoryList()
    }
    fun clearHistory() {
        historyManager.clearHistory()
        historyList.value = emptyList()
    }


    fun translateText() {
        Log.d("transll", inputChatText+" "+ firstLang +" " +secondLang)

        if (inputChatText.isEmpty()) return

        isTranslating = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val result = translationRepository.translateText(inputChatText, firstLang, secondLang){
                        status ->
                    _progressStatus.value = status
                }
                isTranslating = false

                Log.d("transll", inputChatText + " " + firstLang + " " + secondLang)

                result.onSuccess {
                    translatedText = it
                    _translated.value = it

                    val history = HistoryEntity(
                        sourceText = inputChatText,
                        translatedText = it,
                        sourceLangCode = firstLang,
                        targetLangCode = secondLang
                    )
                    addHistory(history)

                }.onFailure {
                    errorMessage = it.message ?: "Translation failed"
                }
            } catch (e: Exception) {
                isTranslating = false
                errorMessage = e.message ?: "Unexpected error"
                Log.e("transll", "Exception during translation: ${e.message}", e)
            }
        }
    }
    fun translate() {
        Log.d("transll", inputChatText+" "+ firstLang +" " +secondLang)

        if (inputChatText.isEmpty()) return

        isTranslating = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val result = translationRepository.translateText(inputChatText, secondLang,firstLang){
                        status ->
                    _progressStatus.value = status
                }
                isTranslating = false

                Log.d("transll", inputChatText + " " + firstLang + " " + secondLang)

                result.onSuccess {
                    translatedText = it
                    _translated.value = it

                    val history = HistoryEntity(
                        sourceText = inputChatText,
                        translatedText = it,
                        sourceLangCode = firstLang,
                        targetLangCode = secondLang
                    )
                    addHistory(history)


                }.onFailure {
                    errorMessage = it.message ?: "Translation failed"
                }
            } catch (e: Exception) {
                isTranslating = false
                errorMessage = e.message ?: "Unexpected error"
                Log.e("transll", "Exception during translation: ${e.message}", e)
            }
        }
    }
    fun clearTranslation() {
        translatedText = ""
        _translated.value=""
    }
    fun setLanguages(first: String, second: String) {
        firstLang = first
        secondLang = second
    }

    fun setTexts(first: String, second: String) {
        firsttext.value = first
        secondtext.value = second
    }


}