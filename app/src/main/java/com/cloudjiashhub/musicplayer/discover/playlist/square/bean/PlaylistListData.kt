package com.cloudjiashhub.musicplayer.discover.playlist.square.bean

import com.google.gson.annotations.SerializedName
import com.cloudjiashhub.musicplayer.common.bean.PlaylistData

/**
 * Created by wangchenyan.top on 2023/9/25.
 */
data class PlaylistListData(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("playlists", alternate = ["playlist", "recommend"])
    val playlists: List<PlaylistData> = emptyList(),
)
