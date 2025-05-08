package com.example.trackdemics.widgets

import androidx.compose.runtime.Composable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.LottieConstants


import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import com.airbnb.lottie.LottieComposition


@Composable
fun LottieFromAssets(
    modifier: Modifier = Modifier,
    assetName: String = "hot_streak.json",
    speed: Float = 1f
)
{
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(assetName))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = speed
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier

    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier
        )
    }
}

