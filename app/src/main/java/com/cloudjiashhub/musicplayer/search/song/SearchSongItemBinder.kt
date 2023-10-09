package com.cloudjiashhub.musicplayer.search.song

import androidx.core.view.isVisible
import me.wcy.common.ext.context
import com.cloudjiashhub.musicplayer.common.bean.SongData
import com.cloudjiashhub.musicplayer.databinding.ItemSearchSongBinding
import com.cloudjiashhub.musicplayer.utils.MusicUtils
import com.cloudjiashhub.musicplayer.utils.getSimpleArtist
import me.wcy.radapter3.RItemBinder

/**
 * Created by wangchenyan.top on 2023/9/20.
 */
class SearchSongItemBinder(private val onItemClick: (SongData, Int) -> Unit) :
    RItemBinder<ItemSearchSongBinding, SongData>() {
    var keywords = ""

    override fun onBind(viewBinding: ItemSearchSongBinding, item: SongData, position: Int) {
        viewBinding.root.setOnClickListener {
            onItemClick(item, position)
        }
        viewBinding.tvTitle.text = MusicUtils.keywordsTint(viewBinding.context, item.name, keywords)
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