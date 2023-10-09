package com.cloudjiashhub.musicplayer.discover.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.wcy.common.ext.toUnMutable
import com.cloudjiashhub.musicplayer.account.service.UserService
import com.cloudjiashhub.musicplayer.common.bean.PlaylistData
import com.cloudjiashhub.musicplayer.discover.DiscoverApi
import com.cloudjiashhub.musicplayer.storage.preference.ConfigPreferences
import javax.inject.Inject

/**
 * Created by wangchenyan.top on 2023/9/25.
 */
@HiltViewModel
class DiscoverViewModel @Inject constructor() : ViewModel() {
    private val _recommendPlaylist = MutableStateFlow<List<PlaylistData>>(emptyList())
    val recommendPlaylist = _recommendPlaylist.toUnMutable()

    @Inject
    lateinit var userService: UserService

    init {
        viewModelScope.launch {
            userService.profile.collectLatest { profile ->
                if (profile != null && ConfigPreferences.apiDomain.isNotEmpty()) {
                    loadRecommendPlaylist()
                }
            }
        }
    }

    private fun loadRecommendPlaylist() {
        viewModelScope.launch {
            kotlin.runCatching {
                DiscoverApi.get().getRecommendPlaylists()
            }.onSuccess {
                _recommendPlaylist.value = it.playlists
            }.onFailure {
            }
        }
    }
}