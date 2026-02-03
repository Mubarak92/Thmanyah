package com.mubarak.thmanyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mubarak.thmanyah.core.design.theme.ThmanyahTheme
import com.mubarak.thmanyah.navigation.ThmanyahNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThmanyahTheme {
                ThmanyahNavGraph()
            }
        }
    }
}
