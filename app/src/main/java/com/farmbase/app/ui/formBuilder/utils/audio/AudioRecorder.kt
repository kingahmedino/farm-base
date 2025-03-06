package com.farmbase.app.ui.formBuilder.utils.audio

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmbase.app.R
import com.farmbase.app.ui.formBuilder.utils.playback.AndroidAudioPlayer
import com.farmbase.app.ui.formBuilder.utils.record.AndroidAudioRecorder
import kotlinx.coroutines.delay
import java.io.File

/**
 * A composable that provides audio recording functionality.
 *
 * @param modifier Modifier to be applied to the composable
 * @param onDismiss Callback invoked when the user dismisses the audio recorder
 * @param onAudioCaptured Callback invoked when an audio recording is captured. Provides the URI and file path.
 */
@Composable
fun AudioRecord(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onAudioCaptured: (Uri, String) -> Unit
) {
    var isRecording by remember { mutableStateOf(false) }
    var isStopped by remember { mutableStateOf(false) }
    var timerText by remember { mutableStateOf("00:00") }
    var seconds by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val recorder = remember { AndroidAudioRecorder(context) }
    val player = remember { AndroidAudioPlayer(context) }
    var audioFile by remember { mutableStateOf<File?>(null) }

    // Timer logic
    LaunchedEffect(isRecording) {
        if (isRecording) {
            seconds = 0 // Reset timer when starting
            while (isRecording) {
                timerText = String.format("%02d:%02d", seconds / 60, seconds % 60)
                delay(1000L)
                seconds++
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar with close button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Record Audio",
                style = MaterialTheme.typography.titleLarge
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Timer display
        Text(
            text = timerText,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Recording buttons
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isStopped) {
                // Record/Stop controls
                IconButton(
                    onClick = {
                        if (!isRecording) {
                            audioFile = File(context.cacheDir, "audio_${System.currentTimeMillis()}.mp3").also {
                                recorder.start(it)
                            }
                            isRecording = true
                        }
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            if (!isRecording) Color.Red.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Record",
                        tint = if (!isRecording) Color.Red else Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = {
                        if (isRecording) {
                            recorder.stop()
                            isRecording = false
                            isStopped = true
                        }
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            if (isRecording) Color.Black.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.StopCircle,
                        contentDescription = "Stop",
                        tint = if (isRecording) Color.Black else Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else {
                // Playback controls
                IconButton(
                    onClick = {
                        audioFile?.let { player.playFile(it) }
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.Green.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.Green,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = {
                        isStopped = false
                        timerText = "00:00"
                        audioFile = null
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save/done button
        if (isStopped) {
            Button(
                onClick = {
                    audioFile?.let { file ->
                        val uri = Uri.parse(file.absolutePath)
                        onAudioCaptured(uri, file.absolutePath)
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                enabled = audioFile != null
            ) {
                Text("Save Recording")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AudioRecorderPreview() {
//    SD_Form_BuilderTheme {
//        AudioRecorder()
//    }
//}
