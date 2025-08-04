package com.example.navcontroller.screen
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import com.example.navcontroller.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun TranslationCardUI(
    modifier: Modifier = Modifier,
    originalText: String,
    translatedText: String,
    onOriginalTextChange: (String) -> Unit,
    onEditClick: () -> Unit,
    onClearClick:  () -> Unit,

) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var shareIcon by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()


    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
               // it.setSpeechRate(0.8f) // Slow down
            }
            else
                Log.e("TTS", "Initialization failed")

        }
    }

    LaunchedEffect(Unit) {
        tts.language = Locale.US
        tts.setSpeechRate(0.8f)
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()

            .padding(horizontal = 15.dp, vertical = 15.dp)
    ) {
        // Original Text Card
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()

                .height(250.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()

                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Original Text",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.edit_icon),
                                contentDescription = "Edit",
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { onEditClick() }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.cross_icon),
                                contentDescription = "Clear",
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { onClearClick() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    BasicTextField(
                        value = originalText,
                        onValueChange = onOriginalTextChange,
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        /*decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                innerTextField()
                            }
                        }*/
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {



                        Icon(
                            painter = painterResource(id = R.drawable.copy_icon),
                            contentDescription = "Copy",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {

                                    clipboardManager.setText(AnnotatedString(originalText))
                                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()

                                }
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.line_icon),
                            contentDescription = "Line",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {  }
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.speak_icon),
                            contentDescription = "Speak",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    tts.setSpeechRate(0.5f)

                                    tts.speak(originalText, TextToSpeech.QUEUE_FLUSH, null, null)


                                }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Translated Text Card
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE7F3FF))
                .height(250.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()

                    .padding(16.dp)
            ) {
                Text(
                    text = "Translated Text",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                BasicTextField(
                    value = translatedText,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    readOnly = true,
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.copy_icon),
                        contentDescription = "Copy",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                clipboardManager.setText(AnnotatedString(translatedText))
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()



                            }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.line_icon),
                        contentDescription = "Line",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {  }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.share_icon),
                        contentDescription = "Share",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(width = 24.dp, height = 20.dp)
                            .clickable(
                                enabled = shareIcon
                            ) {

                                shareIcon=false
                                // Launch coroutine to handle delay
                                coroutineScope.launch {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, translatedText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))

                                    delay(4000) // Delay 2 seconds
                                    shareIcon = true
                                }
                            }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.line_icon),
                        contentDescription = "Line",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {

                            }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.speak_icon),
                        contentDescription = "Speak",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                tts.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                    )
                }
            }
        }
    }
}
