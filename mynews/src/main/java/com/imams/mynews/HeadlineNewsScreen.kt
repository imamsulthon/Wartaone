package com.imams.mynews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.imams.mynews.ui.theme.WartaoneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlineNewsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WartaoneTheme {
                MyNewsNavigation()
            }
        }
    }
}