package com.cloudjiashhub.musicplayer.mine

import me.wcy.common.net.gson.GsonConverterFactory
import me.wcy.common.utils.GsonUtils
import com.cloudjiashhub.musicplayer.discover.playlist.square.bean.PlaylistListData
import com.cloudjiashhub.musicplayer.net.HttpClient
import com.cloudjiashhub.musicplayer.storage.preference.ConfigPreferences
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by wangchenyan.top on 2023/9/26.
 */
interface MineApi {

    @POST("user/playlist")
    suspend fun getUserPlaylist(
        @Query("uid") uid: Long,
        @Query("limit") limit: Int = 1000,
    ): PlaylistListData

    companion object {
        private val api: MineApi by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(ConfigPreferences.apiDomain)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.gson, true))
                .client(HttpClient.okHttpClient)
                .build()
            retrofit.create(MineApi::class.java)
        }

        fun get(): MineApi = api
    }
}