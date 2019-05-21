package com.epf.museumbook.Modeles;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private String MUSEE_TABLE = "MUSEE_TABLE";
    private final String MUSEE_COL1 = "id";
    private final String MUSEE_COL2 = "Adresse";
    private final String MUSEE_COL2 = "CP";
    private final String MUSEE_COL3 = "Dept";
    private final String MUSEE_COL4 = "Ferme";
    private final String MUSEE_COL5 = "FermetureAnnuelle";
    private final String MUSEE_COL6 = "ID";
    private final String MUSEE_COL7 = "Nom";
    private final String MUSEE_COL8 = "PeriodeOuverture";
    private final String MUSEE_COL9 = "Region";
    private final String MUSEE_COL10 = "SiteWeb";
    private final String MUSEE_COL11 = "Ville";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE $MUSEE_TABLE ( $MUSEE_COL1 TEXT, $MUSEE_COL2 TEXT, $MUSEE_COL3 TEXT, $MUSEE_COL4 BOOLEAN, $MUSEE_COL5 STRING, $MUSEE_COL6 STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
