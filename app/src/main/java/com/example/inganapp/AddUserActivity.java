package com.example.inganapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddUserActivity extends AppCompatActivity {
    CheckBox dia, vgt, vgn, alg, adtv;
    EditText name;
    DBHelper DB;
    Cursor cursor;
    SQLiteDatabase sql;
    Button save;
    private String blockCharacterSet = "~#^|$%&*!;.,+=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        name = findViewById(R.id.userName);
        dia = findViewById(R.id.CHdiabetes);
        vgt = findViewById(R.id.CHveget);
        vgn = findViewById(R.id.CHvegan);
        alg = findViewById(R.id.CHallergy);
        adtv = findViewById(R.id.CHadditives);
        save = findViewById(R.id.del);

        name.setFilters(new InputFilter[] { filter });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = String.valueOf(name.getText());
                if (newName == null | newName.trim().isEmpty()){
                    Toast.makeText(AddUserActivity.this, "Необходимо ввести имя пользователя!", Toast.LENGTH_SHORT).show();
                }else {
                    DB = new DBHelper(AddUserActivity.this);
                    sql = DB.open();
                    ContentValues cv = new ContentValues();
                    cv.put("Name", newName);
                    if (dia.isChecked()) {
                        cv.put("diabetes", 1);
                    } else cv.put("diabetes", 0);
                    if (alg.isChecked()) {
                        cv.put("allergy", 1);
                    } else cv.put("allergy", 0);
                    if (vgt.isChecked()) {
                        cv.put("veget", 1);
                    } else cv.put("veget", 0);
                    if (vgn.isChecked()) {
                        cv.put("vegan", 1);
                    } else cv.put("vegan", 0);
                    if (adtv.isChecked()) {
                        cv.put("additives", 1);
                    } else cv.put("additives", 0);
                    sql.insert("Users", null, cv);

                    Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }


    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
}