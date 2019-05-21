package com.epf.museumbook.Modeles;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.epf.museumbook.Modeles.Musee;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "musee_db";
    private static final String MUSEE_TABLE = "MUSEE_TABLE";
    private static final String MUSEE_COL1 = "id";
    private static final String MUSEE_COL2 = "Adresse";
    private static final String MUSEE_COL3 = "CP";
    private static final String MUSEE_COL4 = "Dept";
    private static final String MUSEE_COL5 = "Ferme";
    private static final String MUSEE_COL6 = "FermetureAnnuelle";
    private static final String MUSEE_COL7 = "ID_ext";
    private static final String MUSEE_COL8 = "Nom";
    private static final String MUSEE_COL9 = "PeriodeOuverture";
    private static final String MUSEE_COL10 = "Region";
    private static final String MUSEE_COL11 = "SiteWeb";
    private static final String MUSEE_COL12 = "Ville";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table musee_db (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, addresse TEXT, cp TEXT, dept TEXT, ferme TEXT, fermetureAnnuelle TEXT, ID_ext TEXT, nom TEXT, periodeOuverture TEXT, region TEXT, siteWeb TEXT, ville TEXT )");
        //db.execSQL("CREATE TABLE $MUSEE_TABLE ( " + MUSEE_COL1 + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " + MUSEE_COL2 + " TEXT, " + MUSEE_COL3 + " TEXT, " + MUSEE_COL4 + " INTEGER, " + MUSEE_COL5 + " STRING, " + MUSEE_COL6 + " TEXT, " + MUSEE_COL7 + " TEXT, " + MUSEE_COL8 + " TEXT, " + MUSEE_COL9 + " TEXT, " + MUSEE_COL10 + " TEXT, " + MUSEE_COL11 + " TEXT, " + MUSEE_COL12 + " TEXT )");
        //db.execSQL("CREATE TABLE $JOB_TABLE ( $JOB_COL1 INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,  $JOB_COL2 INTEGER, $JOB_COL3 INTEGER, $JOB_COL4 TEXT, $JOB_COL5 TEXT)")

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MUSEE_TABLE);
        onCreate(db);
    }

public ArrayList<Musee> getMusees(){
        ArrayList<Musee> musees;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + MUSEE_TABLE + " ORDER BY `id` DESC, null", null);
        c.moveToFirst();
        int i = 0;
        musees = new ArrayList<>();
        while (i<c.getCount()){
            boolean ferme = true;
            if(c.getInt(4) == 0){
                ferme = false;
            }
            musees.add(new Musee(c.getString(1), c.getString(2), c.getString(3), ferme, c.getString(5),
                    c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                    c.getString(11)));
            c.moveToNext();
            i++;
        }
        return musees;
    }

    public Musee getMusee(String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        //Get the job corresponding to the job_id passed as a parameter
        Cursor c = db.rawQuery("SELECT * FROM " + MUSEE_TABLE +  " WHERE `ID` = 'ID_ext'",null);
        c.moveToFirst();
        boolean ferme = true;
        if(c.getInt(4) == 0){
            ferme = false;
        }
        return new Musee(c.getString(1), c.getString(2), c.getString(3), ferme, c.getString(5),
                c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                c.getString(11));

    }

    public void insertMusee(Musee musee){
        int ferme = 1;
        if (musee.isFerme()){
            ferme = 0;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MUSEE_COL2, musee.getAdresse());
        contentValues.put(MUSEE_COL3, musee.getCp());
        contentValues.put(MUSEE_COL4, musee.getDept());
        contentValues.put(MUSEE_COL5, ferme);
        contentValues.put(MUSEE_COL6, musee.getFermetureAnnuelle());
        contentValues.put(MUSEE_COL7, musee.getId());
        contentValues.put(MUSEE_COL8, musee.getNom());
        contentValues.put(MUSEE_COL9, musee.getPeriodeOuverture());
        contentValues.put(MUSEE_COL10, musee.getRegion());
        contentValues.put(MUSEE_COL11, musee.getSiteWeb());
        contentValues.put(MUSEE_COL12, musee.getVille());

        db.insert(MUSEE_TABLE, null, contentValues);
    }

    public void updateMusee(Musee musee){

        int ferme = 1;
        if (musee.isFerme()){
            ferme = 0;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MUSEE_COL2, musee.getAdresse());
        contentValues.put(MUSEE_COL3, musee.getCp());
        contentValues.put(MUSEE_COL4, musee.getDept());
        contentValues.put(MUSEE_COL5, ferme);
        contentValues.put(MUSEE_COL6, musee.getFermetureAnnuelle());
        contentValues.put(MUSEE_COL7, musee.getId());
        contentValues.put(MUSEE_COL8, musee.getNom());
        contentValues.put(MUSEE_COL9, musee.getPeriodeOuverture());
        contentValues.put(MUSEE_COL10, musee.getRegion());
        contentValues.put(MUSEE_COL11, musee.getSiteWeb());
        contentValues.put(MUSEE_COL12, musee.getVille());

        String[] args = {musee.getId()};
        db.update(MUSEE_TABLE, contentValues, "`ID_ext` = ?", args);
    }

    public void deleteMusee(Musee musee){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {musee.getId()};
        db.delete(MUSEE_TABLE,"`ID_ext` = ?",args);
    }
}
