package com.example.bricklistapplication

import LegoDataBaseHelper
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_project.*
import java.io.File
import java.time.LocalDateTime
import kotlin.collections.ArrayList


class AddProjectActivity : AppCompatActivity() {

    private var usingPrefix: String? = null
    private var activeOnly: Boolean? = null
    private var choosenId: Int? = null
    private var projectName: String? = null
    private var LegoDataBaseHelper: LegoDataBaseHelper? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        loadApplicationSettings()
        loadDataBase()

        buttonAddPackage.setOnClickListener(){
            addNewProject()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewProject() {
        if(getInputValues()) {
            val fileUrl = usingPrefix + choosenId + ".xml"
            val filePath: File? = this.getExternalFilesDir(null)
            val fileName = choosenId.toString() + ".xml"
            val fullFilePath = filePath.toString() + fileName

            if (downloadFile(fileUrl, fileName, filePath.toString())) {
                val packageItems = loadPackageData(fullFilePath)
                createProject(packageItems)
                createToast("Dodano projekt $projectName")

            } else createToast("Nie znaleziono zestawu o podanym ID!")
        }else createToast("Wprowadzono błędne dane!")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createProject(packageItems: ArrayList<ArrayList<String>>){
        val projectID = LegoDataBaseHelper?.generateProjectID()
        val project:Project?
        println("\n\nProjectID: "+projectID)
        println("ProjectName: "+projectName+"\n\n")
        project = Project(projectID!!.toInt(),projectName.toString(),true, LocalDateTime.now().toString())
        LegoDataBaseHelper?.insertProject(project)

        for (item in packageItems){
            val brickQuantityInStore = 0
            val brickItemType = item[0]
            val brickQuantityInSet = item[1].toInt()
            val brickItemId = item[2]
            val brickColor = item[3]

            val elementID = LegoDataBaseHelper?.generatePackageElementID()
            val brickTypeID = LegoDataBaseHelper?.getItemTypeID(brickItemType)
            val brickColorID = LegoDataBaseHelper?.getColorID(brickColor)
            val brickID = LegoDataBaseHelper?.getPartID(brickItemId)
            val brickCode = LegoDataBaseHelper?.getPartCode(elementID!!)
            val brickDescription = LegoDataBaseHelper!!.getColorName(brickColorID!!) + " " +
                    LegoDataBaseHelper!!.getPartName(elementID!!)

            try {
                val packageElement = SinglePackageElement(
                    elementID,
                    projectID,
                    brickID!!.toInt(),
                    brickTypeID!!.toInt(),
                    brickColorID.toInt(),
                    brickQuantityInSet,
                    brickQuantityInStore,
                    brickCode!!,
                    brickDescription
                )
                LegoDataBaseHelper?.insertPackageElement(packageElement)
                LegoDataBaseHelper?.close()
            }catch (ex:Exception) {
                Log.e("ER","Error creating package element: AddProjectActivity::createProject")
            }
        }
    }

    fun loadPackageData(filePath: String): ArrayList<ArrayList<String>> {
        val xmlHandler = XMLHandler(filePath)
        return xmlHandler.readXML(filePath)
    }

    fun downloadFile(fileUrl: String,fileName: String,filePath: String): Boolean {
        val downloadTask = DownloadTask(fileUrl, fileName, filePath)
        return downloadTask.downloadFromURL()
    }

    fun getInputValues(): Boolean {
        choosenId = editTextPackage.text.toString().toInt()
        projectName = editTextName.text.toString()
        if (choosenId!!.toInt() >=0 && !projectName.isNullOrEmpty() ){
            return true
        }
        return false
    }

    fun createToast(message:String){
        val toast = Toast.makeText(
            applicationContext, message,
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun loadDataBase(){
        LegoDataBaseHelper = LegoDataBaseHelper(applicationContext)
    }

    fun loadApplicationSettings() {
        val sharedPref = getSharedPreferences("Options_prefs", 0)
        if(sharedPref.contains("prefix")) {
            usingPrefix = sharedPref.getString("prefix", "")
        }else usingPrefix = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
        if(sharedPref.contains("activeOnly")) {
            activeOnly = sharedPref.getString("activeOnly", "")?.toBoolean()
        } else activeOnly = false
    }



}
