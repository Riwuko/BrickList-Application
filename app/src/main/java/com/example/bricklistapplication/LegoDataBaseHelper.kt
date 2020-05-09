package com.example.bricklistapplication

import android.annotation.SuppressLint
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class LegoDataBaseHelper(private val mContext: Context?) : SQLiteOpenHelper(mContext, DB_NAME, null, 1) {

    private var mDataBase: SQLiteDatabase? = null

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

    @Throws(SQLException::class)
    fun openDataBase() {
        val stringPath = DB_PATH + DB_NAME
        mDataBase = SQLiteDatabase.openDatabase(stringPath, null, SQLiteDatabase.OPEN_READONLY)

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


}