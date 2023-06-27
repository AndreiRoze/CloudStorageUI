package com.andreirozov.cloudstorageui.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderWidget() {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "my\nstorage",
            fontSize = 48.sp,
            lineHeight = 48.sp
        )

        OutlinedButton(
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            onClick = {
                // TODO menu button click event
            }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "Menu button"
            )
        }
    }
}