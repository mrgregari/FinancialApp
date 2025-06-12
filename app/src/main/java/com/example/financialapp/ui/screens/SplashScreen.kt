package com.example.financialapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.financialapp.R

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie))
        val logoAnimationState = animateLottieCompositionAsState(
            composition = composition,
            clipSpec = LottieClipSpec.Progress(
                min = 0f,
                max = 0.2f
            )
        )

        LottieAnimation(
            composition = composition,
            progress = { logoAnimationState.progress }
        )

        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
            LaunchedEffect(Unit) {
                onSplashFinished()
            }
        }
    }
}