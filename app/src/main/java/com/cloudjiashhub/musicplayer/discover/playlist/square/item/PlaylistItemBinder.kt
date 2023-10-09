package com.cloudjiashhub.musicplayer.discover.playlist.square.item

import androidx.core.view.isVisible
import com.blankj.utilcode.util.SizeUtils
import com.cloudjiashhub.musicplayer.common.bean.PlaylistData
import com.cloudjiashhub.musicplayer.databinding.ItemPlaylistBinding
import com.cloudjiashhub.musicplayer.utils.ConvertUtils
import com.cloudjiashhub.musicplayer.utils.ImageUtils.loadCover
import me.wcy.radapter3.RItemBinder

/**
 * Created by wangchenyan.top on 2023/9/25.
 */
class PlaylistItemBinder(
    private val itemWidth: Int,
    private val showPlayButton: Boolean,
    private val listener: OnItemClickListener
) : RItemBinder<ItemPlaylistBinding, PlaylistData>() {

    override fun onBind(
        viewBinding: ItemPlaylistBinding,
        item: PlaylistData,
        position: Int
    ) {
        viewBinding.root.setOnClickListener {
            listener.onItemClick(item)
        }
        viewBinding.ivPlay.isVisible = showPlayButton
        viewBinding.ivPlay.setOnClickListener {
            listener.onPlayClick(item)
        }
        val lp = viewBinding.ivCover.layoutParams
        lp.width = itemWidth
        lp.height = itemWidth
        viewBinding.ivCover.layoutParams = lp
        viewBinding.ivCover.loadCover(item.coverImgUrl, SizeUtils.dp2px(6f))
        viewBinding.tvPlayCount.text = ConvertUtils.formatPlayCount(item.playCount)
        viewBinding.tvName.text = item.name
    }

    interface OnItemClickListener {
        fun onItemClick(item: PlaylistData)
        fun onPlayClick(item: PlaylistData)
    }
}