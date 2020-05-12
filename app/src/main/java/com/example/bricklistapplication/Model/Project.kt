package com.example.bricklistapplication.Model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.*

class Project(id: Int, name: String, active: Boolean, date: String) {
    private var id : Int? = id
    private var name : String? = name
    private var isActive : Boolean? = active
    private var lastAccessed : String = date

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLastAccessedAsDate(): LocalDateTime {
        return LocalDateTime.parse(lastAccessed)
    }

    fun getId(): Int? {
        return id
    }

    fun getName(): String? {
        return name
    }

    fun getIsActive() : Boolean?{
        return isActive
    }

    fun getLastAccessed(): String?{
        return lastAccessed
    }

    fun setIsActive(value: Boolean){
        isActive = value
    }
}