package com.cloudjiashhub.musicplayer.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import com.cloudjiashhub.musicplayer.service.AudioPlayer
import javax.inject.Inject

/**
 * Created by wcy on 2017/4/18.
 */
@AndroidEntryPoint
class StatusBarReceiver : BroadcastReceiver() {
    @Inject
    lateinit var audioPlayer: AudioPlayer

    override fun onReceive(context: Context, intent: Intent?) {
        val extra = intent?.getStringExtra(EXTRA)
        if (extra == EXTRA_NEXT) {
            audioPlayer.next()
        } else if (extra == EXTRA_PLAY_PAUSE) {
            audioPlayer.playPause()
        }
    }

    companion object {
        const val ACTION_STATUS_BAR = "com.cloudjiashhub.musicplayer.STATUS_BAR_ACTIONS"
        const val EXTRA = "extra"
        const val EXTRA_NEXT = "next"
        const val EXTRA_PLAY_PAUSE = "play_pause"
    }
}