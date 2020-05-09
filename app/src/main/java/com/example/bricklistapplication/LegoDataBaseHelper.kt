import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import com.example.bricklistapplication.Project
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class LegoDataBaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DB_NAME,
        null,
        DB_VERSION
    ) {
    private var mDataBase: SQLiteDatabase? = null
    private val mContext: Context
    private var mNeedUpdate = false

    @Throws(IOException::class)
    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile =
                File(DB_PATH + DB_NAME)
            if (dbFile.exists()) dbFile.delete()
            copyDataBase()
            mNeedUpdate = false
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile =
            File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        if (!checkDataBase()) {
            this.readableDatabase
            close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput =
            mContext.assets.open(DB_NAME)
        val mOutput: OutputStream =
            FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) mOutput.write(mBuffer, 0, mLength)
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    @Throws(SQLException::class)
    fun openDataBase(): Boolean {
        mDataBase = SQLiteDatabase.openDatabase(
            DB_PATH + DB_NAME,
            null,
            SQLiteDatabase.CREATE_IF_NECESSARY
        )
        return mDataBase != null
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null) mDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {}
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (newVersion > oldVersion) mNeedUpdate = true
    }

    companion object {
        private const val DB_NAME = "BrickList.db"
        private var DB_PATH = ""
        private const val DB_VERSION = 1
    }

    init {
        DB_PATH =
            if (Build.VERSION.SDK_INT >= 17) context.applicationInfo.dataDir + "/databases/" else "/data/data/" + context.packageName + "/databases/"
        println(DB_PATH)
        println("\n\n\n\n")
        mContext = context
        copyDataBase()
        this.readableDatabase
    }


    fun getProjects(activeOnly: Boolean) : MutableList<Project>{
        val projects = mutableListOf<Project>()
        val db = this.writableDatabase

        var query = ""
        if (activeOnly) query = "SELECT * FROM Inventories WHERE Active=1"
        else  query = "SELECT * FROM Inventories"

        val cursor = db.rawQuery(query,null)
        while(cursor.moveToNext()){
            val name  = cursor.getString(1)
            val id = cursor.getInt(0)
            val active = cursor.getInt(2).toBoolean()
            val project  = Project(id,name,active)
            projects.add(project)
        }
        cursor.close()
        return projects
    }

    fun Int.toBoolean() = this==1
}