package com.example.inganapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredlistActivity extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;
    ArrayList<String> name;
    ArrayList<String> id;
    ArrayList<String> infos;
    ArrayList<String> wrn;
    DBHelper DB;
    MyAdapter adapter;
    Cursor cursor;
    EditText filter;
    SQLiteDatabase sql;
    int user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getInt("user");

        }
        DB = new DBHelper(this);

        sql = DB.open();
        cursor = sql.rawQuery("SELECT * FROM Users", null);
        name = new ArrayList<>();
        id = new ArrayList<>();
        wrn = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        filter = findViewById(R.id.filter);

    }

    public void onResume() {
        super.onResume();
        try {
            name = new ArrayList<>();
            id = new ArrayList<>();
            wrn = new ArrayList<>();
            adapter = new MyAdapter(this, name, id,wrn, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            displaydata();


            filter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) { }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                // при изменении текста выполняем фильтрацию
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    filterM(s.toString());
                }
            });
        }
        catch (SQLException ex){}

    }

    private void displaydata() {

        Cursor cursor = DB.getdata();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No entry exists", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                wrn.add(cursor.getString(6));

            }
        }
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(this, InginfoActivity.class);
        infos = new ArrayList<>();
        infos = adapter.getId_id();
        String info = (String) infos.get(position);
        intent.putExtra("id", info);
        intent.putExtra("user", user);

        startActivity(intent);


    }
    private void filterM(String text) {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();
        ArrayList<String> filterdIDs = new ArrayList<>();
        ArrayList<String> filterdWRNS = new ArrayList<>();
        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(name.get(i));
                filterdIDs.add(String.valueOf(id.get(i)));

                filterdWRNS.add(String.valueOf(wrn.get(i)));
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames,filterdIDs, filterdWRNS);
    }
    @Override
    public void onBackPressed() {

        startActivity(new Intent(IngredlistActivity.this,MainActivity.class));
        finish();

    }
}