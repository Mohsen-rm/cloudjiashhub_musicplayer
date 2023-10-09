package com.cloudjiashhub.musicplayer.account.bean

import com.google.gson.annotations.SerializedName

data class QrCodeData(
    @SerializedName("qrurl")
    val qrurl: String = ""
)