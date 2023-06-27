package com.andreirozov.cloudstorageui.ui

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreirozov.cloudstorageui.Utils
import com.andreirozov.cloudstorageui.ui.theme.Black
import com.andreirozov.cloudstorageui.ui.theme.LightWhite
import com.andreirozov.cloudstorageui.ui.theme.Mint
import com.andreirozov.cloudstorageui.ui.theme.White

@Composable
fun BottomContentWidget() {
    // States of buttons
    var dropboxState by rememberSaveable { mutableStateOf(false) }
    var iCloudState by rememberSaveable { mutableStateOf(false) }

    // Key for restart pointer input
    var pointerInputKey by rememberSaveable { mutableStateOf(false) }

    // Context used for toast
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(pointerInputKey) {
                awaitPointerEventScope {
                    // Dropbox button coordinates
                    val dropboxPolygon = listOf(
                        Offset(x = 0f, y = 0f),
                        Offset(x = size.width.toFloat(), y = 0f),
                        Offset(x = size.width.toFloat(), y = size.height * 0.5f - 8.dp.toPx()),
                        Offset(x = 0f, y = size.height * 0.3f - 8.dp.toPx())
                    )

                    // iCloud button coordinates
                    val iCloudPolygon = listOf(
                        Offset(x = 0f, y = size.height * 0.3f + 8.dp.toPx()),
                        Offset(x = size.width.toFloat(), y = size.height * 0.5f + 8.dp.toPx()),
                        Offset(x = size.width.toFloat(), y = size.height.toFloat()),
                        Offset(x = 0f, y = size.height.toFloat())
                    )

                    // Touch point coordinates
                    val touchPoint = awaitFirstDown(requireUnconsumed = false)

                    // Calculate which button was touched
                    val isDropboxTouch = Utils.isPointInPolygon(
                        point = touchPoint.position,
                        polygon = dropboxPolygon
                    )
                    val isICloudTouch =
                        Utils.isPointInPolygon(point = touchPoint.position, polygon = iCloudPolygon)

                    // Update buttons state to touched
                    if (isDropboxTouch) {
                        dropboxState = true
                    } else if (isICloudTouch) {
                        iCloudState = true
                    }

                    // Wait when user release button
                    waitForUpOrCancellation()

                    // Buttons click events
                    if (this.currentEvent.type == PointerEventType.Release && isDropboxTouch) {
                        // TODO Dropbox click event
                        Toast
                            .makeText(context, "Dropbox click", Toast.LENGTH_SHORT)
                            .show()
                    } else if (this.currentEvent.type == PointerEventType.Release && isICloudTouch) {
                        // TODO iCloud click event
                        Toast
                            .makeText(context, "iCloud click", Toast.LENGTH_SHORT)
                            .show()
                    }

                    // Update buttons state to released
                    dropboxState = false
                    iCloudState = false

                    // Update key
                    pointerInputKey = !pointerInputKey
                }
            }
    ) {
        DropboxCanvas(dropboxState)

        ICloudCanvas(iCloudState)
    }
}

@Composable
private fun DropboxCanvas(dropboxState: Boolean) {
    // Text measurers for Dropbox
    val dropboxMeasurer = rememberTextMeasurer()
    val dropboxSizeMeasurer = rememberTextMeasurer()

    // Scale of Dropbox button
    val dropboxScale by animateFloatAsState(if (dropboxState) 0.96f else 1f)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = dropboxScale
                scaleY = dropboxScale
            }
    ) {
        drawDropbox(
            drawScope = this,
            dropboxMeasurer = dropboxMeasurer,
            sizeMeasurer = dropboxSizeMeasurer
        )
    }
}

@Composable
private fun ICloudCanvas(iCloudState: Boolean) {
    // Text measurers for iCloud
    val iCloudMeasurer = rememberTextMeasurer()
    val iCloudSizeMeasurer = rememberTextMeasurer()

    // Scale of iCloud button
    val iCloudScale by animateFloatAsState(if (iCloudState) 0.96f else 1f)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = iCloudScale
                scaleY = iCloudScale
            }
    ) {
        drawICloud(
            drawScope = this,
            iCloudMeasurer = iCloudMeasurer,
            sizeMeasurer = iCloudSizeMeasurer
        )
    }
}

private fun drawDropbox(
    drawScope: DrawScope,
    dropboxMeasurer: TextMeasurer,
    sizeMeasurer: TextMeasurer
) {
    // Build Dropbox text
    val dropboxText = buildAnnotatedString {
        withStyle(style = ParagraphStyle(lineHeight = 48.sp)) {
            withStyle(style = SpanStyle(color = White, fontSize = 48.sp)) {
                append("Dropbox")
            }
        }
    }

    // Build size text
    val sizeText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = White, fontSize = 16.sp)) {
            append("28 GB")
        }
    }

    drawScope.apply {
        val dropboxPath = Path().apply {
            moveTo(x = 0f, y = 0f)
            lineTo(x = size.width, y = 0f)
            lineTo(x = size.width, y = size.height * 0.5f - 8.dp.toPx())
            lineTo(x = 0f, y = size.height * 0.3f - 8.dp.toPx())
            close()
        }

        val arrowPath = Path().apply {
            moveTo(x = size.width * 0.75f + 16.dp.toPx(), y = 32.dp.toPx())
            lineTo(x = size.width - 16.dp.toPx(), y = 32.dp.toPx())
            lineTo(x = size.width - 24.dp.toPx(), y = 38.dp.toPx())
            moveTo(x = size.width - 16.dp.toPx(), y = 32.dp.toPx())
            lineTo(x = size.width - 24.dp.toPx(), y = 26.dp.toPx())
        }

        drawIntoCanvas { canvas ->
            canvas.drawOutline(
                outline = Outline.Generic(dropboxPath),
                paint = Paint().apply {
                    color = Mint
                    pathEffect = PathEffect.cornerPathEffect(20.dp.toPx())
                }
            )
        }

        drawPath(
            path = arrowPath,
            color = White,
            style = Stroke(width = 4f, miter = 0f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        rotate(degrees = 11f) {
            drawText(
                textMeasurer = dropboxMeasurer,
                text = dropboxText,
                topLeft = Offset(
                    x = size.width - 24.dp.toPx() - dropboxMeasurer.measure(
                        "Dropbox",
                        style = TextStyle(fontSize = 48.sp, lineHeight = 48.sp)
                    ).size.width,
                    y = size.height * 0.5f - 48.sp.toPx() - 50.dp.toPx()
                )
            )
        }

        drawText(
            textMeasurer = sizeMeasurer,
            text = sizeText,
            topLeft = Offset(x = 16.dp.toPx(), y = 32.dp.toPx() - 8.sp.toPx())
        )
    }
}

private fun drawICloud(
    drawScope: DrawScope,
    iCloudMeasurer: TextMeasurer,
    sizeMeasurer: TextMeasurer
) {
    // Build iCloud text
    val iCloudText = buildAnnotatedString {
        withStyle(style = ParagraphStyle(lineHeight = 48.sp)) {
            withStyle(style = SpanStyle(color = Black, fontSize = 48.sp)) {
                append("ICloud")
            }
        }
    }

    // Build size text
    val sizeText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Black, fontSize = 16.sp)) {
            append("19 GB")
        }
    }

    drawScope.apply {
        val iCloudPath = Path().apply {
            moveTo(x = 0f, y = size.height * 0.3f + 8.dp.toPx())
            lineTo(x = size.width, y = size.height * 0.5f + 8.dp.toPx())
            lineTo(x = size.width, y = size.height)
            lineTo(x = 0f, y = size.height)
            close()
        }

        drawIntoCanvas { canvas ->
            canvas.drawOutline(
                outline = Outline.Generic(iCloudPath),
                paint = Paint().apply {
                    color = LightWhite
                    pathEffect = PathEffect.cornerPathEffect(20.dp.toPx())
                }
            )
        }

        drawPath(
            path = iCloudPath,
            color = Black,
            style = Stroke(
                width = 1.dp.toPx(),
                pathEffect = PathEffect.cornerPathEffect(20.dp.toPx())
            )
        )

        rotate(degrees = 11f) {
            drawText(
                textMeasurer = iCloudMeasurer,
                text = iCloudText,
                topLeft = Offset(x = 16.dp.toPx(), y = size.height * 0.3f + 50.dp.toPx())
            )
        }

        rotate(degrees = 11f) {
            drawText(
                textMeasurer = sizeMeasurer,
                text = sizeText,
                topLeft = Offset(
                    x = size.width - 8.dp.toPx() - iCloudMeasurer.measure(
                        "19 GB",
                        style = TextStyle(fontSize = 16.sp)
                    ).size.width, y = size.height * 0.5f + 8.dp.toPx()
                )
            )
        }
    }
}