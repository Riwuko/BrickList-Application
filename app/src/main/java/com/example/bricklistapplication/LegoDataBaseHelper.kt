package com.example.bricklistapplication

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class LegoDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    private var mDataBase: SQLiteDatabase? = null
    private val mContext: Context? = context

//    DATA-BASE CREATING FUNCTIONS

    fun createDataBase() {

        var temporaryDataBase : SQLiteDatabase?=null
        if (!checkDataBase()) {
            temporaryDataBase = this.readableDatabase
            temporaryDataBase.close()
            copyDataBase()
        }
    }

    private fun checkDataBase(): Boolean {
        var temporaryDataBase : SQLiteDatabase?=null
        val fullPath = DB_PATH + DB_NAME
        try {
            temporaryDataBase = SQLiteDatabase.openDatabase(fullPath, null, SQLiteDatabase.OPEN_READONLY)

        } catch (e: SQLiteException) {
            Log.d("checkDataBase", "DataBase doesn't exist");
        }

        temporaryDataBase?.close()
        return temporaryDataBase != null
    }

    @Throws(IOException::class)
    private fun copyDataBase(): Unit {
        val mInput: InputStream =  mContext!!.getAssets().open(DB_NAME)
        val stringFileName = DB_PATH + DB_NAME
        val mOutput: OutputStream = FileOutputStream(stringFileName)
        val mBuffer = ByteArray(1024)
        var mLength: Int = 0
        while (mInput.read(mBuffer).also({ mLength = it }) > 0) {
            mOutput.write(mBuffer, 0, mLength)
        }
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }


    @Synchronized
    override fun close() {
        if (mDataBase != null)
            mDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        private val DB_NAME = "BrickList.db"
        @SuppressLint("SdCardPath")
        private val DB_PATH = "data/data/com.example.bricklistapplication/databases/"
    }


//    DATA BASE APP OPERATIONS

    fun getProjects(activeOnly: Boolean) : MutableList<Project>{
        val projects = mutableListOf<Project>()
        val db = this.writableDatabase

//        var query = ""
//        if (activeOnly) query = "SELECT * FROM Inventories WHERE Active=1"
//        else  query = "SELECT * FROM Inventories"
//
//        val cursor = db.rawQuery(query,null)
//        while(cursor.moveToNext()){
//            val name  = cursor.getString(1)
//            val id = cursor.getInt(0)
//            val active = cursor.getInt(2).toBoolean()
//            val project  = Project(id,name,active)
//            projects.add(project)
//        }
//        cursor.close()
        return projects
    }

    fun Int.toBoolean() = this==1

}