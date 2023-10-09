package com.cloudjiashhub.musicplayer.service

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.cloudjiashhub.musicplayer.ext.accessEntryPoint

/**
 * Created by wangchenyan.top on 2023/9/18.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AudioPlayerModule {

    @Binds
    abstract fun bindAudioPlayer(
        audioPlayerImpl: AudioPlayerImpl
    ): AudioPlayer

    companion object {
        fun Application.audioPlayer(): AudioPlayer {
            return accessEntryPoint<AudioPlayerEntryPoint>().audioPlayer()
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AudioPlayerEntryPoint {
        fun audioPlayer(): AudioPlayer
    }
}