package com.example.inganapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.inganapp.inginfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewInterface{
    RecyclerView recyclerView;
    ArrayList<String> name;
    ArrayList<String> id;
    ArrayList<String> infos;
    ArrayList<String> wrn;
    ArrayList<String> processedArr;
    DBHelper DB;
    MyAdapter adapter;
    EditText filter;
    String strtext;
    private static final String TAG = "SecondFragment";
    private TextView txtName;
    public HomeFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment SecondFragment.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtName = view.findViewById(R.id.textViewName);

        //processedArr = new ArrayList<>();
        strtext = getArguments().getString("result");
        String pattern = "(,* *[А-яЁё –—-]+[\\s]*\\()|(\\),)|(,*[\\s]*[А-яёЁ ]+[\\s]*[-|–|—][\\s])|(,*[\\s]*[А-яЁё ]+[\\s]*\\:)";
        String proc_string = strtext.replaceAll(pattern, ", ").toLowerCase();

        String[] elements = proc_string.trim().split("\\s*,\\s*");
        List<String> fixedLenghtList = Arrays.asList(elements);
        processedArr = new ArrayList<String>(fixedLenghtList);
        System.out.println("list from comma separated String : " + processedArr);
        //processedArr = (ArrayList<String>) Arrays.asList(strtext.split("\\s*,\\s*"));

        name = new ArrayList<>();
        id = new ArrayList<>();
        wrn = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerview);
        //txtName.setText(proc_string);
        txtName.setText("Найдено " + id.size() + " ингредиентов");
    }
    public void displayReceivedData(String message)
    {

        String pattern = "(,* *[А-яЁё –—-]+[\\s]*\\()|(\\),)|(,*[\\s]*[А-яёЁ ]+[\\s]*[-|–|—][\\s])|(,*[\\s]*[А-яЁё ]+[\\s]*\\:)";
        String proc_string = message.replaceAll(pattern, ", ").toLowerCase();

        String[] elements = proc_string.trim().split("\\s*,\\s*");
        List<String> fixedLenghtList = Arrays.asList(elements);
        processedArr = new ArrayList<String>(fixedLenghtList);
        System.out.println("list from comma separated String : " + processedArr);
        //processedArr = (ArrayList<String>) Arrays.asList(strtext.split("\\s*,\\s*"));


        name = new ArrayList<>();
        id = new ArrayList<>();
        wrn = new ArrayList<>();
        DB = new DBHelper(this.getContext());
        DB.create_db();
        adapter = new MyAdapter(this.getContext(), name, id,wrn, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        displaydata();

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
            displaydata();



        }
        catch (SQLException ex){}

    }
    private void displaydata() {
        for (int i = 0; i< processedArr.size(); i++){
            Cursor cursor = DB.getdata2(processedArr.get(i));
            //cursor.moveToFirst();
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                wrn.add(cursor.getString(6));
            }
        }
        txtName.setText("Найдено " + id.size() + " ингредиентов");
    }
    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(this.getContext(), inginfo.class);
        infos = new ArrayList<>();
        infos = adapter.getId_id();
        String info = (String) infos.get(position);
        intent.putExtra("id", info);

        startActivity(intent);


    }

}
