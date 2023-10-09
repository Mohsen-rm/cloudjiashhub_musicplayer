package com.cloudjiashhub.musicplayer.discover.playlist.square

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.wcy.common.ext.viewBindings
import me.wcy.common.ui.fragment.BaseFragment
import me.wcy.common.widget.pager.TabLayoutPager
import com.cloudjiashhub.musicplayer.R
import com.cloudjiashhub.musicplayer.consts.RoutePath
import com.cloudjiashhub.musicplayer.databinding.FragmentPlaylistSpuareBinding
import com.cloudjiashhub.musicplayer.discover.playlist.square.viewmodel.PlaylistSquareViewModel
import me.wcy.router.annotation.Route

/**
 * Created by wangchenyan.top on 2023/9/26.
 */
@Route(RoutePath.PLAYLIST_SQUARE)
@AndroidEntryPoint
class PlaylistSquareFragment : BaseFragment() {
    private val viewBinding by viewBindings<FragmentPlaylistSpuareBinding>()
    private val viewModel by viewModels<PlaylistSquareViewModel>()
    private var pager: TabLayoutPager? = null

    override fun getRootView(): View {
        return viewBinding.root
    }

    override fun isUseLoadSir(): Boolean {
        return true
    }

    override fun getLoadSirTarget(): View {
        return viewBinding.content
    }

    override fun onReload() {
        super.onReload()
        loadTagList()
    }

    override fun onLazyCreate() {
        super.onLazyCreate()
        initTab()
        loadTagList()
    }

    private fun initTab() {
        lifecycleScope.launch {
            viewModel.tagList.collectLatest { tagList ->
                if (tagList.isNotEmpty() && pager == null) {
                    pager = TabLayoutPager(
                        lifecycle,
                        childFragmentManager,
                        viewBinding.viewPage2,
                        viewBinding.tabLayout
                    ).apply {
                        tagList.forEach { tag ->
                            addFragment(PlaylistTabFragment().apply {
                                arguments = Bundle().apply {
                                    putString("tag", tag)
                                }
                            }, tag)
                        }
                        setup()
                    }
                }
            }
        }
    }

    private fun loadTagList() {
        lifecycleScope.launch {
            showLoadSirLoading()
            val res = viewModel.loadTagList()
            if (res.isSuccess()) {
                showLoadSirSuccess()
            } else {
                showLoadSirError(res.msg)
            }
        }
    }

    override fun getNavigationBarColor(): Int {
        return R.color.play_bar_bg
    }
}