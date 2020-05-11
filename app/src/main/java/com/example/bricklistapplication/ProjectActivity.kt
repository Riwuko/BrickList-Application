package com.example.bricklistapplication

import LegoDataBaseHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import kotlinx.android.synthetic.main.activity_project.*
import kotlin.properties.Delegates

class ProjectActivity : AppCompatActivity() {

    private var LegoDataBase: LegoDataBaseHelper? = null
    private var packageElementsList = mutableListOf<SinglePackageElement>()
    private lateinit var projectName : String
    private var projectID by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        loadDataBase()
        loadProjectData()
        loadProjectFields()
        updatePackageElementsListView()

    }

    fun loadProjectData(){
        val extras = intent.extras
        projectID = extras?.getInt(AlarmClock.EXTRA_MESSAGE + '1')!!
        projectName = extras.getString(AlarmClock.EXTRA_MESSAGE +'2').toString()
    }

    fun loadProjectFields(){
        textViewProjectName.text = projectName
        packageElementsList = LegoDataBase?.getPackageElements(projectID)!!
    }

    fun updatePackageElementsListView(){
        val packageElementAdapter = PackageElementsListAdapter(this,R.layout.package_elements_row,packageElementsList)
        listViewPackage.adapter = packageElementAdapter

    }


    fun loadDataBase(){
        LegoDataBase = LegoDataBaseHelper(applicationContext)
    }
}
