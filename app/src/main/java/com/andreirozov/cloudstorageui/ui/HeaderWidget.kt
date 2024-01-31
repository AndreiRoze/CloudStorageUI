package com.andreirozov.cloudstorageui.ui

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreirozov.cloudstorageui.R

@Composable
fun HeaderWidget() {
    // Context used for toast
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.my_storage),
            modifier = Modifier.weight(1f),
            fontSize = 48.sp,
            lineHeight = 48.sp
        )

        OutlinedButton(
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            onClick = {
                Toast
                    .makeText(context, R.string.menu_click, Toast.LENGTH_SHORT)
                    .show()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.menu_button),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}