package com.example.navcontroller.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.let
import kotlin.text.lowercase
import androidx.activity.compose.LocalActivity



data class LanguageItem(
    val language: String,
    val countryCode: String
)

class LanguageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageSelectionScreen ( )
        }
    }
}

@Composable
fun LanguageSelectionScreen() {
  /*  val languages = listOf(
        LanguageItem("English", "GB"),
        LanguageItem("Urdu", "PK"),
        LanguageItem("French", "FR"),
        LanguageItem("Spanish", "ES"),
        LanguageItem("German", "DE"),
        LanguageItem("Chinese", "CN"),
        LanguageItem("Japanese", "JP"),
        LanguageItem("Korean", "KR"),
        LanguageItem("Arabic", "SA"),
        LanguageItem("Hindi", "IN"),
        LanguageItem("Italian", "IT"),
        LanguageItem("Russian", "RU"),
        LanguageItem("Turkish", "TR")
    )*/
    val languages = listOf(
        LanguageItem("Afrikaans", "ZA"),
        LanguageItem("Albanian", "AL"),
        LanguageItem("Arabic", "SA"),
        LanguageItem("Belarusian", "BY"),
        LanguageItem("Bulgarian", "BG"),
        LanguageItem("Bengali", "BD"),
        LanguageItem("Catalan", "ES"),
        LanguageItem("Chinese", "CN"),
        LanguageItem("Croatian", "HR"),
        LanguageItem("Czech", "CZ"),
        LanguageItem("Danish", "DK"),
        LanguageItem("Dutch", "NL"),
        LanguageItem("English", "GB"),
        LanguageItem("Esperanto", "EU"),
        LanguageItem("Estonian", "EE"),
        LanguageItem("Finnish", "FI"),
        LanguageItem("French", "FR"),
        LanguageItem("Galician", "ES"),
        LanguageItem("Georgian", "GE"),
        LanguageItem("German", "DE"),
        LanguageItem("Greek", "GR"),
        LanguageItem("Haitian Creole", "HT"),
        LanguageItem("Hebrew", "IL"),
        LanguageItem("Hindi", "IN"),
        LanguageItem("Hungarian", "HU"),
        LanguageItem("Icelandic", "IS"),
        LanguageItem("Indonesian", "ID"),
        LanguageItem("Irish", "IE"),
        LanguageItem("Italian", "IT"),
        LanguageItem("Japanese", "JP"),
        LanguageItem("Korean", "KR"),
        LanguageItem("Latvian", "LV"),
        LanguageItem("Lithuanian", "LT"),
        LanguageItem("Macedonian", "MK"),
        LanguageItem("Malay", "MY"),
        LanguageItem("Maltese", "MT"),
        LanguageItem("Norwegian", "NO"),
        LanguageItem("Persian", "IR"),
        LanguageItem("Portuguese", "PT"),
        LanguageItem("Romanian", "RO"),
        LanguageItem("Russian", "RU"),
        LanguageItem("Slovak", "SK"),
        LanguageItem("Slovenian", "SI"),
        LanguageItem("Spanish", "ES"),
        LanguageItem("Swahili", "KE"),
        LanguageItem("Swedish", "SE"),
        LanguageItem("Tamil", "IN"),
        LanguageItem("Thai", "TH"),
        LanguageItem("Turkish", "TR"),
        LanguageItem("Ukrainian", "UA"),
        LanguageItem("Urdu", "PK"),
        LanguageItem("Vietnamese", "VN"),
        LanguageItem("Welsh", "GB")
    )


    var selectedLanguage by remember { mutableStateOf<LanguageItem?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    val activity = LocalActivity.current


    val filteredLanguages = languages.filter {
        it.language.contains(searchQuery, ignoreCase = true)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0XFFf0f6ff))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Language",
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable {
                            selectedLanguage?.let {
                                Log.d("selected_language", it.language)
                                Log.d("selected_country_code", it.countryCode)

                                val resultIntent = Intent().apply {
                                    putExtra("selected_language", it.language)
                                    putExtra("selected_country_code", it.countryCode)
                                }
                                activity?.setResult(Activity.RESULT_OK, resultIntent)
                                activity?.finish()
                            }
                        }
                ){
                    Text(
                        text = "Done",
                        color = Color.Blue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }




            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Language") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
            )


            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Selected Language",
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            selectedLanguage?.let {
                LanguageListItem(
                    languageItem = it,
                    isSelected = true,
                    onSelect = {}, // no action
                    variant = "selectedPreview"
                )
            } ?: Text(
                text = "None selected",
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))



            Text(
                text = "All Languages",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))


            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredLanguages) { languageItem ->
                    LanguageListItem(
                        languageItem = languageItem,
                        isSelected = selectedLanguage == languageItem,
                        onSelect = { selectedLanguage = languageItem }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun LanguageListItem(
    languageItem: LanguageItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    variant: String = "listItem" //  selectedPreview
) {
    val backgroundColor = when (variant) {
        "selectedPreview" -> Color.White
        else -> Color.Black
    }

    val borderColor = if (variant == "selectedPreview") Color.Blue else Color.White
    val textColor = if (variant == "selectedPreview") Color.Black else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(40.dp))
            .background(Color.White)
            .border(2.dp, borderColor, androidx.compose.foundation.shape.RoundedCornerShape(40.dp))
            .clickable { onSelect() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White, CircleShape)
                .padding(4.dp)
        ) {
            AsyncImage(
                model = "https://flagcdn.com/w80/${languageItem.countryCode.lowercase()}.png",
                contentDescription = "${languageItem.language} Flag",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }


        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = languageItem.language,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f),
        )

        RadioButton(
            selected = isSelected,
            onClick = { onSelect() }
        )
    }
}
