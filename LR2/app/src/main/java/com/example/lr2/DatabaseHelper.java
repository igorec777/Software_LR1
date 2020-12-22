package com.example.lr2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lr2.Models.Train;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;

    public static final String ACTIVITY_TABLE = "trains";

    public static final String TABLE_CREATE = "CREATE TABLE "+ ACTIVITY_TABLE +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, " +
            "color INTEGER, " +
            "prepTime INTEGER, " +
            "workTime INTEGER, " +
            "freeTime INTEGER, " +
            "cycleNum INTEGER, " +
            "setNum INTEGER, " +
            "freeOfSet INTEGER);";

    public DatabaseHelper(Context context, String name, int version)
    {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_TABLE);
        onCreate(db);
        db.setVersion(1);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.setVersion(newVersion);
    }

    public void InsertRecord(String title, int color, int prepTime, int workTime, int freeTime, int cycleNum, int setNum, int freeOfSet)
    {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("color", color);
        newValues.put("title", title);
        newValues.put("prepTime", prepTime);
        newValues.put("workTime", workTime);
        newValues.put("freeTime", freeTime);
        newValues.put("cycleNum", cycleNum);
        newValues.put("setNum", setNum);
        newValues.put("freeOfSet", freeOfSet);

        result = db.insert("trains", null, newValues);

        db.close();
        this.close();
    }

    public void ChangeRecord(int id, String title, int color, int prepTime, int workTime, int freeTime, int cycleNum, int setNum, int freeOfSet)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("color", color);
        newValues.put("title", title);
        newValues.put("prepTime", prepTime);
        newValues.put("workTime", workTime);
        newValues.put("freeTime", freeTime);
        newValues.put("cycleNum", cycleNum);
        newValues.put("setNum", setNum);
        newValues.put("freeOfSet", freeOfSet);
        db.update(ACTIVITY_TABLE, newValues, "id = ?", new String[]{Integer.toString(id)});
        db.close();
        this.close();
    }

    public int CountRecords()
    {
        int result;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rowsList = db.query("trains", null, null, null, null, null, null);
        result = rowsList.getCount();
        db.close();
        this.close();
        return result;
    }

    public void DeleteAllRecords(String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
        db.close();
        this.close();
    }

    public void DeleteRecordById(String tableName, int id)
    {
        String query = "DELETE FROM " + tableName + " WHERE id == " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(query);
        db.close();
        this.close();
    }

    public ArrayList<Train> GetAllRecords()
    {
        ArrayList<Train> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ACTIVITY_TABLE, null);

        while(cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            int color = cursor.getInt(2);
            int prepTime = cursor.getInt(3);
            int workTime = cursor.getInt(4);
            int freeTime = cursor.getInt(5);
            int cycleNum = cursor.getInt(6);
            int setNum = cursor.getInt(7);
            int freeOfSet = cursor.getInt(8);
            Train train = new Train(id, title, color, prepTime, workTime, freeTime, cycleNum, setNum, freeOfSet);
            arrayList.add(train);
        }
        db.close();
        this.close();

        return arrayList;
    }

    public ArrayList<Train> GetRecordById(int id)
    {
        ArrayList<Train> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ACTIVITY_TABLE + " WHERE id = " + id, null);

        String title = cursor.getString(1);
        int color = cursor.getInt(2);
        int prepTime = cursor.getInt(3);
        int workTime = cursor.getInt(4);
        int freeTime = cursor.getInt(5);
        int cycleNum = cursor.getInt(6);
        int setNum = cursor.getInt(7);
        int freeOfSet = cursor.getInt(8);
        Train train = new Train(id, title, color, prepTime, workTime, freeTime, cycleNum, setNum, freeOfSet);
        arrayList.add(train);

        db.close();
        this.close();

        return arrayList;
    }
}
