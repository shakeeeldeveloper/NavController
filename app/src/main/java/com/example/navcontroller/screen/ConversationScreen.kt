package com.example.navcontroller.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navcontroller.R
import com.example.navcontroller.screen.HistoryScreen
import com.example.navcontroller.activities.LanguageActivity
import com.example.navcontroller.viewmodels.ChatViewModel
import com.example.navcontroller.viewmodels.ChatSpeechViewModel
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    navController: androidx.navigation.NavController
  /* onOpenHistory: () -> Unit*/
   // onOpenHistory: (Screen.History) -> Unit

) {
    val scrollState = rememberScrollState()
    var isRotated by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }

    val chatViewModel: ChatViewModel = hiltViewModel()

    val context = LocalContext.current
    val activity = LocalActivity.current






    val tts = remember {
        TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(context, "TTS init failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    LaunchedEffect(Unit) {
        tts.language = Locale.US // or Locale("ur") for Urdu
    }


    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }
    fun speakText(text: String, langName: String) {
        val langCode = getLanguageCodeFromName(langName)
        val locale = Locale.forLanguageTag(langCode)
        val availability = tts.isLanguageAvailable(locale)

        if (availability >= TextToSpeech.LANG_AVAILABLE) {
            tts.language = locale
        } else {
            Toast.makeText(context, "Language not supported: $langName. Using English", Toast.LENGTH_SHORT).show()
            tts.language = Locale.ENGLISH
        }

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Conversation",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.con_icon),
                        contentDescription = "History",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { //onOpenHistory()
                                navController.navigate("history")

                                       },
                        tint = Color.Unspecified
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            modifier = Modifier.shadow(4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(rotationZ = if (isRotated) 180f else 0f)

        ) {



            Column {



                        PersonCard(
                    language = chatViewModel.firstLang,
                    person = "first",
                    text=chatViewModel.firsttext,
                    chatViewModel=chatViewModel,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                        .height(300.dp)
                        .graphicsLayer(rotationZ = 180f), // mimic 180-degree rotation
                        onMicClick = {

                            chatViewModel.currentSpeaker="first"
                        },
                            onSpeak = {
                                tts.speak(chatViewModel.firsttext.value, TextToSpeech.QUEUE_FLUSH, null, null)

                               // speakText(chatViewModel.firsttext.value,chatViewModel.firstLang)
                            },
                )
                Spacer(modifier = Modifier.height(15.dp))
                PersonCard(
                    language = chatViewModel.secondLang,
                    person = "second",
                    text=chatViewModel.secondtext,
                    chatViewModel=chatViewModel,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .height(300.dp),
                    onMicClick = {
                        chatViewModel.currentSpeaker="second"


                    },
                    onSpeak = {
                        tts.speak(chatViewModel.secondtext.value, TextToSpeech.QUEUE_FLUSH, null, null)

                        //speakText(chatViewModel.secondtext.value,chatViewModel.secondLang)

                    },
                )
                Spacer(modifier = Modifier.height(15.dp))


            }

            Image(
                painter = painterResource(id = R.drawable.rotate_circle_bg),
                contentDescription = "Center Icon",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 296.dp)
                    .clickable {
                        isRotated = !isRotated
                    }
            )
        }
    }
}

fun getLanguageCodeFromName(name: String): String {
    return when (name.lowercase()) {
        "english" -> "en-US"
        "urdu" -> "ur-PK"
        "french" -> "fr-FR"
        "spanish" -> "es-ES"
        "german" -> "de-DE"
        "chinese" -> "zh-CN"
        "japanese" -> "ja-JP"
        "korean" -> "ko-KR"
        "arabic" -> "ar-SA"
        "hindi" -> "hi-IN"
        "italian" -> "it-IT"
        "russian" -> "ru-RU"
        "turkish" -> "tr-TR"
        else -> "en-GB" // Default to English
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun PersonCard(

    language: String,
    person: String,
    text: MutableState<String>,
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier,
    onMicClick: () -> Unit,
    onSpeak:()-> Unit,

) {
    val  chatSpeechViewModel: ChatSpeechViewModel = hiltViewModel()

    var micClick by remember { mutableStateOf(false) }

    var lastSpeaker by remember { mutableStateOf("first") } // or "second"


    val context = LocalContext.current

    LaunchedEffect(Unit) {
        chatViewModel.setLanguages(chatViewModel.firstLang, chatViewModel.secondLang)
    }
    val translated by chatViewModel.translated.collectAsState()

    LaunchedEffect(translated) {
        if (translated.isNotEmpty()) {
            Log.d("person",chatViewModel.currentSpeaker+"in trans")

            if (chatViewModel.currentSpeaker == "first") {
                chatViewModel.setTexts(chatViewModel.inputChatText,translated)

            } else {
                chatViewModel.setTexts(translated,chatViewModel.inputChatText)


            }


            chatViewModel.clearTranslation()
        }
    }


    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val spokenTextFromMic = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            if (spokenTextFromMic != null) {

                //spokenText = spokenTextFromMic // ✅ set value here
                text.value = spokenTextFromMic .toString()// ✅ set value here
                chatSpeechViewModel.updateSpokenText(spokenTextFromMic)
                chatViewModel.inputChatText = spokenTextFromMic
                lastSpeaker=spokenTextFromMic
                Log.d("person",chatViewModel.currentSpeaker+"in mic")

                if(chatViewModel.currentSpeaker.equals("second"))
                {
                    chatViewModel.translate()

                }
                else
                chatViewModel.translateText()

               /* Toast.makeText(context,chatViewModel.inputText
                        +"${chatViewModel.firstLang}"
                        +"${chatViewModel.secondLang}", Toast.LENGTH_SHORT).show()*/
            }
            else {
                Log.d(
                    "ttttttttt",
                    chatViewModel.inputChatText + "${chatViewModel.firstLang}" + "${chatViewModel.secondLang}"
                )

            }
        }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (chatViewModel.currentSpeaker.equals("first")) {
                val intent = chatSpeechViewModel.getSpeechIntent(chatViewModel.firstLang)
                speechLauncher.launch(intent)
                lastSpeaker="first"
            }
            else{
                val intent = chatSpeechViewModel.getSpeechIntent(chatViewModel.secondLang)
                speechLauncher.launch(intent)
                lastSpeaker="second"
            }
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


            Log.d("cur",language+" ${chatViewModel.currentLangType}     ${chatViewModel.firstLang}      ${chatViewModel.secondLang}")

            chatViewModel.updateSelectedLanguage(language)

        }
    }

    LaunchedEffect(micClick) {
        if (micClick) {
            micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            micClick = false
            onMicClick()
        }
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.End)
                        .background(
                            color = Color(0xFFE7F3FF),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.speak_blue),
                        contentDescription = "Speak",
                        modifier = Modifier
                            .clickable{
                                onSpeak()
                            },
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

/*

                Text(
                    text = if (text.value.isNotBlank()) text.value else "Speak text",

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,

                    color = if (text.value.isNotBlank()) Color.Black else Color.Gray
                )
*/
                val scrollState = rememberScrollState()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .heightIn(max = 100.dp) // height to roughly match your maxLines
                        .verticalScroll(scrollState) // enables scroll if needed
                ) {
                    Text(
                        text = if (text.value.isNotBlank()) text.value else "Speak text",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = if (text.value.isNotBlank()) Color.Black else Color.Gray
                    )
                }



                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    LanguageButton(language) {
                        chatViewModel.currentLangType=person
                        val intent = Intent(context, LanguageActivity::class.java)
                        languageActivityLauncher.launch(intent)
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        micClick=true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.voice_icon1),
                            contentDescription = "Mic",
                            modifier = Modifier.size(40.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}
