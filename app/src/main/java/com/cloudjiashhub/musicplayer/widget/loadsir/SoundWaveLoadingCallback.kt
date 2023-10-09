package com.cloudjiashhub.musicplayer.widget.loadsir

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.cloudjiashhub.musicplayer.R

/**
 * Created by wangchenyan.top on 2023/9/6.
 */
class SoundWaveLoadingCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.load_sir_loading_sound_wave
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}