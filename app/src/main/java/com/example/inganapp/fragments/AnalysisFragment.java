package com.example.inganapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.inganapp.InginfoActivity;
import com.example.inganapp.MyAdapter;
import com.example.inganapp.R;
import com.example.inganapp.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalysisFragment extends Fragment implements RecyclerViewInterface{
    RecyclerView recyclerView;

    ArrayList<String> name;
    ArrayList<String> id, idWRN;
    ArrayList<String> infos;
    ArrayList<String> wrn;
    ArrayList<String> processedArr;
    DBHelper DB;
    MyAdapter adapter;
    Cursor cursorwrn;
    SQLiteDatabase sql;
    String strtext;
    int userID;

    private TextView txtName;
    public AnalysisFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment SecondFragment.
     */
    public static AnalysisFragment newInstance() {
        return new AnalysisFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtName = view.findViewById(R.id.textViewName);

        //processedArr = new ArrayList<>();
        assert getArguments() != null;

        userID = getArguments().getInt("user");
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

        adapter = new MyAdapter(this.getContext(), name, id,wrn, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        displaydata();

    }
    public void onResume() {
        super.onResume();

        try {
            idWRN = new ArrayList<>();
            DB = new DBHelper(this.getContext());
            DB.create_db();
            sql = DB.open();
            cursorwrn = sql.rawQuery("SELECT DISTINCT Ingredients.* \n" +
                    "                FROM Users, Ingredients \n" +
                    "               WHERE (Users._id = " + userID + " AND ((Ingredients.diabetes = Users.diabetes AND Users.diabetes = 1) OR " +
                    "(Ingredients.nonveget = Users.veget AND Users.veget = 1) OR (Ingredients.nonvegan = Users.vegan AND Users.vegan = 1) OR " +
                    "(Ingredients.allergy = Users.allergy AND Users.allergy = 1) OR (Ingredients.additives = Users.additives AND Users.additives = 1))) \n" +
                    "\t\t\t   UNION \n" +
                    "\t\t\t   SELECT DISTINCT Ingredients.* \n" +
                    "                FROM Custom, Ingredients \n" +
                    "               WHERE  (Custom.idU = " + userID + " AND Custom.idI = Ingredients._id) ", null);
            while (cursorwrn.moveToNext()){
                idWRN.add(cursorwrn.getString(0));
            }
            cursorwrn.close();
            name = new ArrayList<>();
            id = new ArrayList<>();
            wrn = new ArrayList<>();
            DB = new DBHelper(this.getContext());
            DB.create_db();
            adapter = new MyAdapter(this.getContext(), name, id,wrn, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            displaydata();
            sql.close();



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

            }
        }
        for (int j=0; j< id.size(); j++){
            if (idWRN.contains(id.get(j))){
                wrn.add(j,"1");
            } else {wrn.add(j,"0");}

        }
        txtName.setText("Найдено " + id.size() + " ингредиентов");
        System.out.println(userID);
    }
    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(this.getContext(), InginfoActivity.class);
        infos = new ArrayList<>();
        infos = adapter.getId_id();
        String info = (String) infos.get(position);
        intent.putExtra("id", info);
        intent.putExtra("user", userID);

        startActivity(intent);


    }



}
