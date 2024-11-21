package com.example.alarmclock

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var calendar: Calendar? = null
    private var materialTimePicker: MaterialTimePicker? = null

    private lateinit var addBTN: Button
    private lateinit var timerTitleTV: TextView
    private lateinit var timerTV: TextView
    private lateinit var exitBTN: Button

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        init()

        addBTN.setOnClickListener {
            materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Выберите время будильника")
                .build()
            materialTimePicker!!.addOnPositiveButtonClickListener {
                calendar = Calendar.getInstance()
                calendar?.set(Calendar.SECOND, 0)
                calendar?.set(Calendar.MILLISECOND, 0)
                calendar?.set(Calendar.MINUTE, materialTimePicker!!.minute)
                calendar?.set(Calendar.HOUR_OF_DAY, materialTimePicker!!.hour)

                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

                alarmManager.setExact(
                    RTC_WAKEUP,
                    calendar?.timeInMillis!!,
                    getAlarmPendingIntent()!!
                )
                startTimerAlarm()
                Toast.makeText(
                    this,
                    "Будильник установлен на ${dateFormat.format(calendar!!.time)}",
                    Toast.LENGTH_LONG
                ).show()
            }
            materialTimePicker!!.show(supportFragmentManager, "tag_picker")
        }
    }

    override fun onResume() {
        super.onResume()
        addBTN.visibility = View.VISIBLE
        timerTV.visibility = View.INVISIBLE
        timerTitleTV.visibility = View.INVISIBLE
    }

    private fun startTimerAlarm() {
        addBTN.visibility = View.INVISIBLE
        timerTV.visibility = View.VISIBLE
        var duration:Long = calendar!!.timeInMillis - System.currentTimeMillis()
        if(duration<0) duration+=86400000
        timer = object  : CountDownTimer(duration,1000){
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished/1000) /3600
                val minutes = ((millisUntilFinished/1000) %3600)/60
                val seconds = (millisUntilFinished/1000) %60

                val time = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds)
                timerTV.text = time
            }
            override fun onFinish() {
                Toast.makeText(
                    applicationContext,
                    "Будильник заработал",
                    Toast.LENGTH_LONG
                ).show()
            }
        }.start()
    }

    private fun getAlarmPendingIntent(): PendingIntent? {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getBroadcast(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun init() {
        addBTN = findViewById(R.id.addBTN)
        timerTV = findViewById(R.id.timerTV)
        timerTitleTV = findViewById(R.id.timerTitleTV)
        exitBTN = findViewById(R.id.exitBTN)
    }
}