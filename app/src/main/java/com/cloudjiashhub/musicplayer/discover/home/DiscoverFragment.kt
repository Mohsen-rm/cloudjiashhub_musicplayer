package com.cloudjiashhub.musicplayer.discover.home

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.wcy.common.ext.toast
import me.wcy.common.ext.viewBindings
import me.wcy.common.widget.decoration.SpacingDecoration
import com.cloudjiashhub.musicplayer.R
import com.cloudjiashhub.musicplayer.account.service.UserService
import com.cloudjiashhub.musicplayer.common.ApiDomainDialog
import com.cloudjiashhub.musicplayer.common.BaseMusicFragment
import com.cloudjiashhub.musicplayer.common.bean.PlaylistData
import com.cloudjiashhub.musicplayer.consts.RoutePath
import com.cloudjiashhub.musicplayer.databinding.FragmentDiscoverBinding
import com.cloudjiashhub.musicplayer.discover.DiscoverApi
import com.cloudjiashhub.musicplayer.discover.home.viewmodel.DiscoverViewModel
import com.cloudjiashhub.musicplayer.discover.playlist.square.item.PlaylistItemBinder
import com.cloudjiashhub.musicplayer.main.MainActivity
import com.cloudjiashhub.musicplayer.service.AudioPlayer
import com.cloudjiashhub.musicplayer.storage.preference.ConfigPreferences
import com.cloudjiashhub.musicplayer.utils.toEntity
import me.wcy.radapter3.RAdapter
import me.wcy.router.CRouter
import javax.inject.Inject

/**
 * Created by wangchenyan.top on 2023/8/21.
 */
@AndroidEntryPoint
class DiscoverFragment : BaseMusicFragment() {
    private val viewBinding by viewBindings<FragmentDiscoverBinding>()
    private val viewModel by viewModels<DiscoverViewModel>()

    private val recommendPlaylistAdapter by lazy {
        RAdapter<PlaylistData>()
    }

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var audioPlayer: AudioPlayer

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
        checkApiDomain(true)
    }

    override fun onLazyCreate() {
        super.onLazyCreate()

        initTitle()
        initTopButton()
        initRecommendPlaylist()
        checkApiDomain(false)
    }

    private fun initTitle() {
        getTitleLayout()?.run {
            addImageMenu(
                R.drawable.ic_menu,
                isDayNight = true,
                isLeft = true
            ).setOnClickListener {
                val activity = requireActivity()
                if (activity is MainActivity) {
                    activity.openDrawer()
                }
            }
        }
        getTitleLayout()?.getContentView()?.setOnClickListener {
            if (ApiDomainDialog.checkApiDomain(requireContext())) {
                CRouter.with(requireActivity()).url(RoutePath.SEARCH).start()
            }
        }
    }

    private fun initTopButton() {
        viewBinding.btnRecommendSong.setOnClickListener {
            CRouter.with(requireActivity()).url(RoutePath.RECOMMEND_SONG).start()
        }
        viewBinding.btnPrivateFm.setOnClickListener {
            toast("敬请期待")
        }
        viewBinding.btnRecommendPlaylist.setOnClickListener {
            toast("敬请期待")
        }
        viewBinding.btnRank.setOnClickListener {
            toast("敬请期待")
        }
    }

    private fun initRecommendPlaylist() {
        viewBinding.tvRecommendPlaylist.setOnClickListener {
            CRouter.with(requireActivity())
                .url(RoutePath.PLAYLIST_SQUARE)
                .start()
        }
        val itemWidth = (ScreenUtils.getAppScreenWidth() - SizeUtils.dp2px(20f)) / 3
        recommendPlaylistAdapter.register(PlaylistItemBinder(itemWidth, true, object :
            PlaylistItemBinder.OnItemClickListener {
            override fun onItemClick(item: PlaylistData) {
                CRouter.with(requireActivity())
                    .url(RoutePath.PLAYLIST_DETAIL)
                    .extra("id", item.id)
                    .start()
            }

            override fun onPlayClick(item: PlaylistData) {
                playPlaylist(item)
            }
        }))
        viewBinding.rvRecommendPlaylist.adapter = recommendPlaylistAdapter
        viewBinding.rvRecommendPlaylist.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBinding.rvRecommendPlaylist.addItemDecoration(
            SpacingDecoration(SizeUtils.dp2px(10f))
        )

        val updateVisibility = {
            if (userService.isLogin() && viewModel.recommendPlaylist.value.isNotEmpty()) {
                viewBinding.tvRecommendPlaylist.isVisible = true
                viewBinding.rvRecommendPlaylist.isVisible = true
            } else {
                viewBinding.tvRecommendPlaylist.isVisible = false
                viewBinding.rvRecommendPlaylist.isVisible = false
            }
        }

        lifecycleScope.launch {
            userService.profile.collectLatest {
                updateVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.recommendPlaylist.collectLatest { recommendPlaylist ->
                updateVisibility()
                recommendPlaylistAdapter.refresh(recommendPlaylist)
            }
        }
    }

    private fun checkApiDomain(isReload: Boolean) {
        if (ConfigPreferences.apiDomain.isNotEmpty()) {
            showLoadSirSuccess()
        } else {
            showLoadSirError("请先设置云音乐API域名")
            if (isReload) {
                ApiDomainDialog.checkApiDomain(requireContext())
            }
        }
    }

    private fun playPlaylist(playlistData: PlaylistData) {
        lifecycleScope.launch {
            showLoading()
            kotlin.runCatching {
                DiscoverApi.get().getPlaylistSongList(playlistData.id)
            }.onSuccess { songListData ->
                dismissLoading()
                if (songListData.code == 200 && songListData.songs.isNotEmpty()) {
                    val songs = songListData.songs.map { it.toEntity() }
                    audioPlayer.replaceAll(songs, songs.first())
                }
            }.onFailure {
                dismissLoading()
            }
        }
    }
}