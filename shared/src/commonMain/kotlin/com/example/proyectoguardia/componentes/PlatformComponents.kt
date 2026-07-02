package com.example.proyectoguardia.componentes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView(
    modifier: Modifier = Modifier,
    isReportingMode: Boolean = false,
    isPlacementActive: Boolean = false,
    reportType: String = "light",
    onPageFinished: () -> Unit
)
