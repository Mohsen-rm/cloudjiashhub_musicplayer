package com.cloudjiashhub.musicplayer.ext

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.IntentFilter
import android.os.Build
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.EntryPointAccessors

/**
 * Created by wangchenyan.top on 2023/7/12.
 */

inline fun <reified T : Any> Application.accessEntryPoint(): T {
    return EntryPointAccessors.fromApplication(this, T::class.java)
}

fun Context.registerReceiverCompat(receiver: BroadcastReceiver, filter: IntentFilter) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
    } else {
        registerReceiver(receiver, filter)
    }
}

fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> {
            this
        }

        is ContextWrapper -> {
            this.baseContext?.findActivity()
        }

        else -> {
            null
        }
    }
}

fun Context.findLifecycleOwner(): LifecycleOwner? {
    return when (this) {
        is LifecycleOwner -> {
            this
        }

        is ContextWrapper -> {
            this.baseContext?.findLifecycleOwner()
        }

        else -> {
            null
        }
    }
}
