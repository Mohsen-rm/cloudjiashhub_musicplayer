package com.cloudjiashhub.musicplayer.account.service

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow
import me.wcy.common.model.CommonResult
import com.cloudjiashhub.musicplayer.account.bean.ProfileData

/**
 * Created by wangchenyan.top on 2023/9/18.
 */
interface UserService {
    val profile: StateFlow<ProfileData?>

    fun getCookie(): String

    fun isLogin(): Boolean

    suspend fun login(cookie: String): CommonResult<ProfileData>

    suspend fun logout()

    fun checkLogin(
        activity: Activity,
        showDialog: Boolean = true,
        onCancel: (() -> Unit)? = null,
        onLogin: (() -> Unit)? = null
    )
}