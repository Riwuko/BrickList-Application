package com.example.bricklistapplication

class SinglePackageElement(
    id:Int, projectID:Int,
    elementID:Int, elementTypeID:Int,
    elementColorID:Int,
    elementQuantityInSet:Int,
    elementQuantityInStore:Int, elementCode: String,
    elementDescription:String, colorCode: Int?, imageCode: String
){
    private val id: Int? = id
    private val InventoryID: Int? = projectID
    private val TypeID: Int? = elementTypeID
    private val ItemID: Int? = elementID
    private val ColorID: Int? = elementColorID
    private val elementQuantityInSet: Int? = elementQuantityInSet
    private var elementQuantityInStore: Int? = elementQuantityInStore
    private var elementCode: String? = setElementCode(elementCode)
    private var elementDescription: String? = setElementDescription(elementDescription)
    private var elementColorCode:String? = colorCode.toString()
    private var elementImageCode:String? = imageCode.toString()

    fun getID():Int?{
        return id
    }

    fun getProjectID(): Int? {
        return InventoryID
    }

    fun getElementID(): Int? {
        return ItemID
    }

    fun getElementTypeID(): Int? {
        return TypeID
    }

    fun getElementColorID(): Int? {
        return ColorID
    }

    fun getQuantityInSet(): Int? {
        return elementQuantityInSet
    }

    fun getQuantityInStore(): Int? {
        return elementQuantityInStore
    }

    fun getElementCode():String?{
        return elementCode
    }

    fun getElementDescription():String?{
        return elementDescription
    }

    fun getElementColorCode():String?{
        return elementColorCode
    }

    fun getElementImageCode():String?{
        return elementImageCode
    }

    fun setQuantityInStore(value:Int){
        elementQuantityInStore = value
    }

    fun setElementCode(value:String): String? {
        if (ItemID!!.toInt()==0){
            return ""
        }
        return value
    }

    fun setElementDescription(value:String): String? {
        if (ItemID!!.toInt()==0){
            return "Brak klocka w bazie!"
        }
        return value
    }

    override fun toString(): String {
        return "$elementCode $elementDescription $ItemID $ColorID"
    }

}