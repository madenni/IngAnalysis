package com.example.inganapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;

public class inginfo extends AppCompatActivity {
    TextView nameBox, dia, vgt, vgn, alg, adtv;
    DBHelper DB;
    MyAdapter adapter;
    Cursor cursor;
    SQLiteDatabase sql;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inginfo);
        DB = new DBHelper(this);

        nameBox = (TextView) findViewById(R.id.textView);
        dia = findViewById(R.id.diabetes);
        vgt = findViewById(R.id.nonveget);
        vgn = findViewById(R.id.nonvegan);
        alg = findViewById(R.id.allergy);
        adtv = findViewById(R.id.additives);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("id");
        }

        //nameBox.setText(String.valueOf(userId));
        sql = DB.open();
        cursor = sql.rawQuery("select * from " + DB.TABLE + " where " +
                DB.COLUMN_ID + "=?", new String[]{userId});
        cursor.moveToFirst();
        nameBox.setText(cursor.getString(1));
        if (cursor.getInt(3) == 1 ){
            dia.setText("осторожно при диабете");
            dia.setBackgroundResource(R.color.red);
        } else {
            dia.setText("подходит при диабете");
            dia.setBackgroundResource(R.color.green);
        }
        if (cursor.getInt(4) == 1 ){
            vgt.setText("не подходит вегетарианцам");
            vgt.setBackgroundResource(R.color.red);
        } else {
            vgt.setText("подходит вегетарианцам");
            vgt.setBackgroundResource(R.color.green);
        }
        if (cursor.getInt(5) == 1 ){
            vgn.setText("не подходит веганам");
            vgn.setBackgroundResource(R.color.red);
        } else {
            vgn.setText("подходит веганам");
            vgn.setBackgroundResource(R.color.green);
        }
        if (cursor.getInt(6) == 1 ){
            alg.setText("возможна аллергия");
            alg.setBackgroundResource(R.color.red);
        } else {
            alg.setText("безопасно при аллергии");
            alg.setBackgroundResource(R.color.green);
        }
        if (cursor.getInt(7) == 1 ){
            adtv.setText("является пищевой добавкой");
            adtv.setBackgroundResource(R.color.red);
        } else {
            adtv.setText("не добавка");
            adtv.setBackgroundResource(R.color.green);
        }






    }
}