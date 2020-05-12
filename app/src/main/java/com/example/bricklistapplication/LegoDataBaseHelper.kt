import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.bricklistapplication.Model.Project
import com.example.bricklistapplication.Model.SinglePackageElement
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDateTime

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

    private lateinit var queryTable: String
    private lateinit var queryColumn: String
    private lateinit var queryCondition: String

//    DATA-BASE LIVE FUNCTIONS

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



//    DATA-BASE OPERATIONS FUNCTIONS

    fun insertRowToTable(table:String, values:ContentValues){
        val db = this.writableDatabase
        db.insert(table,null,values)
        db.close()
    }

    fun updateRowInTable(table:String,values: ContentValues,strFilter:String){
        val db = this.writableDatabase
        db.update(table,values,strFilter,null)
        db.close()
    }

    fun insertProject(project: Project){
        val values = ContentValues()
        values.put("Name",project.getName())
        values.put("Active",project.getIsActive())
        values.put("LastAccessed", project.getLastAccessed())
        insertRowToTable("Inventories",values)
    }

    fun insertPackageElement(packageElement: SinglePackageElement){
        val values = ContentValues()
        values.put("id",packageElement.getID())
        values.put("InventoryID",packageElement.getProjectID())
        values.put("ItemID",packageElement.getElementID())
        values.put("TypeID",packageElement.getElementTypeID())
        values.put("ColorID",packageElement.getElementColorID())
        values.put("QuantityInSet",packageElement.getQuantityInSet())
        values.put("QuantityInStore",packageElement.getQuantityInStore())

        insertRowToTable("InventoriesParts",values)
    }

    fun insertImage(elementCode: String, img:ByteArray?){
        val values = ContentValues()
        values.put("Image",img)
        val strFilter = "Code='$elementCode'"
        updateRowInTable("Codes",values,strFilter)
    }

    fun insertRowIntoCodes(itemID:Int, colorID:Int, code:Int){
        val values = ContentValues()
        values.put("ItemID",itemID)
        values.put("ColorID",colorID)
        values.put("Code",code)
        insertRowToTable("Codes",values)
    }

    fun performRequestToFirstInteger(query:String): Int {
        val db = this.writableDatabase
        val cursor = db.rawQuery(query,null)
        var found = 0
        if(cursor.moveToFirst()){
            found = Integer.parseInt(cursor.getString(0))
            cursor.close()
        }
        db.close()
        return found
    }

    fun performRequestToFirstString(query:String): String {
        val db = this.writableDatabase
        val cursor = db.rawQuery(query,null)
        var found = ""
        if(cursor.moveToFirst()){
            found = cursor.getString(0)
            cursor.close()
        }
        db.close()
        return found
    }

    fun generateProjectID(): Int {
        val query = "SELECT id FROM Inventories WHERE id = (SELECT MAX(id)  FROM Inventories)"
        return performRequestToFirstInteger(query)+1
    }

    fun generatePackageElementID(): Int {
        val query = "SELECT id FROM InventoriesParts WHERE id = (SELECT MAX(id)  FROM InventoriesParts)"
        return performRequestToFirstInteger(query)+1
    }

    fun getPartID(code:String): Int {
        val query = "SELECT id FROM Parts WHERE  Code='$code'"
        return performRequestToFirstInteger(query)
    }

    fun getPartCode(elementID: Int): String {
        val query = "SELECT Code FROM Parts WHERE id=$elementID"
        return performRequestToFirstString(query)
    }

    fun getPartName(elementID: Int): String{
        val query = "SELECT Name FROM Parts WHERE id=$elementID"
        return performRequestToFirstString(query)
    }


    fun getColorID(elementColor:String): Int {
        val query = "SELECT id FROM Colors WHERE Code='$elementColor'"
        return performRequestToFirstInteger(query)
    }

    fun getColorName(colorID: Int): String{
        val query = "SELECT Name FROM Colors WHERE id=$colorID"
        return performRequestToFirstString(query)
    }

    fun getColorCode(elementID:Int):Int{
        val query = "SELECT Code FROM Colors WHERE id=$elementID"
        return performRequestToFirstInteger(query)
    }

    fun getItemTypeID(elementType:String):Int{
        val query = "SELECT id FROM ItemTypes WHERE Code='$elementType'"
        return performRequestToFirstInteger(query)
    }

    fun getItemTypeCode(typeId: Int): String{
        val query = "SELECT Code FROM ItemTypes WHERE id=$typeId"
        return performRequestToFirstString(query)
    }

    fun getCodeImage(elementCode:String): ByteArray?{
        val query = "SELECT Image FROM Codes WHERE  Code='$elementCode'"

        var img : ByteArray? = null
        val db = this.writableDatabase
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()){
            img = cursor.getBlob(0)
            cursor.close()
        }
        return img
    }

    fun getCodeImageCode(itemID:Int,colorID:Int):Int{
        val query = "SELECT Code FROM Codes WHERE ItemID=$itemID AND ColorID=$colorID"
        return performRequestToFirstInteger(query)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getProjects(activeOnly: Boolean) : MutableList<Project>{
        val projects = mutableListOf<Project>()
        val db = this.writableDatabase

        val query :String
        query = if (activeOnly) "SELECT * FROM Inventories WHERE Active=1 ORDER BY LastAccessed desc"
        else "SELECT * FROM Inventories ORDER BY LastAccessed desc"

        val cursor = db.rawQuery(query,null)
        while(cursor.moveToNext()){
            val name  = cursor.getString(1)
            val id = cursor.getInt(0)
            val active = cursor.getInt(2).toBoolean()
            val curDate = LocalDateTime.now().toString()
            val project  = Project(
                id,
                name,
                active,
                curDate
            )
            projects.add(project)
        }
        cursor.close()
        return projects
    }

    fun getPackageElements(projectID: Int): MutableList<SinglePackageElement> {
        val packageElements = mutableListOf<SinglePackageElement>()
        val query = "SELECT * FROM InventoriesParts WHERE InventoryID=$projectID"

        val db = this.writableDatabase
        val cursor = db.rawQuery(query,null)

        while(cursor.moveToNext()){
            val ID = cursor.getInt(0)
            val projectId = cursor.getInt(1)
            val itemID = cursor.getInt(3)
            val typeID = cursor.getInt(2)
            val quantityInSet = cursor.getInt(4)
            val quantityInStore = cursor.getInt(5)
            val colorID = cursor.getInt(6)
            val colorCode = getColorCode(colorID)
            var imageCode = getCodeImageCode(itemID,colorID)
            if (imageCode==0) {
                imageCode = "${ID}999".toInt()
                insertRowIntoCodes(itemID,colorID,imageCode)
            }
            val itemCode = getPartCode(itemID)
            val itemDescription = getColorName(colorID) + " " + getPartName(itemID)
            val typeCode = getItemTypeCode(typeID)

            val part =
                SinglePackageElement(
                    ID,
                    projectId,
                    itemID,
                    typeID,
                    colorID,
                    quantityInSet,
                    quantityInStore,
                    itemCode,
                    itemDescription,
                    colorCode,
                    imageCode.toString(),
                    typeCode
                )
            packageElements.add(part)
        }
        cursor.close()
        return packageElements
    }

    fun updateProjectActivation(isActive:Boolean, projectID:Int){
        val values = ContentValues()
        values.put("Active",isActive.toInt())
        val strFilter = "id=$projectID"
        updateRowInTable("Inventories",values,strFilter)
    }

    fun updateQuantityInStore(elementID: Int, value: Int){
        val values = ContentValues()
        values.put("QuantityInStore",value)
        val strFilter = "id=$elementID"
        updateRowInTable("InventoriesParts",values,strFilter)
    }

    fun updateLastAccess(elementID:Int, time: String){
        val values = ContentValues()
        values.put("LastAccessed",time)
        val strFilter = "id=$elementID"
        updateRowInTable("Inventories",values,strFilter)
}
    
    fun Int.toBoolean() = if (this==1) true else false
    fun Boolean.toInt() = if (this) 1 else 0
}