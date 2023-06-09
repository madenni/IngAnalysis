package com.example.inganapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class UserActivity extends AppCompatActivity {
    int user;
    ViewPager viewPager;
    DBHelper DB;
    Cursor cursor;
    SQLiteDatabase sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getInt("user");

        }
        TextView name = findViewById(R.id.imya);
        UserViewPagerAdapter sectionsPagerAdapter =
                new UserViewPagerAdapter(this, getSupportFragmentManager(), user);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        DB = new DBHelper(this);
        DB.create_db();
        sql = DB.open();
        cursor = sql.rawQuery("SELECT Name FROM Users WHERE Users._id = ?", new String[]{String.valueOf(user)});
        cursor.moveToFirst();
        name.setText(cursor.getString(0));

        //Now show or hide according to your need
        //getSupportActionBar().hide();
        //getSupportActionBar().show();
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(UserActivity.this,MainActivity.class));
        finish();

    }
}