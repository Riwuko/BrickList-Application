package com.example.bricklistapplication.Adapter

import LegoDataBaseHelper
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.bricklistapplication.DownloadTask
import com.example.bricklistapplication.Model.SinglePackageElement
import com.example.bricklistapplication.R

class PackageElementsListAdapter(context: Context?, resource: Int, objects: MutableList<SinglePackageElement>) : ArrayAdapter<SinglePackageElement>(
    context!!, resource, objects) {

    private var adapterContext: Context? = context
    private var adapterLayout: Int = resource
    private val inflater: LayoutInflater = adapterContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var legoDataBaseHelper: LegoDataBaseHelper? = null
    private lateinit var elementRow: LinearLayout
    private lateinit var buttonAdd: Button
    private lateinit var buttonSubtract: Button

    private lateinit var legoAmount: TextView
    private lateinit var legoDesText: TextView
    private lateinit var legoNameText: TextView
    private lateinit var legoImage: ImageView

    private var imageUrlsList: ArrayList<String> = ArrayList()

    fun buttonHandler(button: Button, position: Int, operation:String) {
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

    @SuppressLint("ResourceAsColor")
    fun highlightCollected(position: Int){
        val element = getItem(position)!!
        if(element.getQuantityInStore()==element.getQuantityInSet()) {
            elementRow.setBackgroundColor(Color.parseColor("#4CAF50"))
            buttonAdd.setTextColor(Color.WHITE)
            buttonSubtract.setTextColor(Color.WHITE)
        }
        else{
            elementRow.setBackgroundColor(Color.TRANSPARENT)
            buttonAdd.setTextColor(Color.parseColor("#4CAF50"))
            buttonSubtract.setTextColor(Color.parseColor("#4CAF50"))
        }
    }

    fun loadImageUrlsList(imageCode: String?, colorCode: String?, partCode:String){
        val base1 = "https://www.lego.com/service/bricks/5/2/"
        val base2 = "http://img.bricklink.com/P/"
        val base3 = "https://www.bricklink.com/PL/"
        imageUrlsList.clear()
        imageUrlsList.add(base1 + imageCode)
        imageUrlsList.add(base2 + colorCode + '/' + partCode + ".gif")
        imageUrlsList.add(base3 + partCode + ".jpg")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        loadDataBase()
        val retView: View

        if (convertView == null) {
            val holder = getItem(position)
            retView = inflater.inflate(adapterLayout, null)
            retView.tag = holder
        } else {
            retView = convertView
        }
        elementRow = retView.findViewById<LinearLayout>(R.id.singleRow)

        buttonAdd = retView.findViewById<Button>(R.id.buttonAddElement)
        buttonSubtract = retView.findViewById<Button>(R.id.buttonSubtractElement)
        buttonHandler(buttonAdd, position,"add")
        buttonHandler(buttonSubtract, position,"subtract")

        setAdapterFields(position, retView)
        return retView
    }

    fun getElementImage(position: Int): ByteArray? {
        val element = getItem(position)!!
        var legoImage = legoDataBaseHelper!!.getCodeImage(element.getElementImageCode()!!)
        val name = getItem(position)!!.getElementCode()
        if(legoImage==null) run {
            loadImageUrlsList(element.getElementImageCode(),element.getElementColorCode(),name!!)
            val downloadTask = DownloadTask()
            legoImage = downloadTask.downloadImage(imageUrlsList)
            saveElementImage(legoImage!!,element.getElementImageCode()!!)
            return  legoImage
        }
        return legoImage
    }

    fun saveElementImage(img:ByteArray, imageCode:String){
        legoDataBaseHelper!!.insertImage(imageCode, img)
    }

    fun loadDataBase() {
        legoDataBaseHelper = LegoDataBaseHelper(adapterContext!!)
    }

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
        highlightCollected(position)

        legoNameText.text = element.getElementCode()
        legoDesText.text = element.getElementDescription()

        val image = getElementImage(position)
        try {
            val bm = BitmapFactory.decodeByteArray(image, 0, image!!.size)
            val dm = DisplayMetrics()
            legoImage.minimumHeight = dm.heightPixels
            legoImage.minimumWidth = dm.widthPixels
            legoImage.setImageBitmap(bm)
        }catch (e:Exception){
            Log.e("PEA","Error setting brick picture")
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateAmountField(position: Int){
        val amountInStore = getItem(position)!!.getQuantityInStore().toString()
        val amountInSet = getItem(position)!!.getQuantityInSet().toString()
        legoAmount.text = "$amountInStore/$amountInSet"
        highlightCollected(position)
    }

}