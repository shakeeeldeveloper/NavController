package com.example.navcontroller.screen

import android.R.attr.enabled
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager

import com.example.navcontroller.R
import com.example.navcontroller.activities.LanguageActivity
import com.example.navcontroller.activities.TranslationActivity
import com.example.navcontroller.viewmodels.SpeechViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(


) {
    val viewModel: HomeViewModel = hiltViewModel()
    val speechViewModel: SpeechViewModel = hiltViewModel()
    var textInput by remember { mutableStateOf("") }
    var errorMessage: String = ""
    var isTranslating by remember { mutableStateOf(false) }
    var onTransBtnClick by remember { mutableStateOf(false) }
    var onVoiceInputClick by remember { mutableStateOf(false) }


    val progress by viewModel.progressStatus.collectAsState()



    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    val textFieldFocusRequester = remember { FocusRequester() }

    var btnTrans by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()



    val originalText = viewModel.inputText
    val translatedText = viewModel.translatedText
    val sourceLang = viewModel.firstLang
    val targetLang = viewModel.secondLang



    // Create launcher inside Composable
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val spokenText = data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.getOrNull(0)

            if (spokenText != null) {
                // update your ViewModel or state here
                speechViewModel.updateSpokenText(spokenText)

                textInput=spokenText

            }
        }
    }

    // Call this when you want to start recognition
    fun startSpeechRecognition(selectedLang: String) {
        val intent = speechViewModel.getSpeechIntent(selectedLang)
        speechLauncher.launch(intent)
    }
    LaunchedEffect(onTransBtnClick) {

        viewModel.inputText = textInput
        viewModel.translateTextHome()
        isTranslating = viewModel.isTranslating
        errorMessage = viewModel.errorMessage


    }
    /*LaunchedEffect(onVoiceInputClick) {

        startSpeechRecognition(viewModel.firstLang)


    }*/


    LaunchedEffect(translatedText) {
        if (translatedText.isNotEmpty()) {


            textInput=""
            focusManager.clearFocus()

            Log.d("lang",sourceLang+"   in home $targetLang    $originalText     $translatedText")

            val intent = Intent(context, TranslationActivity::class.java).apply {
                putExtra("original_text", originalText)   // ✅ Correct
                putExtra("translated_text", translatedText)
                putExtra("source_lang", viewModel.firstLang)
                putExtra("target_lang", viewModel.secondLang)
            }

            context.startActivity(intent)

            viewModel.clearTranslation()
        }
    }
    val languageActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val language = data?.getStringExtra("selected_language") ?: ""
            viewModel.updateSelectedLanguage(language)
        }
    }
    val firstLang = viewModel.firstLang
    val secondLang = viewModel.secondLang

    Scaffold { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // <-- apply innerPadding
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Text Translator",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Translator Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF80BDFF)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Quick Translator",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Translate your text into countless languages from across the globe.",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }


                }


            }

            Spacer(modifier = Modifier.height(15.dp))

            // Main Card for Translator Functionality
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(329.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    // Language Selection Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LanguageButton(firstLang) {
                            viewModel.currentLangType="first"
                            val intent = Intent(context, LanguageActivity::class.java)
                            languageActivityLauncher.launch(intent)
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.rotate_icon),
                            contentDescription = "Switch Language",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    viewModel.swapLanguages()
                                }
                        )

                        LanguageButton(secondLang) {
                            viewModel.currentLangType="second"
                            val intent = Intent(context, LanguageActivity::class.java)
                            languageActivityLauncher.launch(intent)
                        }
                    }


                    TextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .focusRequester(textFieldFocusRequester),
                        placeholder = {
                            Text("Type your text here")
                                      },
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



                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(
                            id = if (textInput.isNotEmpty()) R.drawable.trans_svg else R.drawable.voice_icon
                        ),
                        contentDescription = if (textInput.isNotEmpty()) "Translate" else "Voice Input",
                        modifier = Modifier

                            .align(Alignment.End)
                            .clickable(
                                enabled = btnTrans, // ✅ Proper usage
                                onClick = {
                                    btnTrans=false


                                    if (textInput.isNotEmpty()) {
                                        onTransBtnClick=true
                                        //onTransBtnClick()
                                    } else {
                                        onVoiceInputClick=true
                                        startSpeechRecognition(viewModel.firstLang)
                                        //onVoiceInputClick()
                                    }
                                    coroutineScope.launch {
                                        delay(8000)

                                        onVoiceInputClick=false
                                        onTransBtnClick=false
                                        btnTrans=true
                                    }
                                }
                            )

                    )

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when {
                isTranslating -> {

                    if (progress != "Idle" && progress != "Done.") {
                        Text(text = progress, color = Color.Gray)
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
}



@Composable
fun LanguageButton(language: String, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7F3FF)),
        shape = RoundedCornerShape(7.dp),
        modifier = Modifier
            .width(130.dp)
            .height(48.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE7F3FF)),
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = language,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}

