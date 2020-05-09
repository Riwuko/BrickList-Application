package com.example.bricklistapplication

class Project {
    private var id : Int? = null
    private var name : String? = null
    private var isActive : Boolean? = null

    public fun getId(): Int? {
        return id
    }

    public fun getName(): String? {
        return name
    }

    public fun getIsActive() : Boolean?{
        return isActive
    }

    public fun setId(value: Int){
        id = value
    }

    public fun setName(value: String){
        name = value
    }

    public fun setIsActive(value: Boolean){
        isActive = value
    }
}