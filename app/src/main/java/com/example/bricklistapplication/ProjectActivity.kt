package com.example.bricklistapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import kotlinx.android.synthetic.main.activity_project.*

class ProjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        val extras = intent.extras
        val projectName = extras?.getString(AlarmClock.EXTRA_MESSAGE +'2')
        textViewProjectName.text = projectName
    }
}
