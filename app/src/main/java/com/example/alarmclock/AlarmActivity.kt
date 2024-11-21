package com.example.alarmclock

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.system.exitProcess

class AlarmActivity : AppCompatActivity() {

    private lateinit var clockIV: ImageView
    private lateinit var offBTN: Button
    private lateinit var exitBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alarm)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        offBTN.setOnClickListener{
            finish()
            exitProcess(0)
        }
        exitBTN.setOnClickListener{
            finishAffinity()
        }
    }

    private fun init(){
        clockIV = findViewById(R.id.clockIV)
        offBTN = findViewById(R.id.offBTN)
        exitBTN = findViewById(R.id.exitBTN)
    }
}