package com.example.bricklistapplication

import LegoDataBaseHelper
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_project.*
import java.time.LocalDateTime
import kotlin.properties.Delegates

class ProjectActivity : AppCompatActivity() {

    private var legoDataBaseHelper: LegoDataBaseHelper? = null
    private var packageElementsList = mutableListOf<SinglePackageElement>()
    private lateinit var projectName : String
    private var projectID by Delegates.notNull<Int>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        loadDataBase()
        loadProjectData()
        loadProjectFields()
        updatePackageElementsListView()
        updateProjectLastAccessed()
    }


    fun loadProjectData(){
        val extras = intent.extras
        projectID = extras?.getInt(AlarmClock.EXTRA_MESSAGE + '1')!!
        projectName = extras.getString(AlarmClock.EXTRA_MESSAGE +'2').toString()
    }

    fun loadProjectFields(){
        textViewProjectName.text = projectName
        packageElementsList = legoDataBaseHelper?.getPackageElements(projectID)!!
    }

    fun updatePackageElementsListView(){
        val packageElementAdapter = PackageElementsListAdapter(this,R.layout.package_elements_row,packageElementsList)
        listViewPackage.adapter = packageElementAdapter

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateProjectLastAccessed(){
        legoDataBaseHelper?.updateLastAccess(projectID, LocalDateTime.now().toString())
    }

    fun loadDataBase(){
        legoDataBaseHelper = LegoDataBaseHelper(applicationContext)
    }
}
