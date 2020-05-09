package com.example.bricklistapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val DBHelper = LegoDataBaseHelper(applicationContext)
        DBHelper.createDataBase()


        var projects = mutableListOf<Project>()
        listViewProjects.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, PackageActivity::class.java)
                intent.putExtra(EXTRA_MESSAGE + '1', projects.get(position).getId())
                intent.putExtra(EXTRA_MESSAGE + '2', projects.get(position).getName())
                startActivity(intent)
            }

        buttonAddProject.setOnClickListener{
            startActivity(Intent(this, AddProjectActivity::class.java))
        }

        buttonSettings.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}
