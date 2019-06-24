package com.revosleap.networkchecker.internal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.revosleap.networkchecker.Monitor


class DefaultMonitor @JvmOverloads constructor(
    private val context: Context,
    private val listener: Monitor.ConnectivityListener,
    private val connectionType: Int = -1
) : Monitor {

    private val mainHandler = Handler(Looper.getMainLooper())

    private var isConnected: Boolean = false
    private var isRegistered: Boolean = false

    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val wasConnected = isConnected
            isConnected = NetworkUtil.isConnected(context, connectionType)
            if (wasConnected != isConnected) {
                emitEvent()
            }
        }
    }

    private fun emitEvent() {
        Log.i("Monitor", "Network change")
        mainHandler.post {
            listener.onConnectivityChanged(
                connectionType,
                isConnected,
                isConnected && NetworkUtil.isConnectionFast(context)
            )
        }
    }

    private fun register() {
        if (isRegistered) {
            return
        }

        Log.i("Monitor", "Registering")
        isConnected = NetworkUtil.isConnected(context, connectionType)
        //emit once
        emitEvent()
        context.registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        isRegistered = true
    }

    private fun unregister() {
        if (!isRegistered) {
            return
        }

        Log.i("Monitor", "Unregistering")
        context.unregisterReceiver(connectivityReceiver)
        isRegistered = false
    }

    override fun onStart() {
        register()
    }

    override fun onStop() {
        unregister()
    }
}

