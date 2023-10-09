package com.cloudjiashhub.musicplayer

import android.app.Application
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import dagger.hilt.android.HiltAndroidApp
import me.wcy.common.CommonApp
import com.cloudjiashhub.musicplayer.account.service.UserService
import com.cloudjiashhub.musicplayer.common.DarkModeService
import com.cloudjiashhub.musicplayer.common.MusicFragmentContainerActivity
import com.cloudjiashhub.musicplayer.ext.findActivity
import com.cloudjiashhub.musicplayer.service.AudioPlayer
import me.wcy.router.CRouter
import me.wcy.router.RouterClient
import javax.inject.Inject

/**
 * 自定义Application
 * Created by wcy on 2015/11/27.
 */
@HiltAndroidApp
class MusicApplication : Application() {
    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var audioPlayer: AudioPlayer

    @Inject
    lateinit var darkModeService: DarkModeService

    override fun onCreate() {
        super.onCreate()

        CommonApp.init {
            test = BuildConfig.DEBUG
            isDarkMode = { darkModeService.isDarkMode() }
            titleLayoutConfig {
                isStatusBarDarkFontWhenAuto = { darkModeService.isDarkMode().not() }
                textColorAuto = { R.color.common_text_h1_color }
                textColorBlack = { R.color.common_text_h1_color }
                isTitleCenter = false
            }
        }
        initCRouter()
        darkModeService.init()
    }

    private fun initCRouter() {
        CRouter.setRouterClient(
            RouterClient.Builder()
                .baseUrl("app://music")
                .loginProvider { context, callback ->
                    var activity = context.findActivity()
                    if (activity == null) {
                        activity = ActivityUtils.getTopActivity()
                    }
                    if (activity != null) {
                        userService.checkLogin(activity) {
                            callback()
                        }
                    }
                }
                .fragmentContainerIntentProvider {
                    Intent(it, MusicFragmentContainerActivity::class.java)
                }
                .build()
        )
    }
}