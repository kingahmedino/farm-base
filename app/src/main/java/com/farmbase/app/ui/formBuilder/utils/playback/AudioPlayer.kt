package com.farmbase.app.ui.formBuilder.utils.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}