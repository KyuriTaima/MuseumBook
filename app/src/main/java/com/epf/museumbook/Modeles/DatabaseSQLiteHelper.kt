package com.epf.museumbook.Modeles

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

/*
 * This database saves all the Museum information, including the pictures related to it
 * The primary key of the museum table is the rank and not the ID because we could confuse it with the ID
 * sent by the API
 * The pictures are not actually stored in the database, only the URL of the pictures
 */

class DatabaseSQLiteHelper : SQLiteOpenHelper {

    //Set all the database column names and table names into constants
    private val MUSEUM_TABLE = "MUSEUM_TABLE"
    private val MUSEUM_COL1 = "rank"
    private val MUSEUM_COL2 = "Adresse"
    private val MUSEUM_COL3 = "CP"
    private val MUSEUM_COL4 = "Dept"
    private val MUSEUM_COL5 = "Ferme"
    private val MUSEUM_COL6 = "FermetureAnnuelle"
    private val MUSEUM_COL7 = "id"
    private val MUSEUM_COL8 = "Nom"
    private val MUSEUM_COL9 = "PeriodeOuverture"
    private val MUSEUM_COL10 = "Region"
    private val MUSEUM_COL11 = "SiteWeb"
    private val MUSEUM_COL12 = "Ville"

    private val IMAGE_TABLE = "IMAGE_TABLE"
    private val IMAGE_COL1 = "id"
    private val IMAGE_COL2 = "url"
    private val IMAGE_COL3 = "museum_id"


    //This constructor is very useful, if you change anything in the database architecture, column name, table name, add a new table
    // Or just want to empty all the data but keep the architecture, just increment the version number by one
    // It is the last parameter passed in as an Integer in the constructor
    constructor(context: Context) : super(context, "general_db", null, 11) {
        val db: SQLiteDatabase = this.writableDatabase
    }


    //Set all tables and all column names, if you want to change the name of a table or a column, change the constant value
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $MUSEUM_TABLE ( $MUSEUM_COL1 INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,  $MUSEUM_COL2 TEXT, $MUSEUM_COL3 TEXT, $MUSEUM_COL4 TEXT, $MUSEUM_COL5 INTEGER, $MUSEUM_COL6 TEXT, $MUSEUM_COL7 TEXT, $MUSEUM_COL8 TEXT, $MUSEUM_COL9 TEXT, $MUSEUM_COL10 TEXT, $MUSEUM_COL11 TEXT, $MUSEUM_COL12 )")
        db.execSQL("CREATE TABLE $IMAGE_TABLE ($IMAGE_COL1 INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, $IMAGE_COL2 TEXT, $IMAGE_COL3 INTEGER)")
    }

    //onUpgrade method will be called when you increment the version number of the constructor
    //Drops all tables on upgrade to reset them
    //Calls onCreate at the end to reinitialize the database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $MUSEUM_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $IMAGE_TABLE")
        onCreate(db)
    }

    /*
     * This function returns an ArrayList of type Musee containing all the musee in the database
     */
    fun getMusees(): ArrayList<Musee> {
        val musees: ArrayList<Musee> = ArrayList()  //create an ArrayList of Musee
        val db = this.readableDatabase  //Create an instance of the database
        val c = db.rawQuery("SELECT * FROM $MUSEUM_TABLE ORDER BY `rank` DESC, null", null) //get all the musee ordered by rank desc
        c.moveToFirst() //Set cursor to the first line
        var i = 0
        while (i < c.count) {   //check if the cursor is finished

            //This code transforms the int parameter "ferme" into a boolean for the Musee object
            //1 -> true
            //0 -> false
            var ferme = true
            if (c.getInt(4) == 0) {
                ferme = false
            }
            //Build a Musee object with the data from the cursor and add it to the museum list
            musees.add(Musee(c.getInt(0),c.getString(1), c.getString(2), c.getString(3), ferme, c.getString(5),
                    c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                    c.getString(11)))

            //Get all the images from the museum
            val c1 = db.rawQuery("SELECT * FROM $IMAGE_TABLE WHERE `museum_id` IS '${musees.get(musees.size-1).rank}'", null)
            c1.moveToFirst()
            while(!c1.isAfterLast){
                //get the last museum in the musees list
                musees.get(musees.size-1).addImageUrl(c1.getString(1))
                c1.moveToNext()
            }
            c.moveToNext()
            i++
        }
        //return the museum list
        return musees
    }

    //Returns all the images url of a museum
    fun getImages(musee: Musee):ArrayList<String>{
        val db = this.readableDatabase
        var  urlList:ArrayList<String> = arrayListOf()

        val c = db.rawQuery("SELECT * FROM $IMAGE_TABLE WHERE `museum_id` = '${musee.rank}'",null)
        c.moveToFirst()
        while (!c.isAfterLast) {
            urlList.add(c.getString(1))
            c.moveToNext()
        }
        return urlList

    }

    //Inserts images into a museum
    fun insertImages(urlList:ArrayList<String>, rank:Int){
        val db = this.writableDatabase
        var i = 0
        while(i<urlList.size) {
            val contentValues = ContentValues()
            contentValues.put(IMAGE_COL2, urlList.get(i))
            contentValues.put(IMAGE_COL3, rank)
            db.insert(IMAGE_TABLE, null, contentValues)
            i++
        }

    }

    //Returns a museum based on the rank
    fun getMusee(rank: Int): Musee {
        val db = this.readableDatabase
        //Get the job corresponding to the job_id passed as a parameter
        val c = db.rawQuery("SELECT * FROM $MUSEUM_TABLE WHERE `rank` IS $rank", null)
        c.moveToFirst()
        var ferme = true
        if (c.getInt(4) == 0) {
            ferme = false
        }
        return Musee(c.getInt(0),c.getString(1), c.getString(2), c.getString(3), ferme, c.getString(5),
                c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                c.getString(11))

    }

    //Inserts a museum into the database
    fun insertMusee(musee: Musee) {
        //check if the museum already exists, if it does, call update
        val db1 = this.readableDatabase
        var args = arrayOf(musee.id);
        val c1 = db1.rawQuery("SELECT * FROM $MUSEUM_TABLE WHERE `id` IS ?", args)
        if(c1.count == 1){
            updateMusee(musee)
        }else {
            var ferme = 1
            if (musee.isFerme) {
                ferme = 0
            }
            val db = this.writableDatabase
            val contentValues = ContentValues()
            //contentValues.put(MUSEUM_COL1, musee.rank)
            contentValues.put(MUSEUM_COL2, musee.adresse)
            contentValues.put(MUSEUM_COL3, musee.cp)
            contentValues.put(MUSEUM_COL4, musee.dept)
            contentValues.put(MUSEUM_COL5, ferme)
            contentValues.put(MUSEUM_COL6, musee.fermetureAnnuelle)
            contentValues.put(MUSEUM_COL7, musee.id)
            contentValues.put(MUSEUM_COL8, musee.nom)
            contentValues.put(MUSEUM_COL9, musee.periodeOuverture)
            contentValues.put(MUSEUM_COL10, musee.region)
            contentValues.put(MUSEUM_COL11, musee.siteWeb)
            contentValues.put(MUSEUM_COL12, musee.ville)

            db.insert(MUSEUM_TABLE, null, contentValues)
            if(musee.imagesUrl != null) {
                insertImages(musee.imagesUrl, getLastMuseumRank())
            }
        }
    }

    //updates a museum
    fun updateMusee(musee: Musee) {

        var ferme = 1
        if (musee.isFerme) {
            ferme = 0
        }
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MUSEUM_COL2, musee.adresse)
        contentValues.put(MUSEUM_COL3, musee.cp)
        contentValues.put(MUSEUM_COL4, musee.dept)
        contentValues.put(MUSEUM_COL5, ferme)
        contentValues.put(MUSEUM_COL6, musee.fermetureAnnuelle)
        contentValues.put(MUSEUM_COL7, musee.id)
        contentValues.put(MUSEUM_COL8, musee.nom)
        contentValues.put(MUSEUM_COL9, musee.periodeOuverture)
        contentValues.put(MUSEUM_COL10, musee.region)
        contentValues.put(MUSEUM_COL11, musee.siteWeb)
        contentValues.put(MUSEUM_COL12, musee.ville)

        val args = arrayOf(musee.id)
        db.update(MUSEUM_TABLE, contentValues, "`id` = ?", args)
        if(musee.imagesUrl != null) {
            insertImages(musee.imagesUrl, musee.rank)
        }
    }

    //gives the last rank of the museum table
    fun getLastMuseumRank():Int {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM $MUSEUM_TABLE ORDER BY `rank` DESC, null", null)
        if(c.count == 0){
            return 0
        }
        else{
            c.moveToFirst()
            return c.getInt(0)
        }
    }
}