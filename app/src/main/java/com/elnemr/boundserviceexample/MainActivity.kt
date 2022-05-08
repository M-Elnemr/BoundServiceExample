package com.elnemr.boundserviceexample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var mService: DownloadingService
    private var mBound = false
    private var mConnection = object : ServiceConnection{
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            val service = binder as DownloadingService.MyBinder
            mService = service.getService()
            mBound = true
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            mBound = false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_download).setOnClickListener {
            startTheService()
        }
    }

    private fun startTheService() {
        if (mBound){
            MainScope().launch {
                mService.getProgress().collect {
                    withContext(Dispatchers.Main){
                        findViewById<TextView>(R.id.tv_progress).text = it.toString()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, DownloadingService::class.java).also {
            bindService(it, mConnection, Context.BIND_AUTO_CREATE)
        }
    }
}