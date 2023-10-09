package com.cloudjiashhub.musicplayer.account.service

import android.app.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import me.wcy.common.ext.showConfirmDialog
import me.wcy.common.ext.toUnMutable
import me.wcy.common.model.CommonResult
import com.cloudjiashhub.musicplayer.account.AccountApi
import com.cloudjiashhub.musicplayer.account.AccountPreference
import com.cloudjiashhub.musicplayer.account.bean.ProfileData
import com.cloudjiashhub.musicplayer.consts.RoutePath
import me.wcy.router.CRouter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by wangchenyan.top on 2023/8/25.
 */
@Singleton
class UserServiceImpl @Inject constructor() : UserService {
    private val _profile = MutableStateFlow(AccountPreference.profile)
    override val profile = _profile.toUnMutable()

    override fun getCookie(): String {
        return AccountPreference.cookie
    }

    override fun isLogin(): Boolean {
        return profile.value != null
    }

    override suspend fun login(cookie: String): CommonResult<ProfileData> {
        AccountPreference.cookie = cookie
        val res = kotlin.runCatching {
            AccountApi.get().getLoginStatus()
        }
        return if (res.isSuccess) {
            val loginStatusData = res.getOrThrow()
            val status = loginStatusData.data.account.status
            if (status == 0
                && loginStatusData.data.profile != null
            ) {
                val profileData = loginStatusData.data.profile
                _profile.value = profileData
                AccountPreference.profile = profileData
                CommonResult.success(profileData)
            } else {
                AccountPreference.cookie = ""
                CommonResult.fail(status, msg = "login fail")
            }
        } else {
            AccountPreference.cookie = ""
            CommonResult.fail(msg = res.exceptionOrNull()?.message)
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            AccountPreference.clear()
        }
        _profile.value = null
    }

    override fun checkLogin(
        activity: Activity,
        showDialog: Boolean,
        onCancel: (() -> Unit)?,
        onLogin: (() -> Unit)?
    ) {
        if (isLogin()) {
            onLogin?.invoke()
            return
        }
        val startLogin = {
            CRouter.with(activity).url(RoutePath.LOGIN).startForResult {
                if (it.isSuccess()) {
                    onLogin?.invoke()
                }
            }
        }
        if (showDialog.not()) {
            startLogin()
            return
        }
        activity.showConfirmDialog(
            title = "未登录",
            message = "请先登录",
            confirmButton = "去登录",
            onCancelClick = {
                onCancel?.invoke()
            }
        ) {
            startLogin()
        }
    }
}