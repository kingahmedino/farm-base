package com.farmbase.app.ui.formBuilder.utils.playback

import android.content.Context
import android.media.MediaPlayer
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
) : AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        player?.release()

        player = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}
