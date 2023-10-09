package com.cloudjiashhub.musicplayer.search

import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.KeyboardUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.wcy.common.ext.viewBindings
import me.wcy.common.widget.pager.TabLayoutPager
import com.cloudjiashhub.musicplayer.R
import com.cloudjiashhub.musicplayer.common.BaseMusicFragment
import com.cloudjiashhub.musicplayer.consts.RoutePath
import com.cloudjiashhub.musicplayer.databinding.FragmentSearchBinding
import com.cloudjiashhub.musicplayer.databinding.ItemSearchHistoryBinding
import com.cloudjiashhub.musicplayer.databinding.TitleSearchBinding
import com.cloudjiashhub.musicplayer.search.playlist.SearchPlaylistFragment
import com.cloudjiashhub.musicplayer.search.song.SearchSongFragment
import me.wcy.router.annotation.Route

/**
 * Created by wangchenyan.top on 2023/9/20.
 */
@Route(RoutePath.SEARCH)
@AndroidEntryPoint
class SearchFragment : BaseMusicFragment() {
    private val viewBinding by viewBindings<FragmentSearchBinding>()
    private val titleBinding by lazy {
        TitleSearchBinding.bind(getTitleLayout()!!.getContentView()!!)
    }
    private val viewModel by activityViewModels<SearchViewModel>()
    private val menuSearch by lazy {
        getTitleLayout()!!.addTextMenu("搜索", false)!!
    }

    override fun getRootView(): View {
        return viewBinding.root
    }

    override fun onLazyCreate() {
        super.onLazyCreate()

        initTitle()
        initTab()
        initHistory()

        lifecycleScope.launch {
            viewModel.showResult.collectLatest { showResult ->
                viewBinding.llHistory.isVisible = showResult.not()
                viewBinding.llResult.isVisible = showResult
            }
        }

        lifecycleScope.launch {
            delay(200)
            KeyboardUtils.showSoftInput(titleBinding.etSearch)
        }
    }

    private fun initTitle() {
        titleBinding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                menuSearch.performClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        menuSearch.setOnClickListener {
            val keywords = titleBinding.etSearch.text?.trim()?.toString() ?: ""
            if (keywords.isNotEmpty()) {
                KeyboardUtils.hideSoftInput(requireActivity())
                viewModel.search(keywords)
            }
        }
    }

    private fun initTab() {
        val pager = TabLayoutPager(
            lifecycle,
            childFragmentManager,
            viewBinding.viewPage2,
            viewBinding.tabLayout
        )
        pager.addFragment(SearchSongFragment(), "单曲")
        pager.addFragment(SearchPlaylistFragment(), "歌单")
        pager.setup()
    }

    private fun initHistory() {
        lifecycleScope.launch {
            viewModel.historyKeywords.collectLatest { list ->
                viewBinding.flHistory.removeAllViews()
                list.forEach { text ->
                    ItemSearchHistoryBinding.inflate(
                        LayoutInflater.from(context),
                        viewBinding.flHistory,
                        true
                    ).apply {
                        root.text = text
                        root.setOnClickListener {
                            titleBinding.etSearch.setText(text)
                            titleBinding.etSearch.setSelection(text.length)
                            menuSearch.performClick()
                        }
                    }
                }
            }
        }
    }

    override fun onInterceptBackEvent(): Boolean {
        if (viewModel.showResult.value) {
            viewModel.showHistory()
            return true
        }
        return super.onInterceptBackEvent()
    }

    override fun getNavigationBarColor(): Int {
        return R.color.play_bar_bg
    }
}