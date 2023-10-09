package com.cloudjiashhub.musicplayer.main.playlist

import android.os.Bundle
import androidx.core.text.buildSpannedString
import dagger.hilt.android.AndroidEntryPoint
import me.wcy.common.ext.getColorEx
import me.wcy.common.ext.toast
import me.wcy.common.ext.viewBindings
import me.wcy.common.widget.CustomSpan.appendStyle
import com.cloudjiashhub.musicplayer.R
import com.cloudjiashhub.musicplayer.common.BaseMusicActivity
import com.cloudjiashhub.musicplayer.databinding.ActivityCurrentPlaylistBinding
import com.cloudjiashhub.musicplayer.service.AudioPlayer
import com.cloudjiashhub.musicplayer.storage.db.entity.SongEntity
import me.wcy.radapter3.RAdapter
import me.wcy.router.annotation.Route
import javax.inject.Inject

/**
 * 播放列表
 */
@Route("/playlist")
@AndroidEntryPoint
class CurrentPlaylistActivity : BaseMusicActivity() {
    private val viewBinding by viewBindings<ActivityCurrentPlaylistBinding>()
    private val adapter = RAdapter<SongEntity>()

    @Inject
    lateinit var audioPlayer: AudioPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        initView()
        initData()
    }

    private fun initView() {
        viewBinding.root.setOnClickListener {
            onBackPressed()
        }
        viewBinding.tvPlayMode.setOnClickListener {
            toast("切换模式")
        }
        viewBinding.btnClear.setOnClickListener {
            toast("清空")
        }

        adapter.register(CurrentPlaylistItemBinder(audioPlayer))
        viewBinding.recyclerView.adapter = adapter
    }

    private fun initData() {
        audioPlayer.playlist.observe(this) { playlist ->
            playlist ?: return@observe
            val size = playlist.size
            viewBinding.tvTitle.text = buildSpannedString {
                append("当前播放")
                if (size > 0) {
                    appendStyle(
                        "($size)",
                        color = getColorEx(R.color.common_text_h2_color),
                        isBold = true
                    )
                }
            }
            adapter.refresh(playlist)
        }
        audioPlayer.currentSong.observe(this) {
            adapter.notifyDataSetChanged()
        }
    }
}