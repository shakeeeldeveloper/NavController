package com.example.navcontroller.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navcontroller.screen.BeforeTranslateCard
import com.example.navcontroller.screen.TranslationCardUI
import com.example.navcontroller.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TranslationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val original = intent.getStringExtra("original_text") ?: ""
        val translated = intent.getStringExtra("translated_text") ?: ""
        val source = intent.getStringExtra("source_lang") ?: ""
        val target = intent.getStringExtra("target_lang") ?: ""
        enableEdgeToEdge()
        setContent {


            TranslationScreen(
                originalText = original,
                translatedText = translated,
                sourceLang = source,
                targetLang = target,
                activity = this
            )
        }

    }



}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    navController: NavHostController = rememberNavController(),
    originalText: String,
    translatedText: String,
    sourceLang: String,
    targetLang: String,
    activity: TranslationActivity
) {
    var original by remember { mutableStateOf(originalText) }
    var showBeforeCard by remember { mutableStateOf(false) }
    var btnTrans by remember { mutableStateOf(true) }
    var showEditBeforeCard by remember { mutableStateOf(false) }
    val viewModel: HomeViewModel = hiltViewModel()


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Translation") })
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "TranslationCardUI",
            modifier = Modifier.padding(padding)
        ) {
            composable("showEditBeforeCard") {

                BeforeTranslateCard(
                    navController,
                    originalText,
                    translatedText,
                    sourceLang,
                    targetLang,
                    isTranslating = viewModel.isTranslating,
                    errorMessage = viewModel.errorMessage,
                    activity
                )
            }

            composable("showBeforeCard") {
                BeforeTranslateCard(
                    navController,
                    "",
                    "",
                    sourceLang,
                    targetLang,
                    isTranslating = viewModel.isTranslating,
                    errorMessage = viewModel.errorMessage,
                    activity = activity,
                )
            }
            composable("TranslationCardUI") {
                TranslationCardUI(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize(),
                    originalText = original,
                    translatedText = translatedText,
                    onOriginalTextChange = { updatedText -> original = updatedText },
                    onEditClick = {
                        showEditBeforeCard = true
                        navController.popBackStack()
                    },
                    onClearClick = {
                        showBeforeCard = true
                        navController.popBackStack()
                    },

                    )
            }


        }
        if (showEditBeforeCard) {


            navController.navigate("showEditBeforeCard")
        } else if (showBeforeCard) {

            navController.navigate("showBeforeCard")

        } else {
            navController.navigate("TranslationCardUI")


        }
    }
}

// ----------------ORIGINAL ---------------------------
/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    originalText: String,
    translatedText: String,
    sourceLang: String,
    targetLang: String
) {
    var original by remember { mutableStateOf(originalText) }
    var showBeforeCard by remember { mutableStateOf(false) }
    var btnTrans by remember { mutableStateOf(true) }
    var showEditBeforeCard by remember { mutableStateOf(false) }
    val viewModel: HomeViewModel = hiltViewModel()


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Translation") })
        }
    ) { padding ->

        if (showEditBeforeCard) {

            Box(modifier = Modifier.padding(padding)) {
                BeforeTranslateCard(
                    originalText,
                    translatedText,
                    sourceLang,
                    targetLang,
                    isTranslating = viewModel.isTranslating,
                    errorMessage = viewModel.errorMessage
                )
                Log.d("lang",sourceLang+"   in ac eDIT $targetLang    $originalText     $translatedText")

            }
        }
        else if(showBeforeCard){
            Box(modifier = Modifier.padding(padding)) {
                BeforeTranslateCard(
                    "",
                    "",
                    sourceLang,
                    targetLang,
                    isTranslating = viewModel.isTranslating,
                    errorMessage = viewModel.errorMessage
                )
                Log.d("lang",sourceLang+"   in ac X $targetLang    $originalText     $translatedText")

            }
        }
        else {
            TranslationCardUI(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                originalText = original,
                translatedText = translatedText,
                onOriginalTextChange = { updatedText -> original = updatedText },
                onEditClick = {  showEditBeforeCard = true },
                onClearClick = {
                    showBeforeCard = true
                },

            )
        }
    }
}*/

