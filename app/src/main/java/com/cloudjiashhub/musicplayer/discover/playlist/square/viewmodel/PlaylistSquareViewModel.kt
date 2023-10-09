package com.cloudjiashhub.musicplayer.discover.playlist.square.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.wcy.common.ext.toUnMutable
import me.wcy.common.model.CommonResult
import com.cloudjiashhub.musicplayer.discover.DiscoverApi

/**
 * Created by wangchenyan.top on 2023/9/26.
 */
class PlaylistSquareViewModel : ViewModel() {
    private val _tagList = MutableStateFlow<List<String>>(emptyList())
    val tagList = _tagList.toUnMutable()

    suspend fun loadTagList(): CommonResult<Unit> {
        val res = kotlin.runCatching {
            DiscoverApi.get().getPlaylistTagList()
        }
        return if (res.getOrNull()?.code == 200) {
            val list = res.getOrThrow().tags.map { it.name }.toMutableList()
            list.add(0, "全部")
            _tagList.value = list
            CommonResult.success(Unit)
        } else {
            CommonResult.fail(msg = res.exceptionOrNull()?.message)
        }
    }
}