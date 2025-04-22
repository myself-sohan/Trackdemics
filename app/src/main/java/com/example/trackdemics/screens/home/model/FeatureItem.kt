package com.example.trackdemics.screens.home.model

import androidx.compose.ui.graphics.vector.ImageVector

data class FeatureItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)