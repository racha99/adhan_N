package com.example.adhan_application

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.FOREGROUND_SERVICE), PackageManager.PERMISSION_GRANTED)

        timePicker=findViewById(R.id.timePicker)
        val intent=Intent(this,MyService::class.java)
        ServiceCaller(intent)

        timePicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { timePicker, hour, minute ->
            ServiceCaller(intent)
        })

    }

    private fun ServiceCaller(intent: Intent){
        stopService(intent)

        val adhanHour= timePicker.currentHour
        val adhanMinute= timePicker.currentMinute

        intent.putExtra("adhanHour",adhanHour)
        intent.putExtra("adhanMinute",adhanMinute)

        startService(intent)
    }


}
