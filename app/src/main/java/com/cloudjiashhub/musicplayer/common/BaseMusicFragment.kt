package com.cloudjiashhub.musicplayer.common

import com.kingja.loadsir.callback.Callback
import me.wcy.common.ui.fragment.BaseFragment
import com.cloudjiashhub.musicplayer.widget.loadsir.SoundWaveLoadingCallback

/**
 * Created by wangchenyan.top on 2023/9/6.
 */
abstract class BaseMusicFragment : BaseFragment() {

    override fun getLoadingCallback(): Callback {
        return SoundWaveLoadingCallback()
    }

    override fun showLoadSirLoading() {
        loadService?.showCallback(SoundWaveLoadingCallback::class.java)
    }
}