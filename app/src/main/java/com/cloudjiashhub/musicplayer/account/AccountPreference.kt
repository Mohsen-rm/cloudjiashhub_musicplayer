package com.cloudjiashhub.musicplayer.account

import me.wcy.common.CommonApp
import me.wcy.common.storage.IPreferencesFile
import me.wcy.common.storage.PreferencesFile
import com.cloudjiashhub.musicplayer.account.bean.ProfileData
import com.cloudjiashhub.musicplayer.consts.PreferenceName

/**
 * Created by wangchenyan.top on 2023/8/28.
 */
object AccountPreference :
    IPreferencesFile by PreferencesFile(CommonApp.app, PreferenceName.ACCOUNT, false) {
    var cookie by IPreferencesFile.StringProperty("cookie", "")
    var profile by IPreferencesFile.ObjectProperty("profile", ProfileData::class.java)
}