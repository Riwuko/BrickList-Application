package com.example.bricklistapplication

import LegoDataBaseHelper
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi

class PackageElementsListAdapter(context: Context?, resource: Int, objects: MutableList<SinglePackageElement>) : ArrayAdapter<SinglePackageElement>(
    context!!, resource, objects as MutableList<SinglePackageElement>) {

    private var adapterContext: Context? = context
    private var adapterLayout: Int = resource
    private val inflater: LayoutInflater = adapterContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var legoDataBaseHelper: LegoDataBaseHelper? = null

    private lateinit var legoAmount: TextView
    private lateinit var legoDesText: TextView
    private lateinit var legoNameText: TextView
    private lateinit var legoImage: ImageView

    private var imageUrlsList: ArrayList<String> = ArrayList()

    fun buttonHandler(button: Button, position: Int, operation:String) {
        loadDataBase()
        button.setOnClickListener {
            changeAmountValue(operation, position)
            updateAmountField(position)
            notifyDataSetChanged()
        }
    }

    fun changeAmountValue(operation: String, position: Int) {
        val element=getItem(position)!!
        var value:Int = element.getQuantityInStore()!!.toInt()
        var success: Boolean = false
        if (operation=="add"){
            if ((value+1) <= element.getQuantityInSet()!!.toInt()){
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun loadImageUrlsList(brickID: Int?, colorID: Int?, brickCode:String){
        val base1 = "https://www.lego.com/service/bricks/5/2/"
        val base2 = "http://img.bricklink.com/P/"
        val base3 = "https://www.bricklink.com/PL/"
        imageUrlsList.clear()
        imageUrlsList.add(base1 + brickCode)
        imageUrlsList.add(base2 + colorID + '/' + brickCode + ".gif")
        imageUrlsList.add(base3 + brickCode + ".jpg")
        System.lineSeparator()
        print(imageUrlsList)
        System.lineSeparator()
        System.lineSeparator()
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getElementImage(position: Int): ByteArray? {
        val element = getItem(position)!!
        val code = legoDataBaseHelper!!.getCodeImageCode(element.getElementID()!!,element.getElementColorID()!!)
        val photo = legoDataBaseHelper!!.getCodeImage(code)
        val name = getItem(position)!!.getElementCode()
        if(photo==null) run {
            loadImageUrlsList(element.getElementID(),element.getElementColorID(),name!!)
            val downloadTask: DownloadTask = DownloadTask()
            return downloadTask.downloadImage(imageUrlsList)
        }
        return null
    }

    fun loadDataBase() {
        legoDataBaseHelper = LegoDataBaseHelper(adapterContext!!)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    fun setAdapterFields(position: Int, retView: View) {
        val element = getItem(position)!!
        legoNameText = retView.findViewById<TextView>(R.id.textViewLegoName)
        legoDesText = retView.findViewById<TextView>(R.id.textViewLegoDes)
        legoAmount = retView.findViewById<TextView>(R.id.textViewLegoAmount)
        legoImage= retView.findViewById<ImageView>(R.id.imageViewLegoPicture)

        val amountInStore = element.getQuantityInStore().toString()
        val amountInSet = element.getQuantityInSet().toString()
        legoAmount.text = "$amountInStore/$amountInSet"
        legoNameText.text = element.getElementCode()
        legoDesText.text = element.getElementDescription()

        println(element.toString())
        System.lineSeparator()
        System.lineSeparator()
        val image = getElementImage(position)
        val bm = BitmapFactory.decodeByteArray(image, 0, image!!.size)
        val dm = DisplayMetrics()
        legoImage.minimumHeight = dm.heightPixels
        legoImage.minimumWidth = dm.widthPixels
        legoImage.setImageBitmap(bm)
    }

    @SuppressLint("SetTextI18n")
    fun updateAmountField(position: Int){
        val amountInStore = getItem(position)!!.getQuantityInStore().toString()
        val amountInSet = getItem(position)!!.getQuantityInSet().toString()
        legoAmount.text = "$amountInStore/$amountInSet"
    }



}