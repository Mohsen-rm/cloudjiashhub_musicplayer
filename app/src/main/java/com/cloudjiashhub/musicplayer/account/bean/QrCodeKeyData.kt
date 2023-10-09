package com.cloudjiashhub.musicplayer.account.bean

import com.google.gson.annotations.SerializedName

data class QrCodeKeyData(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("unikey")
    val unikey: String = ""
)