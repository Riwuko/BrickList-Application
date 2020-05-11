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

    fun loadDataBase() {
        legoDataBaseHelper = LegoDataBaseHelper(adapterContext!!)
    }

    fun getSingleElementData(position: Int): MutableMap<String, String> {
        loadDataBase()
        val data = mutableMapOf<String, String>()
        val singleElement = getItem(position)

        if(singleElement?.getElementID()!!>0) {
            data.put("brickName", legoDataBaseHelper!!.getItemCode(getItem(position)!!.getElementID()!!))
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

        val buttonAdd = retView.findViewById<Button>(R.id.buttonSubtractElement)
        val buttonSubtract = retView.findViewById<Button>(R.id.buttonAddElement)
        buttonAddHandler(buttonAdd, position)
        buttonSubstractHandler(buttonSubtract, position)

        val singleElement = getSingleElementData(position)
        setAdapterFields(singleElement, retView)
        return retView
    }

    @SuppressLint("SetTextI18n")
    fun setAdapterFields(element: MutableMap<String, String>, retView: View) {
        val legoNameText = retView.findViewById<TextView>(R.id.textViewLegoName)
        val legoDesText = retView.findViewById<TextView>(R.id.textViewLegoDes)
        val legoAmount = retView.findViewById<TextView>(R.id.textViewLegoAmount)

        legoNameText.text = element["brickName"]
        legoDesText.text = element["brickDescription"]
        val amountInStore = element["quantityInStore"].toString()
        val amountInSet = element["quantityInSet"].toString()
        legoAmount.text = "$amountInStore/$amountInSet"
    }


    fun buttonAddHandler(button: Button, position: Int) {
        loadDataBase()
    }

    fun buttonSubstractHandler(button: Button, position: Int) {
        loadDataBase()

    }

}