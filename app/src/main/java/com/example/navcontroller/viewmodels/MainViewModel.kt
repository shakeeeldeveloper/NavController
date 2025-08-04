package com.example.navcontroller.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.*

import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _selectedScreen = mutableStateOf("Home")
    val selectedScreen: State<String> = _selectedScreen

    private val _showHistory = mutableStateOf(false)
    val showHistory: State<Boolean> = _showHistory

    fun setScreen(screen: String) {
        _selectedScreen.value = screen
    }

    fun isOnHomeScreen(): Boolean {
        return _selectedScreen.value == "Home"
    }


    fun setHistory(history: Boolean) {
        _showHistory.value = history
    }

    fun isShowHistory(): Boolean {
        return _showHistory.value == false
    }

}




/*
class MainViewModel : ViewModel() {

    private val _selectedScreen = MutableStateFlow("Home")
    val selectedScreen: StateFlow<String> = _selectedScreen

    fun setScreen(screen: String) {
        _selectedScreen.value = screen
    }

    fun isOnHomeScreen(): Boolean {
        return _selectedScreen.value == "Home"
    }
}
*/
