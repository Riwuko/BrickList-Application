package com.example.bricklistapplication

class Project(id: Int, name: String, active: Boolean) {
    private var id : Int? = id
    private var name : String? = name
    private var isActive : Boolean? = active


    fun getId(): Int? {
        return id
    }

    fun getName(): String? {
        return name
    }

    fun getIsActive() : Boolean?{
        return isActive
    }

    fun setId(value: Int){
        id = value
    }

    fun setName(value: String){
        name = value
    }

    fun setIsActive(value: Boolean){
        isActive = value
    }
}