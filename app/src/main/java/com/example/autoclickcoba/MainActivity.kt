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
import androidx.activity.result.contract.ActivityResultContracts
import com.example.autoclickcoba.service.FloatingClickService
import com.example.autoclickcoba.service.autoClickService

private const val PERMISSION_CODE = 110

class MainActivity : AppCompatActivity() {

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
        val hasPermission = checkAccess()
        "has access? $hasPermission".logd()
        if (!hasPermission) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.canDrawOverlays(this)) {
            askPermission()
        }


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