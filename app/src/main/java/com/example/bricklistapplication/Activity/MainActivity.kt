package com.example.bricklistapplication.Activity

import LegoDataBaseHelper
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import com.example.bricklistapplication.Adapter.ProjectsListAdapter
import com.example.bricklistapplication.Model.Project
import com.example.bricklistapplication.R
import com.example.bricklistapplication.SettingsHandler
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
        updateProjectsListView()
        setButtons()
        setOnClickProject()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume(){
        super.onResume()
        loadApplicationSettings()
        loadDataBase()
        updateProjectsListView()
        setButtons()
        setOnClickProject()
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateProjectsListView(){
        projectsList = LegoDataBase?.getProjects(activeOnly)!!
        val projectsListAdapter =
            ProjectsListAdapter(
                this,
                R.layout.projects_list_row,
                projectsList,
                activeOnly
            )
        listViewProjects.adapter = projectsListAdapter
    }

    fun loadDataBase(){
        LegoDataBase = LegoDataBaseHelper(applicationContext)
    }

    fun loadApplicationSettings() {
        val sharedPref = getSharedPreferences("Options_prefs", 0)
        val settingsHandler = SettingsHandler()
        activeOnly = settingsHandler.getActiveOnly(sharedPref)
        usingPrefix = settingsHandler.getUsingPrefix(sharedPref)
    }



}
