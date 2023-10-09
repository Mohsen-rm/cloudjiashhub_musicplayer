package com.cloudjiashhub.musicplayer.account.login

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.wcy.common.ext.viewBindings
import com.cloudjiashhub.musicplayer.account.bean.CheckLoginStatusData
import com.cloudjiashhub.musicplayer.account.service.UserService
import com.cloudjiashhub.musicplayer.common.BaseMusicFragment
import com.cloudjiashhub.musicplayer.consts.RoutePath
import com.cloudjiashhub.musicplayer.databinding.FragmentLoginBinding
import me.wcy.router.annotation.Route
import javax.inject.Inject

/**
 * Created by wangchenyan.top on 2023/8/28.
 */
@Route(RoutePath.LOGIN)
@AndroidEntryPoint
class LoginFragment : BaseMusicFragment() {
    private val viewBinding by viewBindings<FragmentLoginBinding>()
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var userService: UserService

    override fun getRootView(): View {
        return viewBinding.root
    }

    override fun onLazyCreate() {
        super.onLazyCreate()

        loadQrCode()

        lifecycleScope.launch {
            viewModel.loginStatus.collectLatest { status ->
                viewBinding.tvStatus.setOnClickListener(null)
                if (status == null) {
                    viewBinding.tvStatus.isVisible = true
                    viewBinding.tvStatus.text = "加载中…"
                } else {
                    when (status.code) {
                        CheckLoginStatusData.STATUS_NOT_SCAN -> {
                            viewBinding.tvStatus.isVisible = false
                        }

                        CheckLoginStatusData.STATUS_SCANNING -> {
                            viewBinding.tvStatus.isVisible = true
                            viewBinding.tvStatus.text = "「${status.nickname}」授权中"
                        }

                        CheckLoginStatusData.STATUS_SUCCESS -> {
                            viewBinding.tvStatus.isVisible = true
                            viewBinding.tvStatus.text = status.message
                            getProfile(status.cookie)
                        }

                        CheckLoginStatusData.STATUS_INVALID -> {
                            viewBinding.tvStatus.isVisible = true
                            viewBinding.tvStatus.text = "二维码已失效，点击刷新"
                            viewBinding.tvStatus.setOnClickListener {
                                loadQrCode()
                            }
                        }

                        else -> {
                            viewBinding.tvStatus.isVisible = true
                            viewBinding.tvStatus.text =
                                status.message.ifEmpty { "二维码错误，点击刷新" }
                            viewBinding.tvStatus.setOnClickListener {
                                loadQrCode()
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.qrCode.collectLatest { qrCode ->
                viewBinding.ivQrCode.setImageBitmap(qrCode)
            }
        }
    }

    private fun loadQrCode() {
        lifecycleScope.launch {
            viewModel.getLoginQrCode()
        }
    }

    private fun getProfile(cookie: String) {
        lifecycleScope.launch {
            val res = userService.login(cookie)
            if (res.isSuccessWithData()) {
                setResultAndFinish()
            } else {
                viewBinding.tvStatus.isVisible = true
                viewBinding.tvStatus.text = "登录失败，点击重试"
                viewBinding.tvStatus.setOnClickListener {
                    loadQrCode()
                }
            }
        }
    }
}