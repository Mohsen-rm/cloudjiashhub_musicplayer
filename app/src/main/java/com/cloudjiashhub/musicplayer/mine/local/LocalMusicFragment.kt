package com.cloudjiashhub.musicplayer.mine.local

import android.view.View
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.wcy.common.ext.viewBindings
import me.wcy.common.permission.Permissioner
import com.cloudjiashhub.musicplayer.R
import com.cloudjiashhub.musicplayer.common.BaseMusicFragment
import com.cloudjiashhub.musicplayer.consts.RoutePath
import com.cloudjiashhub.musicplayer.databinding.FragmentLocalMusicBinding
import com.cloudjiashhub.musicplayer.service.AudioPlayer
import com.cloudjiashhub.musicplayer.storage.db.entity.SongEntity
import me.wcy.radapter3.RAdapter
import me.wcy.router.annotation.Route
import javax.inject.Inject

/**
 * Created by wangchenyan.top on 2023/8/30.
 */
@Route(RoutePath.LOCAL_SONG)
@AndroidEntryPoint
class LocalMusicFragment : BaseMusicFragment() {
    private val viewBinding by viewBindings<FragmentLocalMusicBinding>()
    private val localMusicLoader by lazy {
        LocalMusicLoader()
    }
    private val adapter by lazy {
        RAdapter<SongEntity>()
    }

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
        loadData()
    }

    override fun onLazyCreate() {
        super.onLazyCreate()

        adapter.register(LocalSongItemBinder {
            audioPlayer.replaceAll(adapter.getDataList(), it)
        })
        viewBinding.recyclerView.adapter = adapter

        viewBinding.tvPlayAll.setOnClickListener {
            audioPlayer.replaceAll(adapter.getDataList(), adapter.getDataList().first())
        }

        loadData()
    }

    private fun loadData() {
        showLoadSirLoading()
        Permissioner.requestStoragePermission(requireContext()) { granted, shouldRationale ->
            if (granted) {
                lifecycleScope.launch {
                    val songList = withContext(Dispatchers.Default) {
                        localMusicLoader.load(requireContext())
                    }
                    if (songList.isNotEmpty()) {
                        showLoadSirSuccess()
                        adapter.refresh(songList)
                    } else {
                        showLoadSirEmpty(getString(R.string.no_local_music))
                    }
                }
            } else {
                showLoadSirError(getString(R.string.no_permission_storage))
            }
        }
    }

    override fun getNavigationBarColor(): Int {
        return R.color.play_bar_bg
    }
}