package com.cloudjiashhub.musicplayer.mine.local

import com.blankj.utilcode.util.SizeUtils
import com.cloudjiashhub.musicplayer.databinding.ItemLocalSongBinding
import com.cloudjiashhub.musicplayer.storage.db.entity.SongEntity
import com.cloudjiashhub.musicplayer.utils.ImageUtils.loadCover
import com.cloudjiashhub.musicplayer.utils.MusicUtils
import me.wcy.radapter3.RItemBinder

/**
 * Created by wangchenyan.top on 2023/8/30.
 */
class LocalSongItemBinder(private val onItemClick: (SongEntity) -> Unit) :
    RItemBinder<ItemLocalSongBinding, SongEntity>() {

    override fun onBind(viewBinding: ItemLocalSongBinding, item: SongEntity, position: Int) {
        viewBinding.root.setOnClickListener {
            onItemClick(item)
        }
        viewBinding.ivCover.loadCover(item.albumCover, SizeUtils.dp2px(4f))
        viewBinding.tvTitle.text = item.title
        viewBinding.tvArtist.text = MusicUtils.getArtistAndAlbum(item.artist, item.album)
    }
}