package com.example.navcontroller.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navcontroller.model.HistoryEntity
import com.example.navcontroller.repositories.TranslationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val translationRepository: TranslationRepository,
    private val historyManager: HistoryManager,
    private val prefManager: PreferenceManager


) : ViewModel(

) {

  /*  var firstLang by mutableStateOf("English")
        private set

    var secondLang by mutableStateOf("Urdu")
        private set*/

    var firstLang by mutableStateOf("")
        private set

    var secondLang by mutableStateOf("")
        private set

    init {
        loadLanguagesFromPrefs()
    }

    private fun loadLanguagesFromPrefs() {
        firstLang = prefManager.getFirstLang()
        secondLang = prefManager.getSecondLang()
    }

     var currentLangType by mutableStateOf("")

    private val _translated = MutableStateFlow("")
    val translated: StateFlow<String> = _translated

    val _progressStatus = MutableStateFlow("Idle")
    val progressStatus: StateFlow<String> = _progressStatus



    var inputText by mutableStateOf("")
    var translatedText by mutableStateOf("")
    var isTranslating by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun swapLanguages() {

        Log.d("cur", "swipe in viewmodel")

        val temp = firstLang
        firstLang = secondLang
        secondLang = temp
    }

    fun updateSelectedLanguage(language: String) {
        if (currentLangType == "first") {
            firstLang = language
            prefManager.saveFirstLang(language)

        } else if (currentLangType == "second") {
            Log.d("cur",language+" true "+currentLangType)
            prefManager.saveSecondLang(language)

            secondLang = language
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
    fun deleteItem(item: HistoryEntity) {
        historyManager.deleteHistoryItem(item)
        loadHistory()
    }


    fun translateTextHome() {
        Log.d("transll", inputText+" "+ firstLang +" " +secondLang)

        if (inputText.isEmpty()) return

        isTranslating = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val result = translationRepository.translateText(inputText, firstLang, secondLang){
                        status ->
                    _progressStatus.value = status
                }
                isTranslating = false

                Log.d("transll", inputText + " " + firstLang + " " + secondLang)

                result.onSuccess {
                    translatedText = it
                    _translated.value = it

                    val history = HistoryEntity(
                        sourceText = inputText,
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
        Log.d("transll", inputText+" "+ firstLang +" " +secondLang)

        if (inputText.isEmpty()) return

        isTranslating = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val result = translationRepository.translateText(inputText, secondLang,firstLang){
                        status ->
                    _progressStatus.value = status
                }
                isTranslating = false

                Log.d("transll", inputText + " " + firstLang + " " + secondLang)

                result.onSuccess {
                    translatedText = it
                    _translated.value = it
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

    /*fun setCurrentLangType(type: String) {
        currentLangType = type
    }*/
}