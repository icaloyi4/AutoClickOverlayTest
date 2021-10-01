package com.example.autoclickcoba

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.example.alarmmanager.service.AlarmService

class AlarmSetting(val context: Context?) {
    private var am : AlarmManager

    init {
        // set your alarms here
        // getting the alarm manager
        am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun setAlarm(time: Long) {

        //creating a new intent specifying the broadcast receiver
        val i = Intent(context, AlarmService::class.java)

        //creating a pending intent using the intent
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)

        //setting the repeating alarm that will be fired every day
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pi)
        }
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pi)
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(){
        //creating a new intent specifying the broadcast receiver
        val i = Intent(context, AlarmService::class.java)

        //creating a pending intent using the intent
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)

        //setting the repeating alarm that will be fired every day
        am.cancel(pi)
    }

}