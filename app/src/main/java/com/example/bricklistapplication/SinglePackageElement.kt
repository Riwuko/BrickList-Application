package com.example.bricklistapplication

class SinglePackageElement (projectID:Int,elementID:Int, elementTypeID:Int,elementColorID:Int,elementQuantityInSet:Int,elementQuantityInStore:Int){
    private val projectID: Int? = projectID
    private val elementID: Int? = elementID
    private val elementTypeID: Int? = elementTypeID
    private val elementColorID: Int? = elementColorID
    private val elementQuantityInSet: Int? = elementQuantityInSet
    private val elementQuantityInStore: Int? = elementQuantityInStore


    fun getProjectID(): Int? {
        return projectID
    }

    fun getElementID(): Int? {
        return elementID
    }

    fun getElementTypeID(): Int? {
        return elementTypeID
    }

    fun getElementColorID(): Int? {
        return elementColorID
    }

    fun getQuantityInSet(): Int? {
        return elementQuantityInSet
    }

    fun getQuantityInStore(): Int? {
        return elementQuantityInStore
    }
}