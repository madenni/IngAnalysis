package com.example.inganapp.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inganapp.AddUserActivity;
import com.example.inganapp.DBHelper;
import com.example.inganapp.MainActivity;
import com.example.inganapp.R;


public class HabitsFragment extends Fragment {
    int userID;

    CheckBox dia, vgt, vgn, alg, adtv;
    DBHelper DB;
    Cursor cursor;
    SQLiteDatabase sql;
    Button save;
    Button del;
    public static HabitsFragment newInstance() {
        return new HabitsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habits, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //processedArr = new ArrayList<>();
        assert getArguments() != null;

        userID = getArguments().getInt("user");

        dia = view.findViewById(R.id.CHdiabetes);
        vgt = view.findViewById(R.id.CHveget);
        vgn = view.findViewById(R.id.CHvegan);
        alg = view.findViewById(R.id.CHallergy);
        adtv = view.findViewById(R.id.CHadditives);
        save = view.findViewById(R.id.imageButton);
        del = view.findViewById(R.id.del);

    }
    public void onResume() {
        super.onResume();

        try {
            DB = new DBHelper(this.getContext());
            sql = DB.open();
            cursor = sql.rawQuery("SELECT Users.*\n" +
                    "FROM Users\n" +
                    "WHERE Users._id = ?", new String[]{String.valueOf(userID)});
            cursor.moveToFirst();

            if (cursor.getInt(2) == 1){
                dia.setChecked(true);
            } else dia.setChecked(false);
            if (cursor.getInt(3) == 1){
                alg.setChecked(true);
            } else alg.setChecked(false);
            if (cursor.getInt(4) == 1){
                vgt.setChecked(true);
            } else vgt.setChecked(false);
            if (cursor.getInt(5) == 1){
                vgn.setChecked(true);
            } else vgn.setChecked(false);
            if (cursor.getInt(6) == 1){
                adtv.setChecked(true);
            } else adtv.setChecked(false);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues cv = new ContentValues();
                    if (dia.isChecked()){
                        cv.put("diabetes", 1);
                    } else cv.put("diabetes", 0);
                    if (alg.isChecked()){
                        cv.put("allergy", 1);
                    } else cv.put("allergy", 0);
                    if (vgt.isChecked()){
                        cv.put("veget", 1);
                    } else cv.put("veget", 0);
                    if (vgn.isChecked()){
                        cv.put("vegan", 1);
                    } else cv.put("vegan", 0);
                    if (adtv.isChecked()){
                        cv.put("additives", 1);
                    } else cv.put("additives", 0);
                    sql.update("Users", cv, " _id = ? ", new String[]{String.valueOf(userID)});
                    Toast.makeText(getContext(), "Изменения сохранены!", Toast.LENGTH_SHORT).show();
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog diaBox = AskOption();
                    diaBox.show();
                }
            });

        }
        catch (SQLException ex){}

    }
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Удаление аккаунта")
                .setMessage("Вы точно хотите удалить аккаунт?")
                //.setIcon(R.drawable.delete)

                .setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        sql.delete("Users", "_id = ?", new String[]{String.valueOf(userID)});
                        sql.close();
                        // переход к главной activity
                        try {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();

                        } catch(Exception e){
                            Intent intent = new Intent(getContext(), AddUserActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }


                    }

                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }

}