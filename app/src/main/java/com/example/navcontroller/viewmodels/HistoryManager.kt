package com.example.navcontroller.viewmodels


import android.content.Context
import android.content.SharedPreferences
import com.example.navcontroller.model.HistoryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("history_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveHistoryItem(item: HistoryEntity) {
        val current = getHistoryList().toMutableList()
        current.add(0, item) // add new at top
        prefs.edit().putString("history_list", gson.toJson(current)).apply()
    }

    fun getHistoryList(): List<HistoryEntity> {
        val json = prefs.getString("history_list", null) ?: return emptyList()
        val type = object : TypeToken<List<HistoryEntity>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearHistory() {
        prefs.edit().remove("history_list").apply()
    }
    fun deleteHistoryItem(item: HistoryEntity) {
        val current = getHistoryList().toMutableList()
        current.remove(item)
        prefs.edit().putString("history_list", gson.toJson(current)).apply()
    }

}
