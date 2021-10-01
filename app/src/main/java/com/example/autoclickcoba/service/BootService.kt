package com.example.alarmmanager.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alarmmanager.utils.SharedData

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.autoclickcoba.AlarmSetting
import com.example.autoclickcoba.R
import java.lang.Exception


class BootService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        /*if (context!=null){
            val sharedData = SharedData(context)
            var myAlarm = sharedData.getDataLong("myAlarm")

            if (!myAlarm.equals(0)){
                val alarmSetting = AlarmSetting(context)
                alarmSetting.setAlarm(myAlarm)
            }

        }*/

        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val sharedData = SharedData(context!!)
            var myAlarm = sharedData.getDataLong("myAlarm")

            if (!myAlarm.equals(0)) {
                val alarmSetting = AlarmSetting(context)
                alarmSetting.setAlarm(myAlarm)
            }



//                var i = Intent(context, MainActivity::class.java)
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context?.startActivity(i)
        }
    }

    fun showNotification(context: Context, title: String?, body: String?, intent: Intent?) {
        try{
            val notificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = 1
            val channelId = "channel-01"
            val channelName = "Channel Name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    channelId, channelName, importance
                )
                notificationManager.createNotificationChannel(mChannel)
            }
            val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
            /*val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
            val resultPendingIntent: PendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            mBuilder.setContentIntent(resultPendingIntent)*/
            notificationManager.notify(notificationId, mBuilder.build())
        } catch (e : Exception){
            Log.e("Error", e.toString())
        }

    }
}