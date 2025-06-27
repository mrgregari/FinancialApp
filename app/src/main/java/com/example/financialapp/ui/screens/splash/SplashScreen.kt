package com.example.financialapp.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
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