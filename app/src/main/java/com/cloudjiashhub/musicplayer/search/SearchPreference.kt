package com.cloudjiashhub.musicplayer.search

import me.wcy.common.CommonApp
import me.wcy.common.storage.IPreferencesFile
import me.wcy.common.storage.PreferencesFile
import com.cloudjiashhub.musicplayer.consts.PreferenceName

/**
 * Created by wangchenyan.top on 2023/9/21.
 */
object SearchPreference :
    IPreferencesFile by PreferencesFile(CommonApp.app, PreferenceName.SEARCH) {
    var historyKeywords by IPreferencesFile.ListProperty("history_keywords", String::class.java)
}