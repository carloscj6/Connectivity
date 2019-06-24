package com.revosleap.networkchecker.internal


import com.revosleap.networkchecker.Monitor

class NoopMonitor : Monitor {
    override fun onStart() {
        //no-op
    }

    override fun onStop() {
        //no-op
    }
}
