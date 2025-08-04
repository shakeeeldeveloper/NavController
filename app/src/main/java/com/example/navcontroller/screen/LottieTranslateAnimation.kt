package com.example.navcontroller.screen

import androidx.compose.runtime.*
import com.airbnb.lottie.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.navcontroller.screen.ProgressScreen
import com.example.navcontroller.R
import kotlinx.coroutines.delay


@Composable
fun LottieTranslateAnimation(
    onNavigateToAdScreen: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animation_tr)
    )
    val lottieProgress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (progress < 1f) {
            delay(100)
            progress += 0.01f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LottieAnimation(
            composition = composition,
            progress = { lottieProgress },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.Center)
        )

        ProgressScreen(
            progress = progress,
            onProgressComplete = onNavigateToAdScreen,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),

        )
    }
}



