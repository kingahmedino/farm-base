package com.farmbase.app.ui.formBuilder.utils.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AndroidAudioRecorder(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    override fun start(outputFile: File) {
        recorder = createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
        }
    }

    override fun stop() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }
}