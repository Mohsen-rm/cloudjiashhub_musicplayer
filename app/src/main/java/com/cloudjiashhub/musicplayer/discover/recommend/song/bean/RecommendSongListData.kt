package com.cloudjiashhub.musicplayer.discover.recommend.song.bean

import com.google.gson.annotations.SerializedName
import com.cloudjiashhub.musicplayer.common.bean.SongData

/**
 * Created by wangchenyan.top on 2023/9/6.
 */
data class RecommendSongListData(
    @SerializedName("dailySongs")
    val dailySongs: List<SongData> = emptyList()
)
