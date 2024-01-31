package com.andreirozov.cloudstorageui.ui

import android.content.Context
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreirozov.cloudstorageui.R
import com.andreirozov.cloudstorageui.Utils
import com.andreirozov.cloudstorageui.ui.theme.DarkBlue
import com.andreirozov.cloudstorageui.ui.theme.Orange
import com.andreirozov.cloudstorageui.ui.theme.White

@Composable
fun TopContentWidget() {
    // States of buttons
    var googleDriveState by rememberSaveable { mutableStateOf(false) }
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
                    // Google Drive button coordinates
                    val googleDrivePolygon = listOf(
                        Offset(x = 0f, y = 0f),
                        Offset(x = size.width * 0.75f - 8.dp.toPx(), y = 0f),
                        Offset(x = size.width * 0.5f - 8.dp.toPx(), y = size.height.toFloat()),
                        Offset(x = 0f, y = size.height.toFloat())
                    )

                    // iCloud button coordinates
                    val iCloudPolygon = listOf(
                        Offset(x = size.width * 0.75f + 8.dp.toPx(), y = 0f),
                        Offset(x = size.width.toFloat(), y = 0f),
                        Offset(x = size.width.toFloat(), y = size.height.toFloat()),
                        Offset(x = size.width * 0.5f + 8.dp.toPx(), y = size.height.toFloat())
                    )

                    // Touch point coordinates
                    val touchPoint = awaitFirstDown(requireUnconsumed = false)

                    // Calculate which button was touched
                    val isGoogleDriveTouch =
                        Utils.isPointInPolygon(
                            point = touchPoint.position,
                            polygon = googleDrivePolygon
                        )
                    val isICloudTouch =
                        Utils.isPointInPolygon(point = touchPoint.position, polygon = iCloudPolygon)

                    // Update buttons state to touched
                    if (isGoogleDriveTouch) {
                        googleDriveState = true
                    } else if (isICloudTouch) {
                        iCloudState = true
                    }

                    // Wait when user release button
                    waitForUpOrCancellation()

                    // Buttons click events
                    if (this.currentEvent.type == PointerEventType.Release && isGoogleDriveTouch) {
                        Toast
                            .makeText(context, R.string.google_drive_click, Toast.LENGTH_SHORT)
                            .show()
                    } else if (this.currentEvent.type == PointerEventType.Release && isICloudTouch) {
                        Toast
                            .makeText(context, R.string.icloud_click, Toast.LENGTH_SHORT)
                            .show()
                    }

                    // Update buttons state to released
                    googleDriveState = false
                    iCloudState = false

                    // Update key
                    pointerInputKey = !pointerInputKey
                }
            }
    ) {
        GoogleDriveCanvas(
            googleDriveState = googleDriveState,
            context = context
        )
        ICloudCanvas(
            iCloudState = iCloudState,
            context = context
        )
    }
}

@Composable
private fun GoogleDriveCanvas(
    googleDriveState: Boolean,
    context: Context
) {
    // Text measurers for Google Drive
    val googleDriveMeasurer = rememberTextMeasurer()
    val googleSizeMeasurer = rememberTextMeasurer()
    val googleFirstNoteMeasurer = rememberTextMeasurer()
    val googleSecondNoteMeasurer = rememberTextMeasurer()
    val googleThirdNoteMeasurer = rememberTextMeasurer()

    // Scale of Google Drive button
    val googleDriveScale by animateFloatAsState(
        targetValue = if (googleDriveState) 0.96f else 1f,
        label = stringResource(R.string.google_drive_scale_animation)
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = googleDriveScale
                scaleY = googleDriveScale
            }
    ) {
        drawGoogleDrive(
            drawScope = this,
            driveMeasurer = googleDriveMeasurer,
            sizeMeasurer = googleSizeMeasurer,
            firstNoteMeasurer = googleFirstNoteMeasurer,
            secondNoteMeasurer = googleSecondNoteMeasurer,
            thirdNoteMeasurer = googleThirdNoteMeasurer,
            context = context
        )
    }
}

@Composable
private fun ICloudCanvas(
    iCloudState: Boolean,
    context: Context
) {
    // Text measurers for iCloud
    val iCloudMeasurer = rememberTextMeasurer()
    val iCloudFirstNoteMeasurer = rememberTextMeasurer()
    val iCloudSecondNoteMeasurer = rememberTextMeasurer()
    val iCloudThirdNoteMeasurer = rememberTextMeasurer()

    // Scale of iCloud button
    val iCloudScale by animateFloatAsState(
        targetValue = if (iCloudState) 0.96f else 1f,
        label = stringResource(R.string.icloud_scale_animation)
    )

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
            firstNoteMeasurer = iCloudFirstNoteMeasurer,
            secondNoteMeasurer = iCloudSecondNoteMeasurer,
            thirdNoteMeasurer = iCloudThirdNoteMeasurer,
            context = context
        )
    }
}

private fun drawGoogleDrive(
    drawScope: DrawScope,
    driveMeasurer: TextMeasurer,
    sizeMeasurer: TextMeasurer,
    firstNoteMeasurer: TextMeasurer,
    secondNoteMeasurer: TextMeasurer,
    thirdNoteMeasurer: TextMeasurer,
    context: Context
) {
    // Build Google Drive text
    val driveText = buildAnnotatedString {
        withStyle(style = ParagraphStyle(lineHeight = 48.sp)) {
            withStyle(style = SpanStyle(color = White, fontSize = 48.sp)) {
                append(context.getString(R.string.google_drive))
            }
        }
    }

    // Build size text
    val sizeText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = White, fontSize = 16.sp)) {
            append(context.getString(R.string.gb_144))
        }
    }

    // Build first note text
    val firstNoteText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = White, fontSize = 16.sp)) {
            append(context.getString(R.string.photos_video_games))
        }
    }

    // Build second note text
    val secondNoteText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = White, fontSize = 16.sp)) {
            append(context.getString(R.string.documents_pdf_files))
        }
    }

    // Build third note text
    val thirdNoteText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = White, fontSize = 16.sp)) {
            append(context.getString(R.string.icons_shots))
        }
    }

    drawScope.apply {
        val googleDrivePath = Path().apply {
            moveTo(x = 0f, y = 0f)
            lineTo(x = size.width * 0.75f - 8.dp.toPx(), y = 0f)
            lineTo(x = size.width * 0.5f - 8.dp.toPx(), y = size.height)
            lineTo(x = 0f, y = size.height)
            close()
        }

        val arrowPath = Path().apply {
            moveTo(x = size.width / 2, y = 32.dp.toPx())
            lineTo(x = size.width * 0.75f - 32.dp.toPx(), y = 32.dp.toPx())
            lineTo(x = size.width * 0.75f - 40.dp.toPx(), y = 38.dp.toPx())
            moveTo(x = size.width * 0.75f - 32.dp.toPx(), y = 32.dp.toPx())
            lineTo(x = size.width * 0.75f - 40.dp.toPx(), y = 26.dp.toPx())
        }

        drawIntoCanvas { canvas ->
            canvas.drawOutline(
                outline = Outline.Generic(googleDrivePath),
                paint = Paint().apply {
                    color = Orange
                    pathEffect = PathEffect.cornerPathEffect(20.dp.toPx())
                }
            )
        }

        drawPath(
            path = arrowPath,
            color = White,
            style = Stroke(
                width = 4f,
                miter = 0f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        drawText(
            textMeasurer = driveMeasurer,
            text = driveText,
            topLeft = Offset(
                x = 8.dp.toPx(),
                y = size.height - 96.sp.toPx() - 8.dp.toPx()
            )
        )

        drawText(
            textMeasurer = sizeMeasurer,
            text = sizeText,
            topLeft = Offset(
                x = 16.dp.toPx(),
                y = 32.dp.toPx() - 8.sp.toPx()
            )
        )

        drawText(
            textMeasurer = firstNoteMeasurer,
            text = firstNoteText,
            topLeft = Offset(
                x = 8.dp.toPx(),
                y = size.height / 2 - 32.sp.toPx()
            )
        )

        drawText(
            textMeasurer = secondNoteMeasurer,
            text = secondNoteText,
            topLeft = Offset(
                x = 8.dp.toPx(),
                y = size.height / 2
            )
        )

        drawText(
            textMeasurer = thirdNoteMeasurer,
            text = thirdNoteText,
            topLeft = Offset(
                x = 8.dp.toPx(),
                y = size.height / 2 + 32.sp.toPx()
            )
        )
    }
}

private fun drawICloud(
    drawScope: DrawScope,
    iCloudMeasurer: TextMeasurer,
    firstNoteMeasurer: TextMeasurer,
    secondNoteMeasurer: TextMeasurer,
    thirdNoteMeasurer: TextMeasurer,
    context: Context
) {
    // Build iCloud text
    val iCloudText = buildAnnotatedString {
        withStyle(style = ParagraphStyle(lineHeight = 48.sp)) {
            withStyle(style = SpanStyle(color = White, fontSize = 48.sp)) {
                append(context.getString(R.string.icloud))
            }
        }
    }

    // Build first note text
    val firstNoteText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = White, fontSize = 16.sp)) {
            append(context.getString(R.string.music))
        }
    }

    // Build second note text
    val secondNoteText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = White, fontSize = 16.sp)) {
            append(context.getString(R.string.podcasts))
        }
    }

    // Build third note text
    val thirdNoteText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = White, fontSize = 16.sp)) {
            append(context.getString(R.string.design_courses))
        }
    }

    drawScope.apply {
        val iCloudPath = Path().apply {
            moveTo(x = size.width * 0.75f + 8.dp.toPx(), y = 0f)
            lineTo(x = size.width, y = 0f)
            lineTo(x = size.width, y = size.height)
            lineTo(x = size.width * 0.5f + 8.dp.toPx(), y = size.height)
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
                outline = Outline.Generic(iCloudPath),
                paint = Paint().apply {
                    color = DarkBlue
                    pathEffect = PathEffect.cornerPathEffect(20.dp.toPx())
                }
            )
        }

        drawPath(
            path = arrowPath,
            color = White,
            style = Stroke(
                width = 4f,
                miter = 0f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        drawText(
            textMeasurer = iCloudMeasurer,
            text = iCloudText,
            topLeft = Offset(
                x = size.width * 0.5f + 20.dp.toPx(),
                y = size.height - 96.sp.toPx() - 8.dp.toPx()
            )
        )

        drawText(
            textMeasurer = firstNoteMeasurer,
            text = firstNoteText,
            topLeft = Offset(
                x = size.width * 0.70f,
                y = size.height / 2 - 32.sp.toPx()
            )
        )

        drawText(
            textMeasurer = secondNoteMeasurer,
            text = secondNoteText,
            topLeft = Offset(
                x = size.width * 0.67f, y = size.height / 2
            )
        )

        drawText(
            textMeasurer = thirdNoteMeasurer,
            text = thirdNoteText,
            topLeft = Offset(
                x = size.width * 0.64f,
                y = size.height / 2 + 32.sp.toPx()
            )
        )
    }
}