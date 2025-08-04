package com.example.navcontroller.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.ui.text.font.FontWeight


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navcontroller.R
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.activity.compose.LocalActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: androidx.navigation.NavController
   /* onOpenHistory: () -> Unit*/
) {

    val coroutineScope = rememberCoroutineScope()

    //val activity = LocalActivity.current
    val context = LocalContext.current





    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Setting",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    /* Icon(
                         painter = painterResource(id = R.drawable.setting_icon_black),
                         contentDescription = "Settings",
                         modifier = Modifier.padding(end = 12.dp),
                         tint = Color.Unspecified
                     )*/
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            modifier = Modifier.shadow(4.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SettingItem(
                title = "History",
                iconStart = R.drawable.history_icon,
                onClick = {
                    navController.navigate("history")

                }
            )
            SettingItem(
                title = "Rate Us",
                iconStart = R.drawable.rate_us,
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.example.navcontroller")
                    )
                    context.startActivity(intent)
                }
            )
            SettingItem(
                title = "Privacy Policy",
                iconStart = R.drawable.rate_us,
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.freeprivacypolicy.com/live/841a0503-8798-444c-83da-4c50ecac913d")
                    )
                    context.startActivity(intent)
                }
            )
            SettingItem(
                title = "Share App",
                iconStart = R.drawable.share_icon,
                onClick = {
                    coroutineScope.launch {
                        val shareMessage = """
        Translate instantly in any language! 
        Speak or type — it's that easy. 🗣
        
        Download now for free:  
        https://play.google.com/store/apps/details?id=com.example.navcontroller
    """.trimIndent()

                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareMessage)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))

                    }

                }
            )
            /* SettingItem(
                 title = "About Us",
                 iconStart = R.drawable.about_us,
                 onClick = {  }
             )*/

        }
    }
    //}
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    title: String = "Change Language",
    subtitle: String = "Tap to change",
    iconStart: Int = R.drawable.back_icon,
    iconEnd: Int = R.drawable.next_icon,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconStart),
            contentDescription = "Start Icon",
            modifier = Modifier.size(24.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Image(
            painter = painterResource(id = iconEnd),
            contentDescription = "Next Icon",
            modifier = Modifier.size(18.dp)
        )
    }
}


