package com.revosleap.networkchecker

interface Monitor : LifecycleListener {

    interface ConnectivityListener {
        fun onConnectivityChanged(connectionType: Int, isConnected: Boolean, isFast: Boolean)
    }
}
