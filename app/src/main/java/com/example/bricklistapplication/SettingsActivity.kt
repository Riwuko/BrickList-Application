package com.example.bricklistapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.TextView
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
        if(sharedPref.contains("prefix")) {
            usingPrefix = sharedPref.getString("prefix", "")
        }else usingPrefix = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
        if(sharedPref.contains("activeOnly")) {
            activeOnly = sharedPref.getString("activeOnly", "")?.toBoolean()
        } else activeOnly = false
    }

    fun saveApplicationSettings() {
        val sharedPreference =  getSharedPreferences("Options_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val switch = switchShowInactive
        activeOnly = switch.isChecked
        editor.putString("activeOnly",activeOnly.toString())

        usingPrefix = editTextPrefix.text.toString()
        editor.putString("usingPrefix",usingPrefix)

        editor.apply()
    }
}
