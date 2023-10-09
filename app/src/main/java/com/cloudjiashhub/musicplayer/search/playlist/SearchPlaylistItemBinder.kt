package com.cloudjiashhub.musicplayer.search.playlist

import android.annotation.SuppressLint
import com.blankj.utilcode.util.SizeUtils
import me.wcy.common.ext.context
import com.cloudjiashhub.musicplayer.common.bean.PlaylistData
import com.cloudjiashhub.musicplayer.databinding.ItemSearchPlaylistBinding
import com.cloudjiashhub.musicplayer.utils.ConvertUtils
import com.cloudjiashhub.musicplayer.utils.ImageUtils.loadCover
import com.cloudjiashhub.musicplayer.utils.MusicUtils
import me.wcy.radapter3.RItemBinder

/**
 * Created by wangchenyan.top on 2023/9/21.
 */
class SearchPlaylistItemBinder(private val onItemClick: (PlaylistData) -> Unit) :
    RItemBinder<ItemSearchPlaylistBinding, PlaylistData>() {
    var keywords = ""

    @SuppressLint("SetTextI18n")
    override fun onBind(viewBinding: ItemSearchPlaylistBinding, item: PlaylistData, position: Int) {
        viewBinding.root.setOnClickListener {
            onItemClick(item)
        }
        viewBinding.ivCover.loadCover(item.coverImgUrl, SizeUtils.dp2px(4f))
        viewBinding.tvTitle.text = MusicUtils.keywordsTint(viewBinding.context, item.name, keywords)
        viewBinding.tvSubTitle.text = "${item.trackCount}首 , by ${item.creator.nickname} , 播放${
            ConvertUtils.formatPlayCount(item.playCount, 1)
        }次"
    }
}