package com.revosleap.networkchecker

import android.content.Context

interface MonitorFactory {

    fun create(
        context: Context,
        connectionType: Int,
        listener: Monitor.ConnectivityListener
    ): Monitor
}
