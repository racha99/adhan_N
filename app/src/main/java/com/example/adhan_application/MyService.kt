package com.example.adhan_application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.*

class MyService : Service() {

    private var alarmHour: Int? = null
    private var alarmMinute: Int? = null
    private val t = Timer()
    private var mp = MediaPlayer()
    

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alarmHour = intent?.getIntExtra("adhanHour",0)
        alarmMinute = intent?.getIntExtra("adhanMinute", 0)
         mp = MediaPlayer.create(this, R.raw.azan10)


        val notificationIntent = Intent(this, MainActivity::class.java)

        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                if (
                    //((Calendar.getInstance().time.hours==alarmHour) && (Calendar.getInstance().time.minutes == alarmMinute))
                //or
                    ((Calendar.getInstance().time.hours==4) && (Calendar.getInstance().time.minutes == 10) )
                    or ((Calendar.getInstance().time.hours==12) && (Calendar.getInstance().time.minutes == 42) )
                or ((Calendar.getInstance().time.hours==16) && (Calendar.getInstance().time.minutes == 31) )
                or ((Calendar.getInstance().time.hours==19) && (Calendar.getInstance().time.minutes == 39) )
                or ((Calendar.getInstance().time.hours==21) && (Calendar.getInstance().time.minutes == 16) ))
                {
                    //mp.start()

                    try {
                        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)
                        val sound = Uri.parse("android.resource://" + packageName + "/" + R.raw.azan10)
                        val vibre = longArrayOf(500, 1000)
                        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                            .setContentTitle("Prayer time") // title for notification
                            .setContentText("Slat time - " + Calendar.getInstance().time.hours + " : " +
                                    Calendar.getInstance().time.minutes)// message for notification
                            .setSmallIcon(R.drawable.ic_notifications) //small icon for notification
                            .setAutoCancel(true) // clear notification after click
                            .setContentIntent(pendingIntent) //open the app after click on notification
                            .setSound(sound)
                            .setVibrate(vibre)
                            .build()

                        startForeground(1, notification)  // Services d’Arrière-Plan

                        //Notifivation channel
                        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            val channel = NotificationChannel(
                                CHANNEL_ID,
                                "Adhan Service channel",
                                NotificationManager.IMPORTANCE_DEFAULT)
                            channel.description = "YOUR_NOTIFICATION_CHANNEL_DISCRIPTION"
                            manager.createNotificationChannel(channel)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    mp!!.stop()

                }

            }

        }, 0, 2000)

        return START_STICKY                   //Redémarré après terminaison. Indépendant des intents


    }

    override fun onDestroy() {
        mp!!.stop()
        t.cancel()
        super.onDestroy()
    }

    companion object {
        const val CHANNEL_ID = "MyNotificationAdhanServiceChannel"
    }
}