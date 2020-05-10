package com.example.bricklistapplication

import LegoDataBaseHelper
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var usingPrefix: String = ""
    private var activeOnly: Boolean = false
    private var LegoDataBase: LegoDataBaseHelper? = null
    private var projectsList = mutableListOf<Project>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadApplicationSettings()
        loadDataBase()
        updateListViewProjects()
        setButtons()
        setOnClickProject()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume(){
        super.onResume()
        loadDataBase()
        updateListViewProjects()

    }

    fun setOnClickProject(){
        listViewProjects.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, ProjectActivity::class.java)
                intent.putExtra(EXTRA_MESSAGE + '1', projectsList.get(position).getId())
                intent.putExtra(EXTRA_MESSAGE + '2', projectsList.get(position).getName())
                startActivity(intent)
            }
    }

    fun setButtons(){
        buttonAddProject.setOnClickListener{
            startActivity(Intent(this, AddProjectActivity::class.java))
        }

        buttonSettings.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    fun loadDataBase(){
        LegoDataBase = LegoDataBaseHelper(applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateListViewProjects(){
        projectsList = LegoDataBase?.getProjects(activeOnly)!!
        val projectsListAdapter = ProjectsListAdapter(this, R.layout.projects_list_row,projectsList,activeOnly)
        listViewProjects.adapter = projectsListAdapter
    }


    fun loadApplicationSettings() {
        val sharedPref = getSharedPreferences("Options_prefs", 0)
        if(sharedPref.contains("prefix")) {
            usingPrefix = sharedPref.getString("prefix", "").toString()
        } else usingPrefix = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
        if(sharedPref.contains("activeOnly")) {
            activeOnly = sharedPref.getString("activeOnly", "")?.toBoolean()!!
        } else activeOnly = false
    }



}
