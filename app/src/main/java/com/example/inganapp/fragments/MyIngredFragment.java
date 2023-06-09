package com.example.inganapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inganapp.DBHelper;
import com.example.inganapp.MyAdapter;
import com.example.inganapp.R;
import com.example.inganapp.RecyclerViewInterface;
import com.example.inganapp.InginfoActivity;

import java.util.ArrayList;


public class MyIngredFragment extends Fragment implements RecyclerViewInterface {
    int userID;
    ArrayList<String> name;
    ArrayList<String> id;
    ArrayList<String> infos;
    ArrayList<String> wrn;
    RecyclerView recyclerView;
    DBHelper DB;
    MyAdapter adapter;
    TextView text;
    public static MyIngredFragment newInstance() {
            return new MyIngredFragment();
        }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_ingred, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //processedArr = new ArrayList<>();
        assert getArguments() != null;

        userID = getArguments().getInt("user");


        //processedArr = (ArrayList<String>) Arrays.asList(strtext.split("\\s*,\\s*"));

        name = new ArrayList<>();
        id = new ArrayList<>();
        wrn = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerview);
        text = view.findViewById(R.id.textView);


    }
    public void onResume() {
        super.onResume();
        try {
            name = new ArrayList<>();
            id = new ArrayList<>();
            wrn = new ArrayList<>();
            DB = new DBHelper(this.getContext());
            DB.create_db();
            adapter = new MyAdapter(this.getContext(), name, id,wrn, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            displaydata(userID);



        }
        catch (SQLException ex){}

    }
    private void displaydata(int a) {
        Cursor cursor = DB.getdata3(a);
        if(cursor.getCount()==0){
            text.setText("Найдено ингредиентов: 0");
            return;
        }
        else {
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                wrn.add(cursor.getString(6));

            }
            text.setText("Найдено ингредиентов: " + id.size());
        }
    }
    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(this.getContext(), InginfoActivity.class);
        infos = new ArrayList<>();
        infos = adapter.getId_id();
        String info = (String) infos.get(position);
        intent.putExtra("id", info);

        startActivity(intent);


    }
}