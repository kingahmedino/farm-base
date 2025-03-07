package com.farmbase.app.ui.formBuilder.utils.record

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}