package com.imams.mynews.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Stable
@Composable
fun Modifier.skeleton(visible: Boolean) {
    this.placeholder(visible = visible, highlight = PlaceholderHighlight.shimmer())
}


