package com.cloudjiashhub.musicplayer.storage.preference

import com.blankj.utilcode.util.StringUtils
import me.wcy.common.CommonApp
import me.wcy.common.storage.IPreferencesFile
import me.wcy.common.storage.PreferencesFile
import com.cloudjiashhub.musicplayer.R
import com.cloudjiashhub.musicplayer.common.DarkModeService
import com.cloudjiashhub.musicplayer.consts.PreferenceName

/**
 * SharedPreferences工具类
 * Created by wcy on 2015/11/28.
 */
object ConfigPreferences :
    IPreferencesFile by PreferencesFile(CommonApp.app, PreferenceName.CONFIG, false) {

    var filterSize by IPreferencesFile.StringProperty(
        StringUtils.getString(R.string.setting_key_filter_size),
        "0"
    )

    var filterTime by IPreferencesFile.StringProperty(
        StringUtils.getString(R.string.setting_key_filter_time),
        "0"
    )

    var darkMode by IPreferencesFile.StringProperty(
        "dark_mode",
        DarkModeService.DarkMode.Auto.value
    )

    var playMode: Int by IPreferencesFile.IntProperty("play_mode", 0)

    var currentSongId: String by IPreferencesFile.StringProperty("current_song_id", "")

    var apiDomain: String by IPreferencesFile.StringProperty("api_domain", "")
}