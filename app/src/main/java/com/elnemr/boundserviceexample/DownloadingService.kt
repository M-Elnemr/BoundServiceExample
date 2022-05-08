package com.elnemr.boundserviceexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DownloadingService : Service() {
    private val binder = MyBinder()
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun getProgress(): Flow<Float> {
        var progress = 0f

        return flow {
            while (progress < 100) {
                progress += 1f
                delay(500)
                emit(progress)
            }
        }
    }

    inner class MyBinder : Binder() {
        fun getService() = this@DownloadingService
    }
}