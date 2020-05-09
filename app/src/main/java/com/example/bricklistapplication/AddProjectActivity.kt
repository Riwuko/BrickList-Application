package com.example.bricklistapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_project.*
import java.io.File


class AddProjectActivity : AppCompatActivity() {

    private var usingPrefix: String? = null
    private var activeOnly: Boolean? = null
    private var choosenId: Int? = null
    private var projectName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        loadApplicationSettings()

    }

    @SuppressLint("SdCardPath")
    fun buttonAddPackageOnClick(view: View) {
        getInputData()

        val fileUrl = usingPrefix + choosenId + ".xml"
        //val filePath = "/data/data/com.example.bricklistapplication/downloaded_xml/"
        val filePath: File? = this.getExternalFilesDir(null)
        val fileName = choosenId.toString() + ".xml"
        val downloadTask: DownloadTask = DownloadTask(fileUrl, fileName, filePath)
        if (downloadTask.downloadFromURL()) {
            var xmlHandler: XMLHandler = XMLHandler(filePath.toString()+fileName)
            xmlHandler.readXML(filePath.toString() + fileName)
        }
        //showAddItemDialog(this)
    }

    fun getInputData(){
        choosenId = editTextPackage.text.toString().toInt()

    }
    fun showAddItemDialog(c: Context) {
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
