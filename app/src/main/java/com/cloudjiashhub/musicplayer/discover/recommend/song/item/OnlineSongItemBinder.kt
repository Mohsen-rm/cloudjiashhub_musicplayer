package com.cloudjiashhub.musicplayer.discover.recommend.song.item

import androidx.core.view.isVisible
import com.blankj.utilcode.util.SizeUtils
import com.cloudjiashhub.musicplayer.common.bean.SongData
import com.cloudjiashhub.musicplayer.databinding.ItemOnlineSongBinding
import com.cloudjiashhub.musicplayer.utils.ImageUtils.loadCover
import com.cloudjiashhub.musicplayer.utils.getSimpleArtist
import me.wcy.radapter3.RItemBinder

/**
 * Created by wangchenyan.top on 2023/9/15.
 */
class OnlineSongItemBinder(private val onItemClick: (SongData, Int) -> Unit) :
    RItemBinder<ItemOnlineSongBinding, SongData>() {

    override fun onBind(viewBinding: ItemOnlineSongBinding, item: SongData, position: Int) {
        viewBinding.root.setOnClickListener {
            onItemClick(item, position)
        }
        viewBinding.ivCover.loadCover(item.al.picUrl, SizeUtils.dp2px(4f))
        viewBinding.tvTitle.text = item.name
        viewBinding.tvTag.isVisible = item.recommendReason.isNotEmpty()
        viewBinding.tvTag.text = item.recommendReason
        viewBinding.tvSubTitle.text = buildString {
            append(item.getSimpleArtist())
            append(" - ")
            append(item.al.name)
            item.originSongSimpleData?.let { originSong ->
                append(" | 原唱: ")
                append(originSong.artists.joinToString("/") { it.name })
            }
        }
    }
}