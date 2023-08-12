package com.example.moisoftqr.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String db_name = "MoiSoftQR";
    private static final int version = 1;

    private SQLiteDatabase db;

    private String qrtable = "create table qrtable(Qid INTEGER PRIMARY KEY AUTOINCREMENT,QRvalue TEXT)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(qrtable);
    }

    private void open() {
        db = getReadableDatabase();
    }

    public DataBaseHandler(Context context) {
        super(context, db_name, null, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertQRDetails(String QRvalue) {
        open();
        ContentValues cval = new ContentValues();
        cval.put("QRvalue", QRvalue);
        try {
            db.insertOrThrow("qrtable", null, cval);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean delete() {
        open();
        try {
            db.execSQL("delete from qrtable");
            return true;
        } catch (Exception e) {
            Log.e("sndb", e.getMessage());
            return false;
        }
    }

    public ArrayList<HashMap<String, String>> getQRDetails() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        open();
        Cursor cursor = db.rawQuery("select * from qrtable order by qid desc", null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("qid", cursor.getString(0));
                    map.put("QRvalue", cursor.getString(1));

                    list.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }


}
