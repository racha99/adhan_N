package com.example.adhan_application

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
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

        val listView=findViewById<ListView>(R.id.time_listView)
        listView.adapter=MyCustomAdapter(this)

    }
    private class MyCustomAdapter(context:Context):BaseAdapter(){

        private val mContext:Context

        private val salatName= arrayListOf<String>(
            "Sobh(Fajr)","Dohr","Asr","Maghrib","Isha"
        )
        private val timeSalat= arrayListOf<String>(
            "04:10","12:42","16:31","19:39","21:16"
        )

        init {
            this.mContext=context
        }

        override fun getCount(): Int {
            return salatName.size
        }

        override fun getItemId(position: Int): Long {

            return position.toLong()
        }

        override fun getItem(position: Int): Any {

            return "STRING"
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val layoutInflater=LayoutInflater.from(mContext)
            val rowTime=layoutInflater.inflate(R.layout.row_time,parent,false)

           val salat= rowTime.findViewById<TextView>(R.id.textView)
            val time=rowTime.findViewById<TextView>(R.id.textView2)

            salat.text=salatName.get(position)
            time.text=timeSalat.get(position)
            return rowTime

             }

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
