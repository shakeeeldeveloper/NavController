package com.example.navcontroller.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.navcontroller.R
import android.app.Activity

import android.content.Intent

import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.material3.Card
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navcontroller.viewmodels.HomeViewModel
import androidx.compose.runtime.collectAsState

import com.example.navcontroller.activities.LanguageActivity
import com.example.navcontroller.activities.TranslationActivity
import com.example.navcontroller.viewmodels.SpeechViewModel
import java.util.Locale
import android.Manifest
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun BeforeTranslateCard(

    originalText: String,
    translatedText: String,
    sourceLang: String,
    targetLang: String,
    isTranslating: Boolean,
    errorMessage: String

) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    val progress by homeViewModel.progressStatus.collectAsState()


    val speechViewModel: SpeechViewModel = hiltViewModel()


    LaunchedEffect(Unit) {
        homeViewModel.inputText = originalText
        speechViewModel.updateSpokenText(originalText)
    }


    var isText by remember { mutableStateOf(true) }
    var btnTrans by remember { mutableStateOf(true) }
    var crossClick by remember { mutableStateOf(false) }
    var micClick by remember { mutableStateOf(false) }
    var transBtnClick by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    /*
        var isTranslating by remember { mutableStateOf(false) }
    */


    val context = LocalContext.current
    if (originalText == "") {
        crossClick = true
    }

    Log.d("lang", sourceLang + "   $targetLang")



    LaunchedEffect(Unit) {
        homeViewModel.setLanguages(sourceLang, targetLang)
    }


    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val spokenText = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            if (spokenText != null) {
                speechViewModel.updateSpokenText(spokenText)
                homeViewModel.inputText = spokenText
                isText = true
            }
        }
    }
    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = speechViewModel.getSpeechIntent(homeViewModel.firstLang)
            speechLauncher.launch(intent)
        } else {
            Toast.makeText(context, "Microphone permission is required", Toast.LENGTH_SHORT).show()
        }
    }


    val languageActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val language = data?.getStringExtra("selected_language") ?: ""
            Log.d("cur", language + " ${homeViewModel.currentLangType}")


            if (homeViewModel.currentLangType == "first") {
                homeViewModel.setLanguages(language, homeViewModel.secondLang)
            } else if (homeViewModel.currentLangType == "second") {
                homeViewModel.setLanguages(homeViewModel.firstLang, language)


            }
            Log.d(
                "cur",
                language + " ${homeViewModel.currentLangType}     ${homeViewModel.firstLang}      ${homeViewModel.secondLang}"
            )

            homeViewModel.updateSelectedLanguage(language)

        }
    }

    LaunchedEffect(crossClick) {
        if (crossClick) {
            homeViewModel.inputText = ""
            homeViewModel.translatedText = ""
            speechViewModel.updateSpokenText("")
            isText = false
            crossClick = false

        }
    }

    LaunchedEffect(micClick) {
        if (micClick) {
            micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            micClick = false
        }
    }

    val translated by homeViewModel.translated.collectAsState()

    LaunchedEffect(translated) {
        if (translated.isNotEmpty()) {
            val intent = Intent(context, TranslationActivity::class.java).apply {
                putExtra("original_text", homeViewModel.inputText)
                putExtra("translated_text", translated)
                putExtra("source_lang", homeViewModel.firstLang)
                putExtra("target_lang", homeViewModel.secondLang)
            }
            context.startActivity(intent)
            (context as? Activity)?.finish()
            homeViewModel.clearTranslation()
        }
    }


    Log.d("lang", homeViewModel.firstLang + " home   ${homeViewModel.secondLang}")






    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(73.dp)
                .padding(top = 15.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val context = LocalContext.current

                LanguageButton(homeViewModel.firstLang) {
                    homeViewModel.currentLangType = "first"
                    val intent = Intent(context, LanguageActivity::class.java)
                    languageActivityLauncher.launch(intent)
                }

                Icon(
                    painter = painterResource(id = R.drawable.rotate_icon),
                    contentDescription = "Switch Language",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            Log.d("cur", "swipe")

                            homeViewModel.swapLanguages()
                        }
                )

                LanguageButton(homeViewModel.secondLang) {
                    homeViewModel.currentLangType = "second"
                    Log.d("cur", homeViewModel.currentLangType)
                    val intent = Intent(context, LanguageActivity::class.java)
                    languageActivityLauncher.launch(intent)
                }
            }

        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(329.dp)
                .padding(15.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation()
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = homeViewModel.firstLang,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.cross_icon),
                            contentDescription = "Clear Text",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    crossClick = true
                                }
                        )

                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = homeViewModel.inputText,
                        onValueChange = { newText ->
                            homeViewModel.inputText = newText   // Update the view model
                            isText = newText.isNotEmpty()
                            speechViewModel.updateSpokenText(newText) // Optional: Keep in sync
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        placeholder = { Text("Type your text here") },
                        maxLines = 5,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            unfocusedContainerColor = Color.White,

                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            disabledIndicatorColor = Color.White,
                            cursorColor = Color.Black
                        )
                    )


                }

                Image(
                    painter = painterResource(
                        id = if (isText) R.drawable.trans_svg else R.drawable.voice_icon
                    ),
                    contentDescription = "Voice Input",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(15.dp)
                        .clickable(
                            enabled = btnTrans
                        ) {
                            if (homeViewModel.inputText == "") {
                                //  launcher.launch(Manifest.permission.RECORD_AUDIO)

                                micClick = true
                            } else {
                                transBtnClick = true


                                btnTrans = false
                                coroutineScope.launch {
                                    delay(8000)
                                    btnTrans = true
                                }
                            }
                        }

                )
                if (transBtnClick) {
                    homeViewModel.inputText =
                        speechViewModel.spokenText // assign inputText before translate

                    homeViewModel.translateTextHome()
                    transBtnClick = false


                }


            }


        }

        when {
            isTranslating -> {

                if (progress != "Idle" && progress != "Done.") {
                    Text(modifier = Modifier
                        .padding(15.dp),
                        text = progress,
                        color = Color.Gray)
                    // CircularProgressIndicator()
                }

                /*Text(
            text = "Translating...",
            color = Color.Gray,
            fontWeight = FontWeight.Bold
            )*/
            }
            /* translatedText.isNotEmpty() -> Text(



             text = "Translated: $translatedText",
             color = Color.Blue,
             fontWeight = FontWeight.Bold
         )*/
            errorMessage.isNotEmpty() -> Text(
                text = "Error: $errorMessage",
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }

    }






}



