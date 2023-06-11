package com.example.inganapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "ingredDB.db";
    static final String TABLE = "Ingredients";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "Name";
    static final String COLUMN_SYN = "Synonyms";



    public DBHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.context=context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        //DB.execSQL("create Table Userdetails(name TEXT primary key, email TEXT, age TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        //DB.execSQL("drop Table if exists Userdetails");
    }

    public void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists()) {
            //получаем локальную бд как поток
            try(InputStream myInput = context.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }


    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public Cursor getdata(){
        SQLiteDatabase DB = this.open();
        Cursor cursor = DB.rawQuery("SELECT DISTINCT Ingredients.* \n" +
                "                FROM Ingredients", null);
        return cursor;
    }
    public Cursor getdata2(String a) {
        SQLiteDatabase DB = this.open();
        Cursor cursor = null;
        cursor = DB.rawQuery("select Ingredients.* from " + TABLE + " where Synonyms like " +"'%" + a + "%'", null);
        System.out.println("ingredient name: " + a);
        return cursor;
    }
    public Cursor getdata3(int a) {
        SQLiteDatabase DB = this.open();
        Cursor cursor = null;
        cursor = DB.rawQuery("SELECT Ingredients.*\n" +
                "FROM Custom, Ingredients\n" +
                "WHERE Custom.idU = ? AND Custom.idI = Ingredients._id", new String[]{String.valueOf(a)});

        return cursor;
    }
}
