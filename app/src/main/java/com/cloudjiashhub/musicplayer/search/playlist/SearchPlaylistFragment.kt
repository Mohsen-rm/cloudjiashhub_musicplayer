package com.cloudjiashhub.musicplayer.search.playlist

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.wcy.common.model.CommonResult
import me.wcy.common.net.apiCall
import com.cloudjiashhub.musicplayer.common.SimpleMusicRefreshFragment
import com.cloudjiashhub.musicplayer.common.bean.PlaylistData
import com.cloudjiashhub.musicplayer.consts.Consts
import com.cloudjiashhub.musicplayer.consts.RoutePath
import com.cloudjiashhub.musicplayer.search.SearchApi
import com.cloudjiashhub.musicplayer.search.SearchViewModel
import me.wcy.radapter3.RAdapter
import me.wcy.router.CRouter

/**
 * Created by wangchenyan.top on 2023/9/20.
 */
@AndroidEntryPoint
class SearchPlaylistFragment : SimpleMusicRefreshFragment<PlaylistData>() {
    private val viewModel by activityViewModels<SearchViewModel>()
    private val itemBinder by lazy {
        SearchPlaylistItemBinder { item ->
            CRouter.with(requireActivity())
                .url(RoutePath.PLAYLIST_DETAIL)
                .extra("id", item.id)
                .start()
        }.apply {
            keywords = viewModel.keywords.value
        }
    }

    override fun isShowTitle(): Boolean {
        return false
    }

    override fun isRefreshEnabled(): Boolean {
        return false
    }

    override fun onLazyCreate() {
        super.onLazyCreate()
        lifecycleScope.launch {
            viewModel.keywords.collectLatest {
                if (it.isNotEmpty()) {
                    showLoadSirLoading()
                    itemBinder.keywords = it
                    autoRefresh(true)
                }
            }
        }
    }

    override fun initAdapter(adapter: RAdapter<PlaylistData>) {
        adapter.register(itemBinder)
    }

    override suspend fun getData(page: Int): CommonResult<List<PlaylistData>> {
        val keywords = viewModel.keywords.value
        if (keywords.isEmpty()) {
            return CommonResult.success(emptyList())
        }
        val res = apiCall {
            SearchApi.get()
                .search(1000, keywords, Consts.PAGE_COUNT, (page - 1) * Consts.PAGE_COUNT)
        }
        return if (res.isSuccessWithData()) {
            CommonResult.success(res.getDataOrThrow().playlists)
        } else {
            CommonResult.fail(res.code, res.msg)
        }
    }
}