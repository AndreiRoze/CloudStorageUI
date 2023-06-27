package com.andreirozov.cloudstorageui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.andreirozov.cloudstorageui.ui.ContentScreen
import com.andreirozov.cloudstorageui.ui.theme.CloudStorageUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CloudStorageUIApp()
        }
    }
}

@Composable
fun CloudStorageUIApp() {
    CloudStorageUITheme {
        ContentScreen()
    }
}