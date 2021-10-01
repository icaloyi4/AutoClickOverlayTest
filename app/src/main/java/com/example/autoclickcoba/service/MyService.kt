package com.example.alarmmanager.service

import android.app.Service
import android.content.Intent

import android.os.IBinder
import java.lang.UnsupportedOperationException


class MyService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        YourTask()
        return START_STICKY
    }

    private fun YourTask() {
        // call api in background

        // send push notification

        //etc...
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }
}