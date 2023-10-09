package com.cloudjiashhub.musicplayer.discover.playlist.detail.item

import com.cloudjiashhub.musicplayer.common.bean.SongData
import com.cloudjiashhub.musicplayer.databinding.ItemPlaylistSongBinding
import com.cloudjiashhub.musicplayer.utils.getSimpleArtist
import me.wcy.radapter3.RItemBinder

/**
 * Created by wangchenyan.top on 2023/9/22.
 */
class PlaylistSongItemBinder(private val onItemClick: (SongData, Int) -> Unit) :
    RItemBinder<ItemPlaylistSongBinding, SongData>() {

    override fun onBind(viewBinding: ItemPlaylistSongBinding, item: SongData, position: Int) {
        viewBinding.root.setOnClickListener {
            onItemClick(item, position)
        }
        viewBinding.tvIndex.text = (position + 1).toString()
        viewBinding.tvTitle.text = item.name
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