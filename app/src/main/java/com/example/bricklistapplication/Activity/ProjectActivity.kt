package com.example.bricklistapplication.Activity

import LegoDataBaseHelper
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.bricklistapplication.Adapter.PackageElementsListAdapter
import com.example.bricklistapplication.Model.SinglePackageElement
import com.example.bricklistapplication.R
import com.example.bricklistapplication.XMLHandler
import kotlinx.android.synthetic.main.activity_project.*
import java.io.File
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
        exportXMLHandler()
    }
    fun createToast(message:String){
        val toast = Toast.makeText(
            applicationContext, message,
            Toast.LENGTH_LONG
        )
        toast.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun exportXMLHandler(){
        buttonGetXML.setOnClickListener {
            val filePath: File? = this.getExternalFilesDir(null)
            val fileName = "EXPORT_$projectName.xml"
            val fullFilePath = filePath.toString() + fileName

            createToast("Pobieranie...")
            val xmlHandler = XMLHandler()
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val success = xmlHandler.exportXML(
                    packageElementsList as ArrayList<SinglePackageElement>, fullFilePath
                )
                if (success) createToast("Pobrano XML: ${fullFilePath}")
                else createToast("Nie udało się pobrać XML")
            }
        }
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
        val packageElementAdapter =
            PackageElementsListAdapter(
                this,
                R.layout.package_elements_row,
                packageElementsList
            )
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
