package com.example.bricklistapplication

import LegoDataBaseHelper
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

class ProjectsListAdapter(context: Context?, resource: Int, objects: MutableList<Project>?, var active_only: Boolean) : ArrayAdapter<Project>(
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
        val project = getProjectData(position)
        checkBox.setOnClickListener {
            if(checkBox.isChecked){
                legoDataBaseHelper?.activateProject(project["id"]!!.toInt())
                getItem(position)?.setIsActive(true)
            } else{
                getItem(position)!!.setIsActive(false)
                if(this.active_only)
                    dataSource!!.removeAt(position)
                legoDataBaseHelper!!.activateProject(project["id"]!!.toInt())
            }
            notifyDataSetChanged()
        }
    }

    fun getProjectData(position: Int): MutableMap<String, String> {
        val data = mutableMapOf<String,String>()
        data.put("id",getItem(position)?.getId()!!.toString())
        data.put("name",getItem(position)?.getName()!!)
        data.put("isActive",getItem(position)?.getIsActive()!!.toString())
        data.put("lastAccessed",getItem(position)?.getLastAccessed()!!)
        return data
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val project = getProjectData(position)
        val retView : View
        val nameText : TextView
        val checkBox : CheckBox

        if (convertView == null) {
            val holder = Project(project["id"]!!.toInt(), project["name"]!!, project["isActive"]!!.toBoolean(),project["lastAccessed"]!!)
            retView = inflater.inflate(adapterLayout, null)
            retView.tag = holder
        } else {
            retView = convertView
        }
        nameText = retView.findViewById<TextView>(R.id.textViewProjectName)
        checkBox = retView.findViewById<CheckBox>(R.id.checkBoxActivate)
        checkBoxHandler(checkBox,position)
        nameText.text = project["name"]
        return retView
    }


}
