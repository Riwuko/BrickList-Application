package com.example.bricklistapplication

import LegoDataBaseHelper
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_project.*
import java.io.File


class AddProjectActivity : AppCompatActivity() {

    private var usingPrefix: String? = null
    private var activeOnly: Boolean? = null
    private var choosenId: Int? = null
    private var projectName: String? = null
    private var LegoDataBaseHelper: LegoDataBaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        loadApplicationSettings()
        loadDataBase()
    }

    fun buttonAddPackageOnClick(view: View) {
        getIdInputValue()

        val fileUrl = usingPrefix + choosenId + ".xml"
        val filePath: File? = this.getExternalFilesDir(null)
        val fileName = choosenId.toString() + ".xml"
        val fullFilePath = filePath.toString()+fileName

        if (downloadFile(fileUrl,filePath.toString(),fileName)) {
            showNameProjectDialog(this)
            val packageItems = loadPackageData(fullFilePath)
            createProject(packageItems)
        }else{
            var toast =  Toast.makeText(applicationContext,"Nie znaleziono zestawu o podanym ID!",
                Toast.LENGTH_SHORT)
            toast.show()
        }
    }


    fun createProject(packageItems: ArrayList<ArrayList<String>>){
        for (item in packageItems){
            val projectID = LegoDataBaseHelper.generateProjectID()
            val project: Project? = projectName?.let { Project(projectID, it, true) }
            LegoDataBaseHelper.insertProject(project)

            val quantityInStore = 0
            val brickItemType = item[0]
            val brickQuantityInSet = item[1]
            val brickItemId = item[2]
            val brickColor = item[3]

            val brickTypeID = LegoDataBaseHelper.getBrickTypeID(brickItemType)
            val colorID = LegoDataBaseHelper.getColorID(brickColor)
            val brickId = LegoDataBaseHelper.generateBrickID()

            val part = InventoryPart(inventoryID,itemTypeID,itemID,quantityNeeded,0,colorID,extras)
            LegoDataBaseHelper.insertInventoryPart(part)
            LegoDataBaseHelper.close()
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

    fun getIdInputValue(){
        choosenId = editTextPackage.text.toString().toInt()
    }

    fun showNameProjectDialog(c: Context) {
        val taskEditText = EditText(c)
        val dialog: AlertDialog = AlertDialog.Builder(c)
            .setTitle("Nazwij swój projekt")
            .setView(taskEditText)
            .setPositiveButton("Add"
            ) { dialog, which ->
                projectName = taskEditText.text.toString()
            }
            .create()
        dialog.show()
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
