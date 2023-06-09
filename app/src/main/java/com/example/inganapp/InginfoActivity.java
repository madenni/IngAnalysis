package com.example.inganapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InginfoActivity extends AppCompatActivity {
    TextView nameBox, dia, vgt, vgn, alg, adtv, descr;
    DBHelper DB;
    int userID;
    Cursor cursor;
    SQLiteDatabase sql;
    String Id;
    CheckBox check;

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
        descr = findViewById(R.id.textView2);
        check = findViewById(R.id.checkBox);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Id = extras.getString("id");
            userID = extras.getInt("user");
        }

        //nameBox.setText(String.valueOf(userId));
        sql = DB.open();
        cursor = sql.rawQuery("select * from " + DB.TABLE + " where " +
                DB.COLUMN_ID + "=?", new String[]{Id});
        cursor.moveToFirst();
        nameBox.setText(cursor.getString(1));
        //descr.setText(cursor.getString(2));
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
        cursor = sql.rawQuery("Select idI, idU from Custom WHERE idI = " + Id + " AND idU = " + userID, null);
        cursor.moveToFirst();
        if(cursor!=null && cursor.getCount()>0){
            check.setChecked(true);
        } else check.setChecked(false);
        descr.setText(String.valueOf(userID));






        ContentValues cv = new ContentValues();
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cv.put("idI", Integer.parseInt(Id));
                    cv.put("idU", userID);
                    sql.insert("Custom",null, cv);
                } else sql.delete("Custom", "idI = " + Integer.parseInt(Id) + " AND " + userID, null);
                Toast.makeText(InginfoActivity.this, "Изменения сохранены!", Toast.LENGTH_SHORT).show();
            }
        });



    }
}