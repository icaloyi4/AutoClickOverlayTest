package com.example.autoclickcoba

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.alarmmanager.utils.SharedData
import com.example.autoclickcoba.service.FloatingClickService
import com.example.autoclickcoba.service.autoClickService
import java.util.*
import kotlin.concurrent.fixedRateTimer
import android.text.TextUtils

import android.accessibilityservice.AccessibilityService
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import com.example.autoclickcoba.service.WhatsappAccessibilityService





private const val PERMISSION_CODE = 110

class MainActivity : AppCompatActivity() {

    lateinit var timePicker : TimePicker
    private var serviceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                || Settings.canDrawOverlays(this)) {
                serviceIntent = Intent(this@MainActivity,
                    FloatingClickService::class.java)
                startService(serviceIntent)
                onBackPressed()
            } else {
                askPermission()
                shortToast("You need System Alert Window Permission to do this")
            }
        }
        timePicker = findViewById(R.id.timepicker)

        findViewById<Button>(R.id.btn_set_alarm).setOnClickListener {
            // Do some work here
            val calendar: Calendar = Calendar.getInstance()
            if (Build.VERSION.SDK_INT >= 23) {
                calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.hour,
                    timePicker.minute,
                    0
                )
            } else {
                calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.currentHour,
                    timePicker.currentMinute,
                    0
                )
            }



            val sharedData = SharedData(this)
            sharedData.setDataLong("myAlarm", calendar.timeInMillis)


            val alarmSetting = AlarmSetting(this)
            alarmSetting.setAlarm(calendar.timeInMillis)

//            openWhatsApp( "6281333169082")

//            showNotification(this, "Test", "Test Bosque", null)

        }
    }

    private fun openWhatsApp(mobile: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send. Sent by MY_APP")
        sendIntent.putExtra("jid", "$mobile@s.whatsapp.net") //phone number without "+" prefix

        sendIntent.setPackage("com.whatsapp")
        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Error/n", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(sendIntent)
    }

    private fun checkAccess(): Boolean {
        val string = getString(R.string.accessibility_service_id)
        val manager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val list = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        for (id in list) {
            if (string == id.id) {
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
//        val hasPermission = checkAccess()
//        "has access? $hasPermission".logd()
//        if (!hasPermission) {
//            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//            && !Settings.canDrawOverlays(this)) {
//            askPermission()
//        }

        if (!isAccessibilityOn(this, WhatsappAccessibilityService::class.java)) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }


    }

    private fun isAccessibilityOn(
        context: Context,
        clazz: Class<out AccessibilityService?>
    ): Boolean {
        var accessibilityEnabled = 0
        val service = context.packageName + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (ignored: SettingNotFoundException) {
        }
        val colonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                colonSplitter.setString(settingValue)
                while (colonSplitter.hasNext()) {
                    val accessibilityService = colonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun askPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName"))
        resultLauncher.launch(intent)

    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
        }
    }

    override fun onDestroy() {
        serviceIntent?.let {
            "stop floating click service".logd()
            stopService(it)
        }
        autoClickService?.let {
            "stop auto click service".logd()
            it.stopSelf()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) return it.disableSelf()
            autoClickService = null
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }


}