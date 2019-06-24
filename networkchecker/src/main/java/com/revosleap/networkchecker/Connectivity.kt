package com.revosleap.networkchecker

import android.content.Context
import android.util.Log
import com.revosleap.networkchecker.internal.DefaultMonitorFactory

import java.lang.ref.WeakReference
import java.util.HashSet


class Connectivity private constructor(context: Context) {
    private val contextRef: WeakReference<Context>
    private val monitors: MutableSet<Monitor>

    init {
        monitors = HashSet()
        this.contextRef = WeakReference(context)
    }

    fun monitor(connectionType: Int, listener: Monitor.ConnectivityListener): Connectivity? {
        val context = contextRef.get()
        if (context != null)
            monitors.add(DefaultMonitorFactory().create(context, connectionType, listener))

        start()
        return connectivity
    }

    fun monitor(listener: Monitor.ConnectivityListener): Connectivity? {
        return monitor(-1, listener)
    }

    fun start() {
        for (monitor in monitors) {
            monitor.onStart()
        }

        if (monitors.size > 0)
            Log.i(TAG, "started connectivity")
    }

    fun stop() {
        for (monitor in monitors) {
            monitor.onStop()
        }
    }

    companion object {
        private val TAG = "Connectivity"
        private val lock = Any()

        @Volatile
        private var connectivity: Connectivity? = null

        fun from(context: Context): Connectivity? {
            if (connectivity == null) {
                synchronized(lock) {
                    if (connectivity == null) {
                        connectivity = Connectivity(context)
                    }
                }
            }
            return connectivity
        }
    }
}
