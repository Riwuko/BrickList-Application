package com.example.bricklistapplication

import LegoDataBaseHelper
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class PackageElementsListAdapter(context: Context?, resource: Int, objects: MutableList<SinglePackageElement>) : ArrayAdapter<SinglePackageElement>(
    context!!, resource, objects as MutableList<SinglePackageElement>) {

    private var adapterContext: Context? = context
    private var adapterLayout: Int = resource
    private val inflater: LayoutInflater =
        adapterContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var legoDataBaseHelper: LegoDataBaseHelper? = null

    private lateinit var legoAmount: TextView
    private lateinit var legoDesText: TextView
    private lateinit var legoNameText: TextView

    fun loadDataBase() {
        legoDataBaseHelper = LegoDataBaseHelper(adapterContext!!)
    }

    fun getSingleElementData(position: Int): MutableMap<String, String> {
        loadDataBase()
        val data = mutableMapOf<String, String>()
        val singleElement = getItem(position)

        if(singleElement?.getElementID()!!>0) {
            data.put("brickName", legoDataBaseHelper!!.getPartCode(getItem(position)!!.getElementID()!!))
            data.put("brickDescription", legoDataBaseHelper!!.getColorName(getItem(position)!!.getElementColorID()!!) + " " +
                    legoDataBaseHelper!!.getPartName(getItem(position)!!.getElementID()!!) )
        }
        else{
            data.put("brickName", "Brak klocka w bazie")
            data.put("brickDescription", "")
        }
        data.put("brickId", getItem(position)!!.getElementID().toString())
        data.put("quantityInStore", getItem(position)!!.getQuantityInStore().toString())
        data.put("quantityInSet", getItem(position)!!.getQuantityInSet().toString())
        data.put("projectID",getItem(position)!!.getProjectID().toString())

        return data
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val retView: View

        if (convertView == null) {
            val holder = getItem(position)
            retView = inflater.inflate(adapterLayout, null)
            retView.tag = holder
        } else {
            retView = convertView
        }

        val buttonAdd = retView.findViewById<Button>(R.id.buttonAddElement)
        val buttonSubtract = retView.findViewById<Button>(R.id.buttonSubtractElement)
        buttonHandler(buttonAdd, position,"add")
        buttonHandler(buttonSubtract, position,"subtract")

        setAdapterFields(position, retView)
        return retView
    }

    fun buttonHandler(button: Button, position: Int, operation:String) {
        loadDataBase()
        button.setOnClickListener {
            changeAmountValue(operation, position)
            updateAmountField(position)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateAmountField(position: Int){
        val amountInStore = getItem(position)!!.getQuantityInStore().toString()
        val amountInSet = getItem(position)!!.getQuantityInSet().toString()
        legoAmount.text = "$amountInStore/$amountInSet"
    }

    fun changeAmountValue(operation: String, position: Int) {
        val element = getSingleElementData(position)
        var value:Int = element["quantityInStore"]!!.toInt()
        var success: Boolean = false
        if (operation=="add"){
            if ((value+1) <= element["quantityInSet"]!!.toInt()){
                success = true
                value += 1
            }
        }else if (operation=="subtract"){
            if ((value-1) >= 0) {
                success = true
                value -=1
            }
        }
        if (success){
            legoDataBaseHelper!!.updateQuantityInStore(getItem(position)!!.getID()!!, value)
            getItem(position)!!.setQuantityInStore(value)
        }
    }

    @SuppressLint("SetTextI18n")
    fun setAdapterFields(position: Int, retView: View) {
        val element = getSingleElementData(position)
        legoNameText = retView.findViewById<TextView>(R.id.textViewLegoName)
        legoDesText = retView.findViewById<TextView>(R.id.textViewLegoDes)
        legoAmount = retView.findViewById<TextView>(R.id.textViewLegoAmount)

        legoNameText.text = element["brickName"]
        legoDesText.text = element["brickDescription"]
        val amountInStore = element["quantityInStore"].toString()
        val amountInSet = element["quantityInSet"].toString()
        legoAmount.text = "$amountInStore/$amountInSet"
    }

}