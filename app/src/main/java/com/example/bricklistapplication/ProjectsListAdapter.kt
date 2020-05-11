package com.example.bricklistapplication

import LegoDataBaseHelper
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

class ProjectsListAdapter(context: Context?, resource: Int, objects: MutableList<Project>?, var activeOnly: Boolean) : ArrayAdapter<Project>(
    context!!, resource, objects as MutableList<Project>
) {

    private var adapterContext : Context? = context
    private var adapterLayout : Int = resource
    private var dataSource : MutableList<Project>? = objects
    private val inflater: LayoutInflater
            = adapterContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var legoDataBaseHelper: LegoDataBaseHelper? = null


    fun loadDataBase(){
        legoDataBaseHelper = LegoDataBaseHelper(adapterContext!!)
    }

    fun checkBoxHandler(checkBox: CheckBox, position: Int){
        loadDataBase()
        val project = getItem(position)

        if (project!!.getIsActive()!!){
            checkBox.isChecked=true
        }else{
            checkBox.isChecked=false
            if(this.activeOnly) dataSource!!.remove(project)
        }
        checkBox.setOnClickListener {
            if(checkBox.isChecked){
                legoDataBaseHelper?.updateProjectActivation(true,project.getId()!!.toInt())
                getItem(position)?.setIsActive(true)
            } else{
                getItem(position)!!.setIsActive(false)
                if(this.activeOnly)
                    legoDataBaseHelper!!.updateProjectActivation(false,project.getId()!!.toInt())
                    getItem(position)?.setIsActive(false)
                    dataSource!!.remove(project)
            }
            notifyDataSetChanged()
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val retView : View
        val nameText : TextView
        val checkBox : CheckBox

        if (convertView == null) {
            val holder = getItem(position)
            retView = inflater.inflate(adapterLayout, null)
            retView.tag = holder
        } else {
            retView = convertView
        }
        nameText = retView.findViewById<TextView>(R.id.textViewProjectName)
        nameText.text = getItem(position)!!.getName()
        checkBox = retView.findViewById<CheckBox>(R.id.checkBoxActivate)
        checkBoxHandler(checkBox,position)

        return retView
    }


}
