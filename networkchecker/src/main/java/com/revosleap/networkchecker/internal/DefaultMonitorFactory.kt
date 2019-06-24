package com.revosleap.networkchecker.internal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

import com.revosleap.networkchecker.Monitor
import com.revosleap.networkchecker.MonitorFactory


class DefaultMonitorFactory : MonitorFactory {

    override fun create(
        context: Context,
        connectionType: Int,
        listener: Monitor.ConnectivityListener
    ): Monitor {

        val permissionResult = ContextCompat.checkSelfPermission(context, ACCESS_NETWORK_PERMISSION)
        val hasPermission = permissionResult == PackageManager.PERMISSION_GRANTED

        return if (hasPermission)
            DefaultMonitor(context, listener, connectionType)
        else
            NoopMonitor()
    }

    companion object {
        val ACCESS_NETWORK_PERMISSION = Manifest.permission.ACCESS_NETWORK_STATE
    }
}
