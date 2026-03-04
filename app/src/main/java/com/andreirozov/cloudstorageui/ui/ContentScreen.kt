package com.andreirozov.cloudstorageui.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreirozov.cloudstorageui.ui.theme.CloudStorageUITheme

@Composable
fun ContentScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeaderWidget()
            TopContentWidget()
            BottomContentWidget()
        }
    }
}

@Preview(heightDp = 1000)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, heightDp = 1000)
@Composable
private fun ContentScreenPreview() {
    CloudStorageUITheme {
        ContentScreen()
    }
}