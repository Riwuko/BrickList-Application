package com.example.bricklistapplication

import android.content.SharedPreferences
import android.widget.EditText
import android.widget.Switch

class SettingsHandler {

    private var activeOnly: Boolean = false
    private var usingPrefix: String = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"


    fun getActiveOnly(sharedPref: SharedPreferences): Boolean {
        loadApplicationSettings(sharedPref)
        return activeOnly
    }

    fun getUsingPrefix(sharedPref: SharedPreferences): String {
        loadApplicationSettings(sharedPref)
        return usingPrefix
    }

    fun loadApplicationSettings(sharedPref: SharedPreferences) {
        if(sharedPref.contains("prefix")) {
            usingPrefix = sharedPref.getString("prefix", "").toString()
        }
        if(sharedPref.contains("activeOnly")) {
            activeOnly = sharedPref.getString("activeOnly", "")?.toBoolean()!!
        }
    }

    fun saveApplicationSettings(sharedPref: SharedPreferences, switch: Switch, editText: EditText) {
        val editor = sharedPref.edit()
        activeOnly = switch.isChecked
        editor.putString("activeOnly",activeOnly.toString())

        usingPrefix = editText.text.toString()
        editor.putString("usingPrefix",usingPrefix)

        editor.apply()
    }

}