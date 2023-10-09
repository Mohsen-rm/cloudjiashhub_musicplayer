package com.cloudjiashhub.musicplayer.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.AudioEffect
import android.text.TextUtils
import androidx.core.text.buildSpannedString
import me.wcy.common.ext.getColorEx
import me.wcy.common.widget.CustomSpan.appendStyle
import com.cloudjiashhub.musicplayer.R

/**
 * 歌曲工具类
 * Created by wcy on 2015/11/27.
 */
object MusicUtils {

    fun isAudioControlPanelAvailable(context: Context): Boolean {
        return isIntentAvailable(
            context,
            Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
        )
    }

    private fun isIntentAvailable(context: Context, intent: Intent): Boolean {
        return context.packageManager.resolveActivity(
            intent,
            PackageManager.GET_RESOLVED_FILTER
        ) != null
    }

    fun getArtistAndAlbum(artist: String?, album: String?): String? {
        return if (TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            ""
        } else if (!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            artist
        } else if (TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            album
        } else {
            "$artist - $album"
        }
    }

    fun keywordsTint(context: Context, text: String, keywords: String): CharSequence {
        if (text.isEmpty() || keywords.isEmpty()) {
            return text
        }
        val splitText = text.split(keywords)
        return buildSpannedString {
            splitText.forEachIndexed { index, s ->
                append(s)
                if (index < splitText.size - 1) {
                    appendStyle(
                        keywords,
                        color = context.getColorEx(R.color.common_theme_color)
                    )
                }
            }
        }
    }
}