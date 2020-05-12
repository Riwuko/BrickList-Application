package com.example.bricklistapplication.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.TextView
import com.example.bricklistapplication.R
import com.example.bricklistapplication.SettingsHandler
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private var usingPrefix: String? = null
    private var activeOnly: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setCurrentOptions()
        val switch = switchShowInactive
        switch.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            saveApplicationSettings()
        }
        buttonOK.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        setCurrentOptions()
        val switch = switchShowInactive
        switch.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            saveApplicationSettings()
        }
        buttonOK.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun setCurrentOptions(){
        loadApplicationSettings()
        editTextPrefix.setText(usingPrefix, TextView.BufferType.EDITABLE);

        when(this.activeOnly)
        {
            true -> switchShowInactive.isChecked=true
            false -> switchShowInactive.isChecked=false
        }
    }

    fun loadApplicationSettings() {
        val sharedPref = getSharedPreferences("Options_prefs", 0)
        val settingsHandler = SettingsHandler()
        activeOnly = settingsHandler.getActiveOnly(sharedPref)
        usingPrefix = settingsHandler.getUsingPrefix(sharedPref)
    }

    fun saveApplicationSettings() {
        val sharedPreference =  getSharedPreferences("Options_prefs", Context.MODE_PRIVATE)
        val switch = switchShowInactive
        val editText = editTextPrefix
        val settingsHandler = SettingsHandler()
        settingsHandler.saveApplicationSettings(sharedPreference,switch,editText)

    }
}
