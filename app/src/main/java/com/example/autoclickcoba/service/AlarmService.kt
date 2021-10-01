package com.example.alarmmanager.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.autoclickcoba.R
import com.example.autoclickcoba.service.autoClickService
import java.lang.Exception
import java.util.*
import kotlin.concurrent.fixedRateTimer


class AlarmService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Test Alarm", "Start")
//        var mediaPlayer : MediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI)
//        mediaPlayer.start()
        if (context != null) {
        val phoneNumberWithCountryCode = "+6281333169082"
        val message = "Hallo"

//        var intent = Intent(
//            Intent.ACTION_VIEW,
//            Uri.parse(
//                String.format(
//                    "https://api.whatsapp.com/send?phone=%s&text=%s",
//                    phoneNumberWithCountryCode,
//                    message
//                )
//            )
//        )
//        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
            showNotification(context, "Test", "Test Alarm", null)
//            var timer: Timer? = null
//            timer = fixedRateTimer(initialDelay = 3000,
//                period = 1000) {
//                autoClickService?.click(702,
//                    1582)
//                timer?.cancel()
//            }
            openWhatsApp( context, "6281333169082")
        }


    }

    private fun openWhatsApp(context: Context?, mobile: String) {
        try {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send by Service. Sent by MY_APP")
            sendIntent.putExtra("jid", "$mobile@s.whatsapp.net") //phone number without "+" prefix

            sendIntent.setPackage("com.whatsapp")
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(sendIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Tidak Ada WA", Toast.LENGTH_SHORT).show()
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