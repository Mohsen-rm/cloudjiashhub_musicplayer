package com.cloudjiashhub.musicplayer.common

import android.content.Intent
import android.os.Bundle
import com.kingja.loadsir.callback.Callback
import me.wcy.common.ui.activity.BaseActivity
import com.cloudjiashhub.musicplayer.service.PlayService
import com.cloudjiashhub.musicplayer.widget.loadsir.SoundWaveLoadingCallback

/**
 * Created by wangchenyan.top on 2023/9/4.
 */
abstract class BaseMusicActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, PlayService::class.java))
    }

    override fun getLoadingCallback(): Callback {
        return SoundWaveLoadingCallback()
    }

    override fun showLoadSirLoading() {
        loadService?.showCallback(SoundWaveLoadingCallback::class.java)
    }
}