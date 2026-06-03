package com.example.proyectoguardia

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView(
    modifier: Modifier = Modifier,
    isReportingMode: Boolean = false,
    onPageFinished: () -> Unit
)
