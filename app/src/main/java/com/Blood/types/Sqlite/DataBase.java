package com.Blood.types.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.Blood.types.Model.Model;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    public static final String DB_NAME = "Blood.db";
    public static final String DB_TABLE = "BloodType";
    public static final String DB_ID = "ID";
    public static final String NAME = "FullName";
    public static final String NUMBER = "NUMBER";
    public static final String TYPE = "TYPE";
    public static final String LOCATION = "LOCATION";
    public static final int Version = 1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DataBase(Context context) {
        super(context,DB_NAME,null,Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE +
                "(" + DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT   UNIQUE, "
                + NUMBER + " TEXT, " + TYPE + " TEXT, " + LOCATION + " TEXT " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
    }

    public long addData(Model model)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME,model.getName());
        cv.put(NUMBER,model.getNumber());
        cv.put(TYPE,model.getType());
        cv.put(LOCATION,model.getLocation());
        long result = db.insert(DB_TABLE,null,cv);
        db.close();
        return result;
    }

    public ArrayList<Model> getAllRecords(String orderBy){
        ArrayList<Model> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DB_TABLE + " WHERE " + orderBy;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do{
                Model model = new Model(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                records.add(model);
            }while (cursor.moveToNext());
        }
        db.close();
        return records;
    }
}
