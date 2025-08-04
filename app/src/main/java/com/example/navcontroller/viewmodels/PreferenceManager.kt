package com.example.navcontroller.viewmodels

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getFirstLang(): String = prefs.getString("first_lang", "English") ?: "English"
    fun getSecondLang(): String = prefs.getString("second_lang", "Urdu") ?: "Urdu"

    fun saveFirstLang(lang: String) {
        prefs.edit().putString("first_lang", lang).apply()
    }

    fun saveSecondLang(lang: String) {
        prefs.edit().putString("second_lang", lang).apply()
    }

    private val chatPrefs = context.getSharedPreferences("chat_app_prefs", Context.MODE_PRIVATE)

    fun getChatFirstLang(): String = chatPrefs.getString("first_lang", "English") ?: "English"
    fun getChatSecondLang(): String = chatPrefs.getString("second_lang", "Urdu") ?: "Urdu"

    fun saveChatFirstLang(lang: String) {
        chatPrefs.edit().putString("first_lang", lang).apply()
    }

    fun saveChatSecondLang(lang: String) {
        chatPrefs.edit().putString("second_lang", lang).apply()
    }
}
